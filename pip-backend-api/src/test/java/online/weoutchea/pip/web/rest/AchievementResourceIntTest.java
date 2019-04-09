package online.weoutchea.pip.web.rest;

import online.weoutchea.pip.PipApp;

import online.weoutchea.pip.domain.Achievement;
import online.weoutchea.pip.repository.AchievementRepository;
import online.weoutchea.pip.repository.search.AchievementSearchRepository;
import online.weoutchea.pip.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AchievementResource REST controller.
 *
 * @see AchievementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PipApp.class)
public class AchievementResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private AchievementSearchRepository achievementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAchievementMockMvc;

    private Achievement achievement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AchievementResource achievementResource = new AchievementResource(achievementRepository, achievementSearchRepository);
        this.restAchievementMockMvc = MockMvcBuilders.standaloneSetup(achievementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Achievement createEntity(EntityManager em) {
        Achievement achievement = new Achievement()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .count(DEFAULT_COUNT)
            .total(DEFAULT_TOTAL);
        return achievement;
    }

    @Before
    public void initTest() {
        achievementSearchRepository.deleteAll();
        achievement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAchievement() throws Exception {
        int databaseSizeBeforeCreate = achievementRepository.findAll().size();

        // Create the Achievement
        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isCreated());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate + 1);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAchievement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAchievement.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testAchievement.getTotal()).isEqualTo(DEFAULT_TOTAL);

        // Validate the Achievement in Elasticsearch
        Achievement achievementEs = achievementSearchRepository.findOne(testAchievement.getId());
        assertThat(achievementEs).isEqualToComparingFieldByField(testAchievement);
    }

    @Test
    @Transactional
    public void createAchievementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = achievementRepository.findAll().size();

        // Create the Achievement with an existing ID
        achievement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = achievementRepository.findAll().size();
        // set the field null
        achievement.setTitle(null);

        // Create the Achievement, which fails.

        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isBadRequest());

        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = achievementRepository.findAll().size();
        // set the field null
        achievement.setDescription(null);

        // Create the Achievement, which fails.

        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isBadRequest());

        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = achievementRepository.findAll().size();
        // set the field null
        achievement.setTotal(null);

        // Create the Achievement, which fails.

        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isBadRequest());

        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAchievements() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get all the achievementList
        restAchievementMockMvc.perform(get("/api/achievements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(achievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    public void getAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get the achievement
        restAchievementMockMvc.perform(get("/api/achievements/{id}", achievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(achievement.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL));
    }

    @Test
    @Transactional
    public void getNonExistingAchievement() throws Exception {
        // Get the achievement
        restAchievementMockMvc.perform(get("/api/achievements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);
        achievementSearchRepository.save(achievement);
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement
        Achievement updatedAchievement = achievementRepository.findOne(achievement.getId());
        updatedAchievement
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .count(UPDATED_COUNT)
            .total(UPDATED_TOTAL);

        restAchievementMockMvc.perform(put("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAchievement)))
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAchievement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAchievement.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testAchievement.getTotal()).isEqualTo(UPDATED_TOTAL);

        // Validate the Achievement in Elasticsearch
        Achievement achievementEs = achievementSearchRepository.findOne(testAchievement.getId());
        assertThat(achievementEs).isEqualToComparingFieldByField(testAchievement);
    }

    @Test
    @Transactional
    public void updateNonExistingAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Create the Achievement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAchievementMockMvc.perform(put("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isCreated());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);
        achievementSearchRepository.save(achievement);
        int databaseSizeBeforeDelete = achievementRepository.findAll().size();

        // Get the achievement
        restAchievementMockMvc.perform(delete("/api/achievements/{id}", achievement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean achievementExistsInEs = achievementSearchRepository.exists(achievement.getId());
        assertThat(achievementExistsInEs).isFalse();

        // Validate the database is empty
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);
        achievementSearchRepository.save(achievement);

        // Search the achievement
        restAchievementMockMvc.perform(get("/api/_search/achievements?query=id:" + achievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(achievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Achievement.class);
        Achievement achievement1 = new Achievement();
        achievement1.setId(1L);
        Achievement achievement2 = new Achievement();
        achievement2.setId(achievement1.getId());
        assertThat(achievement1).isEqualTo(achievement2);
        achievement2.setId(2L);
        assertThat(achievement1).isNotEqualTo(achievement2);
        achievement1.setId(null);
        assertThat(achievement1).isNotEqualTo(achievement2);
    }
}
