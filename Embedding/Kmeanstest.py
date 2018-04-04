# coding=utf-8
""" 
Created on 2017-04-20 @author: Rcy 
"""
import time
import re
import os
import sys
import codecs
import shutil
from sklearn import feature_extraction
from sklearn.externals import joblib
import numpy
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
import matplotlib.pyplot as plt
path=''
if __name__ == "__main__":
    corpus = []  # 文档预料 空格连接

    # 读取预料 一行预料为一个文档
    for line in open('path', 'r').readlines():
        print line
        corpus.append(line.strip())
    # print corpus
    time.sleep(5)

    # 将文本中的词语转换为词频矩阵 矩阵元素a[i][j] 表示j词在i类文本下的词频
    vectorizer = CountVectorizer()

    # 该类会统计每个词语的tf-idf权值
    transformer = TfidfTransformer()

    # 第一个fit_transform是计算tf-idf 第二个fit_transform是将文本转为词频矩阵
    tfidf = transformer.fit_transform(vectorizer.fit_transform(corpus))

    # 获取词袋模型中的所有词语
    word = vectorizer.get_feature_names()

    # 将tf-idf矩阵抽取出来，元素w[i][j]表示j词在i类文本中的tf-idf权重
    weight = tfidf.toarray()

    resName = "path"
    result = codecs.open(resName, 'w', 'utf-8')
    for j in range(len(word)):
        result.write(word[j] + ' ')
    result.write('\r\n\r\n')

    # 打印每类文本的tf-idf词语权重，第一个for遍历所有文本，第二个for便利某一类文本下的词语权重
    for i in range(len(weight)):
        print u"-------这里输出第", i, u"类文本的词语tf-idf权重------"
        for j in range(len(word)):
            result.write(str(weight[i][j]) + ' ')
        result.write('\r\n\r\n')

    result.close()
    ########################################################################
    #                               第二步 聚类Kmeans
    ##pca
pca = PCA(n_components=2)  # 输出两维
newData = pca.fit_transform(weight)  # 载入N维
print newData


##pca
print 'Start Kmeans:'
from sklearn.cluster import KMeans
if __name__ == '__main__':
    # 设定不同k值以运算
    for k in range(2, 10):
        clf = KMeans(n_clusters=k)  # 设定k  ！！！！！！！！！！这里就是调用KMeans算法
        s = clf.fit(newData)  # 加载数据集合
        numSamples = len(newData)
        centroids = clf.labels_
        print centroids, type(centroids)  # 显示中心点
        print clf.inertia_  # 显示聚类效果
#########################################################################
########################################################################
#                               第三步 图形输出
        mark = ['or', 'ob', 'og', 'ok', '^r', '+r', 'sr', 'dr', '<r', 'pr']
        # 画出所有样例点 属于同一分类的绘制同样的颜色
        for i in xrange(numSamples):
            # markIndex = int(clusterAssment[i, 0])
            plt.plot(newData[i][0], newData[i][1], mark[clf.labels_[i]])  # mark[markIndex])
        mark = ['Dr', 'Db', 'Dg', 'Dk', '^b', '+b', 'sb', 'db', '<b', 'pb']
        # 画出质点，用特殊图型
        centroids = clf.cluster_centers_
        for i in range(k):
            plt.plot(centroids[i][0], centroids[i][1], mark[i], markersize=12)
            # print centroids[i, 0], centroids[i, 1]
        plt.show()
