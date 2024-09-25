package ru.azhdankov.rko.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ScorringResults {

    private List<ScorringResult> scorringResults;

}
