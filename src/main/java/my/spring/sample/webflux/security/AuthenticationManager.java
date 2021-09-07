package my.spring.sample.webflux.security;

import lombok.extern.slf4j.Slf4j;
import my.spring.sample.webflux.service.domain.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private UserDomainService userDomainService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        // TODO: retrieve username from auth token.
        String username = "";

        return userDomainService.findByUsername(username)
                .switchIfEmpty(Mono.empty())
                .map(user -> {
                    log.info("User: [{}]", user.getName());
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, authToken, List.of(grantedAuthority));
                    return usernamePasswordAuthenticationToken;
                });
    }
}
