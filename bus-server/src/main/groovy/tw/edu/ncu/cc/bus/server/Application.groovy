package tw.edu.ncu.cc.bus.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Import
import tw.edu.ncu.cc.bus.server.config.BeanConfig
import tw.edu.ncu.cc.bus.server.config.MvcConfig
import tw.edu.ncu.cc.bus.server.config.SecurityConfig

@EnableCaching
@SpringBootApplication( exclude = [ ErrorMvcAutoConfiguration ] )
@Import( [ SecurityConfig, BeanConfig, MvcConfig ] )
public class Application extends SpringBootServletInitializer {

    public static void main( String[] args ) {
        SpringApplication.run( Application.class, args )
    }

    @Override
    protected SpringApplicationBuilder configure( SpringApplicationBuilder builder ) {
        return builder.sources( Application.class )
    }

}

