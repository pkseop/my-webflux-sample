package my.spring.sample.webflux.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Value("${security.on}")
    private Boolean securityOn;

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        if(securityOn) {
            return http
                    .exceptionHandling()
                    .authenticationEntryPoint((swe, e) -> {
                        return Mono.fromRunnable(() -> {
                            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        });
                    }).accessDeniedHandler((swe, e) -> {
                        return Mono.fromRunnable(() -> {
                            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        });
                    }).and()
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .authenticationManager(authenticationManager)
                    .securityContextRepository(securityContextRepository)
                    .authorizeExchange()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .pathMatchers(HttpMethod.GET, "/").permitAll()       // for health check.
                    .pathMatchers(HttpMethod.GET, "/docs/api-guide.html").permitAll()
                    .anyExchange().authenticated()
                    .and().build();
        } else {
            log.info("============ In configure(). For localhost build & run ============");
            return http.csrf().disable()
                    .authorizeExchange()
                    .anyExchange().permitAll()
                    .and().build();
        }
    }

}
