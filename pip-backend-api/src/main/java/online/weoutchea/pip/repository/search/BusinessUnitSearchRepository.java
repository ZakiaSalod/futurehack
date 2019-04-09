package online.weoutchea.pip.repository.search;

import online.weoutchea.pip.domain.BusinessUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BusinessUnit entity.
 */
public interface BusinessUnitSearchRepository extends ElasticsearchRepository<BusinessUnit, Long> {
}
