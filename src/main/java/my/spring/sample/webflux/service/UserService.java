package my.spring.sample.webflux.service;

import com.google.common.base.Strings;
import my.spring.sample.webflux.collection.User;
import my.spring.sample.webflux.dto.DataResponse;
import my.spring.sample.webflux.model.PageInfo;
import my.spring.sample.webflux.params.GetUserParams;
import my.spring.sample.webflux.utils.MongoQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    private Mono<User> getUserInSession() {
        Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();
        return context.map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(e -> (User)e);
    }

    public Mono<DataResponse> getUsers(GetUserParams params) {
        Mono<Criteria> criteriaMono = this.getUserInSession().map(user -> {
            Criteria criteria = null;
            if(params.getUsernames() != null) {
                criteria = Criteria.where("username").in(params.getUsernames());
            }
            return criteria;
        });

        Mono<Long> monoCount = criteriaMono.flatMap(criteria -> {
            Query countQuery = new Query();
            if(criteria != null)
                countQuery.addCriteria(criteria);
            return reactiveMongoTemplate.count(countQuery, User.class);
        });

        Flux<User> fluxUsers = criteriaMono.map(criteria -> {
            Query query = new Query();
            query.addCriteria(criteria);
            query.skip(params.getPage() * params.getLimit());
            query.limit(params.getLimit());
            query.with(MongoQueryUtil.retrieveSort(params.getSort()));
            return query;
        }).flatMapMany(query -> reactiveMongoTemplate.find(query, User.class));

        return Mono.zip(fluxUsers.collectList(), monoCount)
                .map(elem -> {
                    List<User> data = elem.getT1();
                    Long totalCount = elem.getT2();
                    PageInfo pageInfo = new PageInfo(Long.valueOf(params.getPage()),
                            Long.valueOf(params.getLimit()), (long)data.size(), totalCount);
                    DataResponse dataResponse = new DataResponse();
                    dataResponse.setData(elem.getT1());
                    dataResponse.setPage(pageInfo);
                    return dataResponse;
                });
    }
}
