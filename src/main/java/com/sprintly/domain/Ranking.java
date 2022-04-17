package com.sprintly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ranking.
 */
@Entity
@Table(name = "ranking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ranking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "avgpace")
    private Float avgpace;

    @Column(name = "jhi_rank")
    private Integer rank;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "ranking")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "ranking" }, allowSetters = true)
    private Set<Stats> stats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ranking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Ranking name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAvgpace() {
        return this.avgpace;
    }

    public Ranking avgpace(Float avgpace) {
        this.setAvgpace(avgpace);
        return this;
    }

    public void setAvgpace(Float avgpace) {
        this.avgpace = avgpace;
    }

    public Integer getRank() {
        return this.rank;
    }

    public Ranking rank(Integer rank) {
        this.setRank(rank);
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ranking user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Stats> getStats() {
        return this.stats;
    }

    public void setStats(Set<Stats> stats) {
        if (this.stats != null) {
            this.stats.forEach(i -> i.setRanking(null));
        }
        if (stats != null) {
            stats.forEach(i -> i.setRanking(this));
        }
        this.stats = stats;
    }

    public Ranking stats(Set<Stats> stats) {
        this.setStats(stats);
        return this;
    }

    public Ranking addStats(Stats stats) {
        this.stats.add(stats);
        stats.setRanking(this);
        return this;
    }

    public Ranking removeStats(Stats stats) {
        this.stats.remove(stats);
        stats.setRanking(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ranking)) {
            return false;
        }
        return id != null && id.equals(((Ranking) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ranking{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", avgpace=" + getAvgpace() +
            ", rank=" + getRank() +
            "}";
    }
}
