/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.common;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.Predicate;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.EcNumber;

/**
 *
 * @author joseph
 */
public class EcNumberPredicate implements Predicate {

    private final Collection<Integer> ecNumberFilters;

    public EcNumberPredicate(Collection<Integer> ecNumberFilter) {
        this.ecNumberFilters = ecNumberFilter;
    }

    @Override
    public boolean evaluate(Object obj) {

        if (ecNumberFilters == null || ecNumberFilters.isEmpty()) {
            return true;
        }
        Set<Integer> ecNumberFilter = ecNumberFilters.stream().distinct().collect(Collectors.toSet());

        boolean eval = false;
        if (obj instanceof UniprotEntry) {
            UniprotEntry entry = (UniprotEntry) obj;
            for (EcNumber ec : entry.getEnzymePortalEcNumbersSet()) {
                Integer family = ec.getEc();
                if (ecNumberFilter.contains(family)) {
                    eval = true;
                    break;
                }
            }

        }
        return eval;

    }

}
