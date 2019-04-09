package online.weoutchea.pip.web.rest;

import com.codahale.metrics.annotation.Timed;
import online.weoutchea.pip.domain.BusinessUnit;

import online.weoutchea.pip.repository.BusinessUnitRepository;
import online.weoutchea.pip.repository.search.BusinessUnitSearchRepository;
import online.weoutchea.pip.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BusinessUnit.
 */
@RestController
@RequestMapping("/api")
public class BusinessUnitResource {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitResource.class);

    private static final String ENTITY_NAME = "businessUnit";

    private final BusinessUnitRepository businessUnitRepository;

    private final BusinessUnitSearchRepository businessUnitSearchRepository;

    public BusinessUnitResource(BusinessUnitRepository businessUnitRepository, BusinessUnitSearchRepository businessUnitSearchRepository) {
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitSearchRepository = businessUnitSearchRepository;
    }

    /**
     * POST  /business-units : Create a new businessUnit.
     *
     * @param businessUnit the businessUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new businessUnit, or with status 400 (Bad Request) if the businessUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/business-units")
    @Timed
    public ResponseEntity<BusinessUnit> createBusinessUnit(@Valid @RequestBody BusinessUnit businessUnit) throws URISyntaxException {
        log.debug("REST request to save BusinessUnit : {}", businessUnit);
        if (businessUnit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new businessUnit cannot already have an ID")).body(null);
        }
        BusinessUnit result = businessUnitRepository.save(businessUnit);
        businessUnitSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/business-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /business-units : Updates an existing businessUnit.
     *
     * @param businessUnit the businessUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated businessUnit,
     * or with status 400 (Bad Request) if the businessUnit is not valid,
     * or with status 500 (Internal Server Error) if the businessUnit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/business-units")
    @Timed
    public ResponseEntity<BusinessUnit> updateBusinessUnit(@Valid @RequestBody BusinessUnit businessUnit) throws URISyntaxException {
        log.debug("REST request to update BusinessUnit : {}", businessUnit);
        if (businessUnit.getId() == null) {
            return createBusinessUnit(businessUnit);
        }
        BusinessUnit result = businessUnitRepository.save(businessUnit);
        businessUnitSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, businessUnit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /business-units : get all the businessUnits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of businessUnits in body
     */
    @GetMapping("/business-units")
    @Timed
    public List<BusinessUnit> getAllBusinessUnits() {
        log.debug("REST request to get all BusinessUnits");
        return businessUnitRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /business-units/:id : get the "id" businessUnit.
     *
     * @param id the id of the businessUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the businessUnit, or with status 404 (Not Found)
     */
    @GetMapping("/business-units/{id}")
    @Timed
    public ResponseEntity<BusinessUnit> getBusinessUnit(@PathVariable Long id) {
        log.debug("REST request to get BusinessUnit : {}", id);
        BusinessUnit businessUnit = businessUnitRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(businessUnit));
    }

    /**
     * DELETE  /business-units/:id : delete the "id" businessUnit.
     *
     * @param id the id of the businessUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/business-units/{id}")
    @Timed
    public ResponseEntity<Void> deleteBusinessUnit(@PathVariable Long id) {
        log.debug("REST request to delete BusinessUnit : {}", id);
        businessUnitRepository.delete(id);
        businessUnitSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/business-units?query=:query : search for the businessUnit corresponding
     * to the query.
     *
     * @param query the query of the businessUnit search
     * @return the result of the search
     */
    @GetMapping("/_search/business-units")
    @Timed
    public List<BusinessUnit> searchBusinessUnits(@RequestParam String query) {
        log.debug("REST request to search BusinessUnits for query {}", query);
        return StreamSupport
            .stream(businessUnitSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
