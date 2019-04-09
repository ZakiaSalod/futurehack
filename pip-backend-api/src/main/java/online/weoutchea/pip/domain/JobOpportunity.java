package online.weoutchea.pip.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A JobOpportunity.
 */
@Entity
@Table(name = "job_opportunity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jobopportunity")
public class JobOpportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "position")
    private String position;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "flexibility")
    private Integer flexibility;

    @Column(name = "longevity")
    private Integer longevity;

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

    public String getTitle() {
        return title;
    }

    public JobOpportunity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public JobOpportunity position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getSalary() {
        return salary;
    }

    public JobOpportunity salary(Integer salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getDistance() {
        return distance;
    }

    public JobOpportunity distance(Integer distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getFlexibility() {
        return flexibility;
    }

    public JobOpportunity flexibility(Integer flexibility) {
        this.flexibility = flexibility;
        return this;
    }

    public void setFlexibility(Integer flexibility) {
        this.flexibility = flexibility;
    }

    public Integer getLongevity() {
        return longevity;
    }

    public JobOpportunity longevity(Integer longevity) {
        this.longevity = longevity;
        return this;
    }

    public void setLongevity(Integer longevity) {
        this.longevity = longevity;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public JobOpportunity businessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
        return this;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public User getUser() {
        return user;
    }

    public JobOpportunity user(User user) {
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
        JobOpportunity jobOpportunity = (JobOpportunity) o;
        if (jobOpportunity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobOpportunity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobOpportunity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", position='" + getPosition() + "'" +
            ", salary='" + getSalary() + "'" +
            ", distance='" + getDistance() + "'" +
            ", flexibility='" + getFlexibility() + "'" +
            ", longevity='" + getLongevity() + "'" +
            "}";
    }
}
