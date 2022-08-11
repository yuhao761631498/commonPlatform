package com.gdu.model.config;

public class UrlConfig {

    /**
     * 测试环境IP地址
     */
    public static final String TEST_IP = "http://172.16.63.43";

//    /**
//     * 正式环境IP地址 旧
//     */
//    public static final String PRODUCTION_IP = "http://122.112.203.178";

    /**
     * 正式环境IP地址 新
     */
    public static final String PRODUCTION_IP = "http://36.137.226.187";

//    /**
//     * App的基础IP地址 旧
//     */
//    public static String BASE_IP = "http://122.112.203.178";

    //    /**
//     * App的基础IP地址 新
//     */
    public static String BASE_IP = "http://36.137.226.187";

    /**
     * API请求所用的端口
     */
    public static String BASE_PORT = "8850";

    /**
     * 请求图片所用的端口
     */
    public static String IMG_PORT = "81";

    /**
     * Mqtt的ip地址
     */
    public static String MQTT_IP = "36.137.226.187";

    /**
     * Mqtt的测试ip地址
     */
    public static String MQTT_TEST_IP = "172.16.63.43";

//    /**
//     * Mqtt的正式环境ip地址 旧
//     */
//    public static String MQTT_PRODUCTION_IP = "122.112.203.178";

    /**
     * Mqtt的正式环境ip地址 新
     */
    public static String MQTT_PRODUCTION_IP = "36.137.226.187";

    /**
     * Mqtt的端口地址
     */
    public static String MQTT_PORT = "1883";

    /**
     * Mqtt的用户名
     */
    public static String MQTT_USE_NAME = "admin";

    /**
     * Mqtt的密码
     */
    public static String MQTT_USE_PWD = "admin";

    /**
     * Mqtt的订阅聊天消息主题
     */
    public final static String MQTT_MESSAGE_TOPIC = "app/river/notice/";

    /**
     * Mqtt的订阅主题
     */
    public final static String MQTT_TOPIC = "web/river/caseHandling";

    /**
     * 告警通知订阅主题
     */
    public final static String MQTT_ALARM_NOTICE = "newAlarmOrCaseNoticeTopic/";

    /**
     * 值班安排订阅主题 mqqt/duty/【userId】   userid为自己的用户id
     */
    public final static String MQTT_DUTY_NOTICE = "mqqt/duty/";

    /**
     * Mqtt的发送坐标主题
     */
    public final static String MQTT_SEND_LOCATION_TOPIC = "app/user/coordinate";

    /**
     * 退出app发送的Mqtt主题
     */
    public final static String MQTT_SEND_APP_OFF_LINE = "app/user/offline";

    /**
     * 长江大保护 host
     */
    public static String HttpCP = BASE_IP + ":" + BASE_PORT;


    public static String ImgIp = BASE_IP + ":" + IMG_PORT;

    /**
     * 登录
     */
    public final static String loginUrl = "/fms-auth-center/login";

    /**
     * 个人中心获取用户信息
     */
    public final static String PersonInfo = "/cloud-fms/userManage/getUserDetail";

    /**
     * 获取高点列表信息接口
     */
    public final static String DeviceListInfo = "/cloud-video/deptAndDevice/tree";

    /**
     * <p>获取天气信息</p>
     */
    public static final String Weather_map = "http://api.openweathermap.org/data/2.5/weather";


    //    public final static String ptzControl = "/video-service2/index/api/ptzConrol";
    public final static String ptzControl = "/cloud-video/device/operationPtz";

    /**
     * 搜索文件系统，获取流对应的录像文件列表或日期文件夹列表
     */
    public final static String getMp4RecordFolder = "/video-service1/index/api/getMp4RecordFile";

    /**
     * 搜索文件系统，获取流对应的录像文件
     */
    public final static String getMp4RecordFile = "/video-service1/record/";

    /**
     * SDK/GB接入设备、无人机开启流
     */
    public final static String playStream = "/cloud-video/device/playStream";

    /**
     * 获取某个人所有聊天群
     */
    public final static String getChatHisList = "/fms-river-protection/chat/selectChatHisList";

    /**
     * 案件列表统计
     */
    public final static String selectCountCase = "/fms-river-protection/riverCase/selectCountMyCase";

    /**
     * 获取案件来源和案件类型
     */
    public final static String getTypeOrSource = "/cloud-fms/dict/queryByTypeCode";

    /**
     * 新增案件
     */
    public final static String addCase = "/fms-river-protection/riverCase/add";

    /**
     * 新增巡逻
     */
    public final static String addPatrol = "/fms-river-protection/patrol/add";

    /**
     * 发布巡逻记录点
     */
    public final static String addPatrolRecord = "/fms-river-protection/patrol/addPatrolRecord";

