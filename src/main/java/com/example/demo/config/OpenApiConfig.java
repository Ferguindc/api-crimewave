package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI crimeWaveOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor en entorno de desarrollo");

        Contact contact = new Contact();
        contact.setEmail("admin@crimewave.com");
        contact.setName("CrimeWave Support");
        contact.setUrl("https://www.crimewave.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("CrimeWave API")
                .version("1.0")
                .contact(contact)
                .description("API para la gestión de productos, categorías, usuarios y pedidos de CrimeWave.")
                .termsOfService("https://www.crimewave.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
