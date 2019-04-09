package online.weoutchea.pip.repository.search;

import online.weoutchea.pip.domain.JobShadow;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobShadow entity.
 */
public interface JobShadowSearchRepository extends ElasticsearchRepository<JobShadow, Long> {
}
