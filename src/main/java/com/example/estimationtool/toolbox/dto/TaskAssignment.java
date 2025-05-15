package com.example.estimationtool.toolbox.dto;
import java.util.List;

public record TaskAssignment(List<Integer> userIds, int taskIds) {
}
