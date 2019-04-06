package com.eric.exception;

/*
 *@description:数据库中存在多条相同记录异常类
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/6
 */
public class MultipleDuplicateValuesInDatabaseException extends Exception {


    public MultipleDuplicateValuesInDatabaseException(String message) {
        super(message);
    }
}
