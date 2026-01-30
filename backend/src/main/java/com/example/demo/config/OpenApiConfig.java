package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI libraryManagementOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Library Management System")
						.description("API quản lý thư viện – sách, tác giả, mượn trả, đánh giá, báo cáo.")
						.version("0.0.1-SNAPSHOT"));
	}
}
