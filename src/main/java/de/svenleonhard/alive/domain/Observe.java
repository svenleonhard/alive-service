package de.svenleonhard.alive.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Observe.
 */
@Entity
@Table(name = "observe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Observe implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "startdate")
    private LocalDate startdate;

    @ManyToOne
    @JsonIgnoreProperties(value = "observes", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Observe description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartdate() {
        return startdate;
    }

    public Observe startdate(LocalDate startdate) {
        this.startdate = startdate;
        return this;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public User getUser() {
        return user;
    }

    public Observe user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Observe)) {
            return false;
        }
        return id != null && id.equals(((Observe) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Observe{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", startdate='" + getStartdate() + "'" +
            "}";
    }
}
