package com.springBoot2.functonalInterface;

import com.springBoot2.model.Task;

public interface TaskFactory<P extends Task> {
    P create();
}