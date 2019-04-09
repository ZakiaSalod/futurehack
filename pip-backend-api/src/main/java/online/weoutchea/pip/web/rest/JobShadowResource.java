package online.weoutchea.pip.web.rest;

import com.codahale.metrics.annotation.Timed;
import online.weoutchea.pip.domain.JobShadow;

import online.weoutchea.pip.repository.JobShadowRepository;
import online.weoutchea.pip.repository.UserRepository;
import online.weoutchea.pip.repository.search.JobShadowSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing JobShadow.
 */
@RestController
@RequestMapping("/api")
public class JobShadowResource {

    private final Logger log = LoggerFactory.getLogger(JobShadowResource.class);

    private static final String ENTITY_NAME = "jobShadow";

    private final JobShadowRepository jobShadowRepository;

    private final JobShadowSearchRepository jobShadowSearchRepository;

    private final UserRepository userRepository;

    public JobShadowResource(JobShadowRepository jobShadowRepository, JobShadowSearchRepository jobShadowSearchRepository, UserRepository userRepository) {
        this.jobShadowRepository = jobShadowRepository;
        this.jobShadowSearchRepository = jobShadowSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /job-shadows : Create a new jobShadow.
     *
     * @param jobShadow the jobShadow to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobShadow, or with status 400 (Bad Request) if the jobShadow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-shadows")
    @Timed
    public ResponseEntity<JobShadow> createJobShadow(@RequestBody JobShadow jobShadow) throws URISyntaxException {
        log.debug("REST request to save JobShadow : {}", jobShadow);
        if (jobShadow.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobShadow cannot already have an ID")).body(null);
        }

        jobShadow.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null));

        JobShadow result = jobShadowRepository.save(jobShadow);
        jobShadowSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/job-shadows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-shadows : Updates an existing jobShadow.
     *
     * @param jobShadow the jobShadow to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobShadow,
     * or with status 400 (Bad Request) if the jobShadow is not valid,
     * or with status 500 (Internal Server Error) if the jobShadow couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-shadows")
    @Timed
    public ResponseEntity<JobShadow> updateJobShadow(@RequestBody JobShadow jobShadow) throws URISyntaxException {
        log.debug("REST request to update JobShadow : {}", jobShadow);
        if (jobShadow.getId() == null) {
            return createJobShadow(jobShadow);
        }
        JobShadow result = jobShadowRepository.save(jobShadow);
        jobShadowSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobShadow.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-shadows : get all the jobShadows.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobShadows in body
     */
    @GetMapping("/job-shadows")
    @Timed
    public List<JobShadow> getAllJobShadows() {
        log.debug("REST request to get all JobShadows");
        return jobShadowRepository.findAll();
        }

    /**
     * GET  /job-shadows/:id : get the "id" jobShadow.
     *
     * @param id the id of the jobShadow to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobShadow, or with status 404 (Not Found)
     */
    @GetMapping("/job-shadows/{id}")
    @Timed
    public ResponseEntity<JobShadow> getJobShadow(@PathVariable Long id) {
        log.debug("REST request to get JobShadow : {}", id);
        JobShadow jobShadow = jobShadowRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobShadow));
    }

    /**
     * DELETE  /job-shadows/:id : delete the "id" jobShadow.
     *
     * @param id the id of the jobShadow to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-shadows/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobShadow(@PathVariable Long id) {
        log.debug("REST request to delete JobShadow : {}", id);
        jobShadowRepository.delete(id);
        jobShadowSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-shadows?query=:query : search for the jobShadow corresponding
     * to the query.
     *
     * @param query the query of the jobShadow search
     * @return the result of the search
     */
    @GetMapping("/_search/job-shadows")
    @Timed
    public List<JobShadow> searchJobShadows(@RequestParam String query) {
        log.debug("REST request to search JobShadows for query {}", query);
        return StreamSupport
            .stream(jobShadowSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
