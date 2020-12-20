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
 * Criteria class for the {@link de.svenleonhard.alive.domain.DeviceNotAlive} entity. This class is used
 * in {@link de.svenleonhard.alive.web.rest.DeviceNotAliveResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /device-not-alives?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DeviceNotAliveCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter occured;

    private BooleanFilter confirmed;

    private LongFilter userId;

    public DeviceNotAliveCriteria() {}

    public DeviceNotAliveCriteria(DeviceNotAliveCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.occured = other.occured == null ? null : other.occured.copy();
        this.confirmed = other.confirmed == null ? null : other.confirmed.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public DeviceNotAliveCriteria copy() {
        return new DeviceNotAliveCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getOccured() {
        return occured;
    }

    public void setOccured(ZonedDateTimeFilter occured) {
        this.occured = occured;
    }

    public BooleanFilter getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(BooleanFilter confirmed) {
        this.confirmed = confirmed;
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
        final DeviceNotAliveCriteria that = (DeviceNotAliveCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(occured, that.occured) &&
            Objects.equals(confirmed, that.confirmed) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, occured, confirmed, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceNotAliveCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (occured != null ? "occured=" + occured + ", " : "") +
                (confirmed != null ? "confirmed=" + confirmed + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
