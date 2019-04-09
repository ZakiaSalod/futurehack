package online.weoutchea.pip.repository.search;

import online.weoutchea.pip.domain.Rating;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Rating entity.
 */
public interface RatingSearchRepository extends ElasticsearchRepository<Rating, Long> {
}
