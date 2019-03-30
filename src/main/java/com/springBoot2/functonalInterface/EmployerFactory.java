package com.springBoot2.functonalInterface;

import com.springBoot2.model.Employer;

public interface EmployerFactory<P extends Employer> {
    P create();
}