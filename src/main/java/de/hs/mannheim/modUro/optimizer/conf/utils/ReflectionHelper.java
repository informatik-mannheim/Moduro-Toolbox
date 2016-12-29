package de.hs.mannheim.modUro.optimizer.conf.utils;


import java.lang.reflect.Field;
import java.util.Map;

public interface ReflectionHelper {
    void setParameterDumpFieldValue(Field field, Map<String, String> parsedBlock) throws IllegalAccessException;
}
