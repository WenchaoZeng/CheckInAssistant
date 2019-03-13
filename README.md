# 打卡助手核心机制

通过检测WIFI连接的变化, 智能地识别出你上班的时间点和下班时间点, 从而提醒你准时打卡. 本软件只是提醒, 打卡动作依然是手动的.

# 已支持功能列表

* 支持多个公司WIFI名称和WIFI之间切换. 手机只要任意连上任何一个WIFI, 都可识别为上班. 从所有的WIFI断开连接, 才识别为下班.

* 下班时间没到不会提醒下班: 支持设置下午6:30之前断开公司WIFI, 不会被识别为下班.

* 重复提醒: 若提醒未处理, 则每隔十秒钟提醒一次, 这样不会被遗忘.

* 忽略本次提醒

    * 假设下午去买咖啡, 提醒下班打卡, 可以点击"忽略本次提醒"忽略本次的下班提醒.

    * 假设周六来公司拿点东西, 提醒上班打卡, 可以点击"忽略本次提醒"忽略本次的上班提醒.

* 忽略一小时: 1小时之内不再检测WIFI连接变化. 比如上厕所信号不好, 导致WIFI频繁连接/断开, 这时会频繁提示下班打卡, 可以用"忽略一小时"暂时忽略, 待上完厕再点击"取消忽略"恢复.

# 待开发功能点

* 修复后台进程自动退出的问题

* 运行日志

* 用sleep不行, 10秒提醒不太准时.