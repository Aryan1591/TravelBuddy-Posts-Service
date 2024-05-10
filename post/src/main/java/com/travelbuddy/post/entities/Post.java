package com.travelbuddy.post.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.travelbuddy.post.constants.Constants.Status;
import com.travelbuddy.post.model.Count;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Post {

  @Id
  private String id; // serves as PostId,ChatId and TimelineId
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