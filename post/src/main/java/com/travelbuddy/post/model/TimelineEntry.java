package com.travelbuddy.post.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimelineEntry
{
    private String event;
    private String time;
}