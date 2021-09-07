package my.spring.sample.webflux.params;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class GetUserParams extends GetParams{

    private List<String> usernames;
}
