# 数据仓库前端

### 如何启动

```
yarn install

yarn run dev
```

### 我们干了什么

实现了21*3个api：

对关系型数据库Mysql、分布式文件型数据仓库Hive、图数据库neo4j分别实现了下列功能：

+ Query
  + 按导演查询
  + 按演员查询
  + 按电影类型查询
  + 按分数查询
  + 按电影标题查询
+ Relation
  + 给定演员名，查询合作过的演员、导演名及合作次数
  + 给定导演名，查询合作过的演员、导演名及合作次数
+ Statistics
  + 按照给定年月日季度和比较符统计电影个数
  + 按照给定分数和比较符统计电影个数
  + 按照导演、演员、类型统计电影
  + 按照分数统计电影
+ Trace the source
  - 按照电影asin查找电影由哪些电影合并而来

参考源仓库：

https://github.com/Xuedixuedi/BigDWHouseFrontEnd

