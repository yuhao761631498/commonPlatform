package com.gdu.command.ui.patrol.presenter;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.command.ui.patrol.bean.AddPatrolBean;
import com.gdu.command.ui.patrol.bean.AddRecordResultBean;
import com.gdu.command.ui.patrol.bean.QueryDataBean;
import com.gdu.model.config.UrlConfig;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PatrolService {
    String REQ_NAME_ADD_PATROL = "addPatrol";
    String REQ_NAME_ADD_PATROL_RECORD = "addPatrolRecord";
    String REQ_NAME_DEL_RECORD = "delRecord";
    String REQ_NAME_QUERY_RECORDS = "queryRecords";
    String REQ_NAME_REPORT_LOCATE = "reportLocate";
    String REQ_NAME_UPDATE_RECORD = "updateRecord";
    String REQ_NAME_ADD_CASE = "addCase";

    /**
     * 添加巡逻
     * @return
     */
    @POST(UrlConfig.addPatrol)
    Observable<AddPatrolBean> addPatrol(@Body RequestBody jsonBody);

    /**
     * 发布巡逻记录点
     * @return
     */
    @POST(UrlConfig.addPatrolRecord)
    Observable<AddRecordResultBean> addPatrolRecord(@Body RequestBody jsonBody);

    /**
     * 删除巡逻记录点
     * @return
     */
    @GET(UrlConfig.delRecord)
    Observable<BaseBean<String>> delRecord(@Body RequestBody jsonBody);

    /**
     * 查询巡逻信息
     * @return
     */
    @POST(UrlConfig.queryRecords)
    Observable<QueryDataBean> queryRecords(@Body RequestBody jsonBody);

    /**
     * 上报巡逻实时位置
     * @return
     */
    @POST(UrlConfig.reportLocate)
    Observable<BaseBean<String>> reportLocate(@Body RequestBody jsonBody);

    /**
     * 修改巡逻
     * @return
     */
    @POST(UrlConfig.updateRecord)
    Observable<AddPatrolBean> updateRecord(@Body RequestBody jsonBody);
}
