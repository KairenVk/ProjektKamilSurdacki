package com.tss.config;

import com.tss.websockets.WebSocketEndpointJSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketEndpointJSON(),
                "/webSocketEndPointJSON");
    }

    @Bean
    public AbstractWebSocketHandler webSocketEndpointJSON() {
        return new WebSocketEndpointJSON();
    }
}
