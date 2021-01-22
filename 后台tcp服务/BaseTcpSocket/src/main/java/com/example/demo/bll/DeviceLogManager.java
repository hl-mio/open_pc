package com.example.demo.bll;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.DeviceLogMapper;
import com.example.demo.model.DeviceLog;
import org.springframework.stereotype.Component;

@Component
public class DeviceLogManager extends ServiceImpl<DeviceLogMapper, DeviceLog> {

}