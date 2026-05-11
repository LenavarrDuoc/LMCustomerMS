package cl.duoc.lmcustomerms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient //Viene incluida por defecto la anotación en Spring Boot moderno, pero se recomienda explicitarla para tener claro lo que estamos utilizando.
@SpringBootApplication
public class LmCustomerMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmCustomerMsApplication.class, args);
    }

}
