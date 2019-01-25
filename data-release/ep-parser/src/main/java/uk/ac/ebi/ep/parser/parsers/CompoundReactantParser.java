package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.model.ChebiCompound;
import uk.ac.ebi.ep.model.EnzymeReactionInfo;
import uk.ac.ebi.ep.model.search.model.Compound;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author joseph
 */
@Slf4j
public class CompoundReactantParser extends GenericCompound {

    private static final String XREF_TYPE ="CHEBI";
    private List<LiteCompound> reactants = null;

    public CompoundReactantParser(EnzymePortalParserService enzymePortalParserService) {
        super(enzymePortalParserService);
        reactants = new ArrayList<>();
    }

    private void loadCompound(LiteCompound compound) {

        enzymePortalParserService.createReactant(compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
    }

    @Transactional(readOnly = false)
    @Override
    void loadCompoundToDatabase() {

        try (Stream<EnzymeReactionInfo> reactionInfo = enzymePortalParserService.findAllReactionInfoByXrefTypeAndStream(XREF_TYPE)) {

            reactionInfo.forEach(data -> processReactionInfo(data));

        }
        log.info("Writing to Enzyme Portal database... Number of reactants to write : " + reactants.size());

        reactants
                .stream()
                .filter(compound -> compound != null)
                .forEach(compound -> loadCompound(compound));
        log.warn("-------- Done populating the database with reactants ---------------");
        reactants.clear();

    }

    private void processReactionInfo(EnzymeReactionInfo reactionInfo) {
        ChebiCompound chebiCompound = enzymePortalParserService.findChebiCompoundById(reactionInfo.getXref(), UNIPROT)
                .stream()
                .filter(c -> c.getSource().equalsIgnoreCase(UNIPROT))
                .distinct().findFirst().orElse(null);
        if (chebiCompound == null) {
            chebiCompound = enzymePortalParserService.findChebiCompoundById(reactionInfo.getXref(), IUPAC)
                    .stream()
                    .filter(c -> c.getSource().equalsIgnoreCase(IUPAC))
                    .distinct().findFirst().orElse(null);
        }

        if (chebiCompound != null) {
            String chebiId = chebiCompound.getChebiAccession();
            String chebiName = chebiCompound.getCompoundName();

            if (chebiId != null && !blackList.contains(chebiName) && !StringUtils.isEmpty(chebiName)) {

                LiteCompound entry = new LiteCompound();
                entry.setCompoundId(chebiId);
                entry.setCompoundName(chebiName);
                entry.setUniprotAccession(reactionInfo.getUniprotAccession());
                entry.setCompoundSource(MmDatabase.ChEBI.name());
                entry.setRelationship(Relationship.is_reactant_of.name());
                entry.setCompoundRole(Compound.Role.REACTANT.name());
                entry.setNote(reactionInfo.getReactionDirection());//this is only specific to reactant
                entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=" + chebiId);
                reactants.add(entry);

            }

        }
    }

}
