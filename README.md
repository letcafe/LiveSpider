# LiveSpider
<img src="https://github.com/letcafe/LiveSpider/blob/master/picture/huya_logo.png" width="200px"></image>
## Developer information
##### Maintainer: letcafe
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

## 已知信息：
+ 任务每天在0点刷新
+ 6个百宝箱在早上6点刷新

## 定时任务：
### 爬取数据
+ 0 20/30 * * * * => LiveInfoGetter.updateAllHuYaLiveInfo => 每30分钟更新直播间信息于MySQL
+ 0 0 0 ? * SUN,WED => UserLevelAndTaskGetter.setUserLoginCookie => 每周日、周三零点更新登录Cookie
+ 0 20/15 * * * * => LiveInfoGetter.insertAll => 每30分钟获取前20条LOL的直播信息存MongoDB
+ 0 0 0/4 * * * => UserLevelAndTaskGetter.setUserTaskStatus => 每隔4小时获取用户经验状态存MySQL
+ 25/30 * * * * * => GameTypeGetter.gameTypeScheduled => 每隔30s更新一次游戏列表于MySQL
### 每日任务自动完成
+ 0 0 6 * * * => TaskAutoWorker.watchNumberedLive => 每天凌晨3：00完成观看10名主播的任务
+ 0 0 6 * * * => TaskAutoWorker.sendPubMessage => 每天凌晨3：10完成发送一条弹幕任务
+ 0 15 3 * * * => TaskAutoWorker.subscribeOneLiveRoomTask => 每天凌晨3:15订阅一个DNF主播并10s后取消订阅
+ 0 0 7 * * * => TaskAutoWorker.watchLiveGetSixTreasure => 每天早上7：00观看一小时直播以获得6个宝箱（宝箱6点刷）

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
<img src="https://github.com/letcafe/LiveSpider/blob/master/picture/huya_oath.png"></image>
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

### 2018-08-27
+ 更新：独立出登录成功后的login web driver为一方法，并新增可选参数，是否显示界面，是否加载图片
+ 更新：完成每天早上6点，自动点开10项直播功能
+ 更新：调整代码结构，使其变得更加清晰
+ 更新：使用@ConfigurationProperties，读取yaml关键信息
+ 更新：在日志开头添加入已完成定时功能
+ 下一步：将后台展示到前端，以便知道完成哪些工作（因为感觉开始有点难以维护了）

### 2018-08-28
+ 更新：修复了所有BUG，并且在无GUI的CentOS7中成功部署（不需要装Xvfb,只需--headless和--no-sandbox）
+ 经验：无GUI装Selenium+Chrome，可先用Python测试，所有上JAVA（Linux自带Py好上手测试环境）
+ 调整：配置文件布局进行修改，方便以后扩充，明确Util与Service职责，进行分离
+ 下一步：已完成所有功能测试，正确运行，进入下一步自动化完成任务的开发

### 2018-08-29
+ 更新：huya登录转变为优先使用虎牙APP扫描二维码，通过i.huya.com，然后点击注册，再点击登录，转回原登录流程
+ 修复：修复任务过多，而分配Scheduled池不足，出现长时间停滞卡顿的BUG
+ 修复：观看十场直播任务，务必设置延迟，否则因为切换太快无法计入任务
+ 突破：经测试，有了Cookie + Selenium，在虎牙可以横着走
+ 调整：修正代码布局，明确流程，进一步封装模块

### 2018-09-04
+ 新增：每天早上7点，观看一小时直播（--headless），获取6个宝箱
+ 新增：每天凌晨3:15，订阅一个主播，10s后取消订阅，自动完成订阅任务

### 2018-09-04
+ 新增：LomBok，极大简化JAVA Bean的Getter等样板式代码
+ 新增：.gitignore文件，取消上传logs/,build/,out/,gradle/等
+ 删除：SnakeYAML没有使用的依赖
