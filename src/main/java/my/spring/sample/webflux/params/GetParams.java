package my.spring.sample.webflux.params;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class GetParams {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String sort;

    @Getter(AccessLevel.NONE)
    private Integer page;

    @Getter(AccessLevel.NONE)
    private Integer limit;

    public Integer getPage() {
        if(this.page == null)
            return 0;
        return this.page;
    }

    public Integer getLimit() {
        if(this.limit == null)
            return 20;
        return this.limit;
    }
}
