package com.gdu.client.handler;

import java.io.Serializable;

/**
 * Created by zhilaizhang on 17/9/29.
 */

public interface ActionHandler {
    void doActionStart();

    void doActionEnd();

    void doActionResponse(int status, Serializable message);

    void doActionRawData(Serializable data);

    class ActionAdapter implements ActionHandler{

        @Override
        public void doActionStart() {

        }

        @Override
        public void doActionEnd() {

        }

        @Override
        public void doActionResponse(int status, Serializable message) {

        }

        @Override
        public void doActionRawData(Serializable data) {

        }
    }
}
