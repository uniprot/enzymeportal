/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.service.EnzymePortalParserService;

/**
 *
 * @author joseph
 */
public class Cofactors extends CompoundParser {

    private List<EnzymePortalCompound> compounds = null;
    private static final String COMMENT_TYPE = "COFACTORS";
    private static final String NAME = "Name=([^\\s]+)";
    private static final String XREF = "Xref=ChEBI:([^\\s]+)";
    private static final String NOTE = "Note=([^\\*]+)";

    public Cofactors(ChebiWebServiceClient chebiWsClient, EnzymePortalCompoundRepository compoundRepository, EnzymePortalSummaryRepository enzymeSummaryRepository, EnzymePortalParserService parserService) {
        super(chebiWsClient, compoundRepository, enzymeSummaryRepository, parserService);
        compounds = new ArrayList<>();
    }

    /**
     * parse cofactor comments from uniprot and validates compound names in
     * chebi before storing them at enzyme portal database
     */
    @Override
    public void loadCofactors() {
        List<EnzymePortalSummary> enzymeSummary = enzymeSummaryRepository.findSummariesByCommentType(COMMENT_TYPE);

       
        LOGGER.warn("Number of Regulation Text from EnzymeSummary Table to parse for cofactors " + enzymeSummary.size());
        parseCofactorText(enzymeSummary);
    }

    private void computeSpecialCases(String text, EnzymePortalSummary summary, String note) {
        final Pattern xrefPattern = Pattern.compile(XREF);
        final Matcher xrefMatcher = xrefPattern.matcher(text);

        while (xrefMatcher.find()) {

            String xref = xrefMatcher.group(1).replaceAll(";", "");

            if (xref != null) {
                EnzymePortalCompound compound = searchCompoundInChEBI(xref);

                if (compound != null) {

                    String compoundId = compound.getCompoundId();
                    String compoundName = compound.getCompoundName();
                    String compoundSource = compound.getCompoundSource();
                    String relationship = Relationship.is_cofactor_of.name();
                    String compoundRole = Compound.Role.COFACTOR.name();
                    String url = compound.getUrl();
                    String accession = summary.getUniprotAccession().getAccession();

                    parserService.createCompound(compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);

                    //depreccate later
                    compound.setRelationship(Relationship.is_cofactor_of.name());
                    compound.setUniprotAccession(summary.getUniprotAccession());
                    compound.setCompoundRole(Compound.Role.COFACTOR.name());

                    compound.setNote(note);
                    compounds.add(compound);
                    LOGGER.warn("added compound for special case " + compound.getCompoundId() + " <> " + compound.getCompoundName());

                }

            }

        }

    }

    private void parseCofactorText(List<EnzymePortalSummary> enzymeSummary) {

//        Stream<EnzymePortalSummary> existingStream = enzymeSummary.stream();
//        Stream<List<EnzymePortalSummary>> partitioned = partition(existingStream, 100, 1);
//        AtomicInteger count = new AtomicInteger(1);
//        partitioned.parallel().forEach((chunk) -> {
//             //System.out.println(count.getAndIncrement() + " BATCH SIZE" + chunk.size());
//            chunk.stream().forEach((summary) -> {
//                processCofactors(summary);
//            });
//        });
        

        enzymeSummary.forEach(summary ->{
         processCofactors(summary);
     
        });

        //save compounds
        LOGGER.warn("Writing to Enzyme Portal database... Number of cofactors to write : " + compounds.size());

        //deprecate later
        //compoundRepository.save(compounds);
        compounds.clear();

    }

    private void processCofactors(EnzymePortalSummary summary) {
        String cofactorText = summary.getCommentText();
        String note = "";
        final Pattern notePattern = Pattern.compile(NOTE);
        final Matcher noteMatcher = notePattern.matcher(cofactorText);

        while (noteMatcher.find()) {

            note = noteMatcher.group(1);

        }

        final Pattern namePattern = Pattern.compile(NAME);
        final Matcher nameMatcher = namePattern.matcher(cofactorText);

        while (nameMatcher.find()) {

            String cofactorName = nameMatcher.group(1).replaceAll(";", "");

            if (cofactorName != null) {
                EnzymePortalCompound compound = searchMoleculeInChEBI(cofactorName);

                if (compound != null) {

                    String compoundId = compound.getCompoundId();
                    String compoundName = compound.getCompoundName();
                    String compoundSource = compound.getCompoundSource();
                    String relationship = Relationship.is_cofactor_of.name();
                    String compoundRole = Compound.Role.COFACTOR.name();
                    String url = compound.getUrl();
                    String accession = summary.getUniprotAccession().getAccession();

                    //System.out.println("COMPOUND ID "+ compoundId);
                    parserService.createCompound(compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);
                   
                    //deprecate later
                    compound.setRelationship(Relationship.is_cofactor_of.name());
                    compound.setUniprotAccession(summary.getUniprotAccession());
                    compound.setCompoundRole(Compound.Role.COFACTOR.name());

                    compound.setNote(note);
                    compounds.add(compound);
                  
                }
                if (compound == null) {
                    computeSpecialCases(cofactorText, summary, note);
                }
            }

        }
    }

}
