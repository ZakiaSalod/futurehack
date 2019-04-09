package online.weoutchea.pip.repository.search;

import online.weoutchea.pip.domain.Achievement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Achievement entity.
 */
public interface AchievementSearchRepository extends ElasticsearchRepository<Achievement, Long> {
}
