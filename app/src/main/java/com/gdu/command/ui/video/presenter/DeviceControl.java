package com.gdu.command.ui.video.presenter;

import static com.gdu.model.device.PTZCmdType.PanLeft;
import static com.gdu.model.device.PTZCmdType.PanRight;
import static com.gdu.model.device.PTZCmdType.TiltDown;
import static com.gdu.model.device.PTZCmdType.TiltUp;

import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.client.action.PTZControlAction;
import com.gdu.client.handler.ActionHandler;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.device.PTZCmdType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeviceControl {

    private PTZControlAction mPTZControlAction;
    private PTZCmdType mCurrentPTZCmdType;

    private String mDeviceId;  //设备ID
    private String mDeviceChannel; //设备通道  0 代表可见光，1代表红外光

    public DeviceControl(){

    }

    /**
     *
     * @param pitch   小于128 上   大于128 下
     * @param direct  小于128 右   大于128 左
     */
    public void gimbalMove(String deviceId, String channelId, float pitch, float direct){
        PTZCmdType ptzCmdType;
        int step = 1;
        int absPitch = (int) Math.abs(pitch - 128);
        int absDirect = (int) Math.abs(direct - 128);
        if (absPitch > absDirect) { //上下
            if (pitch < 128) {
                step = (int) (pitch / 16);
                ptzCmdType = TiltUp;
            } else {
                step = (int) (pitch / 31);
                ptzCmdType = TiltDown;
            }
        } else { // 左右
            if (direct < 128) {
                step = (int) (direct / 16);
                ptzCmdType = PanRight;
            } else {
                step = (int) (direct / 31);
                ptzCmdType = PanLeft;
            }
        }
        if (ptzCmdType == mCurrentPTZCmdType) {
            return;
        }
        deviceControl(deviceId, channelId, ptzCmdType, "0", step + "");
    }

    /**
     * 云台停止
     * @param deviceId
     * @param channelId
     */
    public void gimbalUp(String deviceId, String channelId){
        deviceControl(deviceId, channelId, mCurrentPTZCmdType, "1", "0");
    }

    public void deviceControl(String deviceId, String channelId, PTZCmdType cmdType, String stop, String step){
        deviceControl(deviceId, channelId, cmdType, stop, step, false);
    }

    /**
     *
     * @param deviceId  设备ID
     * @param channelId  0 代表可见光，1代表红外光
     * @param cmdType  控制类型
     * @param stop  1停止 0不停止
     * @param step  步进
     * @param isAutoStop  是否自动停止
     */
    public void deviceControl(String deviceId, String channelId, PTZCmdType cmdType, String stop, String step,
                              boolean isAutoStop) {
        MyLogUtils.i("deviceControl() deviceId = " + deviceId + "; channelId = " + channelId
                + "; cmdType = " + cmdType + "; stop = " + stop + "; step = " + step);
        mCurrentPTZCmdType = cmdType;
        if (cmdType == null) {
            return;
        }
        final String type = getCmdTypeValue(cmdType);
        MyLogUtils.i("deviceControl() type = " + type);
        Map<String, String> params = new HashMap<String, String>();
        params.put("horizonSpeed", deviceId);
        params.put("verticalSpeed", deviceId);
        params.put("zoomSpeed", deviceId);
        params.put("deviceId", deviceId);
        params.put("channelId", deviceId);
        params.put("command", deviceId);

//        params.put("device_id", deviceId);
//        params.put("channel_id", channelId);
//        params.put("cmd_type", type);
//        params.put("step", step);
//        params.put("stop", stop);
        mPTZControlAction = new PTZControlAction(UrlConfig.ptzControl, UrlConfig.HttpCP, params);
        mPTZControlAction.setHeader("Authorization", "Bearer " + MMKVUtil.getString(SPStringUtils.TOKEN, "NULL"));
        mPTZControlAction.execute(true, new ActionHandler() {
            @Override
            public void doActionStart() {
                MyLogUtils.i("deviceControl doActionStart()");
            }

            @Override
            public void doActionEnd() {
                MyLogUtils.i("deviceControl doActionEnd()");
            }

            @Override
            public void doActionResponse(int status, Serializable message) {
                MyLogUtils.i("deviceControl doActionResponse() status = " + status
                        + "; cTime " + System.currentTimeMillis());

            }

            @Override
            public void doActionRawData(Serializable data) {
                MyLogUtils.i("deviceControl doActionRawData()");
                if (isAutoStop) {
                    deviceControl(deviceId, channelId, cmdType, "1", "0", false);
                }
            }
        });
    }

    /**
     * 获取控制类型值
     * @param ptzCmdType
     * @return
     */
    private String getCmdTypeValue(PTZCmdType ptzCmdType){
        String cmdType = "stop";
        switch (ptzCmdType){
            case TiltUp:
                cmdType = "up";
                break;
            case TiltDown:
                cmdType = "down";
                break;
            case PanLeft:
                cmdType = "left";
                break;
            case PanRight:
                cmdType = "right";
                break;
            case ZoomIn:
                cmdType = "zoomin";
                break;
            case ZoomOut:
                cmdType = "zoomout";
                break;

//            case FIFocusFar:
//                cmdType = "6";
//                break;
//            case FIFocusNear:
//                cmdType = "7";
//                break;
//            case FIIrisIn:
//                cmdType = "8";
//                break;
//            case FIIrisOut:
//                cmdType = "9";
//                break;

            case Stop:
                cmdType = "stop";
                break;
            default:
                break;
        }
        return cmdType;
    }

}
