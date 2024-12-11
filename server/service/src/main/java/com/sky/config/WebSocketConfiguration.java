package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * PROJECT_NAME: chapter-40-cqwm-websocket
 * NAME: WebConfiguration
 * USER: SHINIAN
 * DATE: 2023/5/26
 * DESCRIPTION : WebSocket的核心Bean注入
 */
@Slf4j
@Configuration
public class WebSocketConfiguration {

    /**
     * 注入ServerEndPoint服务器节点导出Bean到IOC容器中
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        log.info("【开始】 注入WebSocket服务器节点");
        return new ServerEndpointExporter();
    }
}
