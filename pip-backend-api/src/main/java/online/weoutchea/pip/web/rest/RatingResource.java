package online.weoutchea.pip.web.rest;

import com.codahale.metrics.annotation.Timed;
import online.weoutchea.pip.domain.Rating;

import online.weoutchea.pip.repository.RatingRepository;
import online.weoutchea.pip.repository.search.RatingSearchRepository;
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
 * REST controller for managing Rating.
 */
@RestController
@RequestMapping("/api")
public class RatingResource {

    private final Logger log = LoggerFactory.getLogger(RatingResource.class);

    private static final String ENTITY_NAME = "rating";

    private final RatingRepository ratingRepository;

    private final RatingSearchRepository ratingSearchRepository;

    public RatingResource(RatingRepository ratingRepository, RatingSearchRepository ratingSearchRepository) {
        this.ratingRepository = ratingRepository;
        this.ratingSearchRepository = ratingSearchRepository;
    }

    /**
     * POST  /ratings : Create a new rating.
     *
     * @param rating the rating to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rating, or with status 400 (Bad Request) if the rating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ratings")
    @Timed
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) throws URISyntaxException {
        log.debug("REST request to save Rating : {}", rating);
        if (rating.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rating cannot already have an ID")).body(null);
        }
        Rating result = ratingRepository.save(rating);
        ratingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ratings : Updates an existing rating.
     *
     * @param rating the rating to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rating,
     * or with status 400 (Bad Request) if the rating is not valid,
     * or with status 500 (Internal Server Error) if the rating couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ratings")
    @Timed
    public ResponseEntity<Rating> updateRating(@RequestBody Rating rating) throws URISyntaxException {
        log.debug("REST request to update Rating : {}", rating);
        if (rating.getId() == null) {
            return createRating(rating);
        }
        Rating result = ratingRepository.save(rating);
        ratingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rating.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ratings : get all the ratings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ratings in body
     */
    @GetMapping("/ratings")
    @Timed
    public List<Rating> getAllRatings() {
        log.debug("REST request to get all Ratings");
        return ratingRepository.findAll();
        }

    /**
     * GET  /ratings/:id : get the "id" rating.
     *
     * @param id the id of the rating to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rating, or with status 404 (Not Found)
     */
    @GetMapping("/ratings/{id}")
    @Timed
    public ResponseEntity<Rating> getRating(@PathVariable Long id) {
        log.debug("REST request to get Rating : {}", id);
        Rating rating = ratingRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rating));
    }

    /**
     * DELETE  /ratings/:id : delete the "id" rating.
     *
     * @param id the id of the rating to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ratings/{id}")
    @Timed
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        log.debug("REST request to delete Rating : {}", id);
        ratingRepository.delete(id);
        ratingSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ratings?query=:query : search for the rating corresponding
     * to the query.
     *
     * @param query the query of the rating search
     * @return the result of the search
     */
    @GetMapping("/_search/ratings")
    @Timed
    public List<Rating> searchRatings(@RequestParam String query) {
        log.debug("REST request to search Ratings for query {}", query);
        return StreamSupport
            .stream(ratingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
