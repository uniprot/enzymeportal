package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.model.dao.Summary;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalSummaryRepositoryCustom {

    List<Summary> findSummariesByCommentType(String commentType);

    Stream<Summary> streamSummariesByCommentType(String commentType);

}
