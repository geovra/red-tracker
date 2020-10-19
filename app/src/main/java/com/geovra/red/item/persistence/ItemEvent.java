package com.geovra.red.item.persistence;

public class ItemEvent {
  public Item item;

  public static class Created extends ItemEvent {
    public String id = "item://created";

    public Created(Item item) {
      this.item = item;
    }
  }

  public static class Updated extends ItemEvent {
    public String id = "item://updated";
    public Updated(Item item) {
      this.item = item;
    }
  }

  public static class Deleted {
    public String id = "item://deleted";
  }

}
