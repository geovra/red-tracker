package com.geovra.red.item.http;

import androidx.annotation.NonNull;

import com.geovra.red.item.persistence.Status;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class StatusResponse {

  public static class StatusIndex {
    @Getter @Setter List<Status> data;

    @NonNull
    @Override
    public String toString() {
      return data.size() > 0
          ? "[{" + data.get(0).getName() + "}, ...]"
          : "[empty]";
    }
  }
}

