package online.weoutchea.pip.repository;

import online.weoutchea.pip.domain.Rating;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select rating from Rating rating where rating.user.login = ?#{principal.username}")
    List<Rating> findByUserIsCurrentUser();

}
