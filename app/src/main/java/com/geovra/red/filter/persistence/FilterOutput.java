package com.geovra.red.filter.persistence;

import com.geovra.red.category.persistence.Category;
import com.geovra.red.status.persistence.Status;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class FilterOutput {
  @Getter @Setter private String dateFrom;
  @Getter @Setter private String dateTo;
  @Getter @Setter private List<Status> status;
  @Getter @Setter private List<Category> categories;
}
