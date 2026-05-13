package com.killeen.taskflow.db.model.generated;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class RefreshTokenDbExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RefreshTokenDbExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andTokenIsNull() {
            addCriterion("token is null");
            return (Criteria) this;
        }

        public Criteria andTokenIsNotNull() {
            addCriterion("token is not null");
            return (Criteria) this;
        }

        public Criteria andTokenEqualTo(String value) {
            addCriterion("token =", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotEqualTo(String value) {
            addCriterion("token <>", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenGreaterThan(String value) {
            addCriterion("token >", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenGreaterThanOrEqualTo(String value) {
            addCriterion("token >=", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLessThan(String value) {
            addCriterion("token <", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLessThanOrEqualTo(String value) {
            addCriterion("token <=", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLike(String value) {
            addCriterion("token like", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotLike(String value) {
            addCriterion("token not like", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenIn(List<String> values) {
            addCriterion("token in", values, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotIn(List<String> values) {
            addCriterion("token not in", values, "token");
            return (Criteria) this;
        }

        public Criteria andTokenBetween(String value1, String value2) {
            addCriterion("token between", value1, value2, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotBetween(String value1, String value2) {
            addCriterion("token not between", value1, value2, "token");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(OffsetDateTime value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(OffsetDateTime value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(OffsetDateTime value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(OffsetDateTime value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(OffsetDateTime value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(OffsetDateTime value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<OffsetDateTime> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<OffsetDateTime> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(OffsetDateTime value1, OffsetDateTime value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(OffsetDateTime value1, OffsetDateTime value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtIsNull() {
            addCriterion("expires_at is null");
            return (Criteria) this;
        }

        public Criteria andExpiresAtIsNotNull() {
            addCriterion("expires_at is not null");
            return (Criteria) this;
        }

        public Criteria andExpiresAtEqualTo(OffsetDateTime value) {
            addCriterion("expires_at =", value, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtNotEqualTo(OffsetDateTime value) {
            addCriterion("expires_at <>", value, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtGreaterThan(OffsetDateTime value) {
            addCriterion("expires_at >", value, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtGreaterThanOrEqualTo(OffsetDateTime value) {
            addCriterion("expires_at >=", value, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtLessThan(OffsetDateTime value) {
            addCriterion("expires_at <", value, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtLessThanOrEqualTo(OffsetDateTime value) {
            addCriterion("expires_at <=", value, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtIn(List<OffsetDateTime> values) {
            addCriterion("expires_at in", values, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtNotIn(List<OffsetDateTime> values) {
            addCriterion("expires_at not in", values, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtBetween(OffsetDateTime value1, OffsetDateTime value2) {
            addCriterion("expires_at between", value1, value2, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andExpiresAtNotBetween(OffsetDateTime value1, OffsetDateTime value2) {
            addCriterion("expires_at not between", value1, value2, "expiresAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtIsNull() {
            addCriterion("used_at is null");
            return (Criteria) this;
        }

        public Criteria andUsedAtIsNotNull() {
            addCriterion("used_at is not null");
            return (Criteria) this;
        }

        public Criteria andUsedAtEqualTo(OffsetDateTime value) {
            addCriterion("used_at =", value, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtNotEqualTo(OffsetDateTime value) {
            addCriterion("used_at <>", value, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtGreaterThan(OffsetDateTime value) {
            addCriterion("used_at >", value, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtGreaterThanOrEqualTo(OffsetDateTime value) {
            addCriterion("used_at >=", value, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtLessThan(OffsetDateTime value) {
            addCriterion("used_at <", value, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtLessThanOrEqualTo(OffsetDateTime value) {
            addCriterion("used_at <=", value, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtIn(List<OffsetDateTime> values) {
            addCriterion("used_at in", values, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtNotIn(List<OffsetDateTime> values) {
            addCriterion("used_at not in", values, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtBetween(OffsetDateTime value1, OffsetDateTime value2) {
            addCriterion("used_at between", value1, value2, "usedAt");
            return (Criteria) this;
        }

        public Criteria andUsedAtNotBetween(OffsetDateTime value1, OffsetDateTime value2) {
            addCriterion("used_at not between", value1, value2, "usedAt");
            return (Criteria) this;
        }

        public Criteria andSelectorIsNull() {
            addCriterion("selector is null");
            return (Criteria) this;
        }

        public Criteria andSelectorIsNotNull() {
            addCriterion("selector is not null");
            return (Criteria) this;
        }

        public Criteria andSelectorEqualTo(String value) {
            addCriterion("selector =", value, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorNotEqualTo(String value) {
            addCriterion("selector <>", value, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorGreaterThan(String value) {
            addCriterion("selector >", value, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorGreaterThanOrEqualTo(String value) {
            addCriterion("selector >=", value, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorLessThan(String value) {
            addCriterion("selector <", value, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorLessThanOrEqualTo(String value) {
            addCriterion("selector <=", value, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorLike(String value) {
            addCriterion("selector like", value, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorNotLike(String value) {
            addCriterion("selector not like", value, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorIn(List<String> values) {
            addCriterion("selector in", values, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorNotIn(List<String> values) {
            addCriterion("selector not in", values, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorBetween(String value1, String value2) {
            addCriterion("selector between", value1, value2, "selector");
            return (Criteria) this;
        }

        public Criteria andSelectorNotBetween(String value1, String value2) {
            addCriterion("selector not between", value1, value2, "selector");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}