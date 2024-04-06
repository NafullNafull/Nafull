package com.nafull.nafull.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {@Server(url = "https://204e-183-96-52-165.ngrok-free.app/", description = "ngrok uri")},
        info = @Info(title = "NafullNafull API 명세서",
                description = "사용자 어플 서비스 API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {
}
