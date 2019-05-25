# LiveSpider
<img src="https://github.com/letcafe/LiveSpider/blob/master/picture/huya_logo.png" width="200px"></image>
## Developer information
##### Maintainer: letcafe
##### Email: letcafe@outlook.com
## Developing environment
+ Web Container：Apache Tomcat
+ JDK：1.8
+ DEV IDE：Intellij IDEA
+ Project Encoding: UTF-8
## Framework support
>Spring framework support:https://projects.spring.io/spring-framework/  
>SpringFox Swagger support:https://springfox.github.io/springfox/  
>Hibernate support:http://hibernate.org/  
>Maven support:http://maven.apache.org/

## Deploy notice
+ <del>Visit url：http://localhost:8080/index</del>(On the way...)
+ Support: Intellij IDEA

## 已知信息：
+ 任务每天在0点刷新
+ 6个百宝箱在早上7点刷新?(正在验证...)

## 定时任务：
### 爬取数据
+ 0 20/30 * * * * => LiveInfoGetter.updateAllHuYaLiveInfo => 每30分钟更新直播间信息于MySQL
+ 0 0 0 ? * SUN,WED => UserLevelAndTaskGetter.setUserLoginCookie => 每周日、周三零点更新登录Cookie
+ 0 20/15 * * * * => LiveInfoGetter.insertLiveLogsToMongo => 每30分钟获取前20条LOL的直播信息存MongoDB
+ 0 0 0/4 * * * => UserLevelAndTaskGetter.setUserTaskStatus => 每隔4小时获取用户经验状态存MySQL
+ 0 0/10 * * * * * => GameTypeGetter.gameTypeScheduled => 每隔10分钟更新一次游戏列表于MySQL
+ 0 59 23 * * * => GameTypeGetter.gameTypeScheduled => 每天23时59分获取今天任务完成情况于MySQL
### 每日任务自动完成
+ 0 0 3 * * * => TaskAutoWorker.watchNumberedLive => 每天凌晨3：00完成观看10名主播的任务
+ 0 10 3 * * * => TaskAutoWorker.sendPubMessage => 每天凌晨3：10完成发送一条弹幕任务
+ 0 15 3 * * * => TaskAutoWorker.subscribeOneLiveRoomTask => 每天凌晨3:15订阅一个DNF主播并10s后取消订阅
+ 0 20 3 * * * => TaskPrizeGetter.sendGiftTo3LiveRoom => 每天凌晨3:20给LOL列表前三主播送礼物（默认虎粮:id = 4）
+ 0 30 3 * * * => TaskPrizeGetter.guessInLiveRoom => 每天凌晨3:30给完成所有竞猜相关任务
+ 0 30 6 * * * => TaskAutoWorker.watchLiveGetSixTreasure => 每天上午6：30观看55分钟直播以获得6个宝箱，并领取（宝箱6点刷）
+ 0 30 7 * * * => TaskPrizeGetter.getAllTaskPrize => 每天上午7点半收取所有的任务经验

## Framework (Updated in 2018-10-05)
<img src="https://github.com/letcafe/LiveSpider/blob/master/picture/framework.png" width="450px"></image>

