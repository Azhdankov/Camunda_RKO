package ru.azhdankov.rko.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class InitiateProcessVariables {
    private String tin;
    private String pin;
    @JsonAlias("clientRequestUID")
    private String corrKey;
}