    /**
     * 删除巡逻记录点
     */
    public final static String delRecord = "/fms-river-protection/patrol/del";

    /**
     * 查询巡逻信息
     */
    public final static String queryRecords = "/fms-river-protection/patrol/query";

    /**
     * 上报巡逻实时位置
     */
    public final static String reportLocate = "/fms-river-protection/patrol/reportLocate";

    /**
     * 修改巡逻
     */
    public final static String updateRecord = "/fms-river-protection/patrol/update";

    /**
     * 资源分布组织人员-在线/有坐标
     */
    public final static String getSelectListEmployee = "/fms-river-protection/riverCase/selectListEmployee";

    /**
     * 案件信息列表(案件中心分页查询)
     */
    public final static String getCaseInfo = "/fms-river-protection/riverCase/selectPage";

    /**
     * 获取高点设备资源列表
     */
    public final static String getMonitorDevice = "/cloud-video/device/selectListDeviceGDJK";

    /**
     * 无人机设备资源列表
     */
    public final static String getAirplaneInfo = "/cloud-video/device/selectListDeviceWRJ";

    /**
     * 关注点位资源列表
     */
    public final static String getFocusPoint = "/cloud-fms/resourcesInfo/selectListPoint";

    /**
     * 重点线路资源列表
     */
    public final static String getCircuitInfo = "/cloud-fms/resourcesInfo/selectListLine";

    /**
     * 重点区域资源列表
     */
    public final static String getRegionInfo = "/cloud-fms/resourcesInfo/selectListArea";

    /**
     * 获取组织机构信息列表
     */
    public final static String getOrganizationList = "/fms-river-protection/riverCase/selectListDeptInfo";

    /**
     * 二次定位更新案件地址
     */
    public final static String updateLocation = "/fms-river-protection/riverCase/update";

    /**
     * 数据监测 - 案件总数统计
     */
    public final static String getDataCaseTotal = "/fms-river-protection/riverCase/selectAllCountCase";

    /**
     * 数据监测 - 告警总数统计
     */
    public final static String getDataAlarmTotal = "/stru-service/structuration/tAlarm/count";

    /**
     * 数据监测 - 高点监控总数统计
     */
    public final static String getDataHighTotal = "/fms-river-protection/riverCase/selectCountHighDevice";

    /**
     * 案件来源分布
     */
    public final static String getCaseResourceFrom = "/fms-river-protection/riverCase/selectCountCaseByYear";

    /**
     * 案件处置名单排名
     */
    public final static String getCaseRankProgress = "/fms-river-protection/riverCase/selectCountDesignateCase";

    /**
     * 告警事件类型分布，按告警等级、告警类型进行统计，都是请求这个接口
     */
    public final static String getAlarmResInfo = "/stru-service/structuration/tAlarm/alarmTypeStatistics";

    /**
     * 告警数据时间分布趋势
     */
    public final static String getAlarmTimeByYear = "/stru-service/structuration/tAlarm/trend/byYear";

    /**
     * 修改用户信息
     */
    public final static String updateUserInfo = "/cloud-fms/userManage/edit";

    /**
     * 上传头像
     */
    public final static String uploadHeadImg = "/cloud-fms/userManage/uploadHeadImage";

    /**
     * 重置密码
     */
    public final static String resetPsw = "/cloud-fms/userManage/restPassword";

    /**
     * 获取验证码
     */
    public final static String getCheckCode = "/cloud-fms/userManage/getPhoneCheck/{phone}";

    /**
     * 获找回密码通过手机号
     */
    public final static String getPswByPhone = "/cloud-fms/userManage/forgetByPhone";

    /**
     * 获取加载web页面的url  getWebLoadUrl
     */
    public final static String getWebLoadUrl = "/stru-service/structuration/masses-report/globalConfig";


    public final static String getAlarmTypeForDay = "/stru-service/structuration/tAlarm/getAlarmTypeStatistics";

    public final static String getAlarmDealForDay = "/stru-service/structuration/tAlarm/getAlarmHandleStatistics";

    public final static String uploadAlarm = "/stru-service/structuration/tAlarm/generateAlarm";

    public final static String deviceRank = "/stru-service/structuration/tAlarm/rank";

    public final static String dealResult = "/stru-service/structuration/tAlarm/alarmHandleResultStatistics";

    public final static String dutyForDay = "/cloud-video/duty/getDesignateInfo";

    public final static String dutyForMe = "/cloud-video/duty/getList";

    public final static String alarmArea = "/cloud-video/dict/getDept";


    public static void resetIpAndPort() {
        HttpCP = BASE_IP + ":" + BASE_PORT;
        ImgIp = BASE_IP + ":" + IMG_PORT;
    }

    public static String getHostUrl() {
        return BASE_IP + ":" + BASE_PORT;
    }
}
