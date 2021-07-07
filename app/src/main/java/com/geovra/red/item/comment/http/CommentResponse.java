package com.geovra.red.item.comment.http;

import androidx.annotation.NonNull;

import com.geovra.red.item.comment.persistence.Comment;
import com.geovra.red.item.http.ItemResponse;
import com.geovra.red.item.persistence.Item;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class CommentResponse
{
  public static class CommentStore
  {
    @Getter @Setter Comment data;

    public String toString() {
      return String.format("{%s}", data.getBody());
    }
  }


  public static class CommentUpdate
  {
    @Getter @Setter Comment data;

    public String toString() {
      return data.toString();
    }
  }


  public static class CommentRemove
  {
    @Getter @Setter Comment data;

    @NonNull
    @Override
    public String toString() {
      return data.toString();
    }
  }
}

