package online.weoutchea.pip.web.rest;

import online.weoutchea.pip.PipApp;

import online.weoutchea.pip.domain.JobShadow;
import online.weoutchea.pip.repository.JobShadowRepository;
import online.weoutchea.pip.repository.search.JobShadowSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static online.weoutchea.pip.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import online.weoutchea.pip.domain.enumeration.JobShadowType;
/**
 * Test class for the JobShadowResource REST controller.
 *
 * @see JobShadowResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PipApp.class)
public class JobShadowResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final JobShadowType DEFAULT_JOB_SHADOW_TYPE = JobShadowType.INDIVIDUAL;
    private static final JobShadowType UPDATED_JOB_SHADOW_TYPE = JobShadowType.MEDIUM;

    private static final Boolean DEFAULT_TRANSPORT = false;
    private static final Boolean UPDATED_TRANSPORT = true;

    private static final Boolean DEFAULT_LUNCH = false;
    private static final Boolean UPDATED_LUNCH = true;

    @Autowired
    private JobShadowRepository jobShadowRepository;

    @Autowired
    private JobShadowSearchRepository jobShadowSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobShadowMockMvc;

    private JobShadow jobShadow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobShadowResource jobShadowResource = new JobShadowResource(jobShadowRepository, jobShadowSearchRepository);
        this.restJobShadowMockMvc = MockMvcBuilders.standaloneSetup(jobShadowResource)
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
    public static JobShadow createEntity(EntityManager em) {
        JobShadow jobShadow = new JobShadow()
            .datetime(DEFAULT_DATETIME)
            .capacity(DEFAULT_CAPACITY)
            .jobShadowType(DEFAULT_JOB_SHADOW_TYPE)
            .transport(DEFAULT_TRANSPORT)
            .lunch(DEFAULT_LUNCH);
        return jobShadow;
    }

    @Before
    public void initTest() {
        jobShadowSearchRepository.deleteAll();
        jobShadow = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobShadow() throws Exception {
        int databaseSizeBeforeCreate = jobShadowRepository.findAll().size();

        // Create the JobShadow
        restJobShadowMockMvc.perform(post("/api/job-shadows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobShadow)))
            .andExpect(status().isCreated());

        // Validate the JobShadow in the database
        List<JobShadow> jobShadowList = jobShadowRepository.findAll();
        assertThat(jobShadowList).hasSize(databaseSizeBeforeCreate + 1);
        JobShadow testJobShadow = jobShadowList.get(jobShadowList.size() - 1);
        assertThat(testJobShadow.getDatetime()).isEqualTo(DEFAULT_DATETIME);
        assertThat(testJobShadow.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testJobShadow.getJobShadowType()).isEqualTo(DEFAULT_JOB_SHADOW_TYPE);
        assertThat(testJobShadow.isTransport()).isEqualTo(DEFAULT_TRANSPORT);
        assertThat(testJobShadow.isLunch()).isEqualTo(DEFAULT_LUNCH);

        // Validate the JobShadow in Elasticsearch
        JobShadow jobShadowEs = jobShadowSearchRepository.findOne(testJobShadow.getId());
        assertThat(jobShadowEs).isEqualToComparingFieldByField(testJobShadow);
    }

    @Test
    @Transactional
    public void createJobShadowWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobShadowRepository.findAll().size();

        // Create the JobShadow with an existing ID
        jobShadow.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobShadowMockMvc.perform(post("/api/job-shadows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobShadow)))
            .andExpect(status().isBadRequest());

        // Validate the JobShadow in the database
        List<JobShadow> jobShadowList = jobShadowRepository.findAll();
        assertThat(jobShadowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobShadows() throws Exception {
        // Initialize the database
        jobShadowRepository.saveAndFlush(jobShadow);

        // Get all the jobShadowList
        restJobShadowMockMvc.perform(get("/api/job-shadows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobShadow.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].jobShadowType").value(hasItem(DEFAULT_JOB_SHADOW_TYPE.toString())))
            .andExpect(jsonPath("$.[*].transport").value(hasItem(DEFAULT_TRANSPORT.booleanValue())))
            .andExpect(jsonPath("$.[*].lunch").value(hasItem(DEFAULT_LUNCH.booleanValue())));
    }

    @Test
    @Transactional
    public void getJobShadow() throws Exception {
        // Initialize the database
        jobShadowRepository.saveAndFlush(jobShadow);

        // Get the jobShadow
        restJobShadowMockMvc.perform(get("/api/job-shadows/{id}", jobShadow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobShadow.getId().intValue()))
            .andExpect(jsonPath("$.datetime").value(sameInstant(DEFAULT_DATETIME)))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.jobShadowType").value(DEFAULT_JOB_SHADOW_TYPE.toString()))
            .andExpect(jsonPath("$.transport").value(DEFAULT_TRANSPORT.booleanValue()))
            .andExpect(jsonPath("$.lunch").value(DEFAULT_LUNCH.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJobShadow() throws Exception {
        // Get the jobShadow
        restJobShadowMockMvc.perform(get("/api/job-shadows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobShadow() throws Exception {
        // Initialize the database
        jobShadowRepository.saveAndFlush(jobShadow);
        jobShadowSearchRepository.save(jobShadow);
        int databaseSizeBeforeUpdate = jobShadowRepository.findAll().size();

        // Update the jobShadow
        JobShadow updatedJobShadow = jobShadowRepository.findOne(jobShadow.getId());
        updatedJobShadow
            .datetime(UPDATED_DATETIME)
            .capacity(UPDATED_CAPACITY)
            .jobShadowType(UPDATED_JOB_SHADOW_TYPE)
            .transport(UPDATED_TRANSPORT)
            .lunch(UPDATED_LUNCH);

        restJobShadowMockMvc.perform(put("/api/job-shadows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobShadow)))
            .andExpect(status().isOk());

        // Validate the JobShadow in the database
        List<JobShadow> jobShadowList = jobShadowRepository.findAll();
        assertThat(jobShadowList).hasSize(databaseSizeBeforeUpdate);
        JobShadow testJobShadow = jobShadowList.get(jobShadowList.size() - 1);
        assertThat(testJobShadow.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testJobShadow.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testJobShadow.getJobShadowType()).isEqualTo(UPDATED_JOB_SHADOW_TYPE);
        assertThat(testJobShadow.isTransport()).isEqualTo(UPDATED_TRANSPORT);
        assertThat(testJobShadow.isLunch()).isEqualTo(UPDATED_LUNCH);

        // Validate the JobShadow in Elasticsearch
        JobShadow jobShadowEs = jobShadowSearchRepository.findOne(testJobShadow.getId());
        assertThat(jobShadowEs).isEqualToComparingFieldByField(testJobShadow);
    }

    @Test
    @Transactional
    public void updateNonExistingJobShadow() throws Exception {
        int databaseSizeBeforeUpdate = jobShadowRepository.findAll().size();

        // Create the JobShadow

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobShadowMockMvc.perform(put("/api/job-shadows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobShadow)))
            .andExpect(status().isCreated());

        // Validate the JobShadow in the database
        List<JobShadow> jobShadowList = jobShadowRepository.findAll();
        assertThat(jobShadowList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobShadow() throws Exception {
        // Initialize the database
        jobShadowRepository.saveAndFlush(jobShadow);
        jobShadowSearchRepository.save(jobShadow);
        int databaseSizeBeforeDelete = jobShadowRepository.findAll().size();

        // Get the jobShadow
        restJobShadowMockMvc.perform(delete("/api/job-shadows/{id}", jobShadow.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobShadowExistsInEs = jobShadowSearchRepository.exists(jobShadow.getId());
        assertThat(jobShadowExistsInEs).isFalse();

        // Validate the database is empty
        List<JobShadow> jobShadowList = jobShadowRepository.findAll();
        assertThat(jobShadowList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobShadow() throws Exception {
        // Initialize the database
        jobShadowRepository.saveAndFlush(jobShadow);
        jobShadowSearchRepository.save(jobShadow);

        // Search the jobShadow
        restJobShadowMockMvc.perform(get("/api/_search/job-shadows?query=id:" + jobShadow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobShadow.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].jobShadowType").value(hasItem(DEFAULT_JOB_SHADOW_TYPE.toString())))
            .andExpect(jsonPath("$.[*].transport").value(hasItem(DEFAULT_TRANSPORT.booleanValue())))
            .andExpect(jsonPath("$.[*].lunch").value(hasItem(DEFAULT_LUNCH.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobShadow.class);
        JobShadow jobShadow1 = new JobShadow();
        jobShadow1.setId(1L);
        JobShadow jobShadow2 = new JobShadow();
        jobShadow2.setId(jobShadow1.getId());
        assertThat(jobShadow1).isEqualTo(jobShadow2);
        jobShadow2.setId(2L);
        assertThat(jobShadow1).isNotEqualTo(jobShadow2);
        jobShadow1.setId(null);
        assertThat(jobShadow1).isNotEqualTo(jobShadow2);
    }
}
