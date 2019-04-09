package online.weoutchea.pip.web.rest;

import com.codahale.metrics.annotation.Timed;
import online.weoutchea.pip.domain.Achievement;

import online.weoutchea.pip.repository.AchievementRepository;
import online.weoutchea.pip.repository.search.AchievementSearchRepository;
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
 * REST controller for managing Achievement.
 */
@RestController
@RequestMapping("/api")
public class AchievementResource {

    private final Logger log = LoggerFactory.getLogger(AchievementResource.class);

    private static final String ENTITY_NAME = "achievement";

    private final AchievementRepository achievementRepository;

    private final AchievementSearchRepository achievementSearchRepository;

    public AchievementResource(AchievementRepository achievementRepository, AchievementSearchRepository achievementSearchRepository) {
        this.achievementRepository = achievementRepository;
        this.achievementSearchRepository = achievementSearchRepository;
    }

    /**
     * POST  /achievements : Create a new achievement.
     *
     * @param achievement the achievement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new achievement, or with status 400 (Bad Request) if the achievement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/achievements")
    @Timed
    public ResponseEntity<Achievement> createAchievement(@Valid @RequestBody Achievement achievement) throws URISyntaxException {
        log.debug("REST request to save Achievement : {}", achievement);
        if (achievement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new achievement cannot already have an ID")).body(null);
        }
        Achievement result = achievementRepository.save(achievement);
        achievementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/achievements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /achievements : Updates an existing achievement.
     *
     * @param achievement the achievement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated achievement,
     * or with status 400 (Bad Request) if the achievement is not valid,
     * or with status 500 (Internal Server Error) if the achievement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/achievements")
    @Timed
    public ResponseEntity<Achievement> updateAchievement(@Valid @RequestBody Achievement achievement) throws URISyntaxException {
        log.debug("REST request to update Achievement : {}", achievement);
        if (achievement.getId() == null) {
            return createAchievement(achievement);
        }
        Achievement result = achievementRepository.save(achievement);
        achievementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, achievement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /achievements : get all the achievements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of achievements in body
     */
    @GetMapping("/achievements")
    @Timed
    public List<Achievement> getAllAchievements() {
        log.debug("REST request to get all Achievements");
        return achievementRepository.findAll();
        }

    /**
     * GET  /achievements/:id : get the "id" achievement.
     *
     * @param id the id of the achievement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the achievement, or with status 404 (Not Found)
     */
    @GetMapping("/achievements/{id}")
    @Timed
    public ResponseEntity<Achievement> getAchievement(@PathVariable Long id) {
        log.debug("REST request to get Achievement : {}", id);
        Achievement achievement = achievementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(achievement));
    }

    /**
     * DELETE  /achievements/:id : delete the "id" achievement.
     *
     * @param id the id of the achievement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/achievements/{id}")
    @Timed
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        log.debug("REST request to delete Achievement : {}", id);
        achievementRepository.delete(id);
        achievementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/achievements?query=:query : search for the achievement corresponding
     * to the query.
     *
     * @param query the query of the achievement search
     * @return the result of the search
     */
    @GetMapping("/_search/achievements")
    @Timed
    public List<Achievement> searchAchievements(@RequestParam String query) {
        log.debug("REST request to search Achievements for query {}", query);
        return StreamSupport
            .stream(achievementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
