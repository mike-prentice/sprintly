package com.sprintly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Stats.
 */
@Entity
@Table(name = "stats")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Stats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "distance_ran")
    private Float distanceRan;

    @Column(name = "time")
    private Duration time;

    @Max(value = 200)
    @Column(name = "cadence")
    private Integer cadence;

    @Column(name = "avgpace")
    private Float avgpace;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "stats" }, allowSetters = true)
    private Ranking ranking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Stats id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getDistanceRan() {
        return this.distanceRan;
    }

    public Stats distanceRan(Float distanceRan) {
        this.setDistanceRan(distanceRan);
        return this;
    }

    public void setDistanceRan(Float distanceRan) {
        this.distanceRan = distanceRan;
    }

    public Duration getTime() {
        return this.time;
    }

    public Stats time(Duration time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public Integer getCadence() {
        return this.cadence;
    }

    public Stats cadence(Integer cadence) {
        this.setCadence(cadence);
        return this;
    }

    public void setCadence(Integer cadence) {
        this.cadence = cadence;
    }

    public Float getAvgpace() {
        return this.avgpace;
    }

    public Stats avgpace(Float avgpace) {
        this.setAvgpace(avgpace);
        return this;
    }

    public void setAvgpace(Float avgpace) {
        this.avgpace = avgpace;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stats user(User user) {
        this.setUser(user);
        return this;
    }

    public Ranking getRanking() {
        return this.ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public Stats ranking(Ranking ranking) {
        this.setRanking(ranking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stats)) {
            return false;
        }
        return id != null && id.equals(((Stats) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stats{" +
            "id=" + getId() +
            ", distanceRan=" + getDistanceRan() +
            ", time='" + getTime() + "'" +
            ", cadence=" + getCadence() +
            ", avgpace=" + getAvgpace() +
            "}";
    }
}
