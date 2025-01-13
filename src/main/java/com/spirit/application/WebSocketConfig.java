package com.spirit.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for enabling WebSocket support with STOMP messaging.
 * <p>This class configures the WebSocket message broker and registers the STOMP endpoints
 * required for real-time communication.</p>
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @Configuration}: Marks this class as a configuration class for Spring.</li>
 *   <li>{@code @EnableWebSocketMessageBroker}: Enables WebSocket message handling using a message broker.</li>
 * </ul>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker for handling incoming and outgoing messages.
     * <p>The message broker supports:</p>
     * <ul>
     *   <li>{@code /topic} for broadcasting messages to multiple subscribers.</li>
     *   <li>{@code /queue} for point-to-point messaging.</li>
     * </ul>
     * <p>The application-level destination prefix is set to {@code /app}.</p>
     * @param config the {@link MessageBrokerRegistry} to configure.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers STOMP endpoints for WebSocket communication.
     * <p>Defines a WebSocket endpoint at {@code /ws} and enables fallback to SockJS
     * for clients that do not support WebSocket.</p>
     * @param registry the {@link StompEndpointRegistry} to register endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
