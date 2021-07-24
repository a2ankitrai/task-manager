package com.ank.manager.impl;

import com.ank.model.Process;
import lombok.Synchronized;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.Optional;

@Log
public class PriorityTaskManager extends SimpleTaskManager {

    public PriorityTaskManager(long maxCapacity) {
        super(maxCapacity);
    }

    @Override
    @Synchronized
    public void addProcess(Process process) {
        if (isCapacityFull()) {
            Optional<Process> lowerPriorityProcess =
                    this.processCollection.stream()
                                          .filter(p -> p.getPriority().getValue() < process.getPriority().getValue())
                                          .min(Comparator.comparing(Process::getPriority)
                                                         .thenComparing(Process::getCreationTime));

            lowerPriorityProcess
                    .ifPresentOrElse(p -> {
                                         p.kill();
                                         processCollection.remove(p);
                                         processCollection.add(process);
                                     },
                                     () -> log.info("Capacity full. No current process exist with a lower Priority. " +
                                                            "Skipping adding process"));
        } else {
            processCollection.add(process);
        }
    }

}
