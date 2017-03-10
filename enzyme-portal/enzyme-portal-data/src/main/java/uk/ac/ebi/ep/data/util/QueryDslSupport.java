
package uk.ac.ebi.ep.data.util;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.EntityPathBase;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.data.querydsl.QPageRequest;

/**
 *
 * @author joseph
 * @param <E>
 * @param <Q>
 */
public class QueryDslSupport<E, Q extends EntityPathBase<E>> extends QueryDslRepositorySupport {

  public QueryDslSupport(Class<E> clazz) {
    super(clazz);
  }

  protected Page<E> readPage(JPAQuery query, Q qEntity, Pageable pageable) {
    if (pageable == null) {
      return readPage(query, qEntity, new QPageRequest(0, Integer.MAX_VALUE));
    }
    long total = query.clone(super.getEntityManager()).count(); // need to clone to have a second query, otherwise all items would be in the list
    JPQLQuery pagedQuery = getQuerydsl().applyPagination(pageable, query);
    List<E> content = total > pageable.getOffset() ? pagedQuery.list(qEntity) : Collections.<E> emptyList();
    return new PageImpl<>(content, pageable, total);
  }

}
