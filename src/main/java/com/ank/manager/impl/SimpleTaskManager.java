package com.ank.manager.impl;

import com.ank.exception.CapacityBreachedException;
import com.ank.exception.ProcessNotExistException;
import com.ank.manager.TaskManager;
import com.ank.model.DisplayOrder;
import com.ank.model.Process;
import com.ank.model.TaskPriority;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.java.Log;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Log
public class SimpleTaskManager implements TaskManager {

    protected final long capacity;
    protected Collection<Process> processCollection;

    public SimpleTaskManager(long capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Invalid capacity provided");
        }
        this.capacity = capacity;
        this.processCollection = new LinkedList<>();
    }

    @Override
    @Synchronized
    public void addProcess(Process process) {
        if (isCapacityFull()) {
            throw new CapacityBreachedException("Maximum Capacity Reached \n Try Deleting any existing processes.");
        }

        processCollection.add(process);
    }

    @Override
    public List<Process> listActiveProcesses(DisplayOrder displayOrder) {
        return processCollection.stream()
                                .sorted(displayOrder.processComparator())
                                .collect(Collectors.toList());
    }

    @Override
    @Synchronized
    public void killProcess(final String processId) {
        Process process = processCollection.stream()
                                           .filter(p -> p.getProcessId().equals(processId))
                                           .findFirst()
                                           .orElseThrow(ProcessNotExistException::new);
        process.kill();
        processCollection.remove(process);
    }

    @Override
    @Synchronized
    public void killGroup(TaskPriority taskPriority) {
        List<Process> processList = processCollection.stream()
                                                     .filter(p -> p.getPriority() == taskPriority)
                                                     .collect(Collectors.toList());

        processList.forEach(Process::kill);
        processList.forEach(processCollection::remove);
    }

    @Override
    @Synchronized
    public void killAll() {
        processCollection.forEach(Process::kill);
        processCollection.clear();
    }

    protected boolean isCapacityFull() {
        return this.capacity == this.getProcessCollection().size();
    }
}
