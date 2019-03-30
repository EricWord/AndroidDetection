package com.eric.tools.AndroidManifestHelper;

import com.eric.bean.Authority;
import com.eric.bean.AuthorityApkMap;
import com.eric.bean.AuthorityApkMapExample;
import com.eric.bean.AuthorityExample;
import com.eric.dao.AuthorityApkMapMapper;
import com.eric.dao.AuthorityMapper;
import com.eric.tools.MD5.MD5Utils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @ClassName: AuthorityInsertCallable
 * @Description: 实现Callable接口的权限批量插入
 * @Author: Eric
 * @Date: 2019/3/17 0017
 * @Email: xiao_cui_vip@163.com
 */
/*@Component  //这个注解不要忘掉
public class AuthorityInsertCallable implements Callable<Integer> {
    private String au;
    private Integer apkId;


    @Resource
    private AuthorityMapper authorityMapper;
    @Resource
    private AuthorityApkMapMapper authorityApkMapMapper;

    public static AuthorityInsertCallable authorityInsertCallable;

    @PostConstruct
    public void init(){
        authorityInsertCallable=this;
    }


    public AuthorityInsertCallable() {
    }

    public AuthorityInsertCallable(String au, Integer apkId) {
        this.au = au;
        this.apkId = apkId;
    }

    @Override
    public Integer call() throws Exception {
        //最终的返回值n的含义是插入的权限和apk的映射关系的条数
        int n=0;
        //获取权限的md5值
        String auMd5 = MD5Utils.MD5Encode(au, "utf8");
        //先查询数据库中有没有该权限
        AuthorityExample authorityExample = new AuthorityExample();
        AuthorityExample.Criteria criteria = authorityExample.createCriteria();
        criteria.andAuthorityMd5EqualTo(auMd5);
        List<Authority> authorities=null;
        if(null==authorityInsertCallable.authorityMapper){
            System.out.println("authorityMapper为空");

        }else{
        authorities = authorityInsertCallable.authorityMapper.selectByExample(authorityExample);

        }
        //数据库中没有该权限
        if(authorities.size()==0){
        Authority authority = new Authority(au, auMd5);
        authorityInsertCallable.authorityMapper.insertSelective(authority);
            Integer authorityId = authority.getAuthorityId();
            AuthorityApkMap authorityApkMap = new AuthorityApkMap(apkId, authorityId);
            n+= authorityInsertCallable.authorityApkMapMapper.insertSelective(authorityApkMap);


        }else{
            //数据库中有记录
            Authority authority = authorities.get(0);
            Integer authorityId = authority.getAuthorityId();
            //查询映射关系是否在数据库中已经存在
            AuthorityApkMapExample authorityApkMapExample = new AuthorityApkMapExample();
            AuthorityApkMapExample.Criteria authorityApkMapCriteria = authorityApkMapExample.createCriteria();
            authorityApkMapCriteria.andAuthorityIdEqualTo(authorityId);
            List<AuthorityApkMap> authorityApkMaps = authorityInsertCallable.authorityApkMapMapper.selectByExample(authorityApkMapExample);
            //数据库中没有该映射关系
            if(authorityApkMaps.size()==0){
                //插入该映射关系
                AuthorityApkMap authorityApkMap = new AuthorityApkMap(apkId, authorityId);
                n+=authorityInsertCallable.authorityApkMapMapper.insertSelective(authorityApkMap);
            }
            //否则什么也不做
        }
        return n;
    }
}*/
