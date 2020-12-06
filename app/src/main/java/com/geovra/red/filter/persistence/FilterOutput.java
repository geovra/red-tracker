package com.geovra.red.filter.persistence;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class FilterOutput {
  @Getter @Setter private String dateFrom;
  @Getter @Setter private String dateTo;
  @Getter @Setter private List<String> categories;
  @Getter @Setter private List<String> status;
}
