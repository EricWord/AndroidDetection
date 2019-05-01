package com.eric.service;


import com.eric.bean.*;
import com.eric.dao.*;
import com.eric.tools.csv.CSVUtils;
import com.eric.tools.csv.FileConstantUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 *@description:生成CSV格式文件的服务层
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/3/31
 */
@Service
public class CSVService {
    @Autowired
    ApkMapper apkMapper;
    @Autowired
    ApiMapper apiMapper;
    @Autowired
    AuthorityMapper authorityMapper;
    @Autowired
    ApiApkMapMapper apiApkMapMapper;
    @Autowired
    AuthorityApkMapMapper authorityApkMapMapper;

    /**
     * 将数据库中的表转为CSV格式的文件，该方法只需要执行一次即可
     */
    public void createCSVFile() {
        DateTime dateTime = new DateTime();
        //当前时间
        String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
        //将apk表转为CSV格式的文件
        tableApk2CSV(stringDate);
        //将API表转为CSV格式的文件
        tableApi2CSV(stringDate);
        //将权限表转为CSV格式的文件
        tableAuthority2CSV(stringDate);
        //将API-应用关系映射表转为CSV格式的文件
        tableApiApkMap2CSV(stringDate);
        //将权限-应用关系映射表转为CSV格式文件
        tableAuthorityApkMap2CSV(stringDate);

    }

    /**
     * 将权限-应用关系映射表转为CSV格式文件
     *
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */
    public void tableAuthorityApkMap2CSV(String stringDate) {
        //tb_authority_apk_map
        List<AuthorityApkMap> authorityApkMaps = authorityApkMapMapper.selectByExample(null);
        //遍历每一个authorityApkMap
        List<String> authorityApkMapTableFiledList = new ArrayList<>();
        for (AuthorityApkMap authorityApkMap : authorityApkMaps) {
            authorityApkMapTableFiledList.add(authorityApkMap.getAuthorityId().toString());
            authorityApkMapTableFiledList.add(authorityApkMap.getApkId().toString());
        }
        System.out.println(">>>>>>authorityApkMapTableFiledList的大小为:" + authorityApkMapTableFiledList.size());
        //将tb_authority_apk_map数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_AUTHORITY_APK_MAP_LIST, authorityApkMapTableFiledList, "D:\\cgs\\File\\CSV", "tb_authority_apk_map_" + stringDate);
        authorityApkMapTableFiledList.clear();
    }

    /**
     * 将API-应用关系映射表转为CSV格式的文件
     *
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */
    public void tableApiApkMap2CSV(String stringDate) {
        List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(null);
        //遍历每一个apiApkMaps
        List<String> apiApkMapsTableFiledList = new ArrayList<>();
        for (ApiApkMap apiApkMap : apiApkMaps) {
            apiApkMapsTableFiledList.add(apiApkMap.getApiId().toString());
            apiApkMapsTableFiledList.add(apiApkMap.getApkId().toString());
        }
        System.out.println(">>>>>>>apiApkMapsTableFiledList的大小为：" + apiApkMapsTableFiledList.size());
        //将tb_api_apk_map数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_API_APK_MAP_LIST, apiApkMapsTableFiledList, "D:\\cgs\\File\\CSV", "tb_api_apk_map_" + stringDate);
        apiApkMapsTableFiledList.clear();
    }

    /**
     * 将权限表转为CSV格式的文件
     *
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */
    public void tableAuthority2CSV(String stringDate) {
        List<Authority> authorities = authorityMapper.selectByExample(null);
        //遍历每一个authority
        List<String> authorityTableFiledList = new ArrayList<>();
        for (Authority authority : authorities) {

            authorityTableFiledList.add(authority.getAuthorityId().toString());
            authorityTableFiledList.add(authority.getAuthorityContent());
            authorityTableFiledList.add(authority.getAuthorityMd5());
        }

        System.out.println(">>>>>>authorityTableFiledList的大小为：" + authorityTableFiledList.size());

        //将tb_authority数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_AUTHORITY_LIST, authorityTableFiledList, "D:\\cgs\\File\\CSV", "tb_authority_" + stringDate);
        //释放空间，防止堆溢出
        authorityTableFiledList.clear();
    }

