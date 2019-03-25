package com.eric.tools.api;

import com.eric.bean.Api;
import com.eric.bean.ApiApkMap;
import com.eric.bean.ApiApkMapExample;
import com.eric.bean.ApiExample;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.tools.MD5.MD5Utils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @ClassName: ApiInsertCallable
 * @Description: 批量将api存储到数据库
 * @Author: Eric
 * @Date: 2019/3/16 0016
 * @Email: xiao_cui_vip@163.com
 */
@Component
public class ApiInsertCallable implements Callable<Integer> {
    private String item;
    private int apkId;
    @Resource
    private ApiApkMapMapper apiApkMapMapper;
    @Resource
    private ApiMapper apiMapper;
    //----------------解决注入非controller或service文件使用@autowired注解注入mapper文件无效的问题
    public static ApiInsertCallable apiInsertCallable;

    @PostConstruct
    public void init() {
        apiInsertCallable = this;

    }

    public ApiInsertCallable() {
    }

    public ApiInsertCallable(String item, int apkId) {
        this.item = item;
        this.apkId = apkId;
    }

    @Override
    public Integer call() throws Exception {
        int n = 0;

        String md5Value = MD5Utils.MD5Encode(item, "utf8");
        //先查询数据库中有没有该API
        ApiExample apiExample = new ApiExample();
        ApiExample.Criteria criteria = apiExample.createCriteria();
        criteria.andApiMad5EqualTo(md5Value);
        List<Api> apis = null;
        if (apiInsertCallable.apiMapper == null) {
            System.out.println("ApiInsertCallable中的apiMapper是空");
        } else {
            apis = apiInsertCallable.apiMapper.selectByExample(apiExample);
        }
        Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
        //数据库中没有该API
        if (apis.size() == 0) {
            apiInsertCallable.apiMapper.insertSelective(api);
            Integer apiId = api.getApiId();
            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
            apiInsertCallable.apiApkMapMapper.insertSelective(apiApkMap);
        } else {
            Api api1 = null;
            //数据库中有该记录
            if (apis.size() == 1) {
                api1 = apis.get(0);
                Integer apiId = api1.getApiId();
                //查询映射关系是否在数据库中已经存在
                ApiApkMapExample apiApkMapExample = new ApiApkMapExample();
                ApiApkMapExample.Criteria apiApkCriteria = apiApkMapExample.createCriteria();
                apiApkCriteria.andApiIdEqualTo(apiId);
                apiApkCriteria.andApkIdEqualTo(apkId);
                List<ApiApkMap> apiApkMaps = apiInsertCallable.apiApkMapMapper.selectByExample(apiApkMapExample);
                if (apiApkMaps.size() == 0) {
                    //数据库中没有该映射关系
                    ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                    //插入
                    n = apiInsertCallable.apiApkMapMapper.insertSelective(apiApkMap);
                }//否则什么也不做
            } else {
                System.out.println("数据库中有多条重复的api");

            }

        }
        return n;
    }
}
