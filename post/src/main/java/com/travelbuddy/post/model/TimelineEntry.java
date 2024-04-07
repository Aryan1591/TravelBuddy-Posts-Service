package com.travelbuddy.post.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimelineEntry {
  private String event;
  private LocalDate time; // instant in LocalDate if required
}
