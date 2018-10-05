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
>Gradle support:https://gradle.org/

## Deploy notice
+ <del>Visit url：http://localhost:8080/index</del>(On the way...)
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
+ 0 59 23 * * * => GameTypeGetter.gameTypeScheduled => 每天23时59分获取今天任务完成情况于MySQL
### 每日任务自动完成
+ 0 0 3 * * * => TaskAutoWorker.watchNumberedLive => 每天凌晨3：00完成观看10名主播的任务
+ 0 10 3 * * * => TaskAutoWorker.sendPubMessage => 每天凌晨3：10完成发送一条弹幕任务
+ 0 15 3 * * * => TaskAutoWorker.subscribeOneLiveRoomTask => 每天凌晨3:15订阅一个DNF主播并10s后取消订阅
+ 0 20 3 * * * => TaskPrizeGetter.sendGiftTo3LiveRoom => 每天凌晨3:20给LOL列表前三主播送礼物（默认虎粮:id = 4）
+ 0 1 6 * * * => TaskAutoWorker.watchLiveGetSixTreasure => 每天上午6：05观看55分钟直播以获得6个宝箱（宝箱6点刷）
+ 0 0 7 * * * => TaskPrizeGetter.getAllTaskPrize => 每天上午7点收取所有的任务经验

## Framework (Updated in 2018-10-05)
<img src="https://github.com/letcafe/LiveSpider/blob/master/picture/framework.png" width="450px"></image>

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
+ ing：数据库结构持续调整中...
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
+ \~ 独立出登录成功后的login web driver为一方法，并新增可选参数，是否显示界面，是否加载图片
+ \~ 完成每天早上6点，自动点开10项直播功能
+ \~ 调整代码结构，使其变得更加清晰
+ \~ 使用@ConfigurationProperties，读取yaml关键信息
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
+ \? 似乎登录Cookie并不会过期（未检验）

### 2018-10-04
+ \+ 完成给三个主播送礼物的任务:sendGiftTo3LiveRoom()
+ \~ 调整了日志的存档目录为${sys:user.home}保证在用户home目录可获得日志，无论windows or linux
+ \~ 调整了开发者日志为新的风格："+,-,?,~,*,TO DO"
+ \~ 阅读虎牙前端源码，优化发送弹幕任务完成的实现方式，极大程度保证弹幕发送成功率=100%
+ \~ 调整在每天早上7点收获所有任务奖励经验
+ \~ 去除大量HttpUtils改用Spring工具，简化了异常捕获

### 2018-10-04
+ \+ AOP(com.letcafe.aop.CookieInRedisCheck)拦截定时任务使得在缓存失效时可以及时容错，用以恢复Cookie值
+ \~ 绘制项目架构图
+ \- 删除了不必要的Parser包以及相关类