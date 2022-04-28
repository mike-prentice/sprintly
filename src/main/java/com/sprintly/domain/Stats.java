package com.sprintly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
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

    @Column(name = "distance")
    private Float distance;

    @Column(name = "time")
    private Float time;

    @Column(name = "avgpace")
    private Float avgpace;

    @ManyToOne
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

    public Float getDistance() {
        return this.distance;
    }

    public Stats distance(Float distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getTime() {
        return this.time;
    }

    public Stats time(Float time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Float time) {
        this.time = time;
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
            ", distance=" + getDistance() +
            ", time=" + getTime() +
            ", avgpace=" + getAvgpace() +
            "}";
    }
}
