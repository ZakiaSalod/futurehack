package online.weoutchea.pip.web.rest;

import online.weoutchea.pip.PipApp;

import online.weoutchea.pip.domain.BusinessUnit;
import online.weoutchea.pip.repository.BusinessUnitRepository;
import online.weoutchea.pip.repository.search.BusinessUnitSearchRepository;
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
 * Test class for the BusinessUnitResource REST controller.
 *
 * @see BusinessUnitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PipApp.class)
public class BusinessUnitResourceIntTest {

    private static final String DEFAULT_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SIZE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private BusinessUnitSearchRepository businessUnitSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBusinessUnitMockMvc;

    private BusinessUnit businessUnit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BusinessUnitResource businessUnitResource = new BusinessUnitResource(businessUnitRepository, businessUnitSearchRepository);
        this.restBusinessUnitMockMvc = MockMvcBuilders.standaloneSetup(businessUnitResource)
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
    public static BusinessUnit createEntity(EntityManager em) {
        BusinessUnit businessUnit = new BusinessUnit()
            .unitName(DEFAULT_UNIT_NAME)
            .size(DEFAULT_SIZE)
            .address(DEFAULT_ADDRESS);
        return businessUnit;
    }

    @Before
    public void initTest() {
        businessUnitSearchRepository.deleteAll();
        businessUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusinessUnit() throws Exception {
        int databaseSizeBeforeCreate = businessUnitRepository.findAll().size();

        // Create the BusinessUnit
        restBusinessUnitMockMvc.perform(post("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnit)))
            .andExpect(status().isCreated());

        // Validate the BusinessUnit in the database
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessUnit testBusinessUnit = businessUnitList.get(businessUnitList.size() - 1);
        assertThat(testBusinessUnit.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testBusinessUnit.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testBusinessUnit.getAddress()).isEqualTo(DEFAULT_ADDRESS);

        // Validate the BusinessUnit in Elasticsearch
        BusinessUnit businessUnitEs = businessUnitSearchRepository.findOne(testBusinessUnit.getId());
        assertThat(businessUnitEs).isEqualToComparingFieldByField(testBusinessUnit);
    }

    @Test
    @Transactional
    public void createBusinessUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = businessUnitRepository.findAll().size();

        // Create the BusinessUnit with an existing ID
        businessUnit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessUnitMockMvc.perform(post("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnit)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessUnit in the database
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUnitNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessUnitRepository.findAll().size();
        // set the field null
        businessUnit.setUnitName(null);

        // Create the BusinessUnit, which fails.

        restBusinessUnitMockMvc.perform(post("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnit)))
            .andExpect(status().isBadRequest());

        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBusinessUnits() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get all the businessUnitList
        restBusinessUnitMockMvc.perform(get("/api/business-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getBusinessUnit() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);

        // Get the businessUnit
        restBusinessUnitMockMvc.perform(get("/api/business-units/{id}", businessUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(businessUnit.getId().intValue()))
            .andExpect(jsonPath("$.unitName").value(DEFAULT_UNIT_NAME.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusinessUnit() throws Exception {
        // Get the businessUnit
        restBusinessUnitMockMvc.perform(get("/api/business-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessUnit() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);
        businessUnitSearchRepository.save(businessUnit);
        int databaseSizeBeforeUpdate = businessUnitRepository.findAll().size();

        // Update the businessUnit
        BusinessUnit updatedBusinessUnit = businessUnitRepository.findOne(businessUnit.getId());
        updatedBusinessUnit
            .unitName(UPDATED_UNIT_NAME)
            .size(UPDATED_SIZE)
            .address(UPDATED_ADDRESS);

        restBusinessUnitMockMvc.perform(put("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBusinessUnit)))
            .andExpect(status().isOk());

        // Validate the BusinessUnit in the database
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeUpdate);
        BusinessUnit testBusinessUnit = businessUnitList.get(businessUnitList.size() - 1);
        assertThat(testBusinessUnit.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testBusinessUnit.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testBusinessUnit.getAddress()).isEqualTo(UPDATED_ADDRESS);

        // Validate the BusinessUnit in Elasticsearch
        BusinessUnit businessUnitEs = businessUnitSearchRepository.findOne(testBusinessUnit.getId());
        assertThat(businessUnitEs).isEqualToComparingFieldByField(testBusinessUnit);
    }

    @Test
    @Transactional
    public void updateNonExistingBusinessUnit() throws Exception {
        int databaseSizeBeforeUpdate = businessUnitRepository.findAll().size();

        // Create the BusinessUnit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBusinessUnitMockMvc.perform(put("/api/business-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessUnit)))
            .andExpect(status().isCreated());

        // Validate the BusinessUnit in the database
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBusinessUnit() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);
        businessUnitSearchRepository.save(businessUnit);
        int databaseSizeBeforeDelete = businessUnitRepository.findAll().size();

        // Get the businessUnit
        restBusinessUnitMockMvc.perform(delete("/api/business-units/{id}", businessUnit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean businessUnitExistsInEs = businessUnitSearchRepository.exists(businessUnit.getId());
        assertThat(businessUnitExistsInEs).isFalse();

        // Validate the database is empty
        List<BusinessUnit> businessUnitList = businessUnitRepository.findAll();
        assertThat(businessUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBusinessUnit() throws Exception {
        // Initialize the database
        businessUnitRepository.saveAndFlush(businessUnit);
        businessUnitSearchRepository.save(businessUnit);

        // Search the businessUnit
        restBusinessUnitMockMvc.perform(get("/api/_search/business-units?query=id:" + businessUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessUnit.class);
        BusinessUnit businessUnit1 = new BusinessUnit();
        businessUnit1.setId(1L);
        BusinessUnit businessUnit2 = new BusinessUnit();
        businessUnit2.setId(businessUnit1.getId());
        assertThat(businessUnit1).isEqualTo(businessUnit2);
        businessUnit2.setId(2L);
        assertThat(businessUnit1).isNotEqualTo(businessUnit2);
        businessUnit1.setId(null);
        assertThat(businessUnit1).isNotEqualTo(businessUnit2);
    }
}
