package com.travelbuddy.post.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timeline {
  List<TimelineEntry> timeline;
}