## 使用手册(Usage Manual)
### 一、使用流程
1. 在Windows、Linux或者Mac中安装Chrome浏览器;
2. 根据你的Chrome的版本选择下载对应的chromedriver，[下载链接](http://npm.taobao.org/mirrors/chromedriver/ "chromedriver国内镜像加速地址");

|chromedriver版本|支持的chrome版本|发布时间|
|:--------------:|:-------------:|:------:|
|v2.43|v69-71|2018-10-16|
|v2.42|v68-70|2018-09-13|
|v2.41|v67-69|2018-07-27|
|v2.40|v66-68|2018-06-07|
|v2.39|v66-68|2018-05-30|
|v2.38|v65-67|2018-04-17|
|v2.37|v64-66|2018-03-16|
|v2.36|v63-65|2018-03-02|
|v2.35|v62-64|2018-01-10|
|...|...|...|

3. 修改classpath://resources/huya.yaml中的huya.CHROME_DRIVER_LOCATION属性为你下载的ChromeDriver部署位置
4. 修改classpath://resources/huya.yaml中的huya.YY_ID为你的虎牙登录账号（账号、手机号都可以）
5. 修改classpath://resources/huya.yaml中的huya.PASSWORD为你的虎牙登录密码
6. 配置classpath://resources/application.yaml中active的profile为dev（默认）
7. 配置classpath://resources/application-dev.yaml中MySQL,MongoDB和Redis连接为你自己的连接
8. 所有的任务完成时间如classpath://resources/huya.yaml所示，为Linux crontab形式定时器，可自行灵活调整
9. 所有的数据表模型定义均位于：classpath://resources/schema目录下，请自行先导入DB（也可使用Hibernate生成）
10. 启动LiveSpider工程

### 二、注意事项：
1. 项目默认以--headless模式启动，不需要GUI，想开启GUI，设置huya.yaml中的SYSTEM_IS_OPEN_GUI为true;
2. 工程由UTF-8编码，乱码问题将文件以UTF-8形式打开;
## Developing log

### 日志说明
1. '+'：新增
2. '-'：删除
3. '~'：更新
4. '?'：疑问
5. '*'：重点
6. 'TO DO'：目标
7. ...
### 2018-08-05 
+ <del>\+ 爬取京东图书等信息</del>

### 2018-08-10
+ \~ 泛型传参结构
+ \+ HuYa游戏列表(gid + name)
+ \+ Spring @Scheduled(@EnableScheduling + @Scheduled)

### 2018-08-13
+ \~ 数据库结构持续调整中...
+ \+ HuYa主播信息：bean<=>pojo正确映射

### 2018-08-16
+ \+ (mysql)30分钟定时更新Live列表（HuYaGameType.java）
+ \+ (mysql)5分钟定时更新所有Live大类前20直播间信息（HuYaLiveInfo.java）
+ \+ (mongodb)5分钟定时插入所有Live大类前20直播间日志留存（LiveInfoLog.java）

### 2018-08-16(to do plan)
#### research result：HuYa模拟登录流程：重点在于获取oath_token：
1. POST请求：https://www.huya.com/udb_web/authorizeURL.php?do=authorizeEmbedURL&callbackURL=https://www.huya.com/udb_web/udbport2.php?do=callback
2. 响应中包含oauth_token字段（96 char）
3. 带着token以POST请求如下路径：https://lgn.yy.com/lgn/oauth/x2/s/login_asyn.do?username=1656777876&pwdencrypt={huya固定编码后密码，可作常量不变，具体F12去获取}&oauth_token={上述步骤token}&denyCallbackURL=&UIStyle=xelogin&appid=5216&mxc=&vk=&isRemMe=1&mmc=&vv=&hiido=1
<img src="https://github.com/letcafe/LiveSpider/blob/master/picture/huya_oath.png"></image>
4. 拿到token后请为所欲为...

### 2018-08-17(to do plan)
#### research result：HuYa模拟登录流程：重点在于获取oath_token：
+ 2018-08-16中的问题，对于常规Jsoup难以解决，使用Selenium解决：
+ Selenium gradle:http://mvnrepository.com/search?q=selenium

### 2018-08-22
+ \+ SeleniumStarterTest，结合chromedriver.exe实现模拟登陆操作（已实现，但需要基于GUI的浏览器/可考虑加入PhantomJS）
+ \+ CookieResponseTest，通过纯HttpClient解析WebFlow，实现部分基于Http的模拟（未完全实现）
+ \+ 成功获得所有用户任务数量，情况信息接口内容
+ TO DO：过滤出干净的数据或者找到Selenium做WebApi得到JSON

### 2018-08-23
+ \+ HuYaUserLevel及其相关POJO
+ \+ 完成对用户信息模型HuYaUserLevel的封装及操作，并定时爬取
+ TO DO：模拟登陆的鲁棒性需要提升——成功率

### 2018-08-26
+ \+ 单机缓存6天的cookie信息
+ \+ 根据Redis的Cookie值,更新用户等级信息
+ TO DO：进一步封装cookie

### 2018-08-27
+ \+ 使用@ConfigurationProperties，读取yaml关键信息
+ \~ 独立出登录成功后的login web driver为一方法，并新增可选参数，是否显示界面，是否加载图片
+ \~ 完成每天早上6点，自动点开10项直播功能
+ \~ 调整代码结构，使其变得更加清晰
+ \~ 在日志开头添加入已完成定时功能
+ TO DO：将后台展示到前端，以便知道完成哪些工作（因为感觉开始有点难以维护了）

### 2018-08-28
+ \~ 修复了所有BUG，并且在无GUI的CentOS7中成功部署（不需要装Xvfb,只需--headless和--no-sandbox）
+ \~ 配置文件布局进行修改，方便以后扩充，明确Util与Service职责，进行分离
+ TO DO：已完成所有功能测试，正确运行，进入下一步自动化完成任务的开发

### 2018-08-29
+ \~ huya登录转变为优先使用虎牙APP扫描二维码，通过i.huya.com，然后点击注册，再点击登录，转回原登录流程
+ \~ 修复任务过多，而分配Scheduled池不足，出现长时间停滞卡顿的BUG
+ \~ 观看十场直播任务，务必设置延迟，否则因为切换太快无法计入任务
+ \~ 修正代码布局，明确流程，进一步封装模块

### 2018-09-04
+ \+ 每天早上7点，观看一小时直播（--headless），获取6个宝箱
+ \+ 每天凌晨3:15，订阅一个主播，10s后取消订阅，自动完成订阅任务

### 2018-09-04
+ \+ LomBok，极大简化JAVA Bean的Getter等样板式代码
+ \+ .gitignore文件，取消上传logs/,build/,out/,gradle/等
+ \- SnakeYAML没有使用的依赖

### 2018-09-08
+ \+ 添加Swagger文档组件备后续开放接口使用
+ TO DO：使用多线程、线程池加速LiveInfoGetter.updateHuYaLiveInfoById()方法的执行速度,目前执行一次时间20Min

### 2018-10-03
+ \+ 每天在23时59分将当天的任务完成情况存于数据库中
+ \+ 每隔2小时执行获取所有任务完成的奖励
+ \~ 修改部分Http/Https工具类调用为spring.http，后期推荐全改为Spring(方法：强制删除其他Http工具类)
+ \? 似乎虎牙登录Cookie并不会过期（未检验）

### 2018-10-04
+ \+ 完成给三个主播送礼物的任务:sendGiftTo3LiveRoom()
+ \~ 调整了日志的存档目录为${sys:user.home}保证在用户home目录可获得日志，无论windows or linux
+ \~ 调整了开发者日志为新的风格："+,-,?,~,*,TO DO"
+ \~ 阅读虎牙前端源码，优化发送弹幕任务完成的实现方式，极大程度保证弹幕发送成功率=100%
+ \~ 调整在每天早上7点收获所有任务奖励经验
+ \~ 去除大量HttpUtils改用Spring工具，简化了异常捕获

### 2018-10-05
+ \+ AOP(com.letcafe.aop.CookieInRedisCheck)拦截定时任务使得在缓存失效时可以及时容错，用以恢复Cookie值
+ \+ 绘制项目简介架构图
+ \- 不必要的Parser包以及相关类
+ \- 所有的Http工具类调用，改为Spring接口Http，进一步减少异常抛出

### 2018-10-06
+ \+ @Value和SpringEL运行时注入属性
+ \- @ConfigurationProperties包以及相关类的束缚
+ \~ SpringBoot 2.0.0 => 2.0.5
+ \~ Spring-ORM 5.0.4 => 5.1.0
+ \? 方法变量内对于一个等待释放的资源Thread.sleep(timeout)和Object.wait(timeout)效果一致，无法优化

### 2018-10-07
+ \+ 自定义banner.txt(/resources/banner.txt)，以替换SpringBoot原字符画
+ \~ 给主播赠送礼物BUG修复
+ \~ 发送弹幕操作BUG修复

### 2018-10-16
+ \+ 自动领取所有宝箱，并作日志记录(在完成观看时间后自动执行，共享同一个WebDriver)
+ \~ 通过JS控制登陆极大简化模拟登陆获取Cookie流程和准确率（->100%）
+ \~ 调整Logger为format格式输出
+ \~ Thread.sleep(long) => TimeUnit.[MILLISECONDS | SECONDS | MINUTES].sleep(long)

### 2018-10-21
+ \+ YAMLConfig对自定义YAML配置属性的读取，剥离出虎牙配置文件huya.yaml等
+ \~ 所有@Scheduled(cron = "cronStr")改用Spring YAML属性注入，以便后续统一调整
<img src="https://github.com/letcafe/LiveSpider/blob/master/picture/huya_yaml_config.png"></image>

### 2018-10-22
+ \+ 每日自动下注完成竞猜任务
+ \+ 使用Generator达到取之即用，职责分层的效果

### 2018-11-05
+ \+ 使用YAML占位符保持代码中的配置一致性
+ \+ SpringBoot的Gradle-plugin:2.0.2插件进行不依赖IDEA的构建
+ \+ Actuator进行程序监控
+ \+ 引入AdminLTE并且整合Thymeleaf模板，并修饰大量配置路径
+ \+ 引入Spring Security进行安全控制

### 2018-11-09
+ \~ 统一JPA和Hibernate Session于一个配置文件中并不产生冲突，JpaConfig.java
+ \+ 少量接口测试
+ \~ 修复Actuator信息metrics显示不全的问题

### 2019-01-08
+ \~ 修复webDriver在未成功初始化，溢出导致Server崩溃的情况
+ \~ 修复了由于Cookie域的原因所导致的无法初始化WebDriver的问题

### 2019-01-24
+ \+ 提供OAuth2的基础认证接口（暂未找到应用）
+ \+ 提供支持Selenium各类浏览器的结构框架
+ \+ 在原有对Chrome的支持上，新增Firefox支持
+ \~ webdriver获取的结构


### 2019-05-16
+ \~ 项目由gradle改用Maven构建

### 2019-05-23
+ \~ 修复了POM中的maven-compiler-plugin插件，以后无需每次打开项目重新配置路径
+ \~ 简化了登录中Cookie获取的流程和代码

### 2019-05-23
+ \~ 优化WebDriver底层获取流程
+ \~ 将浏览器内核环境变量配置放入static{}块中，避免不必要的多创建
+ \~ 修复定期任务中刷新Cookie到Redis中WebDriver为空的BUG