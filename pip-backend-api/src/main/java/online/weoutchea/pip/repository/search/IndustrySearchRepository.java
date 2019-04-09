package online.weoutchea.pip.repository.search;

import online.weoutchea.pip.domain.Industry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Industry entity.
 */
public interface IndustrySearchRepository extends ElasticsearchRepository<Industry, Long> {
}
