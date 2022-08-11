package com.gdu.model.config;

import com.gdu.model.alarm.AlarmInfo;
import com.gdu.model.cases.CommentInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.organization.OrganizationInfo;
import com.gdu.model.user.PersonInfoBean;

import java.util.List;
import java.util.Map;

/**
 * App 中全局变量
 *
 */
public class GlobalVariable {


    private GlobalVariable() {
    }

    public static boolean mainActivityHadCreated = false;

    /**
     *  案件
     */
    public static String EXTRA_CASE = "extra_case";

    /**
     * 案件状态
     */
    public static String EXTRA_CASE_STATUS = "extra_case_status";

    /**
     *  案件ID
     */
    public static String EXTRA_CASE_ID = "extra_case_id";

    /**
     * 案件处置请求
     */
    public static final int REQUEST_DEAL_CASE_CODE = 100;

    /**
     * 案件批示请求
     */
    public static final int REQUEST_COMMENT_CASE_CODE = 101;

    /**
     * 当前处置的案件
     */
    public static IssueBean sCurrentIssueBean;

    /**
     * 当前组织机构
     */
    public static OrganizationInfo sOrganizationInfo;

    /**
     * 当前案件的批示列表
     */
    public static List<CommentInfo> sCommentInfoList;

    /**
     * 个人信息
     */
    public static PersonInfoBean sPersonInfoBean;

    /**
     * 聊天记录列表
     */
    public static Map<String, String> mLastMessageMap;

    /**
     * 案件记录列表
     */
    public static List<Map<String, Object>> sCaseRecords;

    /**
     * 告警信息
     */
    public static AlarmInfo sAlarmInfo;

    /**
     * 是否有app菜单权限
     */
    public static boolean isAppMenu;

    /**
     * 人员活动
     */
    public static String findPeople="0";


    /**
     * 非法垂钓
     */
    public static String illegalFish="0";

    /**
     * 船舶闯入
     */
    public static String findShip="0";

    /**
     * 非法捕捞
     */
    public static String staffRetention="0";


    /**
     * 待接警
     */
    public static String waitReceiveAlarm="0";
    /**
     * 待处理
     */
    public static String waitDealAlarm="0";

    /**
     * 已处理
     */
    public static String hasDealAlarm="0";

    /**
     * 今日报警总数
     */
    public static String alarmTotalNum="0";

}
