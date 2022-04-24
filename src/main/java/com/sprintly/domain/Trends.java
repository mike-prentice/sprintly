package com.sprintly.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Trends.
 */
@Entity
@Table(name = "trends")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Trends implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "avg_pace")
    private Float avgPace;

    @Column(name = "avgdistance")
    private Float avgdistance;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trends id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAvgPace() {
        return this.avgPace;
    }

    public Trends avgPace(Float avgPace) {
        this.setAvgPace(avgPace);
        return this;
    }

    public void setAvgPace(Float avgPace) {
        this.avgPace = avgPace;
    }

    public Float getAvgdistance() {
        return this.avgdistance;
    }

    public Trends avgdistance(Float avgdistance) {
        this.setAvgdistance(avgdistance);
        return this;
    }

    public void setAvgdistance(Float avgdistance) {
        this.avgdistance = avgdistance;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trends user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trends)) {
            return false;
        }
        return id != null && id.equals(((Trends) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trends{" +
            "id=" + getId() +
            ", avgPace=" + getAvgPace() +
            ", avgdistance=" + getAvgdistance() +
            "}";
    }
}