    /**
     * 将API表转为CSV格式的文件
     *
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */
    public void tableApi2CSV(String stringDate) {
        List<Api> apis = apiMapper.selectByExample(null);
        //遍历每一个api
        List<String> apiTableFiledList = new ArrayList<>();
        for (Api api : apis) {
            apiTableFiledList.add(api.getApiId().toString());
            apiTableFiledList.add(api.getApiContent());
            apiTableFiledList.add(api.getApiMad5());
        }
        System.out.println(">>>>>>>apiTableFiledList的大小为：" + apiTableFiledList.size());
        //将tb_api数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_API_LIST, apiTableFiledList, "D:\\cgs\\File\\CSV", "tb_api_" + stringDate);
        apiTableFiledList.clear();
    }

    /**
     * 将apk表转为CSV格式的文件
     *
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */

    public void tableApk2CSV(String stringDate) {
        //查询数tb_apk表中的所有记录
        List<Apk> apks = apkMapper.selectByExample(null);
        //遍历每一个apk
        List<String> tableFiledList = new ArrayList<>();
        for (Apk apk : apks) {
            tableFiledList.add(apk.getApkId().toString());
            tableFiledList.add(apk.getPackageName());
            tableFiledList.add(apk.getApkAttribute().toString());

        }
        System.out.println(">>>>>tableFiledList的大小为：" + tableFiledList.size());
        //将tb_apk数据库转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_APK_LIST, tableFiledList, "D:\\cgs\\File\\CSV", "tb_apk_" + stringDate);
        tableFiledList.clear();
    }

    /**
     * 生成最终可以用于机器学习的CSV格式的文件
     *
     * @param des
     */
    public void createFinalCSV(String des) {
        DateTime dateTime = new DateTime();
        //当前时间
        String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
        /**
         * 实现思路：
         * 1.生成表头
         * 2.生成每一行 表头中的数据只需要包含应用名称+各种权限+应用属性
         *  2.1
         */
        //1.生成表头
        //构造符合工具类要求的表头List
        List<String> head = new ArrayList<>();
        head.add("package_name");
        List<Authority> authorities = authorityMapper.selectByExample(null);
        for (Authority authority : authorities) {
            String authorityContent = authority.getAuthorityContent();
            head.add(authorityContent);
        }
        head.add("apk_attribute");

        //2.生成每一行数据,构造每一行的数据
        List<String> dataList = new ArrayList<>();

        //获取所有的apk
        List<Apk> apks = apkMapper.selectByExample(null);
        //对每一个apk进行操作
        for (Apk apk : apks) {
            prepareDataForCreateCSV(authorities, apk, dataList);

            //添加属性
            if (apk.getApkAttribute() == 0) {
                dataList.add(0 + "");
                continue;

            }
            if (apk.getApkAttribute() == 1) {
                dataList.add(1 + "");
                continue;

            }

        }

        //生成CSV格式的文件
        CSVUtils.createCSVFile(head, dataList, des, "androidDetection_" + stringDate);

    }

