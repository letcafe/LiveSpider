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
+ ing：数据库结构持续调整中...
+ 新增：HuYa主播信息：bean<=>pojo正确映射

### 2018-08-16
+ 新增：(mysql)30分钟定时更新Live列表（HuYaGameType.java）
+ 新增：(mysql)5分钟定时更新所有Live大类前20直播间信息（HuYaLiveInfo.java）
+ 新增：(mongodb)5分钟定时插入所有Live大类前20直播间日志留存（LiveInfoLog.java）

### 2018-08-16(to do plan)
#### research result：HuYa模拟登录流程：重点在于获取oath_token：
1. POST请求：https://www.huya.com/udb_web/authorizeURL.php?do=authorizeEmbedURL&callbackURL=https://www.huya.com/udb_web/udbport2.php?do=callback
2. 响应中包含oauth_token字段（96 char）
3. 带着token以POST请求如下路径：https://lgn.yy.com/lgn/oauth/x2/s/login_asyn.do?username=1656777876&pwdencrypt={huya固定编码后密码，可作常量不变，具体F12去获取}&oauth_token={上述步骤token}&denyCallbackURL=&UIStyle=xelogin&appid=5216&mxc=&vk=&isRemMe=1&mmc=&vv=&hiido=1
+ ![image](https://github.com/letcafe/LiveSpider/blob/master/picture/huya_oath.png)
4. 拿到token后请为所欲为...

### 2018-08-17(to do plan)
#### research result：HuYa模拟登录流程：重点在于获取oath_token：
+ 2018-08-16中的问题，对于常规Jsoup难以解决，使用Selenium解决：
+ Selenium gradle:http://mvnrepository.com/search?q=selenium

### 2018-08-22
+ 测试类：SeleniumStarterTest，结合chromedriver.exe实现模拟登陆操作（已实现，但需要基于GUI的浏览器/可考虑加入PhantomJS）
+ 测试类：CookieResponseTest，通过纯HttpClient解析WebFlow，实现部分基于Http的模拟（未完全实现）
+ 新增：成功获得所有用户任务数量，情况信息接口内容
+ 下一步：过滤出干净的数据或者找到Selenium做WebApi得到JSON

### 2018-08-23
+ 测试类：HuYaUserLevel及其相关POJO
+ 新增：完成对用户信息模型HuYaUserLevel的封装及操作，并定时爬取
+ 下一步：模拟登陆的鲁棒性需要提升——成功率

### 2018-08-26
+ 集成Redis：单机缓存6天的cookie信息
+ 新增：根据Redis的Cookie值,更新用户等级信息
+ 下一步：进一步封装cookie
