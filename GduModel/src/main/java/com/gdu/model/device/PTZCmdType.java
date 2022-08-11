package com.gdu.model.device;

/**
 * 云台控制类型
 */
public enum  PTZCmdType {
    /** 云台向右方运动 */
    PanRight(1, "云台向右方运动"),
    /** 云台向左方运动 */
    PanLeft(2, "云台向左方运动"),
    /** 云台向上方运动 */
    TiltUp(3, "云台向上方运动"),
    /** 云台向下方运动 */
    TiltDown(4, "云台向下方运动"),
    /** 云台变倍放大 */
    ZoomIn(5, "云台变倍放大"),
    /** 云台变倍缩小 */
    ZoomOut(6, "云台变倍缩小"),
    /** 所有动作停止 */
    Stop(7, "所有动作停止"),
    /** 设置预置位 */
    SetPresetBit(8, "设置预置位"),
    /** 调用预置位 */
    CallPresetBit(9, "调用预置位"),
    /** 删除预置位 */
    DeletePresetBit(10, "删除预置位"),
    /** 加入巡航点 */
    AddCruisePoint(11, "加入巡航点"),
    /** 删除巡航点 */
    DeleteCruisePoint(12, "删除巡航点"),
    /** 设置巡航速度 */
    SetCruiseSpeed(13, "设置巡航速度"),
    /** 设置巡航停留时间 */
    SetCruiseStopTime(14, "设置巡航停留时间"),
    /** 开始巡航 */
    StartCruise(15, "开始巡航"),
    /** 开始自动扫描 */
    StartAutoScan(16, "开始自动扫描"),
    /** 设置自动扫描左边界 */
    SetAutoScanLeftPeriphery(17, "设置自动扫描左边界"),
    /** 设置自动扫描右边界 */
    SetAutoScanRightPeriphery(18, "设置自动扫描右边界"),
    /** 设置自动扫描速度 */
    SetAutoScanSpeed(19, "设置自动扫描速度"),
    /** 光圈缩小 */
    FIIrisOut(20, "光圈缩小"),
    /** 光圈放大 */
    FIIrisIn(21, "光圈放大"),
    /** 聚焦近 */
    FIFocusNear(22, "聚焦近"),
    /** 聚焦远 */
    FIFocusFar(23, "聚焦远"),
    /** 停止FI指令 */
    FIStop(24, "停止FI指令"),
    /** 辅助开关开 */
    AssistOpen(25, "辅助开关开"),
    /** 辅助开关关 */
    AssistClose(26, "辅助开关关"),
    /** 速度加 */
    SpeedPlus(27, "速度加"),
    /** 速度减 */
    SpeedMinus(28, "速度减");

    private int key;
    private String value;
    PTZCmdType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
