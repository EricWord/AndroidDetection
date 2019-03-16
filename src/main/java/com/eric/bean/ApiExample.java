package com.eric.bean;

import java.util.ArrayList;
import java.util.List;

public class ApiExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ApiExample() {
        oredCriteria = new ArrayList<Criteria>();
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
            criteria = new ArrayList<Criterion>();
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

        public Criteria andApiIdIsNull() {
            addCriterion("api_id is null");
            return (Criteria) this;
        }

        public Criteria andApiIdIsNotNull() {
            addCriterion("api_id is not null");
            return (Criteria) this;
        }

        public Criteria andApiIdEqualTo(Integer value) {
            addCriterion("api_id =", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotEqualTo(Integer value) {
            addCriterion("api_id <>", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdGreaterThan(Integer value) {
            addCriterion("api_id >", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("api_id >=", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdLessThan(Integer value) {
            addCriterion("api_id <", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdLessThanOrEqualTo(Integer value) {
            addCriterion("api_id <=", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdIn(List<Integer> values) {
            addCriterion("api_id in", values, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotIn(List<Integer> values) {
            addCriterion("api_id not in", values, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdBetween(Integer value1, Integer value2) {
            addCriterion("api_id between", value1, value2, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotBetween(Integer value1, Integer value2) {
            addCriterion("api_id not between", value1, value2, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiContentIsNull() {
            addCriterion("api_content is null");
            return (Criteria) this;
        }

        public Criteria andApiContentIsNotNull() {
            addCriterion("api_content is not null");
            return (Criteria) this;
        }

        public Criteria andApiContentEqualTo(String value) {
            addCriterion("api_content =", value, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentNotEqualTo(String value) {
            addCriterion("api_content <>", value, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentGreaterThan(String value) {
            addCriterion("api_content >", value, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentGreaterThanOrEqualTo(String value) {
            addCriterion("api_content >=", value, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentLessThan(String value) {
            addCriterion("api_content <", value, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentLessThanOrEqualTo(String value) {
            addCriterion("api_content <=", value, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentLike(String value) {
            addCriterion("api_content like", value, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentNotLike(String value) {
            addCriterion("api_content not like", value, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentIn(List<String> values) {
            addCriterion("api_content in", values, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentNotIn(List<String> values) {
            addCriterion("api_content not in", values, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentBetween(String value1, String value2) {
            addCriterion("api_content between", value1, value2, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiContentNotBetween(String value1, String value2) {
            addCriterion("api_content not between", value1, value2, "apiContent");
            return (Criteria) this;
        }

        public Criteria andApiMad5IsNull() {
            addCriterion("api_mad5 is null");
            return (Criteria) this;
        }

        public Criteria andApiMad5IsNotNull() {
            addCriterion("api_mad5 is not null");
            return (Criteria) this;
        }

        public Criteria andApiMad5EqualTo(String value) {
            addCriterion("api_mad5 =", value, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5NotEqualTo(String value) {
            addCriterion("api_mad5 <>", value, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5GreaterThan(String value) {
            addCriterion("api_mad5 >", value, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5GreaterThanOrEqualTo(String value) {
            addCriterion("api_mad5 >=", value, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5LessThan(String value) {
            addCriterion("api_mad5 <", value, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5LessThanOrEqualTo(String value) {
            addCriterion("api_mad5 <=", value, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5Like(String value) {
            addCriterion("api_mad5 like", value, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5NotLike(String value) {
            addCriterion("api_mad5 not like", value, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5In(List<String> values) {
            addCriterion("api_mad5 in", values, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5NotIn(List<String> values) {
            addCriterion("api_mad5 not in", values, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5Between(String value1, String value2) {
            addCriterion("api_mad5 between", value1, value2, "apiMad5");
            return (Criteria) this;
        }

        public Criteria andApiMad5NotBetween(String value1, String value2) {
            addCriterion("api_mad5 not between", value1, value2, "apiMad5");
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