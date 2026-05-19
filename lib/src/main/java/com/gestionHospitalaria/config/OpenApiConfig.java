package com.gestionHospitalaria.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("GHospital API")
                .version("1.0.0")
                .description("""
                    API REST del sistema de gestión hospitalaria GHospital.

                    Roles disponibles y sus endpoints principales:
                    - **ADMIN** — gestión de usuarios
                    - **RECEPCIONISTA** — gestión de citas (`/api/citas`)
                    - **MÉDICO** — diagnósticos (`/api/diagnosticos`), recetas (`/api/recetas`)
                    - **ENFERMERO** — constantes vitales (`/api/constantes`)
                    - **FARMACÉUTICO** — dispensaciones (`/api/dispensaciones`), consulta de recetas (`/api/recetas`)
                    - **PACIENTE** — historial médico (`/api/pacientes/{id}/historial`)

                    Credenciales de demo: ver README.
                    """)
                .contact(new Contact()
                    .name("GHospital Team")
                    .url("https://github.com/davidoficialdegui/GHospital")));
    }
}
