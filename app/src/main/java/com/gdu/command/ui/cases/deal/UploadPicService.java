package com.gdu.command.ui.cases.deal;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.HostUrl;
import java.util.List;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 上传图片的Service
 * @author wixche
 */
public interface UploadPicService {

    /**
     * 上传图片和视频（多文件）
     * @param body
     * @return
     */
    @Multipart
    @POST(HostUrl.UPLOAD_MULTIPART_FILES)
    Observable<BaseBean<String>> uploadMultiFile(@Part MultipartBody.Part body);

    /**
     * 上传图片和视频（单文件）
     * @param body
     * @return
     */
    @Multipart
    @POST(HostUrl.UPLOAD_CHAT_FILE)
    Observable<BaseBean<String>> uploadFile(@Part MultipartBody.Part body);
}
