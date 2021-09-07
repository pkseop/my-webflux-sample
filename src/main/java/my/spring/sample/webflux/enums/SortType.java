package my.spring.sample.webflux.enums;

import lombok.Getter;

public enum SortType implements Findable {
    ASC("ASC", "Ascending"),
    DESC("DESC", "Descending");

    @Getter
    private String value;

    @Getter
    private String desc;

    private SortType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static SortType get(String value) {
        return Findable.getValueOf(SortType.class, value);
    }

}
