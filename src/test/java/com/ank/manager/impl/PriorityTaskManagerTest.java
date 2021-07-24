package com.ank.manager.impl;

import com.ank.manager.TaskManager;
import com.ank.model.Process;
import com.ank.model.TaskPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PriorityTaskManagerTest {

    private static final int CAPACITY = 3;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new PriorityTaskManager(CAPACITY);
    }

    @Test
    @DisplayName("Should drop adding process when lower priority process is not available")
    public void shouldDropAddingProcess() {
        Process p1 = new Process(TaskPriority.MEDIUM);
        taskManager.addProcess(p1);

        Process p2 = new Process(TaskPriority.MEDIUM);
        taskManager.addProcess(p2);

        Process p3 = new Process(TaskPriority.HIGH);
        taskManager.addProcess(p3);

        Process p4 = new Process(TaskPriority.LOW);
        taskManager.addProcess(p4);

        var activeProcessList = taskManager.listActiveProcesses();
        assertThat(activeProcessList).hasSize(3);
        assertThat(activeProcessList).contains(p1, p2, p3);
        assertThat(activeProcessList).doesNotContain(p4);
    }

    @Test
    @DisplayName("Should replace older process of Lower priority when high priority process is added")
    public void shouldReplaceLowerPriority() {

        Process p1 = new Process(TaskPriority.LOW);
        taskManager.addProcess(p1);

        Process p2 = new Process(TaskPriority.MEDIUM);
        taskManager.addProcess(p2);

        Process p3 = new Process(TaskPriority.MEDIUM);
        taskManager.addProcess(p3);

        Process p4 = new Process(TaskPriority.HIGH);
        taskManager.addProcess(p4);

        var activeProcessList = taskManager.listActiveProcesses();
        assertThat(activeProcessList).hasSize(3);
        assertThat(activeProcessList).contains(p2, p3, p4);
        assertThat(activeProcessList).doesNotContain(p1);
    }
}
