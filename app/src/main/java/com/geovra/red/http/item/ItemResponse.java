package com.geovra.red.http.item;

import androidx.annotation.NonNull;
import com.geovra.red.model.Item;
import java.util.List;
import lombok.*;

public class ItemResponse {

  public static class ItemStatus {
    @Getter @Setter ItemStatusData data;

    @NonNull
    @Override
    public String toString() {
      return data.toString();
    }

    public static class ItemStatusData { // Throw-away class
      @Getter @Setter String status;
      @Getter @Setter String timestamp;
      public String toString() {
        return "{" + getStatus() + ", "+ getTimestamp()  + "}";
      }
    }
  }


  public static class ItemIndex {
    @Getter @Setter List<Item> data;

    @NonNull
    @Override
    public String toString() {
      return data.size() > 0
          ? "[{" + data.get(0).getTitle() + "}, ...]"
          : "[empty]";
    }
  }


  public static class ItemStore {
    @Getter @Setter String id;
    @Getter @Setter String title;
    @Getter @Setter String description;

    public String toString() {
      return String.format("{%s}", getTitle());
    }

  }


  public static class ItemUpdate {
    @Getter @Setter ItemUpdateData data;

    @NonNull
    @Override
    public String toString() {
      return data.toString();
    }

    public static class ItemUpdateData { // Throw-away class
      @Getter @Setter String id;
      @Getter @Setter String title;
      public String toString() {
        return "{" + getTitle()  + "}";
      }
    }
  }


  public static class ItemRemove {
    @Getter @Setter ItemRemoveData data;

    @NonNull
    @Override
    public String toString() {
      return data.toString();
    }

    public static class ItemRemoveData { // Throw-away class
      @Getter @Setter String id;
      @Getter @Setter String title;
      public String toString() {
        return "{" + getId() + ", "+ getTitle()  + "}";
      }
    }
  }

}

