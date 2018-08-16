# LiveSpider
## Developer information
##### Maintainer: LetCafe
##### Email: letcafe@outlook.com
## Developing environment
+ Web Container：Spring embed tomcat
+ JDK：1.8
+ DEV IDE：Intellij IDEA
## Framework support
>Spring framework support:https://projects.spring.io/spring-framework/  
>SpringFox Swagger support:https://springfox.github.io/springfox/  
>Hibernate support:http://hibernate.org/  
>Gradle support:https://gradle.org/

## Deploy notice
+ Visit url：http://localhost:8080/index
+ Support: Intellij IDEA

## Developing log
### 2018-08-05 
+ 新增：爬取京东图书等信息

### 2018-08-10
+ 更新：泛型传参结构
+ 新增：HuYa游戏列表(gid + name)
+ 新增：Spring @Scheduling定时任务(@EnableScheduling + @Scheduling)

### 2018-08-13
+ Ing：数据库结构持续调整中...
+ 新增：HuYa主播信息：bean<=>pojo正确映射

### 2018-08-16
+ 新增：(mysql)30分钟定时更新Live列表（HuYaGameType.java）
+ 新增：(mysql)5分钟定时更新所有Live大类前20直播间信息（HuYaLiveInfo.java）
+ 新增：(mongodb)5分钟定时插入所有Live大类前20直播间日志留存（LiveInfoLog.java）

