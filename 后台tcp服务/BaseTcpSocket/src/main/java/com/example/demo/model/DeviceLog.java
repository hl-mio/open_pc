package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

// 设备日志数据表
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLog {
    /// uuid
    String uuid;
    /// 设备id
    String devId;
    /// 设备名称
    String devName;
    /// 数据
    String devData;
    /// 日期
    Date devTime;
    /// 收或发标记
    String sR;
    /// 状态
    String devDataState;
    /// 转义后的设备数据
    String devDataInfo;
}
