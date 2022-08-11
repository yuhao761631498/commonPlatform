package com.gdu.command.ui.video.bean;

import java.io.Serializable;
import java.util.List;

public class PlayStreamBean implements Serializable {

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String action;
        private String app;
        private String channelId;
        private String deviceCode;
        private String deviceID;
        private String flv;
        private String fmp4;
        private String hls;
        private String https_flv;
        private String https_fmp4;
        private String https_hls;
        private String https_ts;
        private String ip;
        private String mediaServerId;
        private String rtc;
        private String rtmp;
        private String rtmps;
        private String rtsp;
        private String rtsps;
        private String stream;
        private String streamId;
        private String subStreamPlayUrl;
        private String ts;
        private String useSubStream;
        private String wsPort;
        private String ws_flv;
        private String ws_fmp4;
        private String ws_hls;
        private String ws_ts;
        private String wss_flv;
        private String wss_fmp4;
        private String wss_hls;
        private String wss_ts;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(String deviceID) {
            this.deviceID = deviceID;
        }

        public String getFlv() {
            return flv;
        }

        public void setFlv(String flv) {
            this.flv = flv;
        }

        public String getFmp4() {
            return fmp4;
        }

        public void setFmp4(String fmp4) {
            this.fmp4 = fmp4;
        }

        public String getHls() {
            return hls;
        }

        public void setHls(String hls) {
            this.hls = hls;
        }

        public String getHttps_flv() {
            return https_flv;
        }

        public void setHttps_flv(String https_flv) {
            this.https_flv = https_flv;
        }

        public String getHttps_fmp4() {
            return https_fmp4;
        }

        public void setHttps_fmp4(String https_fmp4) {
            this.https_fmp4 = https_fmp4;
        }

        public String getHttps_hls() {
            return https_hls;
        }

        public void setHttps_hls(String https_hls) {
            this.https_hls = https_hls;
        }

        public String getHttps_ts() {
            return https_ts;
        }

        public void setHttps_ts(String https_ts) {
            this.https_ts = https_ts;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getMediaServerId() {
            return mediaServerId;
        }

        public void setMediaServerId(String mediaServerId) {
            this.mediaServerId = mediaServerId;
        }

        public String getRtc() {
            return rtc;
        }

        public void setRtc(String rtc) {
            this.rtc = rtc;
        }

        public String getRtmp() {
            return rtmp;
        }

        public void setRtmp(String rtmp) {
            this.rtmp = rtmp;
        }

        public String getRtmps() {
            return rtmps;
        }

        public void setRtmps(String rtmps) {
            this.rtmps = rtmps;
        }

        public String getRtsp() {
            return rtsp;
        }

        public void setRtsp(String rtsp) {
            this.rtsp = rtsp;
        }

        public String getRtsps() {
            return rtsps;
        }

        public void setRtsps(String rtsps) {
            this.rtsps = rtsps;
        }

        public String getStream() {
            return stream;
        }

        public void setStream(String stream) {
            this.stream = stream;
        }

        public String getStreamId() {
            return streamId;
        }

        public void setStreamId(String streamId) {
            this.streamId = streamId;
        }

        public String getSubStreamPlayUrl() {
            return subStreamPlayUrl;
        }

        public void setSubStreamPlayUrl(String subStreamPlayUrl) {
            this.subStreamPlayUrl = subStreamPlayUrl;
        }

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public String getUseSubStream() {
            return useSubStream;
        }

        public void setUseSubStream(String useSubStream) {
            this.useSubStream = useSubStream;
        }

        public String getWsPort() {
            return wsPort;
        }

        public void setWsPort(String wsPort) {
            this.wsPort = wsPort;
        }

        public String getWs_flv() {
            return ws_flv;
        }

        public void setWs_flv(String ws_flv) {
            this.ws_flv = ws_flv;
        }

        public String getWs_fmp4() {
            return ws_fmp4;
        }

        public void setWs_fmp4(String ws_fmp4) {
            this.ws_fmp4 = ws_fmp4;
        }

        public String getWs_hls() {
            return ws_hls;
        }

        public void setWs_hls(String ws_hls) {
            this.ws_hls = ws_hls;
        }

        public String getWs_ts() {
            return ws_ts;
        }

        public void setWs_ts(String ws_ts) {
            this.ws_ts = ws_ts;
        }

        public String getWss_flv() {
            return wss_flv;
        }

        public void setWss_flv(String wss_flv) {
            this.wss_flv = wss_flv;
        }

        public String getWss_fmp4() {
            return wss_fmp4;
        }

        public void setWss_fmp4(String wss_fmp4) {
            this.wss_fmp4 = wss_fmp4;
        }

        public String getWss_hls() {
            return wss_hls;
        }

        public void setWss_hls(String wss_hls) {
            this.wss_hls = wss_hls;
        }

        public String getWss_ts() {
            return wss_ts;
        }

        public void setWss_ts(String wss_ts) {
            this.wss_ts = wss_ts;
        }
    }
}
