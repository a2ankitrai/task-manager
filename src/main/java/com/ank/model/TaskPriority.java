package com.ank.model;

import lombok.Getter;

@Getter
public enum TaskPriority {
    LOW(0), MEDIUM(5), HIGH(10);

    int value;

    TaskPriority(int value) {
        this.value = value;
    }
}
