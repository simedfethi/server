package com.avanquest.scibscrm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.avanquest.scibscrm.domain.Monnaie} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.MonnaieResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monnaies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class MonnaieCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter moneyName;

    private StringFilter moneyIsocode;

    private Boolean distinct;

    public MonnaieCriteria() {}

    public MonnaieCriteria(MonnaieCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.moneyName = other.moneyName == null ? null : other.moneyName.copy();
        this.moneyIsocode = other.moneyIsocode == null ? null : other.moneyIsocode.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MonnaieCriteria copy() {
        return new MonnaieCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMoneyName() {
        return moneyName;
    }

    public StringFilter moneyName() {
        if (moneyName == null) {
            moneyName = new StringFilter();
        }
        return moneyName;
    }

    public void setMoneyName(StringFilter moneyName) {
        this.moneyName = moneyName;
    }

    public StringFilter getMoneyIsocode() {
        return moneyIsocode;
    }

    public StringFilter moneyIsocode() {
        if (moneyIsocode == null) {
            moneyIsocode = new StringFilter();
        }
        return moneyIsocode;
    }

    public void setMoneyIsocode(StringFilter moneyIsocode) {
        this.moneyIsocode = moneyIsocode;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MonnaieCriteria that = (MonnaieCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(moneyName, that.moneyName) &&
            Objects.equals(moneyIsocode, that.moneyIsocode) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moneyName, moneyIsocode, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonnaieCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (moneyName != null ? "moneyName=" + moneyName + ", " : "") +
            (moneyIsocode != null ? "moneyIsocode=" + moneyIsocode + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
