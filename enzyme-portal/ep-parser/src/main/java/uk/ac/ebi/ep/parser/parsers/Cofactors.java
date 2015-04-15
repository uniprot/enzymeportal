/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.parser.helper.Relationship;
import static uk.ac.ebi.ep.parser.inbatch.PartitioningSpliterator.partition;

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

    public Cofactors(ChebiWebServiceClient chebiWsClient, EnzymePortalCompoundRepository compoundRepository, EnzymePortalSummaryRepository enzymeSummaryRepository) {
        super(chebiWsClient, compoundRepository, enzymeSummaryRepository);
        compounds = new ArrayList<>();
    }

    /**
     * parse cofactor comments from uniprot and validates compound names in
     * chebi before storing them at enzyme portal database
     */
    @Override
    public void loadCofactors() {
        List<EnzymePortalSummary> enzymeSummary = enzymeSummaryRepository.findSummariesByCommentType(COMMENT_TYPE);

        System.out.println("num summary to parser "+ enzymeSummary.size());
        LOGGER.warn("Number of Regulation Text from EnzymeSummary Table for cofactors " + enzymeSummary.size());
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

                    compound.setRelationship(Relationship.is_cofactor_of.name());
                    compound.setUniprotAccession(summary.getUniprotAccession());
                    compound.setCompoundRole(Compound.Role.COFACTOR.name());

                    compound.setNote(note);
                    compounds.add(compound);
                    LOGGER.warn("added compound for special case " + compound.getCompoundId() + " <> " + compound.getCompoundName() );

                }

            }

        }

    }

    private void parseCofactorText(List<EnzymePortalSummary> enzymeSummary) {

        Stream<EnzymePortalSummary> existingStream = enzymeSummary.stream();
        Stream<List<EnzymePortalSummary>> partitioned = partition(existingStream, 500, 1);
        AtomicInteger count = new AtomicInteger(1);
        partitioned.parallel().forEach((chunk) -> {
           // System.out.println(count.getAndIncrement() + " BATCH SIZE" + chunk.size());
            chunk.stream().forEach((summary) -> {
                processCofactors(summary);
            });
        });

        //save compounds
        LOGGER.warn("Writing to Enzyme Portal database... Number of cofactors to write : " + compounds.size());

        
        compoundRepository.save(compounds);
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

//    public static void main(String args[]) {
//        System.out.println("ok");
//        String text = "Name=Mg(2+); Xref=ChEBI:CHEBI:18420; Name=Mn(2+); Xref=ChEBI:CHEBI:29035; Note=Binds 1 divalent metal cation per subunit; can use either Mg(2+) or Mn(2+)";
//        
//        String cofactorName = null;
//        String xref = null;
//        String note = null;
//        
//        final Pattern namePattern = Pattern.compile("Name=([^\\s]+)");
//        final Matcher nameMatcher = namePattern.matcher(text);
//
////        if (nameMatcher.lookingAt()) {
////            cofactorName = nameMatcher.group(1);
////        }
//        while (nameMatcher.find()) {
//
//            //System.out.println("the matcher "+ nameMatcher.group(1));
//            //String cofactorName = nameMatcher.group(1).replaceAll(";", "");
//            cofactorName = nameMatcher.group(1).replaceAll(";", "");
//            System.out.println("Name result " + cofactorName);
//            //searchMoleculeInChEBI(cofactorName);
//
//        }
//        
//        final Pattern idPattern = Pattern.compile("Xref=ChEBI:([^\\s]+)");
//        final Matcher idMatcher = idPattern.matcher(text);
//
////        if (idMatcher.lookingAt()) {
////            xref = idMatcher.group(1);
////        }
//        while (idMatcher.find()) {
//
//            //String xref = idMatcher.group(1).replaceAll(";", "");
//            xref = idMatcher.group(1).replaceAll(";", "");
//            System.out.println("id result " + xref);
//            
//        }
//        
//        final Pattern notePattern = Pattern.compile("Note=([^\\*]+)");
//        final Matcher noteMatcher = notePattern.matcher(text);
//        
//        while (noteMatcher.find()) {
//            
//            note = noteMatcher.group(1);
//            System.out.println("note result " + note);
//            
//        }
//        
//        System.out.println("name " + cofactorName + "id " + xref + " note " + note);
//        
//    }
}
