package com.geovra.red.comment.http;

import androidx.annotation.NonNull;

import com.geovra.red.comment.persistence.Comment;

import lombok.Getter;
import lombok.Setter;

public class CommentResponse
{
  public static class CommentStore
  {
    @Getter @Setter
    Comment data;

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

