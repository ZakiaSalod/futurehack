package online.weoutchea.pip.repository.search;

import online.weoutchea.pip.domain.JobOpportunity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobOpportunity entity.
 */
public interface JobOpportunitySearchRepository extends ElasticsearchRepository<JobOpportunity, Long> {
}
