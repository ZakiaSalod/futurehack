package online.weoutchea.pip.repository;

import online.weoutchea.pip.domain.BusinessUnit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the BusinessUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {
    @Query("select distinct business_unit from BusinessUnit business_unit left join fetch business_unit.industries")
    List<BusinessUnit> findAllWithEagerRelationships();

    @Query("select business_unit from BusinessUnit business_unit left join fetch business_unit.industries where business_unit.id =:id")
    BusinessUnit findOneWithEagerRelationships(@Param("id") Long id);

}
