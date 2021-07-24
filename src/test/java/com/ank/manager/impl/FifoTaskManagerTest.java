package com.ank.manager.impl;

import com.ank.manager.TaskManager;
import com.ank.model.DisplayOrder;
import com.ank.model.Process;
import com.ank.model.TaskPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FifoTaskManagerTest {

    private static final int CAPACITY = 3;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new FifoTaskManager(CAPACITY);
    }

    @Test
    @DisplayName("Should replace old process when capacity reached")
    public void shouldReplaceOldProcessAfterCapacityReached() {
        Process p1 = new Process(TaskPriority.HIGH);
        taskManager.addProcess(p1);

        Process p2 = new Process(TaskPriority.LOW);
        taskManager.addProcess(p2);

        Process p3 = new Process(TaskPriority.MEDIUM);
        taskManager.addProcess(p3);

        Process p4 = new Process(TaskPriority.LOW);
        taskManager.addProcess(p4);

        var activeProcess = taskManager.listActiveProcesses(DisplayOrder.CREATION_TIME);

        assertThat(activeProcess).hasSize(3);
        assertThat(activeProcess).contains(p2, p3, p4);
        assertThat(activeProcess).doesNotContain(p1);
    }
}
