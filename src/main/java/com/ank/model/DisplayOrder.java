package com.ank.model;

import java.util.Comparator;

public enum DisplayOrder {
    PROCESS_ID, PRIORITY, CREATION_TIME;

    public Comparator<Process> processComparator() {
        if (this == CREATION_TIME)
            return Comparator.comparing(Process::getCreationTime);

        if (this == PRIORITY)
            return Comparator.comparingInt(p -> p.getPriority().getValue());

        else
            return Comparator.comparing(Process::getProcessId);
    }
}
