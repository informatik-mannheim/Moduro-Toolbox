package de.hs.mannheim.modUro.optimizer.conf.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterDumpValue {
    enum ParameterDumpValueType {BOOLEAN, STRING, DOUBLE, INT }
    String key();
    ParameterDumpValueType type();
}
