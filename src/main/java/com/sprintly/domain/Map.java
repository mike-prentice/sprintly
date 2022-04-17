package com.sprintly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Map.
 */
@Entity
@Table(name = "map")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Map implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "distance")
    private Float distance;

    @Column(name = "time_start")
    private Instant timeStart;

    @Column(name = "time_stop")
    private Instant timeStop;

    @JsonIgnoreProperties(value = { "user", "ranking" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Stats stats;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Map id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getDistance() {
        return this.distance;
    }

    public Map distance(Float distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Instant getTimeStart() {
        return this.timeStart;
    }

    public Map timeStart(Instant timeStart) {
        this.setTimeStart(timeStart);
        return this;
    }

    public void setTimeStart(Instant timeStart) {
        this.timeStart = timeStart;
    }

    public Instant getTimeStop() {
        return this.timeStop;
    }

    public Map timeStop(Instant timeStop) {
        this.setTimeStop(timeStop);
        return this;
    }

    public void setTimeStop(Instant timeStop) {
        this.timeStop = timeStop;
    }

    public Stats getStats() {
        return this.stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Map stats(Stats stats) {
        this.setStats(stats);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        return id != null && id.equals(((Map) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Map{" +
            "id=" + getId() +
            ", distance=" + getDistance() +
            ", timeStart='" + getTimeStart() + "'" +
            ", timeStop='" + getTimeStop() + "'" +
            "}";
    }
}
