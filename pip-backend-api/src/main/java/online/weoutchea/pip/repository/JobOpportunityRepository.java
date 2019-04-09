package online.weoutchea.pip.repository;

import online.weoutchea.pip.domain.JobOpportunity;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the JobOpportunity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, Long> {

    @Query("select job_opportunity from JobOpportunity job_opportunity where job_opportunity.user.login = ?#{principal.username}")
    List<JobOpportunity> findByUserIsCurrentUser();

}
