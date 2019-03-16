package com.eric.dao;

import com.eric.bean.AuthorityApkMap;
import com.eric.bean.AuthorityApkMapExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthorityApkMapMapper {
    long countByExample(AuthorityApkMapExample example);

    int deleteByExample(AuthorityApkMapExample example);

    int deleteByPrimaryKey(Integer apkId);

    int insert(AuthorityApkMap record);

    int insertSelective(AuthorityApkMap record);

    List<AuthorityApkMap> selectByExample(AuthorityApkMapExample example);

    AuthorityApkMap selectByPrimaryKey(Integer apkId);

    int updateByExampleSelective(@Param("record") AuthorityApkMap record, @Param("example") AuthorityApkMapExample example);

    int updateByExample(@Param("record") AuthorityApkMap record, @Param("example") AuthorityApkMapExample example);

    int updateByPrimaryKeySelective(AuthorityApkMap record);

    int updateByPrimaryKey(AuthorityApkMap record);
}