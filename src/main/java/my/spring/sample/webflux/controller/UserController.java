package my.spring.sample.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import my.spring.sample.webflux.service.UserService;
import my.spring.sample.webflux.dto.DataResponse;
import my.spring.sample.webflux.enums.SortType;
import my.spring.sample.webflux.params.GetUserParams;
import my.spring.sample.webflux.validator.EnumCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Mono<DataResponse> searchEventLogAuth(@RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,      // yyyy-MM-dd'T'HH:mm:ss.SSSXXX,
                                                 @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                 @RequestParam(required=false) List<String> usernames,
                                                 @RequestParam(required = false) @EnumCheck(enumClass = SortType.class) String sort,
                                                 @RequestParam(required = false) Integer page,
                                                 @RequestParam(required = false) Integer limit) {

        GetUserParams params = GetUserParams.builder()
                .startDate(startDate)
                .endDate(endDate)
                .usernames(usernames)
                .sort(sort)
                .page(page)
                .limit(limit)
                .build();

        return userService.getUsers(params);
    }
}
