package my.spring.sample.webflux.controller;

import my.spring.sample.webflux.collection.User;
import my.spring.sample.webflux.config.RestDocConfiguration;
import my.spring.sample.webflux.dto.DataResponse;
import my.spring.sample.webflux.enums.SortType;
import my.spring.sample.webflux.model.PageInfo;
import my.spring.sample.webflux.params.GetUserParams;
import my.spring.sample.webflux.service.UserService;
import my.spring.sample.webflux.utils.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = UserController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import(RestDocConfiguration.class) // 테스트 설정 import
@AutoConfigureRestDocs
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    private static final String BEARER_TOKEN = "Bearer Jkn8tGSZxYL5yVnPv0WeIKSmtaJWrc7S";

    private String genPath(String path) {
        return "user-controller-test/" + path;
    }

    @Test
    public void getUsers() throws Exception {
        User u1 = new User();
        u1.setId(StringUtil.genObjectId());
        u1.setUsername("u1@my.com");
        u1.setName("Michael");
        u1.created();

        User u2 = new User();
        u2.setId(StringUtil.genObjectId());
        u2.setUsername("u2@my.com");
        u2.setName("Anna");
        u2.created();

        PageInfo pageInfo = new PageInfo(0L, 20L, 2L, 2L);

        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(List.of(u1, u2));
        dataResponse.setPage(pageInfo);

        Mono<DataResponse> monoRes = Mono.just(dataResponse);

        when(userService.getUsers(any(GetUserParams.class)))
                .thenReturn(monoRes);

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/user")
                    .queryParam("sort", SortType.ASC.getValue())
                    .queryParam("page", "0")
                    .queryParam("limit", "20")
                    .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", BEARER_TOKEN)
            .exchange()
            .expectStatus().isOk()
            .expectBody(DataResponse.class)
            .consumeWith(
                document(genPath("get-users"),
                    requestParameters(
                        parameterWithName("startDate").description("Start date time. Search range: startDate <= createdAt < endDate.").optional(),
                        parameterWithName("endDate").description("End date time. Search range: startDate <= createdAt < endDate.").optional(),
                        parameterWithName("usernames").description("List of username").optional(),
                        parameterWithName("sort").description("Sort by created time.\n\nValue: 'ASC', 'DESC'. Default: 'DESC'").optional(),
                        parameterWithName("page").description("Page number.\n\nDefault: 0").optional(),
                        parameterWithName("limit").description("Fetch limit count.\n\nDefault: 20").optional()
                    ),
                    responseFields(
                        fieldWithPath("data[].id").type(JsonFieldType.STRING).description("Log's ID."),
                        fieldWithPath("data[].username").type(JsonFieldType.STRING).description("Username."),
                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("User name."),
                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("Created time of user"),
                        fieldWithPath("data[].updatedAt").type(JsonFieldType.STRING).description("Updated time of user"),
                        fieldWithPath("page.page").type(JsonFieldType.NUMBER).description("Page number."),
                        fieldWithPath("page.limit").type(JsonFieldType.NUMBER).description("Fetch limit."),
                        fieldWithPath("page.count").type(JsonFieldType.NUMBER).description("Fetch count."),
                        fieldWithPath("page.totalCount").type(JsonFieldType.NUMBER).description("Total count."),
                        fieldWithPath("page.totalPages").type(JsonFieldType.NUMBER).description("Total page count.")
                    )
                )
            );
    }
}
