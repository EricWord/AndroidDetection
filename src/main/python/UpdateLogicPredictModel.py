import pandas as pd
# 设置显示的最大列、宽等参数，消掉打印不完全中间的省略号
pd.set_option('display.max_columns', 1000)
pd.set_option('display.width', 1000)
pd.set_option('display.max_colwidth', 1000)
# 划分数据集用到的
from sklearn .model_selection  import train_test_split
# 特征工程用到的
from sklearn.preprocessing import  StandardScaler
# 逻辑回归用到的
from sklearn.linear_model import LogisticRegression
# 查看精确率和召回率用到的
from  sklearn.metrics import  classification_report
from  sklearn.metrics import roc_auc_score
# 保存模型用到的
from sklearn.externals import joblib
# 控制台调用Java程序传递参数用到
import sys

def updateModel(csvPath,preModelPath,newModelSavePath):
    # 1.读取数据
    data=pd.read_csv(csvPath)
    # 特征
    x=data.iloc[:,1:-1]
    # 标签
    y=data["apk_attribute"]

    # 2.划分数据集
    x_train,x_test,y_train,y_test=train_test_split(x,y,test_size=0.1,random_state=33)
    # 3.特征工程
    # 实例化StandardScaler
    transfer = StandardScaler()
    x_train=transfer.fit_transform(x_train)
    x_test=transfer.transform(x_test)
    # 加载模型
    estimator = joblib.load(preModelPath)
    estimator.fit(x_train,y_train)

    # 保存模型 保存路径
    joblib.dump(estimator,newModelSavePath)

    # 模型品评估
    y_predict=estimator.predict(x_test)


    # 查看精确率和召回率
    report=classification_report(y_test,y_predict,labels=[0,1],target_names=["正常","恶意"])
    return  report


if __name__ == "__main__":
    a = []
    for i in range(1, len(sys.argv)):
        a.append((sys.argv[i]))
    print("新模型评估结果：\n")

    print(updateModel(a[0],a[1],a[2]))