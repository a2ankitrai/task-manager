package com.ank.model;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

import java.util.UUID;

@Getter
@Log
@ToString
public class Process {

    private final String processId;
    private final TaskPriority priority;
    private final Long creationTime;

    public Process(TaskPriority priority) {
        this.priority = priority;
        this.processId = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
    }

    public void kill() {
        log.info("Killing process :" + this);
    }


}
