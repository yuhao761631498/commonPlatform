package com.gdu.command.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.model.device.PTZCmdType;


/**
 * Created by Administrator on 2016/12/14.
 */
public class GimbalControlView extends LinearLayout implements View.OnClickListener {

    private final Context context;

//    private ImageView mGimbalIv;
//    private FrameLayout mGimbalBgFl;
    private SteeringWheelView mGimbalRockerSwv;

//    private ImageView mGimbalBgIv;
//    private Handler mHandler;
//
//    private TextView tv_smallPitch;
//    private TextView tv_smallRoll;
//    private boolean isSmallPitch;
//    private boolean isDismissed;

    private TextView mZoomPTZView;
    private TextView mFocusPTZView;
    private TextView mIrisPTZView;
    private SeekBar mSpeedSeekBar;
    private TextView mSpeedTextView;

    private Group view_streamFormat;
    private TextView tv_streamFormat;
    private TextView tv_SDStream;
    private TextView tv_HDStream;

    private OnGimbalControlListener mOnGimbalControlListener;

    public GimbalControlView(Context context) {
        this(context, null);
    }

    public GimbalControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GimbalControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews();
    }

    public void setOnGimbalControlListener(OnGimbalControlListener onGimbalControlListener){
        mOnGimbalControlListener = onGimbalControlListener;
    }

    protected void initViews() {
        LayoutInflater.from(context).inflate(R.layout.view_gimbal_control, this);
        initHandler();
        findView();
        initView();
        initClickListener();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
//
//        if (mGimbalOriX == 0 && mGimbalOriX == 0) {
//            mGimbalOriX = mGimbalIv.getX();   //????????????????????????,??????????????????????????????????????????????????????
//            mGimbalOriY = mGimbalIv.getY();
//        }
//    }

    private void initHandler() {
//        mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case 0:
////                        mRollValueTv.setText(Math.round(GlobalVariable.HolderPitch * 1.0 / 100) + "??");
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//        };
    }

//    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//        if (visibility == View.VISIBLE) {
//            if (updateThread == null || !updateThread.isAlive()) {
//                isRun = true;
//                updateThread = new Thread(runnable);
//                updateThread.start();
//            }
//        } else {
//            gimbalActionUp();
//            if (updateThread != null && updateThread.isAlive()) {
//                isRun = false;
//                updateThread.interrupt();
//                updateThread = null;
//            }
//        }
//    }
//
//    private boolean isRun;
//    private Thread updateThread;
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            while (isRun) {
//                if (isSmallPitch) {
//                    mHandler.sendEmptyMessage(0);
//                }
//                try {
//                    Thread.sleep(300);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

    private void findView() {
//        mGimbalIv = (ImageView) findViewById(R.id.iv_gimbal_move);
//        mGimbalBgFl = (FrameLayout) findViewById(R.id.fl_gimbal_bg);
//        mGimbalBgIv = (ImageView) findViewById(R.id.iv_gimbal_circle);

        mGimbalRockerSwv = findViewById(R.id.swv_gimbalRocker);

        mZoomPTZView = findViewById(R.id.zoom_ptzview);
        mFocusPTZView = findViewById(R.id.focus_ptzview);
        mIrisPTZView = findViewById(R.id.iris_ptzview);
        mSpeedTextView = findViewById(R.id.speed_ptzview);

        view_streamFormat = findViewById(R.id.view_streamFormat);
        tv_streamFormat = findViewById(R.id.tv_streamFormat);
        tv_SDStream = findViewById(R.id.tv_SDStream);
        tv_HDStream = findViewById(R.id.tv_HDStream);

        mSpeedSeekBar = findViewById(R.id.sb_speed);

    }

    private void initView(){
    }

    public void showOrHideStreamFormatBtn(boolean isShow) {
        tv_streamFormat.setVisibility(isShow ? VISIBLE : GONE);
    }

//    /***********************
//     * ?????????????????????---ron
//     * @param isPitch
//     */
//    private void smallPitchSelected(boolean isPitch) {
//        isSmallPitch = isPitch;
//        if (isPitch) {
//            tv_smallPitch.setSelected(true);
//            tv_smallRoll.setSelected(false);
//        } else {
//            tv_smallPitch.setSelected(false);
//            tv_smallRoll.setSelected(true);
////            mRollValueTv.setText(currentRollValue + "");
//        }
//    }

    private void initClickListener() {
//        mGimbalIv.setOnTouchListener(this);
        mZoomPTZView.setOnClickListener(this);
        mFocusPTZView.setOnClickListener(this);
        mIrisPTZView.setOnClickListener(this);
        mSpeedTextView.setOnClickListener(this);
        tv_streamFormat.setOnClickListener(this);
        tv_SDStream.setOnClickListener(this);
        tv_HDStream.setOnClickListener(this);


        mSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mSpeedTextView.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mGimbalRockerSwv.setListener(new SteeringWheelView.OnItemClickListener() {
            @Override
            public void onSingleClick(int position) {
                MyLogUtils.i("onSingleClick() position = " + position);
                gimbalMove(position, true);
            }

            @Override
            public void onLongClick(int position) {
                MyLogUtils.i("onLongClick() position = " + position);
                gimbalMove(position, false);
            }

            @Override
            public void onActionUp() {
                MyLogUtils.i("onActionUp()");
                if (mOnGimbalControlListener != null) {
                    mOnGimbalControlListener.onGimbalUp();
                }
            }
        });
    }

    private void gimbalMove(int position, boolean isAutoStop) {
        if (mOnGimbalControlListener != null) {
            PTZCmdType cmdType = null;
            switch (position) {
                case 1:
                    cmdType = PTZCmdType.PanRight;
                    break;
                case 2:
                    cmdType = PTZCmdType.TiltDown;
                    break;
                case 3:
                    cmdType = PTZCmdType.PanLeft;
                    break;
                case 4:
                    cmdType = PTZCmdType.TiltUp;
                    break;
                default:
                    break;
            }
            mOnGimbalControlListener.onGimbalMoveNew(cmdType, isAutoStop);
        }
    }

    /****************
     * ???????????????
     */
    public void init() {

    }

//    /****************
//     * ???????????????????????????
//     * @param isAdd ?????????+1
//     */
//    public void setHolderPitch(boolean isAdd) {
//        byte angle = (byte) (Math.round(GlobalVariable.HolderPitch * 1.0 / 100) + (isAdd ? 1 : -1));
//        if (angle > 30)
//            angle = 30;
//        else if (angle < -120) {
//            angle = -120;
//        }
//
//        GduApplication.getSingleApp().gduCommunication.setHolderAngleMinus(angle, new SocketCallBack() {
//            @Override
//            public void callBack(byte code, GduFrame bean) {
//
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zoom_ptzview:
                mOnGimbalControlListener.onZoomClick();
                showTextColor(mZoomPTZView);
                break;

            case R.id.focus_ptzview:
                mOnGimbalControlListener.onFocusClick();
                showTextColor(mFocusPTZView);
                break;

            case R.id.iris_ptzview:
                mOnGimbalControlListener.onIrisClick();
                showTextColor(mIrisPTZView);
                break;

            case R.id.speed_ptzview:
                mOnGimbalControlListener.onSpeedClick();
                showTextColor(mSpeedTextView);
                break;

            case R.id.tv_streamFormat:
                if (view_streamFormat.getVisibility() == VISIBLE) {
                    view_streamFormat.setVisibility(GONE);
                } else {
                    view_streamFormat.setVisibility(VISIBLE);
                }
                break;

            case R.id.tv_SDStream:
                view_streamFormat.setVisibility(GONE);
                tv_streamFormat.setText("??????");
                if (mOnGimbalControlListener != null) {
                    mOnGimbalControlListener.changeStreamFormat(1);
                }
                break;

            case R.id.tv_HDStream:
                view_streamFormat.setVisibility(GONE);
                tv_streamFormat.setText("??????");
                if (mOnGimbalControlListener != null) {
                    mOnGimbalControlListener.changeStreamFormat(2);
                }
                break;

            default:
                break;
        }
    }

    private void showTextColor(TextView textView){
        mSpeedTextView.setTextColor(getContext().getResources().getColor(R.color.color_80FFFFFF));
        mZoomPTZView.setTextColor(getContext().getResources().getColor(R.color.color_80FFFFFF));
        mFocusPTZView.setTextColor(getContext().getResources().getColor(R.color.color_80FFFFFF));
        mIrisPTZView.setTextColor(getContext().getResources().getColor(R.color.color_80FFFFFF));
        textView.setTextColor(getContext().getResources().getColor(R.color.color_ffffff));
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        float x = event.getRawX();
//        float y = event.getRawY();
//
//        switch (v.getId()) {
//            case R.id.iv_gimbal_move:
//                moveGimbal(event, x, y);
//                return true;
//            default:
//                break;
//        }
//        return false;
//    }
//
//    private byte currentRollValue = 0;
//
//    /**********************
//     * roll??????
//     *********************/
//    private void changRoll(boolean add) {
//        if (add) {
//            currentRollValue++;
//        } else {
//            currentRollValue--;
//        }
//        byte rollMin = -30;
//        byte rollMax = 30;
////        if (GlobalVariable.gimbalType == GimbalType.ByrdT_4kc ||
////                GlobalVariable.gimbalType == GimbalType.ByrdT_4k ||
////                GlobalVariable.gimbalType == GimbalType.ByrdT_4ka) {
////            rollMin = -20;
////            rollMax = 20;
////        }
//        if (currentRollValue < rollMin) {
//            currentRollValue = rollMin;
//        } else if (currentRollValue > rollMax) {
//            currentRollValue = rollMax;
//        }
//
////        GduApplication.getSingleApp().gduCommunication.holderRollChange(currentRollValue);
////        mRollValueTv.setText(currentRollValue + "");
//    }
//
//    /**
//     * ??????????????????
//     */
//    private float mGimbalOriX;
//    private float mGimbalOriY;
//
//    /**
//     * ??????????????????xy?????????
//     */
//    private float mGimbalPitch;
//    private float mGimbalDirect;
//
//    /** ??????????????????????????? */
//    private float mGimbalDownX;
//    private float mGimbalDownY;
//    public static final int GIMBAL_MAX_VALUE = 255; // ?????????????????????
//
//    private void moveGimbal(MotionEvent event, float x, float y) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isDismissed = false;
//                gimbalActionDown(x, y);
//                mGimbalIv.setBackgroundResource(R.mipmap.gimbal_setting_button_activated);// ????????????????????????
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(isDismissed){
//                    return;
//                }
//                mGimbalIv.setBackgroundResource(R.mipmap.gimbal_setting_button_activated);
//                float mXDelta = x - mGimbalDownX;         // X ??????????????????
//                float mYDelta = y - mGimbalDownY;         // Y ??????????????????
//                float bgW = mGimbalBgFl.getWidth() / 2;   // ????????????????????????
//                float bgH = mGimbalBgFl.getHeight() / 2;  // ????????????????????????
//                float gimbalW = mGimbalIv.getWidth() / 2; // ?????????????????????
//                float gimbalH = mGimbalIv.getHeight() / 2;// ?????????????????????
//
//                /**
//                 *????????????????????????????????????????????????????????????????????????
//                 */
//                if (Math.abs(mXDelta) + mGimbalIv.getWidth() / 2 > bgW) { // ?????????????????????????????????
//                    if (mXDelta > 0) {
//                        mXDelta = bgW - gimbalW;
//                    } else {
//                        mXDelta = -bgW + gimbalW;
//                    }
//                }
//                if (Math.abs(mYDelta) + mGimbalIv.getHeight() / 2 > bgH) {// ?????????????????????????????????
//                    if (mYDelta > 0) {
//                        mYDelta = bgH - gimbalH;
//                    } else {
//                        mYDelta = -bgH + gimbalH;
//                    }
//                }
//
//                /**
//                 * ??????????????????
//                 */
//                mGimbalIv.setX(mXDelta + mGimbalOriX);
//                mGimbalIv.setY(mYDelta + mGimbalOriY);
//                /**
//                 * X Y????????????????????????
//                 */
//                float wRange = bgW - gimbalW;
//                float hRange = bgH - gimbalH;
//
//                mGimbalPitch = (int) (mYDelta / hRange * GIMBAL_MAX_VALUE / 2 + GIMBAL_MAX_VALUE / 2);
//
//                mGimbalDirect = (int) (mXDelta / wRange * GIMBAL_MAX_VALUE / 2 + GIMBAL_MAX_VALUE / 2);
//                if (mGimbalDirect > 128) {
//                    mGimbalDirect = 255 - mGimbalDirect;
//                } else if (mGimbalDirect < 128) {
//                    mGimbalDirect = 255 - mGimbalDirect;
//                }
//                if (mOnGimbalControlListener != null) {
//                    mOnGimbalControlListener.onGimbalMove(mGimbalPitch, mGimbalDirect);
//                }
////                GduApplication.getSingleApp().gduCommunication.beginControlHolder((byte) 128, (byte) mGimbalPitch, (byte) mGimbalDirect);
//                Log.d("zhaijiang", "pitch" + mGimbalPitch + "    direct" + mGimbalDirect);
//                break;
//            case MotionEvent.ACTION_UP:
//                mGimbalIv.setBackgroundResource(R.mipmap.button_default_gimbal);
//                gimbalActionUp();
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * ?????????????????????????????????????????????????????????
//     *
//     * @param x ?????????x??????
//     * @param y ?????????y??????
//     */
//    private void gimbalActionDown(float x, float y) {
//        mGimbalDownX = x;
//        mGimbalDownY = y;
//    }
//
//    /**
//     * <p>????????????????????????</p>
//     * <ul>
//     * <li>128?????????????????????128??????????????????</li>
//     * </ul>
//     */
//    public void gimbalActionUp() {
//        mGimbalDirect = 128;
//        mGimbalPitch = 128;
//        mGimbalIv.setX(mGimbalOriX);
//        mGimbalIv.setY(mGimbalOriY);
//        if (mOnGimbalControlListener != null) {
//            mOnGimbalControlListener.onGimbalUp();
//        }
//    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public interface OnGimbalControlListener{
        void onGimbalMove(float pitch, float direct);
        /**
         * ??????????????????
         */
        void onGimbalUp();

        /**
         * ??????????????????
         * @param cmdType
         * @param isAutoStop
         */
        void onGimbalMoveNew(PTZCmdType cmdType, boolean isAutoStop);

        /**
         * ??????????????????
         */
        void onZoomClick();

        /**
         * ??????????????????
         */
        void onFocusClick();

        /**
         * ??????????????????
         */
        void onIrisClick();

        /**
         * ??????????????????
         */
        void onSpeedClick();

        /**
         * ????????????(??????/??????)
         */
        void changeStreamFormat(int type);
    }
}
