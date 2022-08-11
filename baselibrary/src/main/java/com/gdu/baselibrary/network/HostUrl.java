package com.gdu.baselibrary.network;

import com.gdu.model.config.UrlConfig;

/**
 * @author by DELL
 * @date on 2017/12/22
 * @describe
 */

public interface HostUrl {
    /**
     * app接口Host
     */
    String HOST_URL = UrlConfig.BASE_IP + ":" + UrlConfig.BASE_PORT + "/";

    /*** 模块 */
    String HOST_Gank_Image = "api/data/福利/{pageSize}/{page}";

    /**
     * 登录接口
     */
    String LOGIN = "/fms-auth-center/login";

    /**
     * 案件列表统计
     */
    String SELECT_COUNT_MY_CASE = "/fms-river-protection/riverCase/selectCountMyCase";

    /**
     * 获取分派案件列表
     */
    String GET_DESIGNATE_CASE_LIST = "/fms-river-protection/riverCase/selectMysCasePage";

    /**
     * 获取案件列表
     */
    String SELECT_PAGE = "/fms-river-protection/riverCase/selectPage";

    /**
     * 获取案件待批示列表
     */
    String GET_WAIT_COMMENT_CASE_LIST = "/fms-river-protection/riverComment/selectCaseWaitComment";

    /**
     * 获取查看权限列表
     */
    String QUERY_USER_ROLE_TREE = "/cloud-fms/role/queryUserRoleTree";

    /**
     * 案件分派修改接口-接警、完成
     */
    String DESIGNATE_STATUS_UPDATE = "/fms-river-protection/riverDesignate/designateUpdate";

    /**
     * 案件处置
     */
    String DESIGNATE_CASE = "/fms-river-protection/riverDesignate/designateCase";
    /**
     * 案件批示
     */
    String COMMENT_CASE = "/fms-river-protection/riverComment/add";

    /**
     * 查询案件批示
     */
    String SELECT_CASE_COMMENTS = "/fms-river-protection/riverComment/selectCaseCommentS";

    /**
     * 案件结案
     */
    String FINISH_CASE = "/fms-river-protection/riverCase/finishCase";

    /**
     * 上传文件
     */
    String UPLOAD_CHAT_FILE = "/fms-river-protection/file/upload";

    /**
     * 多文件上传
     */
    String UPLOAD_MULTIPART_FILES = "/fms-river-protection/file/uploads";

    /**
     * 获取消息历史记录
     */
    String GET_CHAT_HISTORY = "/fms-river-protection/chat/listPage";

    /**
     * 获取案件详情
     */
    String GET_CASE_DETAIL = "/fms-river-protection/riverCase/selectDetail";

    /**
     * 获取案件详情(新)
     */
    String GET_CASE_DETAIL_NEW = "stru-service/structuration/tAlarmHandle/getAlarmHandleRecordById";

    /**
     案件记录-处置记录
     */
    String SELECT_CASE_RECORD = "/fms-river-protection/riverCase/selectCaseRecord";

    /**
     查询组织树
     */
    String GET_DEPT_TREE = "/cloud-fms/dept/getDeptTree";

    /**
     查询人员分页信息
     */
    String GET_EMPLOYEE_TREE = "/cloud-fms/employee/selectPage";

    /**
     修改用户头像
     */
    String UPDATE_HEAD_IMG = "/cloud-fms/sysuser/updateHeadImg";

    /**
     * 修改用户密码
     */
    String UPDATE_PWD = "/cloud-fms/sysuser/updatePwd";

    /**
     * 获取某个人所有聊天群
     */
    String GET_CHAT_HIS_LIST = "/fms-river-protection/chat/selectChatHisList";

    /**
     * 获取告警中心
     */
    String GET_ALARM_LIST = "/stru-service/structuration/tAlarm/query";

    /**
     * 获取告警备注
     */
    String GET_ALARM_REMARK = "/stru-service/structuration/tAlarmRemark/query";

    /**
     * 更新告警状态
     */
    String UPDATE_ALARM = "/stru-service/structuration/tAlarm/update";

    /**
     * 备注告警
     */
    String REMARK_ALARM = "/stru-service/structuration/tAlarmRemark/insert";

    /**
     * App升级
     */
    String APP_UPGRADE = "/api/other/update_check";

    /**
     * 获取设备列表
     */
//    String GET_DEVICE_LIST = "/stru-service/structuration/tAlarm/devices";
    String GET_DEVICE_LIST = "/cloud-video/device/list";

    /**
     * 获取某一设备预警列表
     */
    String GET_ALARM_DEVICE_INFO = "/stru-service/structuration/tAlarm/query";

    /**
     * 获取案件通知列表
     */
    String GET_MY_CASE = "/fms-river-protection/riverCase/getMyCase";

    /**
     * 修改案件通知为已读
     */
    String READ_CASE = "/fms-river-protection/riverCase/readCase";

    /**
     * 获取告警中心(新-支持筛选)
     */
//    String GET_ALARM_LIST_NEW = "/stru-service/structuration/tAlarm/webQuery";
    String GET_ALARM_LIST_NEW = "/stru-service/structuration/tAlarm/appQuery";

    /**
     * 关注/取消关注
     */
    String ATTENTION = "/stru-service/structuration/tAlarm/user/attention";

    /**
     * 获取通知消息列表
     */
    String MESSAGE_NOTIFY_LIST = "/stru-service/structuration/msg/list";

    /**
     * app端预警类型统计
     */
    String APP_ALARM_TYPE_STATISTICS = "/stru-service/structuration/tAlarm/appAlarmTypeStatistics";

    /**
     * 是否有app菜单权限
     */
    String IS_APP_MENU = "/cloud-fms/role/isAppMenu";

    /**
     * 查询分派和处理信息(获取案件详情信息)
     */
    String GET_CASE_DETAIL_INFO = "/stru-service/structuration/tAlarmAssignment/appQueryAssignmentAndHandleInfo";

    /**
     * 查询分派记录
     */
    String QUERY_ASSIGNMENT = "/stru-service/structuration/tAlarmAssignment/queryAssignment";

    /**
     * 分派预警
     */
    String ASSIGNMENT = "/stru-service/structuration/tAlarmAssignment/assignment";

    /**
     * 预警处理
     */
    String ALARM_HANDLE = "/stru-service/structuration/tAlarmHandle/handle";

    /**
     * 处理信息列表
     */
    String QUERY_ALARM_HANDLE_RECORD = "/stru-service/structuration/tAlarmHandle/queryAlarmHandleRecord";

    /**
     * 补充处理
     */
    String SUPPLEMENT_HANDLE = "/stru-service/structuration/tAlarmSupplementHandle/supplementHandle";

    /**
     * 预警接警
     */
    String ANSWER_POLICE = "/stru-service/structuration/tAlarmAssigned/answerPolice";

    /**
     * 查询告警被分派列表
     */
    String QUERY_ASSIGNMENT_LIST = "/stru-service/structuration/tAlarmAssigned/queryAssignmentByAlarmId";

    /**
     * 聊天处理信息详情
     */
    String GET_ALARM_HANDLE_RECORD = "/stru-service/structuration/tAlarmHandle/getAlarmHandleRecord";

    /**
     * 获取聊天列表顶部预计详情
     */
    String GET_ALARM_BY_ALARM_ID = "/stru-service/structuration/tAlarmHandle/getAlarmByAlarmId";
}
