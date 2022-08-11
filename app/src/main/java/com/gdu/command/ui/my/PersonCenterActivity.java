package com.gdu.command.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.network.RetrofitUploadClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.base.dialog.SelectDepartmentBottomDialog;
import com.gdu.command.ui.cases.deal.UploadPicService;
import com.gdu.command.ui.setting.SelectDepartmentListEntity;
import com.gdu.command.ui.setting.UpdatePersonGenderActivity;
import com.gdu.command.ui.setting.UpdateUserInfoActivity;
import com.gdu.command.ui.setting.UpdateUserInfoBean;
import com.gdu.command.ui.splash.LoginService;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.user.PersonInfoBean;
import com.gdu.utils.MyVariables;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private ImageView iv_head_img;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_phone_number;
    private TextView tv_work_unit;
    private TextView tv_unit_phone_number;
    private TextView tv_id_card;
    private TextView tv_login_out;
    private LinearLayout ll_user_name;
    private LinearLayout ll_nick_name;
    private LinearLayout ll_sex;
    private LinearLayout ll_unit;
    private LinearLayout ll_job_no;
    private LinearLayout ll_office_phone;
    private LinearLayout ll_phone_number;
    private LinearLayout ll_email;
    private LinearLayout ll_id_card;

    private PersonInfoBean personInfoBean;
    private static final int REQUEST_PIC_CODE = 100;  //图片请求
    private List<String> imgPath;

    private SelectDepartmentBottomDialog selectDeptDialog;

    private Gson mGson;
    private LoginService service;
    private UploadPicService mUploadPicService;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_center;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        mGson = new Gson();
        service = RetrofitClient.getAPIService(LoginService.class);
        mUploadPicService = RetrofitUploadClient.getAPIService(UploadPicService.class);

        initView();
        initClickListener();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_head_img = findViewById(R.id.iv_head_img);
        tv_name = findViewById(R.id.tv_name);
        tv_sex = findViewById(R.id.tv_sex);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_work_unit = findViewById(R.id.tv_work_unit);
        tv_unit_phone_number = findViewById(R.id.tv_unit_phone_number);
        tv_id_card = findViewById(R.id.tv_id_card);
        tv_login_out = findViewById(R.id.tv_login_out);
        ll_user_name = findViewById(R.id.ll_user_name);
        ll_nick_name = findViewById(R.id.ll_nick_name);
        ll_sex = findViewById(R.id.ll_sex);
        ll_unit = findViewById(R.id.ll_unit);
        ll_job_no = findViewById(R.id.ll_job_no);
        ll_office_phone = findViewById(R.id.ll_office_phone);
        ll_phone_number = findViewById(R.id.ll_phone_number);
        ll_email = findViewById(R.id.ll_email);
        ll_id_card = findViewById(R.id.ll_id_card);
    }

    private void initClickListener() {
        tv_login_out.setOnClickListener(this);
        iv_head_img.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ll_user_name.setOnClickListener(this);
        ll_nick_name.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
        ll_unit.setOnClickListener(this);
        ll_job_no.setOnClickListener(this);
        ll_office_phone.setOnClickListener(this);
        ll_phone_number.setOnClickListener(this);
        ll_email.setOnClickListener(this);
        ll_id_card.setOnClickListener(this);

        getPersonInfo();
        getDepartmentTree();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_out:
                MMKVUtil.putString(SPStringUtils.TOKEN, "NULL");
                finish();
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_head_img: //头像
                updateHeadPic();
                break;

            case R.id.ll_user_name:  //修改姓名
                jumpToUpdateActivity("姓名",
                        valueOf(tv_name),
                        MyVariables.UPDATE_USER_NAME_RESULT_CODE);
                break;

            case R.id.ll_sex:  //修改性别
                Intent intent = new Intent();
                intent.setClass(this, UpdatePersonGenderActivity.class);
                intent.putExtra(MyVariables.UPDATE_PERSON_INFO, valueOf(tv_sex));
                intent.putExtra(MyVariables.UPDATE_INFO_RESULT_CODE, MyVariables.UPDATE_USER_SEX_RESULT_CODE);
                startActivityForResult(intent, MyVariables.UPDATE_USER_SEX_RESULT_CODE);
                break;

            case R.id.ll_id_card:  //修改身份证号
                jumpToUpdateActivity("身份证号",
                        valueOf(tv_id_card),
                        MyVariables.UPDATE_ID_CARD_RESULT_CODE);
                break;

            case R.id.ll_unit: //选择部门
                if (selectDeptDialog != null) {
                    if (!selectDeptDialog.isShowing()) {
                        selectDeptDialog.show();
                    }
                }
                break;

            case R.id.ll_office_phone:  //修改办公电话
                jumpToUpdateActivity("办公电话",
                       valueOf(tv_unit_phone_number),
                        MyVariables.UPDATE_OFFICE_PHONE_RESULT_CODE);
                break;

            case R.id.ll_phone_number: //修改电话
                jumpToUpdateActivity("联系方式",
                       valueOf(tv_phone_number),
                        MyVariables.UPDATE_PHONE_NO_RESULT_CODE);
                break;
        }
    }

    /**
     * 选择部门弹框
     */
    private void showSelectDeptDialog(SelectDepartmentListEntity entity) {
        if (selectDeptDialog == null) {
            selectDeptDialog = new SelectDepartmentBottomDialog(this, entity);
            selectDeptDialog.showSelectDialog();
        }
        selectDeptDialog.setOnItemSelectedListener(new SelectDepartmentBottomDialog.OnBottomSelectListener() {
            @Override
            public void onClick(String value, String deptCode) {
                tv_work_unit.setText(value);
                updateUserInfo("deptCode", deptCode);
            }
        });
    }

    /**
     * 跳转到信息修改页
     */
    private void jumpToUpdateActivity(String title, String info, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, UpdateUserInfoActivity.class);
        intent.putExtra(MyVariables.UPDATE_TITLE_NAME, title);
        intent.putExtra(MyVariables.UPDATE_PERSON_INFO, info);
        intent.putExtra(MyVariables.UPDATE_INFO_RESULT_CODE, requestCode);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 修改头像
     */
    private void updateHeadPic() {
        ImageSelector.builder()
                .useCamera(true) // 设置是否使用拍照
                .setSingle(true)  //设置是否单选
                .canPreview(true) //是否可以预览图片，默认为true
                .setContainVideo(false)
                .setContainPhoto(true)
                .setMaxSelectCount(1)
                .start((Activity) mContext, REQUEST_PIC_CODE); // 打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_PIC_CODE) {
            //获取选择图片返回的数据
            imgPath = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            MyImageLoadUtils.loadCircleImageWithUrl(this, imgPath.get(0), R.mipmap.default_head, iv_head_img);
            uploadFiles(imgPath);
            MyLogUtils.d("图片地址为：" + imgPath.toString());
        }
        String str = data.getStringExtra(MyVariables.UPDATE_PERSON_INFO);
        if (requestCode == MyVariables.UPDATE_USER_NAME_RESULT_CODE) {
            tv_name.setText(str);
        } else if (requestCode == MyVariables.UPDATE_OFFICE_PHONE_RESULT_CODE) {
            tv_unit_phone_number.setText(str);
        } else if (requestCode == MyVariables.UPDATE_PHONE_NO_RESULT_CODE) {
            tv_phone_number.setText(str);
        } else if (requestCode == MyVariables.UPDATE_ID_CARD_RESULT_CODE) {
            StringBuilder stringBuilder = new StringBuilder(str);
            if (str.length() >= 18) {
                stringBuilder.replace(4, 14, "****");
                tv_id_card.setText(stringBuilder.toString());
            } else {
                tv_id_card.setText(str);
            }
        } else if (requestCode == MyVariables.UPDATE_USER_SEX_RESULT_CODE) {
            tv_sex.setText(str);
        }
    }

    /**
     * 获取用户信息
     */
    private void getPersonInfo() {
        showProgressDialog(getString(R.string.loading));
        service = RetrofitClient.getAPIService(LoginService.class);
        service.getPersonInfo().enqueue(new Callback<PersonInfoBean>() {
            @Override
            public void onResponse(Call<PersonInfoBean> call, Response<PersonInfoBean> response) {
                MyLogUtils.d("onResponse()");
                hideProgressDialog();
                personInfoBean = response.body();
                final boolean isEmptyData = personInfoBean == null || personInfoBean.getCode() != 0 || personInfoBean.getData() == null;
                if (isEmptyData) {
                    return;
                }
                GlobalVariable.sPersonInfoBean = personInfoBean;
                //头像
                MyImageLoadUtils.loadCircleImageWithUrl(PersonCenterActivity.this, personInfoBean.getData().getHeadImg(), R.mipmap.default_head, iv_head_img);
                // 姓名
                tv_name.setText(personInfoBean.getData().getUsername());
                // 性别
                tv_sex.setText(personInfoBean.getData().getUserGender() == 0 ? getString(R.string.woman) : getString(R.string.man));
                // 联系方式
                tv_phone_number.setText(personInfoBean.getData().getMobile());
                // 所属机构
                tv_work_unit.setText(personInfoBean.getData().getDeptName());
                // 办公电话
                tv_unit_phone_number.setText(personInfoBean.getData().getOfficeMobile());
                // 身份证号码
                if (!isEmpty(personInfoBean.getData().getIdCard())) {
                    StringBuilder stringBuilder = new StringBuilder(personInfoBean.getData().getIdCard());
                    if (personInfoBean.getData().getIdCard().length() >= 18) {
                        stringBuilder.replace(4, 14, "****");
                        tv_id_card.setText(stringBuilder.toString());
                    } else {
                        tv_id_card.setText(personInfoBean.getData().getIdCard());
                    }
                }
            }

            @Override
            public void onFailure(Call<PersonInfoBean> call, Throwable e) {
                hideProgressDialog();
                MyLogUtils.e("获取用户信息接口失败", e);
            }
        });
    }

    /**
     * 获取组织部门树
     */
    private void getDepartmentTree() {
        PersonService service = RetrofitClient.getAPIService(PersonService.class);
        service.getDepartmentTree().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<SelectDepartmentListEntity>() {
                    @Override
                    public void accept(SelectDepartmentListEntity bean) throws Throwable {
                        if (bean.getData() != null && bean.getData().size() != 0) {
                            showSelectDeptDialog(bean);
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("获取组织部门树出错", throwable);
                });
    }

    /**
     * 修改头像
     * @param deptCode
     */
    private void updateUserInfo(String key, String deptCode) {
        Map<String, Object> map  = new HashMap<>();
        map.put(key, deptCode);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application" + "/json;charset=UTF-8"));
        service.updateUserInfo(body).subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<UpdateUserInfoBean>() {
                    @Override
                    public void accept(UpdateUserInfoBean bean) throws Throwable {
                        if (bean.getCode() == 0) {
                            ToastUtil.s("修改成功");
                        } else {
                            ToastUtil.s("修改失败");
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("修改个人信息出错", throwable);
                });
    }

    /**
     * 上传文件
     * @param images
     */
    public void uploadFiles(List<String> images) {
        final int[] totalSize = {0};
        boolean haveImage;
        if (images != null && images.size() > 0) {
            MyLogUtils.d("uploadFiles() imgSize = " + images.size());
            totalSize[0] = images.size();
            haveImage = true;
        } else {
            MyLogUtils.d("uploadFiles() imgSize = 0");
            images = new ArrayList<>();
            haveImage = false;
        }

        final boolean isEmptyData = !haveImage;
        if (isEmptyData) {
            return;
        }

        final int[] sucNum = {0};
        final int[] failNum = {0};
        final StringBuffer sb = new StringBuffer();
        Observable.just(images)
                .flatMapIterable(list ->
                        Luban.with(this).load(list).filter(path ->
                                !CommonUtils.isEmptyList(list)).ignoreBy(MyConstants.LUBAN_IGONE_SIZE)
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                        MyLogUtils.d("CompressListener onStart()");
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        MyLogUtils.d("CompressListener onSuccess()");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        MyLogUtils.d("CompressListener onError()");
                                        MyLogUtils.e("压缩图片出错", e);
                                    }
                                }).get())
                .concatMap((Function<File, ObservableSource<BaseBean<String>>>) file -> {
                    RequestBody requestBody = RequestBody.create(file,
                            MediaType.parse("multipart/form-data"));
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file",
                            file.getName(), requestBody);
                    return mUploadPicService.uploadFile(body);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(RxLife.to(getBaseActivity()))
                .subscribe(response -> {
                    MyLogUtils.d("onNext()");
                    if (CommonUtils.isEmptyString(response.data)) {
                        failNum[0]++;
                        return;
                    }
                    final String result = response.data;
                    if (!CommonUtils.isEmptyString(sb.toString())) {
                        sb.append(",");
                    }
                    sb.append(result);
                    sucNum[0]++;
                    updateUserInfo("headImage", sb.toString());
                }, throwable -> {
                    MyLogUtils.d("onError()");
                    MyLogUtils.e("图片上传出错", throwable);
                });

    }

}