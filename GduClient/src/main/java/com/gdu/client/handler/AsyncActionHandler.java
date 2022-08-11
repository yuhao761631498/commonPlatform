package com.gdu.client.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by zhilaizhang on 17/9/29.
 */

public class AsyncActionHandler extends Handler {
    protected final static int    OP_START       = 1;
    protected final static int    OP_RESPONSE    = 2;
    protected final static int    OP_END         = 3;
    public final static  String ACTION_OP      = "com.action.op";
    public final static  String ACTION_STATUS  = "com.action.status";
    public final static  String ACTION_MESSAGE = "com.action.message";
    public final static  String ACTION_DATA    = "com.action.data";
    public final static  String ACTION_RESULT  = "com.action.result";

    protected ActionHandler actionHandler;

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    public void setActionHandler(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        if (bundle != null) {
            actionHandler.doActionResponse(bundle.getInt(ACTION_STATUS),
                    bundle.getSerializable(ACTION_DATA));
            actionHandler.doActionRawData(bundle.getSerializable(ACTION_DATA));
        }
    }
}

