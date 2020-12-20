package de.svenleonhard.alive.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DeviceNotAlive.
 */
@Entity
@Table(name = "device_not_alive")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeviceNotAlive implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "occured")
    private ZonedDateTime occured;

    @Column(name = "confirmed")
    private Boolean confirmed;

    @ManyToOne
    @JsonIgnoreProperties(value = "deviceNotAlives", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getOccured() {
        return occured;
    }

    public DeviceNotAlive occured(ZonedDateTime occured) {
        this.occured = occured;
        return this;
    }

    public void setOccured(ZonedDateTime occured) {
        this.occured = occured;
    }

    public Boolean isConfirmed() {
        return confirmed;
    }

    public DeviceNotAlive confirmed(Boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public User getUser() {
        return user;
    }

    public DeviceNotAlive user(User user) {
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
        if (!(o instanceof DeviceNotAlive)) {
            return false;
        }
        return id != null && id.equals(((DeviceNotAlive) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceNotAlive{" +
            "id=" + getId() +
            ", occured='" + getOccured() + "'" +
            ", confirmed='" + isConfirmed() + "'" +
            "}";
    }
}
