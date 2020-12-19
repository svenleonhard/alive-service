package de.svenleonhard.alive.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link de.svenleonhard.alive.domain.AliveMessage} entity. This class is used
 * in {@link de.svenleonhard.alive.web.rest.AliveMessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alive-messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AliveMessageCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter sendtime;

    private ZonedDateTimeFilter receivetime;

    private IntegerFilter retrycount;

    private LongFilter userId;

    public AliveMessageCriteria() {}

    public AliveMessageCriteria(AliveMessageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sendtime = other.sendtime == null ? null : other.sendtime.copy();
        this.receivetime = other.receivetime == null ? null : other.receivetime.copy();
        this.retrycount = other.retrycount == null ? null : other.retrycount.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public AliveMessageCriteria copy() {
        return new AliveMessageCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getSendtime() {
        return sendtime;
    }

    public void setSendtime(ZonedDateTimeFilter sendtime) {
        this.sendtime = sendtime;
    }

    public ZonedDateTimeFilter getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(ZonedDateTimeFilter receivetime) {
        this.receivetime = receivetime;
    }

    public IntegerFilter getRetrycount() {
        return retrycount;
    }

    public void setRetrycount(IntegerFilter retrycount) {
        this.retrycount = retrycount;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AliveMessageCriteria that = (AliveMessageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sendtime, that.sendtime) &&
            Objects.equals(receivetime, that.receivetime) &&
            Objects.equals(retrycount, that.retrycount) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sendtime, receivetime, retrycount, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AliveMessageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sendtime != null ? "sendtime=" + sendtime + ", " : "") +
                (receivetime != null ? "receivetime=" + receivetime + ", " : "") +
                (retrycount != null ? "retrycount=" + retrycount + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
