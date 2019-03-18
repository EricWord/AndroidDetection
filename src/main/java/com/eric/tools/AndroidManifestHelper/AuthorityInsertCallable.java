package com.eric.tools.AndroidManifestHelper;

import java.util.concurrent.Callable;

/**
 * @ClassName: AuthorityInsertCallable
 * @Description: 实现Callable接口的权限批量插入
 * @Author: Eric
 * @Date: 2019/3/17 0017
 * @Email: xiao_cui_vip@163.com
 */

public class AuthorityInsertCallable implements Callable<Integer> {
    private String item;
    private Integer apkId;


    public AuthorityInsertCallable() {
    }

    public AuthorityInsertCallable(String item, Integer apkId) {
        this.item = item;
        this.apkId = apkId;
    }

    @Override
    public Integer call() throws Exception {


        return null;
    }
}
