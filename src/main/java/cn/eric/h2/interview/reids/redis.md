#Redis数据类型
Redis中支持的数据类型到5.0.5版本为止，一共有9种。分别是：

1、Binary-safe strings(二进制安全字符串)  
2、Lists(列表)  
3、Sets(集合)  
4、Sorted sets(有序集合)  
5、Hashes(哈希)  
6、Bit arrays (or simply bitmaps)(位图)  
7、HyperLogLogs  
8、 geospatial  
9、Streams  
虽然这里列出了9种，但是我们最常用的就是前面5种。  

在Redis中，针对每种数据类型都提供了不同的类型的命令,下面就让我们依次来介绍一下。

#1.Binary-safe strings(二进制安全字符串)  
字符串类型是我们使用最广泛的一种类型,而且Redis中的key值只能用字符串来存储，而value就可以支持9种数据类型。

##Redis当中的字符串可以存储三种数据类型：  
字符串  
整数  
浮点数

##应用场景
字符串类型的应用场景非常丰富，正常的热点数据都可以采用字符串类型来进行缓存，主要可以应用如下场景：

1、热点数据及其对象缓存  
2、分布式Session共享  
3、分布式锁(利用setnx命令)  
4、Redis独立部署，可以用来作为全局唯一ID  
5、利用其原子性递增命令，可以作为计数器或者限流等  

#2.Lists(列表)
Redis中的List列表内部的元素也是字符串，我们可以将指定元素添加到列表中的指定位置。
列表数据类型的操作命令一般都会有小写字母l开头。

##常用命令:  
lpush key value1 value2： 将一个或者多个value插入到列表key的头部,key不存在则创建key。<br>
lpushx key value1 value2： 将value插入到列表key的头部,key不存在则不做任何处理。<br>
lpop key: 移除并返回key值的列表头元素。<br>
rpush、rpushx、rpop

#3.Sets(集合)
Redis中的集合是一个String类型的无序集合，集合中元素唯一不可重复。

##常用命令:
sadd key member: 将一个或多个元素member加入到集合key当中,并返回添加成功的数目，如果元素已存在则被忽略
sismember key member: 判断元素member是否存在集合key中
srem key member : 移除集合key中的元素，不存在的元素会被忽略
smove source dest member: 将元素member从集合source中移动到dest中，如果member不存在，则不执行任何操作
smembers key: 返回集合key中所有元素

#4.Sorted Sets(有序集合)
Redis中的有序集合和集合的区别是有序集合中的每个元素都会关联一个double类型的分数，然后按照分数从小到大的顺序进行排列。

##常用命令
Sorted Sets集合的操作命令一般都以z开头

zadd key score member: 将一个或多个元素member及其score(分值用于排序）添加到有序集合key中
zscore key member： 返回有序集合key中member成员的score
zincrby key num member： 将有序集合key中的member加上num ，num可以为负数
zcount key min max ： 返回有序集合key中score值在min(含)到max(含)之间的member数量
zrange key start stop ： 返回有序集合key中score从小到大排列后start(含)到end(含)之间的所有member
zrevrange key start stop ： 返回有序集合key中score从大到小排列后start(含)到end(含)之间的所有member
zrank key member ： 返回有序集合中member中元素排名(从小到大)，返回的结果从0开始计算
zrevrank key member ： 返回有序集合中member中元素排名(从大到小)，返回的结果从0开始计算

#5.Hashes(哈希)
哈希表中存储的是一个key和value的映射表。操作哈希数据类型的命令一般都是h开头。

常用命令
hset key field value: 
