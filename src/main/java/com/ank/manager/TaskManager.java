package com.ank.manager;

import com.ank.model.DisplayOrder;
import com.ank.model.Process;
import com.ank.model.TaskPriority;

import java.util.List;

public interface TaskManager {

    void addProcess(Process process);

    List<Process> listActiveProcesses(DisplayOrder displayOrder);

    void killProcess(String processId);

    void killGroup(TaskPriority taskPriority);

    void killAll();

    default List<Process> listActiveProcesses(){
        return listActiveProcesses(DisplayOrder.PROCESS_ID);
    }

}
