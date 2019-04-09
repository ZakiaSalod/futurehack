package online.weoutchea.pip.web.rest;

import online.weoutchea.pip.PipApp;

import online.weoutchea.pip.domain.JobOpportunity;
import online.weoutchea.pip.repository.JobOpportunityRepository;
import online.weoutchea.pip.repository.UserRepository;
import online.weoutchea.pip.repository.search.JobOpportunitySearchRepository;
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
 * Test class for the JobOpportunityResource REST controller.
 *
 * @see JobOpportunityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PipApp.class)
public class JobOpportunityResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SALARY = 1;
    private static final Integer UPDATED_SALARY = 2;

    private static final Integer DEFAULT_DISTANCE = 1;
    private static final Integer UPDATED_DISTANCE = 2;

    private static final Integer DEFAULT_FLEXIBILITY = 1;
    private static final Integer UPDATED_FLEXIBILITY = 2;

    private static final Integer DEFAULT_LONGEVITY = 1;
    private static final Integer UPDATED_LONGEVITY = 2;

    @Autowired
    private JobOpportunityRepository jobOpportunityRepository;

    @Autowired
    private JobOpportunitySearchRepository jobOpportunitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    private MockMvc restJobOpportunityMockMvc;

    private JobOpportunity jobOpportunity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobOpportunityResource jobOpportunityResource = new JobOpportunityResource(jobOpportunityRepository, jobOpportunitySearchRepository, userRepository);
        this.restJobOpportunityMockMvc = MockMvcBuilders.standaloneSetup(jobOpportunityResource)
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
    public static JobOpportunity createEntity(EntityManager em) {
        JobOpportunity jobOpportunity = new JobOpportunity()
            .title(DEFAULT_TITLE)
            .position(DEFAULT_POSITION)
            .salary(DEFAULT_SALARY)
            .distance(DEFAULT_DISTANCE)
            .flexibility(DEFAULT_FLEXIBILITY)
            .longevity(DEFAULT_LONGEVITY);
        return jobOpportunity;
    }

    @Before
    public void initTest() {
        jobOpportunitySearchRepository.deleteAll();
        jobOpportunity = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobOpportunity() throws Exception {
        int databaseSizeBeforeCreate = jobOpportunityRepository.findAll().size();

        // Create the JobOpportunity
        restJobOpportunityMockMvc.perform(post("/api/job-opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOpportunity)))
            .andExpect(status().isCreated());

        // Validate the JobOpportunity in the database
        List<JobOpportunity> jobOpportunityList = jobOpportunityRepository.findAll();
        assertThat(jobOpportunityList).hasSize(databaseSizeBeforeCreate + 1);
        JobOpportunity testJobOpportunity = jobOpportunityList.get(jobOpportunityList.size() - 1);
        assertThat(testJobOpportunity.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobOpportunity.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testJobOpportunity.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testJobOpportunity.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testJobOpportunity.getFlexibility()).isEqualTo(DEFAULT_FLEXIBILITY);
        assertThat(testJobOpportunity.getLongevity()).isEqualTo(DEFAULT_LONGEVITY);

        // Validate the JobOpportunity in Elasticsearch
        JobOpportunity jobOpportunityEs = jobOpportunitySearchRepository.findOne(testJobOpportunity.getId());
        assertThat(jobOpportunityEs).isEqualToComparingFieldByField(testJobOpportunity);
    }

    @Test
    @Transactional
    public void createJobOpportunityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobOpportunityRepository.findAll().size();

        // Create the JobOpportunity with an existing ID
        jobOpportunity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobOpportunityMockMvc.perform(post("/api/job-opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOpportunity)))
            .andExpect(status().isBadRequest());

        // Validate the JobOpportunity in the database
        List<JobOpportunity> jobOpportunityList = jobOpportunityRepository.findAll();
        assertThat(jobOpportunityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobOpportunities() throws Exception {
        // Initialize the database
        jobOpportunityRepository.saveAndFlush(jobOpportunity);

        // Get all the jobOpportunityList
        restJobOpportunityMockMvc.perform(get("/api/job-opportunities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobOpportunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.[*].flexibility").value(hasItem(DEFAULT_FLEXIBILITY)))
            .andExpect(jsonPath("$.[*].longevity").value(hasItem(DEFAULT_LONGEVITY)));
    }

    @Test
    @Transactional
    public void getJobOpportunity() throws Exception {
        // Initialize the database
        jobOpportunityRepository.saveAndFlush(jobOpportunity);

        // Get the jobOpportunity
        restJobOpportunityMockMvc.perform(get("/api/job-opportunities/{id}", jobOpportunity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobOpportunity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE))
            .andExpect(jsonPath("$.flexibility").value(DEFAULT_FLEXIBILITY))
            .andExpect(jsonPath("$.longevity").value(DEFAULT_LONGEVITY));
    }

    @Test
    @Transactional
    public void getNonExistingJobOpportunity() throws Exception {
        // Get the jobOpportunity
        restJobOpportunityMockMvc.perform(get("/api/job-opportunities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobOpportunity() throws Exception {
        // Initialize the database
        jobOpportunityRepository.saveAndFlush(jobOpportunity);
        jobOpportunitySearchRepository.save(jobOpportunity);
        int databaseSizeBeforeUpdate = jobOpportunityRepository.findAll().size();

        // Update the jobOpportunity
        JobOpportunity updatedJobOpportunity = jobOpportunityRepository.findOne(jobOpportunity.getId());
        updatedJobOpportunity
            .title(UPDATED_TITLE)
            .position(UPDATED_POSITION)
            .salary(UPDATED_SALARY)
            .distance(UPDATED_DISTANCE)
            .flexibility(UPDATED_FLEXIBILITY)
            .longevity(UPDATED_LONGEVITY);

        restJobOpportunityMockMvc.perform(put("/api/job-opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobOpportunity)))
            .andExpect(status().isOk());

        // Validate the JobOpportunity in the database
        List<JobOpportunity> jobOpportunityList = jobOpportunityRepository.findAll();
        assertThat(jobOpportunityList).hasSize(databaseSizeBeforeUpdate);
        JobOpportunity testJobOpportunity = jobOpportunityList.get(jobOpportunityList.size() - 1);
        assertThat(testJobOpportunity.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJobOpportunity.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testJobOpportunity.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testJobOpportunity.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testJobOpportunity.getFlexibility()).isEqualTo(UPDATED_FLEXIBILITY);
        assertThat(testJobOpportunity.getLongevity()).isEqualTo(UPDATED_LONGEVITY);

        // Validate the JobOpportunity in Elasticsearch
        JobOpportunity jobOpportunityEs = jobOpportunitySearchRepository.findOne(testJobOpportunity.getId());
        assertThat(jobOpportunityEs).isEqualToComparingFieldByField(testJobOpportunity);
    }

    @Test
    @Transactional
    public void updateNonExistingJobOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = jobOpportunityRepository.findAll().size();

        // Create the JobOpportunity

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobOpportunityMockMvc.perform(put("/api/job-opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobOpportunity)))
            .andExpect(status().isCreated());

        // Validate the JobOpportunity in the database
        List<JobOpportunity> jobOpportunityList = jobOpportunityRepository.findAll();
        assertThat(jobOpportunityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobOpportunity() throws Exception {
        // Initialize the database
        jobOpportunityRepository.saveAndFlush(jobOpportunity);
        jobOpportunitySearchRepository.save(jobOpportunity);
        int databaseSizeBeforeDelete = jobOpportunityRepository.findAll().size();

        // Get the jobOpportunity
        restJobOpportunityMockMvc.perform(delete("/api/job-opportunities/{id}", jobOpportunity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobOpportunityExistsInEs = jobOpportunitySearchRepository.exists(jobOpportunity.getId());
        assertThat(jobOpportunityExistsInEs).isFalse();

        // Validate the database is empty
        List<JobOpportunity> jobOpportunityList = jobOpportunityRepository.findAll();
        assertThat(jobOpportunityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobOpportunity() throws Exception {
        // Initialize the database
        jobOpportunityRepository.saveAndFlush(jobOpportunity);
        jobOpportunitySearchRepository.save(jobOpportunity);

        // Search the jobOpportunity
        restJobOpportunityMockMvc.perform(get("/api/_search/job-opportunities?query=id:" + jobOpportunity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobOpportunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.[*].flexibility").value(hasItem(DEFAULT_FLEXIBILITY)))
            .andExpect(jsonPath("$.[*].longevity").value(hasItem(DEFAULT_LONGEVITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobOpportunity.class);
        JobOpportunity jobOpportunity1 = new JobOpportunity();
        jobOpportunity1.setId(1L);
        JobOpportunity jobOpportunity2 = new JobOpportunity();
        jobOpportunity2.setId(jobOpportunity1.getId());
        assertThat(jobOpportunity1).isEqualTo(jobOpportunity2);
        jobOpportunity2.setId(2L);
        assertThat(jobOpportunity1).isNotEqualTo(jobOpportunity2);
        jobOpportunity1.setId(null);
        assertThat(jobOpportunity1).isNotEqualTo(jobOpportunity2);
    }
}
