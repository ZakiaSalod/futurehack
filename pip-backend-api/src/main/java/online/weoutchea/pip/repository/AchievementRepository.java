package online.weoutchea.pip.repository;

import online.weoutchea.pip.domain.Achievement;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Achievement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    @Query("select achievement from Achievement achievement where achievement.user.login = ?#{principal.username}")
    List<Achievement> findByUserIsCurrentUser();

}
