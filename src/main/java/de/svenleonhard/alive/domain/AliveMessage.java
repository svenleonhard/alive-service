package de.svenleonhard.alive.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AliveMessage.
 */
@Entity
@Table(name = "alive_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AliveMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sendtime")
    private ZonedDateTime sendtime;

    @Column(name = "receivetime")
    private ZonedDateTime receivetime;

    @Column(name = "retrycount")
    private Integer retrycount;

    @ManyToOne
    @JsonIgnoreProperties(value = "aliveMessages", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSendtime() {
        return sendtime;
    }

    public AliveMessage sendtime(ZonedDateTime sendtime) {
        this.sendtime = sendtime;
        return this;
    }

    public void setSendtime(ZonedDateTime sendtime) {
        this.sendtime = sendtime;
    }

    public ZonedDateTime getReceivetime() {
        return receivetime;
    }

    public AliveMessage receivetime(ZonedDateTime receivetime) {
        this.receivetime = receivetime;
        return this;
    }

    public void setReceivetime(ZonedDateTime receivetime) {
        this.receivetime = receivetime;
    }

    public Integer getRetrycount() {
        return retrycount;
    }

    public AliveMessage retrycount(Integer retrycount) {
        this.retrycount = retrycount;
        return this;
    }

    public void setRetrycount(Integer retrycount) {
        this.retrycount = retrycount;
    }

    public User getUser() {
        return user;
    }

    public AliveMessage user(User user) {
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
        if (!(o instanceof AliveMessage)) {
            return false;
        }
        return id != null && id.equals(((AliveMessage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AliveMessage{" +
            "id=" + getId() +
            ", sendtime='" + getSendtime() + "'" +
            ", receivetime='" + getReceivetime() + "'" +
            ", retrycount=" + getRetrycount() +
            "}";
    }
}
