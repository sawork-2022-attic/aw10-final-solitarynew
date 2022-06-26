package com.micropos.delivery;

import com.micropos.delivery.service.DeliveryService;
import com.micropos.dto.DeliveryDto;
import com.micropos.dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.function.Consumer;

@SpringBootApplication
@EnableEurekaClient
public class DeliveryApplication {
    private static final Logger log = LoggerFactory.getLogger(DeliveryApplication.class);

    @Resource
    DeliveryService deliveryService;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DeliveryApplication.class);
        application.setWebApplicationType(WebApplicationType.REACTIVE);
        application.run(args);
    }

    @Bean
    Consumer<OrderDto> sms() {
        return order -> {
            DeliveryDto deliveryDto = new DeliveryDto();
            deliveryDto.setId(order.getId());
            deliveryDto.setOrderId(order.getId());
            deliveryDto.setStatus(DeliveryDto.StatusEnum.CREATED);
            deliveryService.createDelivery(deliveryDto).subscribe();
        };
    }
}
