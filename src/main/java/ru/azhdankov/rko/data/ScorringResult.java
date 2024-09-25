package ru.azhdankov.rko.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ScorringResult {
  private String type;
  private String name;
  private String isCompleted;
}
