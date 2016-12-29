package de.hs.mannheim.modUro.optimizer.conf.utils;


import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDumpValue;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectionHelperImpl implements ReflectionHelper {

    @Override
    public void setParameterDumpFieldValue(Field field, Map<String, String> parsedBlock) throws IllegalAccessException {
        ParameterDumpValue parameterDumpValueAnnotation
                = field.getDeclaredAnnotation(ParameterDumpValue.class);
        String hashmapKey = parameterDumpValueAnnotation.key();
        String hashMapValue = parsedBlock.get(hashmapKey);
        switch (parameterDumpValueAnnotation.type()) {
            case BOOLEAN:
                field.set(this, Boolean.parseBoolean(hashMapValue));
                break;
            case DOUBLE:
                field.set(this, Double.parseDouble(hashMapValue));
                break;
            case INTEGER:
                field.set(this, Integer.parseInt(hashMapValue));
                break;
            case STRING:
                field.set(this, hashMapValue);
                break;
            default:
                throw new IllegalStateException("Type is not allowed for field: " + field.getName());
        }
    }
}