    /**
     * 创建用于初始训练模型使用的数据集、增强训练模型使用的数据集
     *
     * @param des     数据集存放路径
     * @param goodNum 数据集中正常样本的数量
     * @param badNum  数据集中恶意样本的数量
     */
    public void createPartCSV(String des, int goodNum, int badNum) {
        /**
         * 实现思路：
         * 1.生成表头
         * 2.生成每一行 表头中的数据只需要包含应用名称+各种权限+应用属性
         *  2.1
         */
        //1.生成表头
        //构造符合工具类要求的表头List
        List<String> head = new ArrayList<>();
        head.add("package_name");
        List<Authority> authorities = authorityMapper.selectByExample(null);
        for (Authority authority : authorities) {
            String authorityContent = authority.getAuthorityContent();
            head.add(authorityContent);
        }
        head.add("apk_attribute");

        //2.生成每一行数据,构造每一行的数据
        List<String> dataList = new ArrayList<>();

        //获取所有的apk
        List<Apk> apks = apkMapper.selectByExample(null);
        //对每一个apk进行操作
        int goodApkFlag = 1;
        int badApkFlag = 1;
        for (Apk apk : apks) {
            //正常应用
            if (apk.getApkAttribute() == 0) {
                //正常应用计数+1
                if (goodApkFlag < goodNum + 1) {
                    goodApkFlag++;
                } else {
                    //正常应用样本已经够数,跳过本次循环
                    continue;
                }

            }
            //恶意应用
            if (apk.getApkAttribute() == 1) {
                //恶意应用计数+1
                if (badApkFlag < badNum + 1) {
                    badApkFlag++;
                } else {
                    //恶意样本已经够数，跳过本轮循环
                    continue;
                }

            }
            //正常样本和恶意样本都已经够数，结束循环
            if (goodApkFlag == goodNum + 1 && badApkFlag == badNum + 1) {
                break;
            }
            prepareDataForCreateCSV(authorities, apk, dataList);

            //添加属性
            if (apk.getApkAttribute() == 0) {
                dataList.add(0 + "");
                //如果当前应用的属性是正常应用就不可能是恶意应用，直接跳过本轮循环即可，不用再继续往下执行了
                continue;

            }
            if (apk.getApkAttribute() == 1) {
                dataList.add(1 + "");
                continue;
            }
        }

        //生成CSV格式的文件
        CSVUtils.createCSVFile(head, dataList, des, "androidDetection_part_" + goodNum + "good_" + badNum + "bad");

    }

    /**
     * 创建用于初始训练模型使用的数据集、增强训练模型使用的数据集,可以指定基数
     *
     * @param des     数据集存放路径
     * @param goodNum 数据集中正常样本的数量
     * @param badNum  数据集中恶意样本的数量
     * @param baseNum 基数
     */
    public void createPartCSVWithBaseNum(String des, int goodNum, int badNum, int baseNum) {
        int goodApkFlag = 0;
        int badApkFlag = 0;

        //1.生成表头
        //构造符合工具类要求的表头List
        List<String> head = new ArrayList<>();
        head.add("package_name");
        List<Authority> authorities = authorityMapper.selectByExample(null);
        for (Authority authority : authorities) {
            String authorityContent = authority.getAuthorityContent();
            head.add(authorityContent);
        }
        head.add("apk_attribute");

        //2.生成每一行数据,构造每一行的数据
        List<String> dataList = new ArrayList<>();

        //获取所有的apk
        List<Apk> apks = apkMapper.selectByExample(null);
        //对每一个apk进行操作
        for (Apk apk : apks) {
            if (goodApkFlag < baseNum) {
                goodApkFlag++;
                //跳过本轮循环
                continue;

            }
            if (badApkFlag < baseNum) {
                badApkFlag++;
                continue;
            }
            //正常应用
            if (apk.getApkAttribute() == 0) {
                //正常应用计数+1
                if (goodApkFlag < goodNum + baseNum) {
                    goodApkFlag++;
                } else {
                    //正常应用样本已经够数,跳过本次循环
                    continue;
                }

            }
            //恶意应用
            if (apk.getApkAttribute() == 1) {
                //恶意应用计数+1
                if (badApkFlag < badNum + baseNum) {
                    badApkFlag++;
                } else {
                    //恶意样本已经够数，跳过本轮循环
                    continue;
                }

            }
            //正常样本和恶意样本都已经够数，结束循环
            if (goodApkFlag == goodNum && badApkFlag == badNum) {
                break;
            }
            prepareDataForCreateCSV(authorities, apk, dataList);

            //添加属性
            if (apk.getApkAttribute() == 0) {
                dataList.add(0 + "");
                //如果当前应用的属性是正常应用就不可能是恶意应用，直接跳过本轮循环即可，不用再继续往下执行了
                continue;

            }
            if (apk.getApkAttribute() == 1) {
                dataList.add(1 + "");
                continue;
            }
        }

        //生成CSV格式的文件
        CSVUtils.createCSVFile(head, dataList, des, "androidDetection_part_" + goodNum + "good_" + badNum + "bad_base_"+baseNum);

    }

