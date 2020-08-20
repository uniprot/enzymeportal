/*
 * The MIT License
 *
 * Copyright 2020 joseph.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package uk.ac.ebi.ep.indexservice.service;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;

/**
 *
 * @author joseph
 */
public interface ApiIndexService {

    Mono<EnzymeSearchResult> enzymeSearchResult(QueryBuilder queryBuilder);

    Mono<ProteinGroupSearchResult> proteinGroupSearchResult(QueryBuilder queryBuilder);

    int getEnzymeHitCount(QueryBuilder enzymeQueryBuilder);

    int getProteinHitCount(QueryBuilder enzymeQueryBuilder);

    Flux<EnzymeEntry> getEnzyme(QueryBuilder enzymeQueryBuilder);

    EnzymeEntry getEnzymeEntry(QueryBuilder enzymeQueryBuilder);

    List<EnzymeEntry> getEnzymeEntries(QueryBuilder enzymeQueryBuilder);

    Flux<EnzymeEntry> getEnzymes(QueryBuilder enzymeQueryBuilder, String searchTerm);

    Flux<EnzymeEntry> getEnzymes(QueryBuilder enzymeQueryBuilder, String resourceId, IndexQueryType resourceQueryType);

    Flux<ProteinGroupEntry> proteinsByEc(String ec, int limit);

    Flux<ProteinGroupEntry> proteinResult(QueryBuilder queryBuilder);

    List<ProteinGroupEntry> getProteinGroupEntry(QueryBuilder queryBuilder);

}
