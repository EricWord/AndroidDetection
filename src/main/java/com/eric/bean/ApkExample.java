package com.eric.bean;

import java.util.ArrayList;
import java.util.List;

public class ApkExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ApkExample() {
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

        public Criteria andApkIdIsNull() {
            addCriterion("apk_id is null");
            return (Criteria) this;
        }

        public Criteria andApkIdIsNotNull() {
            addCriterion("apk_id is not null");
            return (Criteria) this;
        }

        public Criteria andApkIdEqualTo(Integer value) {
            addCriterion("apk_id =", value, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdNotEqualTo(Integer value) {
            addCriterion("apk_id <>", value, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdGreaterThan(Integer value) {
            addCriterion("apk_id >", value, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("apk_id >=", value, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdLessThan(Integer value) {
            addCriterion("apk_id <", value, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdLessThanOrEqualTo(Integer value) {
            addCriterion("apk_id <=", value, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdIn(List<Integer> values) {
            addCriterion("apk_id in", values, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdNotIn(List<Integer> values) {
            addCriterion("apk_id not in", values, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdBetween(Integer value1, Integer value2) {
            addCriterion("apk_id between", value1, value2, "apkId");
            return (Criteria) this;
        }

        public Criteria andApkIdNotBetween(Integer value1, Integer value2) {
            addCriterion("apk_id not between", value1, value2, "apkId");
            return (Criteria) this;
        }

        public Criteria andPackageNameIsNull() {
            addCriterion("package_name is null");
            return (Criteria) this;
        }

        public Criteria andPackageNameIsNotNull() {
            addCriterion("package_name is not null");
            return (Criteria) this;
        }

        public Criteria andPackageNameEqualTo(String value) {
            addCriterion("package_name =", value, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameNotEqualTo(String value) {
            addCriterion("package_name <>", value, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameGreaterThan(String value) {
            addCriterion("package_name >", value, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameGreaterThanOrEqualTo(String value) {
            addCriterion("package_name >=", value, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameLessThan(String value) {
            addCriterion("package_name <", value, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameLessThanOrEqualTo(String value) {
            addCriterion("package_name <=", value, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameLike(String value) {
            addCriterion("package_name like", value, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameNotLike(String value) {
            addCriterion("package_name not like", value, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameIn(List<String> values) {
            addCriterion("package_name in", values, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameNotIn(List<String> values) {
            addCriterion("package_name not in", values, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameBetween(String value1, String value2) {
            addCriterion("package_name between", value1, value2, "packageName");
            return (Criteria) this;
        }

        public Criteria andPackageNameNotBetween(String value1, String value2) {
            addCriterion("package_name not between", value1, value2, "packageName");
            return (Criteria) this;
        }

        public Criteria andApkAttributeIsNull() {
            addCriterion("apk_attribute is null");
            return (Criteria) this;
        }

        public Criteria andApkAttributeIsNotNull() {
            addCriterion("apk_attribute is not null");
            return (Criteria) this;
        }

        public Criteria andApkAttributeEqualTo(Integer value) {
            addCriterion("apk_attribute =", value, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeNotEqualTo(Integer value) {
            addCriterion("apk_attribute <>", value, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeGreaterThan(Integer value) {
            addCriterion("apk_attribute >", value, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeGreaterThanOrEqualTo(Integer value) {
            addCriterion("apk_attribute >=", value, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeLessThan(Integer value) {
            addCriterion("apk_attribute <", value, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeLessThanOrEqualTo(Integer value) {
            addCriterion("apk_attribute <=", value, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeIn(List<Integer> values) {
            addCriterion("apk_attribute in", values, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeNotIn(List<Integer> values) {
            addCriterion("apk_attribute not in", values, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeBetween(Integer value1, Integer value2) {
            addCriterion("apk_attribute between", value1, value2, "apkAttribute");
            return (Criteria) this;
        }

        public Criteria andApkAttributeNotBetween(Integer value1, Integer value2) {
            addCriterion("apk_attribute not between", value1, value2, "apkAttribute");
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