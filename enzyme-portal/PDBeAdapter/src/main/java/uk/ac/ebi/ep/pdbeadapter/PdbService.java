/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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

    /**
     * build a concrete PDB information for summaries, experiments,
     * molecules,publications and structural domain
     *
     * @param pdbId pdbe id
     * @return a concrete PDB object with relevant information
     */
    public PDB computeProteinStructure(String pdbId) {

        PDB pdb = new PDB();
        pdb.setId(pdbId);

        PdbSearchResult summary = pdbeRestService.getPdbSummaryResults(pdbId);

        List<PDBe> pdbSummary = summary.get(pdbId);
        if (pdbSummary != null && !pdbSummary.isEmpty()) {

            pdb = computeSummary(pdbSummary, pdb);
        }

        //molecules
        PDBmolecules molecules = pdbeRestService.getPDBmoleculeResults(pdbId);

        if (molecules != null) {
            List<Molecule> mol = molecules.get(pdbId);

            if (mol != null && !mol.isEmpty()) {

                List<Polypeptide> peptides = computeChains(mol).stream().distinct().collect(Collectors.toList());
                pdb.setPolypeptides(peptides);
                pdb.setSmallMoleculeLigands(computeHeterogen(mol));

                //pdb.setPolypeptides(computePolypeptides(mol));
                //pdb.setSmallMoleculeLigands(computeSmallMoleculeLigands(mol));
            }
        }

        //experiements
        PDBexperiments experiments = pdbeRestService.getPDBexperimentResults(pdbId);

        if (experiments != null) {

            List<PDBexperiment> pdbExperiments = experiments.get(pdbId);

            if (pdbExperiments != null) {

                pdb = computeExperiment(pdbExperiments, pdb);
            }

        }
        //publications
        PDBePublications publications = pdbeRestService.getPDBpublicationResults(pdbId);
        if (publications != null) {

            List<PDBePublication> publication = publications.get(pdbId);
            if (publication != null) {

                pdb = computePublications(publication, pdb);

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

    private PDB computeSummary(List<PDBe> pdbSummary, final PDB pdb) {

        final String inputFormat = "yyyyMMdd";
        final String outputFormat = "MMMM dd, yyyy";

        pdbSummary.stream().map(eSummary -> {
            pdb.setTitle(eSummary.getTitle());
            return eSummary;
        }).map(eSummary -> {
            pdb.setExperimentMethod(eSummary.getExperimentalMethod());
            return eSummary;
        }).forEach(eSummary -> {
            try {
                pdb.setDepositionDate(pdbDateConverter(inputFormat, eSummary.getDepositionDate(), outputFormat));
                pdb.setRevisionDate(pdbDateConverter(inputFormat, eSummary.getRevisionDate(), outputFormat));
                pdb.setReleaseDate(pdbDateConverter(inputFormat, eSummary.getReleaseDate(), outputFormat));
            } catch (ParseException e) {
                LOGGER.error("Error while parsing PDB Date", e);
            }
        });

        return pdb;
    }

    private PDB computeExperiment(List<PDBexperiment> pdbExperiments, final PDB pdb) {
        pdbExperiments.stream().map(experiment -> {
            pdb.setResolution(String.valueOf(experiment.getResolution()));
            return experiment;
        }).map(experiment -> {
            pdb.setrFactor(String.valueOf(experiment.getRFactor()));
            return experiment;
        }).map(experiment -> {
            pdb.setSpacegroup(experiment.getSpacegroup());
            return experiment;
        }).forEach(experiment -> {
            pdb.setrFree(String.valueOf(experiment.getRFree()));
        });

        return pdb;
    }

    private List<SmallMoleculeLigand> computeSmallMoleculeLigands(List<Molecule> mol) {
        Deque<SmallMoleculeLigand> ligands = new LinkedList<>();
        for (Molecule m : mol) {

            if ("Bound".equalsIgnoreCase(m.getMoleculeType())) {

                SmallMoleculeLigand ligand = new SmallMoleculeLigand();
                ligand.setLabel("Small molecule ligands");
                ligand.getMolecules().add(m);
                ligands.add(ligand);

            }

        }
        return ligands.stream().collect(Collectors.toList());
    }

    private List<SmallMoleculeLigand> computeHeterogen(List<Molecule> mol) {
        Deque<SmallMoleculeLigand> ligands = new LinkedList<>();
        for (Molecule m : mol) {

            if ("Bound".equalsIgnoreCase(m.getMoleculeType())) {
              
                SmallMoleculeLigand ligand = new SmallMoleculeLigand();
                ligand.setChainId(m.getInChains().toString());
                ligand.getMolecules().add(m);
                ligands.add(ligand);

            }

        }
        return ligands.stream().collect(Collectors.toList());
    }

    private List<Polypeptide> computePolypeptides(List<Molecule> mol) {
        Deque<Polypeptide> polypeptides = new LinkedList<>();
        for (Molecule m : mol) {

            if ("polypeptide(L)".equalsIgnoreCase(m.getMoleculeType())) {
                Polypeptide polypeptide = new Polypeptide();
                for (Source s : m.getSource()) {
                    polypeptide.setOrganism(s.getOrganismScientificName());
                    polypeptide.setResidues(s.getMappings());

                }

                polypeptide.setLabel("Polypeptide chain");
                polypeptide.setMoleculeName(m.getMoleculeName());

                polypeptide.setProtein(true);
                polypeptides.push(polypeptide);

            }

        }

        return polypeptides.stream().collect(Collectors.toList());
    }

    private List<Polypeptide> computeChains(List<Molecule> mol) {
        Deque<Polypeptide> polypeptides = new LinkedList<>();
        for (Molecule m : mol) {

            if ("polypeptide(L)".equalsIgnoreCase(m.getMoleculeType())) {

                for (String chain : m.getInChains()) {
                    Polypeptide polypeptide = new Polypeptide();
                    for (Source s : m.getSource()) {
                        polypeptide.setOrganism(s.getOrganismScientificName());
                        polypeptide.setResidues(s.getMappings());

                    }

                    polypeptide.setLabel("Chain: " + chain);
                    polypeptide.setChainId(m.getInChains().toString());
                    polypeptide.setMoleculeName(m.getMoleculeName());

                    polypeptide.setProtein(true);
                    polypeptides.push(polypeptide);

                }
            }

        }

        return polypeptides.stream().sorted().collect(Collectors.toList());
    }

    private PDB computePublications(List<PDBePublication> publication, final PDB pdb) {
        PDBePublication primaryCitation = publication.stream().findFirst().get();
        pdb.setEntryAuthors(primaryCitation.getAuthorList());
        pdb.setPrimaryCitation(primaryCitation.getTitle());
        String info = primaryCitation.getJournalInfo().getPdbAbbreviation()
                + " vol:" + primaryCitation.getJournalInfo().getVolume()
                + " page:" + primaryCitation.getJournalInfo().getPages()
                + " (" + primaryCitation.getJournalInfo().getYear() + ")";
        pdb.setPrimaryCitationInfo(info);
        return pdb;
    }

}
