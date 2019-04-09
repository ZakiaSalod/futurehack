package online.weoutchea.pip.web.rest;

import com.codahale.metrics.annotation.Timed;
import online.weoutchea.pip.domain.JobOpportunity;

import online.weoutchea.pip.repository.JobOpportunityRepository;
import online.weoutchea.pip.repository.UserRepository;
import online.weoutchea.pip.repository.search.JobOpportunitySearchRepository;
import online.weoutchea.pip.security.SecurityUtils;
import online.weoutchea.pip.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing JobOpportunity.
 */
@RestController
@RequestMapping("/api")
public class JobOpportunityResource {

    private final Logger log = LoggerFactory.getLogger(JobOpportunityResource.class);

    private static final String ENTITY_NAME = "jobOpportunity";

    private final JobOpportunityRepository jobOpportunityRepository;

    private final JobOpportunitySearchRepository jobOpportunitySearchRepository;

    private final UserRepository userRepository;

    public JobOpportunityResource(JobOpportunityRepository jobOpportunityRepository, JobOpportunitySearchRepository jobOpportunitySearchRepository, UserRepository userRepository) {
        this.jobOpportunityRepository = jobOpportunityRepository;
        this.jobOpportunitySearchRepository = jobOpportunitySearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /job-opportunities : Create a new jobOpportunity.
     *
     * @param jobOpportunity the jobOpportunity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobOpportunity, or with status 400 (Bad Request) if the jobOpportunity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-opportunities")
    @Timed
    public ResponseEntity<JobOpportunity> createJobOpportunity(@RequestBody JobOpportunity jobOpportunity) throws URISyntaxException {
        log.debug("REST request to save JobOpportunity : {}", jobOpportunity);
        if (jobOpportunity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobOpportunity cannot already have an ID")).body(null);
        }

        jobOpportunity.setDistance(getRandRating());
        jobOpportunity.setFlexibility(getRandRating());
        jobOpportunity.setLongevity(getRandRating());

        jobOpportunity.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null));

        JobOpportunity result = jobOpportunityRepository.save(jobOpportunity);
        jobOpportunitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/job-opportunities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private Integer getRandRating() {
        return ThreadLocalRandom.current().nextInt(3, 10 + 1);
    }

    /**
     * PUT  /job-opportunities : Updates an existing jobOpportunity.
     *
     * @param jobOpportunity the jobOpportunity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobOpportunity,
     * or with status 400 (Bad Request) if the jobOpportunity is not valid,
     * or with status 500 (Internal Server Error) if the jobOpportunity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-opportunities")
    @Timed
    public ResponseEntity<JobOpportunity> updateJobOpportunity(@RequestBody JobOpportunity jobOpportunity) throws URISyntaxException {
        log.debug("REST request to update JobOpportunity : {}", jobOpportunity);
        if (jobOpportunity.getId() == null) {
            return createJobOpportunity(jobOpportunity);
        }
        JobOpportunity result = jobOpportunityRepository.save(jobOpportunity);
        jobOpportunitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobOpportunity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-opportunities : get all the jobOpportunities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobOpportunities in body
     */
    @GetMapping("/job-opportunities")
    @Timed
    public List<JobOpportunity> getAllJobOpportunities() {
        log.debug("REST request to get all JobOpportunities");
        return jobOpportunityRepository.findAll();
        }

    /**
     * GET  /job-opportunities/:id : get the "id" jobOpportunity.
     *
     * @param id the id of the jobOpportunity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobOpportunity, or with status 404 (Not Found)
     */
    @GetMapping("/job-opportunities/{id}")
    @Timed
    public ResponseEntity<JobOpportunity> getJobOpportunity(@PathVariable Long id) {
        log.debug("REST request to get JobOpportunity : {}", id);
        JobOpportunity jobOpportunity = jobOpportunityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobOpportunity));
    }

    /**
     * DELETE  /job-opportunities/:id : delete the "id" jobOpportunity.
     *
     * @param id the id of the jobOpportunity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-opportunities/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobOpportunity(@PathVariable Long id) {
        log.debug("REST request to delete JobOpportunity : {}", id);
        jobOpportunityRepository.delete(id);
        jobOpportunitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-opportunities?query=:query : search for the jobOpportunity corresponding
     * to the query.
     *
     * @param query the query of the jobOpportunity search
     * @return the result of the search
     */
    @GetMapping("/_search/job-opportunities")
    @Timed
    public List<JobOpportunity> searchJobOpportunities(@RequestParam String query) {
        log.debug("REST request to search JobOpportunities for query {}", query);
        return StreamSupport
            .stream(jobOpportunitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
