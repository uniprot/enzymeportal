/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.pdbeadapter.experiment.PDBexperiment;
import uk.ac.ebi.ep.pdbeadapter.experiment.PDBexperiments;
import uk.ac.ebi.ep.pdbeadapter.molecule.Molecule;
import uk.ac.ebi.ep.pdbeadapter.molecule.PDBmolecules;
import uk.ac.ebi.ep.pdbeadapter.molecule.Source;
import uk.ac.ebi.ep.pdbeadapter.publication.PDBePublication;
import uk.ac.ebi.ep.pdbeadapter.publication.PDBePublications;
import uk.ac.ebi.ep.pdbeadapter.summary.PDBe;
import uk.ac.ebi.ep.pdbeadapter.summary.PdbSearchResult;

/**
 *
 * @author joseph
 */
public class PdbService {

    private final PDBeRestService pdbeRestService;
    private static final Logger LOGGER = Logger.getLogger(PdbService.class);

    public PdbService(PDBeRestService pdbeRestService) {
        this.pdbeRestService = pdbeRestService;
    }

    public PdbSearchResult getPdbSearchResults(String pdbId) {

        return pdbeRestService.getPdbSummaryResults(pdbId);

    }

    public PDB computeProteinStructure(String pdbId) {

        PDB pdb = new PDB();
        pdb.setId(pdbId);

        PdbSearchResult summary = pdbeRestService.getPdbSummaryResults(pdbId);

        List<PDBe> pdbSummary = summary.get(pdbId);
        if (pdbSummary != null && !pdbSummary.isEmpty()) {

            final String inputFormat = "yyyyMMdd";
            //final String outputFormat = "EEE MMM dd HH:mm:ss z yyyy";
            final String outputFormat = "MMMM dd, yyyy";

            pdbSummary.stream().map((eSummary) -> {
                pdb.setTitle(eSummary.getTitle());
                return eSummary;
            }).map((eSummary) -> {
                pdb.setExperimentMethod(eSummary.getExperimentalMethod());
                return eSummary;
            }).forEach((eSummary) -> {
                try {
                    pdb.setDepositionDate(pdbDateConverter(inputFormat, eSummary.getDepositionDate(), outputFormat));
                    pdb.setRevisionDate(pdbDateConverter(inputFormat, eSummary.getRevisionDate(), outputFormat));
                    pdb.setReleaseDate(pdbDateConverter(inputFormat, eSummary.getReleaseDate(), outputFormat));
                } catch (ParseException e) {
                    LOGGER.error("Error while parsing PDB Date", e);
                }
            });
        }

        //molecules
        PDBmolecules molecules = pdbeRestService.getPDBmoleculeResults(pdbId);

        if (molecules != null) {
            List<Molecule> mol = molecules.get(pdbId);

            if (mol != null && !mol.isEmpty()) {
                List<PdbEntity> entities = computeEntities(mol);

                pdb.setPdbEntities(entities);
            }
        }

        //experiements
        PDBexperiments experiments = pdbeRestService.getPDBexperimentResults(pdbId);

        if (experiments != null) {

            List<PDBexperiment> pdbExperiments = experiments.get(pdbId);

            if (pdbExperiments != null) {
                pdbExperiments.stream().map((experiment) -> {
                    pdb.setResolution(String.valueOf(experiment.getResolution()));
                    return experiment;
                }).map((experiment) -> {
                    pdb.setrFactor(String.valueOf(experiment.getRFactor()));
                    return experiment;
                }).forEach((experiment) -> {
                    pdb.setrFree(String.valueOf(experiment.getRFree()));
                });
            }

        }
        //publications
        PDBePublications publications = pdbeRestService.getPDBpublicationResults(pdbId);
        if (publications != null) {

            List<PDBePublication> publication = publications.get(pdbId);
            if (publication != null) {
                PDBePublication primaryCitation = publication.stream().findFirst().get();
                pdb.setEntryAuthors(primaryCitation.getAuthorList());
                pdb.setPrimaryCitation(primaryCitation.getTitle());
                String info = primaryCitation.getJournalInfo().getPdbAbbreviation()
                        + " vol:" + primaryCitation.getJournalInfo().getVolume()
                        + " page:" + primaryCitation.getJournalInfo().getPages()
                        + " (" + primaryCitation.getJournalInfo().getYear() + ")";
                pdb.setPrimaryCitationInfo(info);

            }

        }

        String domains = pdbeRestService.getStructuralDomain(pdbId);
        pdb.setStructuralDomain(domains);

        //provenance
        pdb.getProvenance().add("Data source: PDBe.");
        String info = "EMBL-EBI's Protein Data Bank in Europe (PDBe) is the European resource for the collection, "
                + "organisation and dissemination of data on biological macromolecular structures. "
                + "In collaboration with the other worldwide Protein Data Bank (wwPDB) partners we work to collate, "
                + "maintain and provide access to the global repository of macromolecular structure data (PDB).";
        pdb.getProvenance().add(info);

        return pdb;
    }

    /**
     * Format a time from a given format to given target format
     *
     * @param inputFormat
     * @param inputTimeStamp
     * @param outputFormat
     * @return
     * @throws ParseException
     */
    private String pdbDateConverter(final String inputFormat,
            String inputTimeStamp, final String outputFormat)
            throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(
                inputFormat).parse(inputTimeStamp));
    }

    private List<PdbEntity> computeEntities(List<Molecule> mol) {
        LinkedList<PdbEntity> entities = new LinkedList<>();
        for (Molecule m : mol) {

            if ("polypeptide(L)".equalsIgnoreCase(m.getMoleculeType())) {
                String organism = null;
                for (Source s : m.getSource()) {
                    organism = s.getOrganismScientificName();
                }

                PdbEntity entity = new PdbEntity();
                entity.setLabel("Chain " + m.getInChains().stream().distinct().findFirst().get());
                entity.getMolecules().add(m);
                entity.setOrganism(organism);
                entity.setProtein(true);
                entities.push(entity);

            } else if ("Bound".equalsIgnoreCase(m.getMoleculeType())) {
                PdbEntity entity = new PdbEntity();
                entity.setLabel("Heterogen " + m.getInStructAsyms().stream().findFirst().get());
                entity.getMolecules().add(m);
                entities.add(entity);

            }

        }
        return entities;
    }

}
