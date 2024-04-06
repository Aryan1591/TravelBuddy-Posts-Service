package com.travelbuddy.post.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

  private String _id;
  private String source;
  private String destination;
  private String startDate;
  private String endDate;
  private Integer maleCount;
  private Integer femaleCount;
  private List<String> sequenceOfEvents;
  private Double amount;
  private List<String> users;
  private Status status;
}
