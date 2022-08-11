package com.gdu.command.ui.cases;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.model.config.UrlConfig;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Copyright (C), 2020-2030, 武汉中旗生物医疗电子有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/6/22 16:59
 *
 * @author yuhao
 */
public interface PublicLoadUrlService {

    /**
     *获取web页面的url
     *
     */
    @GET(UrlConfig.getWebLoadUrl)
    Call<BaseBean<String>> getWebLoadUrl();
}
