package com.travelbuddy.post.entities;

import com.travelbuddy.post.constants.Constants.Status;
import com.travelbuddy.post.model.Count;
import com.travelbuddy.post.model.TimelineEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Post {

    @Id
    private String id; // serves as PostId and ChatId
    private String title;
    private String source;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Count count;
    private List<TimelineEntry> events;
    private Double amount;
    private List<String> users;
    private Status status;
}