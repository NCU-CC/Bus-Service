package tw.edu.ncu.cc.bus.server.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import tw.edu.ncu.cc.oauth.resource.filter.ApiTokenDecisionFilter

@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class SecurityConfig {

    @Order( 1 )
    @Configuration
    public static class OauthGuard1 extends WebSecurityConfigurerAdapter {

        @Autowired
        def ApiTokenDecisionFilter apiTokenDecisionFilter

        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.requestMatchers()
                    .antMatchers( "/v*/providers/**" )
                    .antMatchers( "/v*/routes/**" )
                .and()
                    .addFilterAfter( apiTokenDecisionFilter, UsernamePasswordAuthenticationFilter )
                    .csrf().disable()
        }
    }

    @Order( 2 )
    @Configuration
    public static class ManagementAPI extends WebSecurityConfigurerAdapter {

        @Value( '${custom.management.security.access}' )
        def String managementAccess

        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.antMatcher( "/management/**" )
                    .authorizeRequests()
                        .anyRequest().access( managementAccess )
        }

    }

}
