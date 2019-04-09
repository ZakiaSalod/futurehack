package online.weoutchea.pip.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import online.weoutchea.pip.domain.enumeration.JobShadowType;

/**
 * A JobShadow.
 */
@Entity
@Table(name = "job_shadow")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jobshadow")
public class JobShadow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datetime")
    private ZonedDateTime datetime;

    @Column(name = "capacity")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_shadow_type")
    private JobShadowType jobShadowType;

    @Column(name = "transport")
    private Boolean transport;

    @Column(name = "lunch")
    private Boolean lunch;

    @ManyToOne
    private BusinessUnit businessUnit;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public JobShadow datetime(ZonedDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public JobShadow capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public JobShadowType getJobShadowType() {
        return jobShadowType;
    }

    public JobShadow jobShadowType(JobShadowType jobShadowType) {
        this.jobShadowType = jobShadowType;
        return this;
    }

    public void setJobShadowType(JobShadowType jobShadowType) {
        this.jobShadowType = jobShadowType;
    }

    public Boolean isTransport() {
        return transport;
    }

    public JobShadow transport(Boolean transport) {
        this.transport = transport;
        return this;
    }

    public void setTransport(Boolean transport) {
        this.transport = transport;
    }

    public Boolean isLunch() {
        return lunch;
    }

    public JobShadow lunch(Boolean lunch) {
        this.lunch = lunch;
        return this;
    }

    public void setLunch(Boolean lunch) {
        this.lunch = lunch;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public JobShadow businessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
        return this;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public User getUser() {
        return user;
    }

    public JobShadow user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        JobShadow jobShadow = (JobShadow) o;
        if (jobShadow.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobShadow.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobShadow{" +
            "id=" + getId() +
            ", datetime='" + getDatetime() + "'" +
            ", capacity='" + getCapacity() + "'" +
            ", jobShadowType='" + getJobShadowType() + "'" +
            ", transport='" + isTransport() + "'" +
            ", lunch='" + isLunch() + "'" +
            "}";
    }
}
