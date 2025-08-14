package br.edu.imepac.atendimento.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(
        scanBasePackages = {
                "br.edu.imepac",
                "br.edu.imepac.atendimento", // Pacote específico deste serviço
                "br.edu.imepac.comum"       // Pacote comum com Services, Models, etc.
        }
)
@EnableJpaRepositories(basePackages = {"br.edu.imepac.comum.repositories"})
@EntityScan(basePackages = {"br.edu.imepac.comum.models"})
public class ClinicaMedicaAtendimentoApp {
    public static void main(String[] args) {
        SpringApplication.run(ClinicaMedicaAtendimentoApp.class, args);
    }
}
