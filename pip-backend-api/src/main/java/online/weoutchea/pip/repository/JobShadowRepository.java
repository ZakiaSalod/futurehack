package online.weoutchea.pip.repository;

import online.weoutchea.pip.domain.JobShadow;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the JobShadow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobShadowRepository extends JpaRepository<JobShadow, Long> {

    @Query("select job_shadow from JobShadow job_shadow where job_shadow.user.login = ?#{principal.username}")
    List<JobShadow> findByUserIsCurrentUser();

}
