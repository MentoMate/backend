package com.example.mentoringproject.ElasticSearch.util;

import java.util.List;

public class SearchResult<T> {

  private final int totalPages;
  private final List<T> items;

  public SearchResult(int totalPages, List<T> items) {
    this.totalPages = totalPages;
    this.items = items;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public List<T> getItems() {
    return items;
  }
}
