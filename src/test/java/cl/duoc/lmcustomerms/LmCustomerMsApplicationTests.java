package cl.duoc.lmcustomerms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
})
//@EnableAutoConfiguration(exclude = {
//        DataSourceAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class
//})
@ActiveProfiles("test")
class LmCustomerMsApplicationTests {

    @Test
    void contextLoads() {
        // Pasa automáticamente si la estructura de dependencias inyectadas es correcta
    }
}