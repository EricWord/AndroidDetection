package com.eric.dao;

import com.eric.bean.ApiApkMap;
import com.eric.bean.ApiApkMapExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiApkMapMapper {
    long countByExample(ApiApkMapExample example);

    int deleteByExample(ApiApkMapExample example);

    int deleteByPrimaryKey(Integer apkId);

    int insert(ApiApkMap record);

    int insertSelective(ApiApkMap record);

    List<ApiApkMap> selectByExample(ApiApkMapExample example);

    ApiApkMap selectByPrimaryKey(Integer apkId);

    int updateByExampleSelective(@Param("record") ApiApkMap record, @Param("example") ApiApkMapExample example);

    int updateByExample(@Param("record") ApiApkMap record, @Param("example") ApiApkMapExample example);

    int updateByPrimaryKeySelective(ApiApkMap record);

    int updateByPrimaryKey(ApiApkMap record);
}