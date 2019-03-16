package com.eric.dao;

import com.eric.bean.Apk;
import com.eric.bean.ApkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApkMapper {
    long countByExample(ApkExample example);

    int deleteByExample(ApkExample example);

    int deleteByPrimaryKey(Integer apkId);

    int insert(Apk record);

    int insertSelective(Apk record);

    List<Apk> selectByExample(ApkExample example);

    Apk selectByPrimaryKey(Integer apkId);

    int updateByExampleSelective(@Param("record") Apk record, @Param("example") ApkExample example);

    int updateByExample(@Param("record") Apk record, @Param("example") ApkExample example);

    int updateByPrimaryKeySelective(Apk record);

    int updateByPrimaryKey(Apk record);
}