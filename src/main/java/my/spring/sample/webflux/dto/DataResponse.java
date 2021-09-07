package my.spring.sample.webflux.dto;

import lombok.Getter;
import lombok.Setter;
import my.spring.sample.webflux.model.PageInfo;

@Getter
@Setter
public class DataResponse {

    private Object data;

    private PageInfo page;
}
