package com.travelbuddy.post.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

  private String _id; // serves as PostId,ChatId and TimelineId
  private String source;
  private String destination; // also the Title of the post
  private LocalDate startDate;
  private LocalDate endDate;
  private Count count;
  private List<String> sequenceOfEvents;
  private Double amount;
  private List<String> users;
  private Status status;
}
