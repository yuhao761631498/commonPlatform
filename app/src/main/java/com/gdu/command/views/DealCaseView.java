package com.gdu.command.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.command.R;
import com.gdu.baselibrary.utils.CommonUtils;

/**
 * 案情处置view
 */
public class DealCaseView extends BaseView{


    private TextView mConfirmButton;
    private TextView mCancelButton;
    private EditText mCommentEditText;

    private String mComments;
    private OnDealCaseListener mOnDealCaseListener;

    public DealCaseView(Context context) {
        super(context);
    }

    public DealCaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DealCaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setOnDealCaseListener(OnDealCaseListener onDealCaseListener){
        mOnDealCaseListener = onDealCaseListener;
    }

    @Override
    public void initView() {
        super.initView();
        mConfirmButton = findViewById(R.id.confirm_button);
        mCancelButton = findViewById(R.id.cancel_button);
        mCommentEditText = findViewById(R.id.case_comments_edittext);
    }

    @Override
    public void initListener() {
        mConfirmButton.setOnClickListener(view -> {
            if (CommonUtils.isEmptyString(mComments)) {
                ToastUtil.s("输入内容不能为空");
            } else {
                CommonUtils.hideInputSoftKey(getContext(), mCommentEditText);
                mOnDealCaseListener.onConfirmClick(mComments);
                setVisibility(GONE);
            }
        });
        mCancelButton.setOnClickListener(view -> setVisibility(GONE));
        mCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mComments = editable.toString();
            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_deal_case;
    }

    public interface OnDealCaseListener{
        void onConfirmClick(String comments);
    }

}
