package com.micropos.gateway;//package com.example.demo;

import com.micropos.dto.DeliveryDto;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class HttpOutboundGateway {
    @Bean
    public IntegrationFlow outGate() {
        // 从消息的payload中获取id，然后构造url
        return IntegrationFlows.from("sampleChannel")
                .handle(WebFlux.outboundGateway(message ->
                        UriComponentsBuilder.fromUriString("http://localhost:8086/api/deliveries/{deliveryId}")
                                .buildAndExpand(message.getPayload())
                                .toUri())
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(DeliveryDto.class))
                .get();
    }
}
