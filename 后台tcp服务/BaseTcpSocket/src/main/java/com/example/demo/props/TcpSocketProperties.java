package com.example.demo.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Data
@Configuration
@ConfigurationProperties("socket")
public class TcpSocketProperties {

    Integer port;
    String charsetName;

    String redis_open_pc_key;
    String redis_shutdown_pc_key;

}
