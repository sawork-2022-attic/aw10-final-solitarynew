package com.micropos.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GatewayApplication.class);
        application.setWebApplicationType(WebApplicationType.REACTIVE);
        application.run(args);

    }

    @Bean
    @LoadBalanced
    RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
	public DirectChannel sampleChannel() {
		return new DirectChannel();
	}


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("products", r -> r.path("/api/products/**")
                        .uri("lb://pos-products"))
                .route("carts", r -> r.path("/api/carts/**")
                        .uri("lb://pos-carts"))
                .route("orders", r -> r.path("/api/orders/**")
                        .uri("lb://pos-order"))
                .route("counter", r -> r.path("/api/counter/**")
                        .uri("lb://pos-counter"))
                .route("pos-delivery", r -> r.path("/api/deliveries")
                        .uri("lb://pos-delivery"))
                .build();
    }

}
