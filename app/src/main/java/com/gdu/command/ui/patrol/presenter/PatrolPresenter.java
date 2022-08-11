package com.gdu.command.ui.patrol.presenter;

import android.content.Intent;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.ui.cases.CaseService;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.command.ui.patrol.bean.QueryDataBean;
import com.gdu.command.uploadpic.IUploadPicDiaryView;
import com.gdu.command.uploadpic.UploadPicPresenter;
import com.gdu.util.TimeUtils;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PatrolPresenter extends BasePresenter {
    private IPatrolDiaryView mPatrolDiaryView;
    private UploadPicPresenter mUploadPicPresenter;

    public PatrolPresenter() {
        mUploadPicPresenter = new UploadPicPresenter();
    }

    public void setIView(IPatrolDiaryView view) {
        mPatrolDiaryView = view;
    }

    public void setUploadPicDiaryView(IUploadPicDiaryView view) {
        if (view != null) {
            mUploadPicPresenter.attachView(this);
            mUploadPicPresenter.setIView(view);
        }
    }

    public void uploadFiles(List<String> images) {
        mUploadPicPresenter.uploadFiles(images, null);
    }

    /**
     * 添加巡逻
     * @param latStart 开始位置维度
     * @param lonStart 开始位置经度
     * @param patrolTypeName 巡逻类型名称
     * @param patrolUser 巡逻人
     */
    public void addPatrol(double latStart, double lonStart, String patrolTypeName, int patrolUser) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("latStart", latStart);
        map.put("lonStart", lonStart);
        map.put("patrolTypeName", patrolTypeName);
        if (patrolUser != 0) {
            map.put("patrolUser", patrolUser);
        }

        final String strEntity = new Gson().toJson(map);
        final RequestBody body = RequestBody.create(strEntity,
                MediaType.parse("application/json;charset=UTF-8"));
        PatrolService mService = RetrofitClient.getAPIService(PatrolService.class);
        mService.addPatrol(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.getData() == null || bean.getCode() != 0;
                    if (isEmpty) {
                        if (bean!=null&&bean.getCode() == 401) {
                            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                        mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_PATROL,
                                false, null);
                        return;
                    }
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_PATROL,
                            true, bean);
                }, throwable -> {
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_PATROL,
                            false, null);
                    MyLogUtils.e("添加巡逻请求出错", throwable);
                });
    }

    /**
     * 发布巡逻记录点
     * @param content 巡逻记录内容
     * @param files 记录图片文件
     * @param lat 记录地点纬度
     * @param lon 记录地点经度
     * @param locateName 记录地点名称
     * @param patrolId 巡逻表主键id
     * @param typeName 巡逻记录类型名称
     */
    public void addPatrolRecord(String content, String files, double lat, double lon,
                               String locateName, int patrolId, String typeName) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("files", files);
        map.put("lat", lat);
        map.put("lon", lon);
        map.put("locateName", locateName);
        map.put("patrolId", patrolId);
        map.put("typeName", typeName);

        final String strEntity = new Gson().toJson(map);
        final RequestBody body = RequestBody.create(strEntity,
                MediaType.parse("application/json;charset=UTF-8"));
        PatrolService mService = RetrofitClient.getAPIService(PatrolService.class);
        mService.addPatrolRecord(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.getData() == null;
                    if (isEmpty) {
                        if (bean!=null&&bean.getCode() == 401) {
                            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                        mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_PATROL_RECORD,
                                false, null);
                        return;
                    }
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_PATROL_RECORD,
                            true, bean);
                }, throwable -> {
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_PATROL_RECORD,
                            false, null);
                    MyLogUtils.e("添加巡逻记录点请求出错", throwable);
                });
    }

    /**
     * 删除巡逻信息
     * @param id 记录ID
     */
    public void delRecord(int id) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);

        final String strEntity = new Gson().toJson(map);
        final RequestBody body = RequestBody.create(strEntity,
                MediaType.parse("application/json;charset=UTF-8"));
        PatrolService mService = RetrofitClient.getAPIService(PatrolService.class);
        mService.delRecord(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.data == null;
                    if (isEmpty) {
                        if (bean!=null&&bean.code == 401) {
                            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                        mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_DEL_RECORD,
                                false, null);
                        return;
                    }
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_DEL_RECORD,
                            true, bean);
                }, throwable -> {
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_DEL_RECORD,
                            false, null);
                    MyLogUtils.e("删除巡逻记录点请求出错", throwable);
                });
    }

    /**
     * 查询巡逻信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param id 巡逻id
     * @param patrolRecordTypeName 巡查记录类型名称
     * @param patrolTypeName 巡逻类型名称
     * @param patrolUser 巡逻人
     * @param sort 排序 0从小到大 1从大到小
     */
    public void queryRecord(String endTime, String startTime, int id, String patrolRecordTypeName,
                             String patrolTypeName, int patrolUser, int sort) {
        MyLogUtils.d("queryRecord() endTime = " + endTime + "; startTime = " + startTime
                + "; id = " + id + "; patrolRecordTypeName = " + patrolRecordTypeName
                + "; patrolTypeName = " + patrolTypeName + "; patrolUser = " + patrolUser
                + "; sort = " + sort);
        final HashMap<String, Object> map = new HashMap<>();
        if (id != 0) {
            map.put("id", id);
        }
        if (!CommonUtils.isEmptyString(startTime)) {
            map.put("startTime", startTime);
        }
        if (!CommonUtils.isEmptyString(endTime)) {
            map.put("endTime", endTime);
        }
        if (!CommonUtils.isEmptyString(patrolRecordTypeName)) {
            map.put("patrolRecordTypeName", patrolRecordTypeName);
        }
        if (!CommonUtils.isEmptyString(patrolTypeName)) {
            map.put("patrolTypeName", patrolTypeName);
        }
        if (patrolUser != 0) {
            map.put("patrolUser", patrolUser);
        }
        if (sort != -1) {
            map.put("sort", sort);
        }

        final String strEntity = new Gson().toJson(map);
        final RequestBody body = RequestBody.create(strEntity,
                MediaType.parse("application/json;charset=UTF-8"));
        PatrolService mService = RetrofitClient.getAPIService(PatrolService.class);
        mService.queryRecords(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.getData() == null;
                    if (isEmpty) {
                        if (bean!=null&&bean.getCode() == 401) {
                            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                        mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_QUERY_RECORDS,
                                false, null);
                        return;
                    }
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_QUERY_RECORDS,
                            true, bean);
                }, throwable -> {
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_QUERY_RECORDS,
                            false, null);
                    MyLogUtils.e("查询巡逻记录请求出错", throwable);
                });
    }

    private boolean isTimeOK(String startTime, String endTime, QueryDataBean.DataBean bean){
        if (CommonUtils.isEmptyString(startTime) || CommonUtils.isEmptyString(endTime)) {
            return false;
        }
        long capTime = 0;
        try {
            capTime = TimeUtils.sdf_ymdhms.parse(bean.getStartTime()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final long st = TimeUtils.getTimeStamp(startTime, "yyyy/MM/dd HH:mm:ss");
        final long et = TimeUtils.getTimeStamp(endTime, "yyyy/MM/dd HH:mm:ss");
        return capTime > st && capTime < et;
    }

    private boolean havePatrolType(Map<String, Boolean> map, QueryDataBean.DataBean bean) {
        if (map == null) {
            return false;
        }
        Boolean status = map.get(bean.getPatrolTypeName());
        if (status == null) {
            return false;
        }
        return status;
    }

    private boolean haveRecordType(Map<String, Boolean> map, QueryDataBean.DataBean bean) {
        if (map == null) {
            return false;
        }
        Boolean status = map.get(bean.getPatrolTypeName());
        if (status == null) {
            return false;
        }
        return status;
    }

    /**
     * 上报巡逻实时位置
     * @param patrolId 巡逻id *
     * @param lat 位置纬度
     * @param lon 位置经度
     */
    public void reportLocate(int patrolId, double lat, double lon) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("patrolId", patrolId);
        map.put("lat", lat);
        map.put("lon", lon);

        final String strEntity = new Gson().toJson(map);
        final RequestBody body = RequestBody.create(strEntity,
                MediaType.parse("application/json;charset=UTF-8"));
        PatrolService mService = RetrofitClient.getAPIService(PatrolService.class);
        mService.reportLocate(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.data == null;
                    if (isEmpty) {
                        if (bean!=null&&bean.code== 401) {
                            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                        mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_REPORT_LOCATE,
                                false, null);
                        return;
                    }
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_REPORT_LOCATE,
                            true, bean);
                }, throwable -> {
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_REPORT_LOCATE,
                            false, null);
                    MyLogUtils.e("上报位置请求出错", throwable);
                });
    }

    /**
     * 修改巡逻
     * @param id 巡逻id
     * @param latEnd 位置纬度
     * @param lonEnd 位置经度
     * @param patrolDistance 巡逻距离
     * @param patrolPathPic 巡逻轨迹图片
     */
    public void updateRecord(int id, double latEnd, double lonEnd, int patrolDistance,
                             String patrolPathPic) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("latEnd", latEnd);
        map.put("lonEnd", lonEnd);
        map.put("patrolDistance", patrolDistance);
        map.put("patrolPathPic", patrolPathPic);

        final String strEntity = new Gson().toJson(map);
        final RequestBody body = RequestBody.create(strEntity,
                MediaType.parse("application/json;charset=UTF-8"));
        PatrolService mService = RetrofitClient.getAPIService(PatrolService.class);
        mService.updateRecord(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.getData() == null || bean.getCode() != 0;
                    if (isEmpty) {
                        if (bean!=null&&bean.getCode() == 401) {
                            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                        mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_UPDATE_RECORD,
                                false, null);
                        return;
                    }
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_UPDATE_RECORD,
                            true, bean);
                }, throwable -> {
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_UPDATE_RECORD,
                            false, null);
                    MyLogUtils.e("更新巡逻记录请求出错", throwable);
                });
    }

    /**
     * 添加案件
     * @param caseDesc 案件简要描述 *
     * @param caseName 案件名称 *
     * @param caseStartTime 案发时间 *
     * @param caseType 案件类型 *
     * @param infoSource 案件来源 *
     * @param receivingAlarmMan 接警人、接案人员 *
     * @param reportAddr 举报地址 *
     * @param reportMan 举报人 *
     * @param reportTime 报警时间 *
     * @param alarmId 告警id
     * @param caseFile 案件文件(多个以逗号分隔)
     * @param designateMan 指派人(逗号分隔ID-人员ID)
     * @param latitude 举报地址纬度 *
     * @param longitude 举报地址经度 *
     * @param reportTel 举报电话 *
     * @param reporterGender 举报人性别
     * @param reporterIdentity 举报人身份证号
     */
    public void addCase(String caseDesc, String caseName, String caseStartTime, String caseType,
                        String infoSource, String receivingAlarmMan, String reportAddr,
                        String reportMan, String reportTime, int alarmId, String caseFile,
                        String designateMan, double latitude, double longitude,
                        String reportTel, String reporterGender, String reporterIdentity) {
        final Map<String, Object> map = new HashMap<>();
        map.put("caseDesc", caseDesc);
        map.put("caseName", caseName);
        map.put("caseStartTime", caseStartTime);
        map.put("caseType", caseType);
        map.put("infoSource", infoSource);
        if (!CommonUtils.isEmptyString(receivingAlarmMan)) {
            map.put("receivingAlarmMan", receivingAlarmMan);
        }
        map.put("reportAddr", reportAddr);
        map.put("reportMan", reportMan);
        map.put("reportTime", reportTime);
        if (alarmId != 0) {
            map.put("alarmId", alarmId);
        }
        if (!CommonUtils.isEmptyString(caseFile)) {
            map.put("caseFile", caseFile);
        }
        if (!CommonUtils.isEmptyString(designateMan)) {
            map.put("designateMan", designateMan);
        }
        if (latitude != 0) {
            map.put("latitude", latitude);
        }
        if (longitude != 0) {
            map.put("longitude", longitude);
        }
        if (!CommonUtils.isEmptyString(reportTel)) {
            map.put("reportTel", reportTel);
        }
        if (!CommonUtils.isEmptyString(reporterGender)) {
            map.put("reporterGender", reporterGender);
        }
        if (!CommonUtils.isEmptyString(reporterIdentity)) {
            map.put("reporterIdentity", reporterIdentity);
        }

        final String strEntity = new Gson().toJson(map);
        final RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json;" +
                "charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.addCase(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isFail = bean == null || bean.getCode() != 0;
                    if (isFail) {
                        if (bean!=null&&bean.getCode() == 401) {
                            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                        }
                        mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_CASE,
                                false, null);
                        return;
                    }
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_CASE,
                            true, bean);
                }, throwable -> {
                    mPatrolDiaryView.onRequestCallback(PatrolService.REQ_NAME_ADD_CASE,
                            false, null);
                    MyLogUtils.e("新增案件出错", throwable);
                });
    }

}