    /**
     * 创建除了基础训练集之外的数据集
     *
     * @param des     数据集存放路径
     * @param goodNum 数据集中正常样本的数量
     * @param badNum  数据集中恶意样本的数量
     * @param baseNum 基数(已经生成csv文件的数据集大小)
     */
    public void createRemainSinglePartCSV(String des, int goodNum, int badNum, int baseNum) {
        //正常应用样本计数
        int goodApkFlag = 0;
        int badApkFlag = 0;

        //1.生成表头
        //构造符合工具类要求的表头List
        List<String> head = new ArrayList<>();
        head.add("package_name");
        List<Authority> authorities = authorityMapper.selectByExample(null);
        for (Authority authority : authorities) {
            String authorityContent = authority.getAuthorityContent();
            head.add(authorityContent);
        }
        head.add("apk_attribute");
        //获取所有的apk
        List<Apk> apks = apkMapper.selectByExample(null);
        //对每一个apk进行操作
        for (Apk apk : apks) {
            //当前apk的数据
            List<String> currentApkDataList = new ArrayList<>();
            //正常应用
            if (apk.getApkAttribute() == 0) {
                //正常应用计数+1
                if (goodApkFlag < baseNum) {
                    goodApkFlag++;
                    //跳过本轮循环
                    continue;

                } else {
                    //已经生成csv文件的数据集已经过滤掉
                    //下面的数据集就是要本次要生成csv文件的数据集
                    if (goodApkFlag < goodNum + baseNum) {
                        //正常应用计数+1
                        goodApkFlag++;
                        //每一个apk生成一个单独的csv文件
                        prepareDataForCreateCSV(authorities, apk, currentApkDataList);
                        //添加当前应用属性
                        currentApkDataList.add(0 + "");
                    }
                }
                //生成CSV格式的文件
                CSVUtils.createCSVFile(head, currentApkDataList, des, "androidDetection_single_good_" + goodApkFlag);
                //跳过本轮接下来的循环
                continue;
            }
            //恶意应用
            if (apk.getApkAttribute() == 1) {
                //恶意应用计数+1
                if (badApkFlag < baseNum) {
                    badApkFlag++;
                    //跳过本轮循环
                    continue;
                } else {
                    if (badApkFlag < badNum + baseNum) {

                        badApkFlag++;
                        //构造数据
                        prepareDataForCreateCSV(authorities, apk, currentApkDataList);
                        //添加当前应用属性
                        currentApkDataList.add(1 + "");
                    }
                    //每一个apk生成一个单独的csv文件
                    //生成CSV格式的文件
                    CSVUtils.createCSVFile(head, currentApkDataList, des, "androidDetection_single_bad_" + badApkFlag);
                    //跳过本轮接下来的循环
                    continue;
                }
            }
            //正常样本和恶意样本都已经够数，结束循环
            if (goodApkFlag == goodNum + 1 && badApkFlag == badNum + 1) {
                break;
            }
        }
    }

    /**
     * 为生成CSV文件准备数据
     *
     * @param authorities        权限
     * @param apk                应用
     * @param currentApkDataList 当前apk文件的数据集合
     */
    private void prepareDataForCreateCSV(List<Authority> authorities, Apk apk, List<String> currentApkDataList) {
        //构造数据
        currentApkDataList.add(apk.getPackageName());
        //遍历每一个权限，然后查找权限-apk映射表，确定当前apk有没有该权限
        //上面注释掉的代码应该有问题，不应该遍历权限来确定映射关系
        //而是应该遍历apk来确定映射关系
        AuthorityApkMapExample authorityApkMapExample = new AuthorityApkMapExample();
        AuthorityApkMapExample.Criteria criteria = authorityApkMapExample.createCriteria();
        criteria.andApkIdEqualTo(apk.getApkId());
        //获取到权限关系映射
        List<AuthorityApkMap> authorityApkMaps = authorityApkMapMapper.selectByExample(authorityApkMapExample);
        //根据映射关系可以确定当前apk都有哪些权限
        List<String> currentApkAuthorityList = new ArrayList<>();
        for (AuthorityApkMap authorityApkMap : authorityApkMaps) {
            Authority authority = authorityMapper.selectByPrimaryKey(authorityApkMap.getAuthorityId());
            currentApkAuthorityList.add(authority.getAuthorityContent());
        }
        for (Authority authority : authorities) {
            if (currentApkAuthorityList.contains(authority.getAuthorityContent())) {
                currentApkDataList.add(1 + "");

            } else {
                currentApkDataList.add(0 + "");
            }
        }
    }
}
