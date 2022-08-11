package com.gdu.command.ui.resource;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.resource.ResAirplaneInfoBean;
import com.gdu.model.resource.ResCaseInfoBean;
import com.gdu.model.resource.ResHighPointDeviceBean;
import com.gdu.model.resource.ResRegionBean;
import com.gdu.model.resource.ResCircuitBean;
import com.gdu.model.resource.ResFocusPointBean;
import com.gdu.model.resource.ResOrganizationBean;
import com.gdu.model.resource.ResPersonInfoBean;
import java.util.List;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 资源分布请求接口相关方法
 */
public interface ResourceService {

    /**
     * 获取案件信息
     * @param jsonBody
     * @return
     */
    @POST(UrlConfig.getCaseInfo)
    Observable<BaseBean<ResCaseInfoBean>> getCase(@Body RequestBody jsonBody);

    /**
     * 获取组织人员信息
     * @param jsonBody
     * @return
     */
    @POST(UrlConfig.getSelectListEmployee)
    Observable<BaseBean<List<ResPersonInfoBean>>> getPerson(@Body RequestBody jsonBody);

    /**
     * 高点设备资源列表
     * @return
     */
    @POST(UrlConfig.getMonitorDevice)
    Observable<BaseBean<List<ResHighPointDeviceBean>>> getHighMonitor();

    /**
     * 无人机列表资源
     * @return
     */
    @POST(UrlConfig.getAirplaneInfo)
    Observable<BaseBean<List<ResAirplaneInfoBean>>> getAirplane();

    /**
     * 组织机构列表
     * @return
     */
    @POST(UrlConfig.getOrganizationList)
    Observable<BaseBean<List<ResOrganizationBean>>> getOrganization();

    /**
     * 获取关注点位资源
     * @return
     */
    @POST(UrlConfig.getFocusPoint)
    Observable<BaseBean<List<ResFocusPointBean>>> getFocus();

    /**
     * 获取重点线路资源
     * @return
     */
    @POST(UrlConfig.getCircuitInfo)
    Observable<BaseBean<List<ResCircuitBean>>> getCircuit();

    /**
     * 获取重点区域面资源
     * @return
     */
    @POST(UrlConfig.getRegionInfo)
    Observable<BaseBean<List<ResRegionBean>>> getRegion();
}
