package my.spring.sample.webflux.config;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsWebTestClientConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class RestDocConfiguration {

    @Bean
    public RestDocsWebTestClientConfigurationCustomizer restDocsWebTestClientConfigurationCustomizer() {
        return configurer -> configurer.operationPreprocessors()
                .withRequestDefaults(
                    modifyUris()
                        .scheme("https")
                        .host("api.my-sample.com")
                        .removePort(),
                    prettyPrint())
                .withResponseDefaults(prettyPrint());
    }
}
