package my.spring.sample.webflux.service.domain;

import my.spring.sample.webflux.collection.User;
import my.spring.sample.webflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDomainService {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
