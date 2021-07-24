package com.ank.manager.impl;

import com.ank.model.Process;
import lombok.Synchronized;

import java.util.LinkedList;

public class FifoTaskManager extends SimpleTaskManager {

    public FifoTaskManager(long capacity) {
        super(capacity);
    }

    @Override
    @Synchronized
    public void addProcess(Process process) {
        if (isCapacityFull()) {
            var processList = (LinkedList<Process>) processCollection;
            processList.removeFirst()
                       .kill();
        }
        processCollection.add(process);
    }
}
