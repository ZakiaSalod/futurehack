package online.weoutchea.pip.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BusinessUnit.
 */
@Entity
@Table(name = "business_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "businessunit")
public class BusinessUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(name = "jhi_size")
    private String size;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "businessUnit")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JobShadow> jobShadows = new HashSet<>();

    @OneToMany(mappedBy = "businessUnit")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JobOpportunity> opportunities = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "business_unit_industry",
               joinColumns = @JoinColumn(name="business_units_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="industries_id", referencedColumnName="id"))
    private Set<Industry> industries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public BusinessUnit unitName(String unitName) {
        this.unitName = unitName;
        return this;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSize() {
        return size;
    }

    public BusinessUnit size(String size) {
        this.size = size;
        return this;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAddress() {
        return address;
    }

    public BusinessUnit address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<JobShadow> getJobShadows() {
        return jobShadows;
    }

    public BusinessUnit jobShadows(Set<JobShadow> jobShadows) {
        this.jobShadows = jobShadows;
        return this;
    }

    public BusinessUnit addJobShadow(JobShadow jobShadow) {
        this.jobShadows.add(jobShadow);
        jobShadow.setBusinessUnit(this);
        return this;
    }

    public BusinessUnit removeJobShadow(JobShadow jobShadow) {
        this.jobShadows.remove(jobShadow);
        jobShadow.setBusinessUnit(null);
        return this;
    }

    public void setJobShadows(Set<JobShadow> jobShadows) {
        this.jobShadows = jobShadows;
    }

    public Set<JobOpportunity> getOpportunities() {
        return opportunities;
    }

    public BusinessUnit opportunities(Set<JobOpportunity> jobOpportunities) {
        this.opportunities = jobOpportunities;
        return this;
    }

    public BusinessUnit addOpportunity(JobOpportunity jobOpportunity) {
        this.opportunities.add(jobOpportunity);
        jobOpportunity.setBusinessUnit(this);
        return this;
    }

    public BusinessUnit removeOpportunity(JobOpportunity jobOpportunity) {
        this.opportunities.remove(jobOpportunity);
        jobOpportunity.setBusinessUnit(null);
        return this;
    }

    public void setOpportunities(Set<JobOpportunity> jobOpportunities) {
        this.opportunities = jobOpportunities;
    }

    public Set<Industry> getIndustries() {
        return industries;
    }

    public BusinessUnit industries(Set<Industry> industries) {
        this.industries = industries;
        return this;
    }

    public BusinessUnit addIndustry(Industry industry) {
        this.industries.add(industry);
        return this;
    }

    public BusinessUnit removeIndustry(Industry industry) {
        this.industries.remove(industry);
        return this;
    }

    public void setIndustries(Set<Industry> industries) {
        this.industries = industries;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessUnit businessUnit = (BusinessUnit) o;
        if (businessUnit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), businessUnit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BusinessUnit{" +
            "id=" + getId() +
            ", unitName='" + getUnitName() + "'" +
            ", size='" + getSize() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
