package com.ank.manager.impl;

import com.ank.exception.CapacityBreachedException;
import com.ank.manager.TaskManager;
import com.ank.model.DisplayOrder;
import com.ank.model.Process;
import com.ank.model.TaskPriority;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleTaskManagerTest {

    private static final int CAPACITY = 3;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new SimpleTaskManager(CAPACITY);
    }

    @DisplayName("Should only allow processes to be added until the capacity size reached")
    @Test
    public void shouldThrowCapacityBreachedException() {

        // Adding process till capacity
        addProcessUtil(CAPACITY, TaskPriority.LOW);

        assertThrows(CapacityBreachedException.class,
                     () -> taskManager.addProcess(new Process(TaskPriority.LOW)));
    }

    @Test
    @DisplayName("Should remove the process from task manager after killing it.")
    public void shouldRemoveProcessAfterKilling() {

        Process process = new Process(TaskPriority.MEDIUM);
        taskManager.addProcess(process);

        taskManager.killProcess(process.getProcessId());

        assertEquals(taskManager.listActiveProcesses().size(), 0);
    }

    @Test
    @DisplayName("Should be able to Kill all processes")
    public void shouldKillAllProcess() {

        addProcessUtil(CAPACITY, TaskPriority.MEDIUM);

        taskManager.killAll();
        assertEquals(taskManager.listActiveProcesses().size(), 0);
    }

    @Test
    @DisplayName("Should be able to Kill processes by Group")
    public void shouldKillProcessByGroup() {

        addProcessUtil(1, TaskPriority.LOW);
        addProcessUtil(2, TaskPriority.MEDIUM);

        taskManager.killGroup(TaskPriority.MEDIUM);

        assertEquals(taskManager.listActiveProcesses().size(), 1);
        assertEquals(taskManager.listActiveProcesses().get(0).getPriority(), TaskPriority.LOW);
    }

    @Test
    @DisplayName("List process ordered by ProcessId")
    public void shouldListProcessOrderedByProcessID() {

        taskManager.addProcess(new Process(TaskPriority.HIGH));
        taskManager.addProcess(new Process(TaskPriority.LOW));
        taskManager.addProcess(new Process(TaskPriority.MEDIUM));

        List<Process> activeProcesses = taskManager.listActiveProcesses(DisplayOrder.PROCESS_ID);

        assertThat(activeProcesses.size()).isEqualTo(3);
        assertThat(activeProcesses.stream()).isSortedAccordingTo(DisplayOrder.PROCESS_ID.processComparator());
    }

    @Test
    @DisplayName("List process ordered by Priority")
    public void shouldListProcessOrderedByPriority() {

        taskManager.addProcess(new Process(TaskPriority.HIGH));
        taskManager.addProcess(new Process(TaskPriority.LOW));
        taskManager.addProcess(new Process(TaskPriority.MEDIUM));

        List<Process> activeProcesses = taskManager.listActiveProcesses(DisplayOrder.PRIORITY);

        assertThat(activeProcesses.size()).isEqualTo(3);
        assertThat(activeProcesses.get(0).getPriority()).isEqualTo(TaskPriority.LOW);
        assertThat(activeProcesses.get(1).getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(activeProcesses.get(2).getPriority()).isEqualTo(TaskPriority.HIGH);
    }

    @Test
    @DisplayName("List process ordered by Creation Time")
    public void shouldListProcessOrderedByCreationTime() {

        Process p1 = new Process(TaskPriority.HIGH);
        taskManager.addProcess(p1);

        Process p2 = new Process(TaskPriority.LOW);
        taskManager.addProcess(p2);

        Process p3 = new Process(TaskPriority.MEDIUM);
        taskManager.addProcess(p3);

        List<Process> activeProcesses = taskManager.listActiveProcesses(DisplayOrder.CREATION_TIME);

        assertThat(activeProcesses.size()).isEqualTo(3);
        assertThat(activeProcesses.get(0)).isEqualTo(p1);
        assertThat(activeProcesses.get(1)).isEqualTo(p2);
        assertThat(activeProcesses.get(2)).isEqualTo(p3);
    }

    @SneakyThrows
    protected void addProcessUtil(int number, TaskPriority priority) {
        for (int i = 0; i < number; i++) {
            taskManager.addProcess(new Process(priority));
            Thread.sleep(100);
        }
    }

}
