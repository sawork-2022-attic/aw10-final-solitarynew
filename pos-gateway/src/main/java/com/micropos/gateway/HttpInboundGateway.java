package com.micropos.gateway;//package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.stereotype.Component;

@Component
public class HttpInboundGateway {

    @Bean
    public IntegrationFlow inGate() {
        // 将路径参数id放入消息的payload中
        return IntegrationFlows.from(WebFlux.inboundGateway("/api/deliveries/{deliveryId}")
                        .requestMapping(m -> m.methods(HttpMethod.GET))
                        .payloadExpression("#pathVariables.deliveryId"))
                .headerFilter("accept-encoding", false)
                .channel("sampleChannel")
                .get();
    }
}
