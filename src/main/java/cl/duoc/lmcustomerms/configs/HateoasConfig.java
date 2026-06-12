package cl.duoc.lmcustomerms.configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class HateoasConfig {

//    @Bean
//    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
//        ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
//        FilterRegistrationBean<ForwardedHeaderFilter> registration = new FilterRegistrationBean<>(filter);
//
//        // Esto le da prioridad absoluta sobre otros filtros en Tomcat
//        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return registration;
//    }
}