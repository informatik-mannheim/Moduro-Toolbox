package de.hs.mannheim.modUro.optimizer.conf.model;


import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class ParameterDumpBaseComponent {

    public ParameterDumpBaseComponent(Map<String, String> parsedBlock) throws IllegalAccessException {
        Field[] blockFields = this.getClass().getDeclaredFields();
        for (Field field : blockFields) {
            field.setAccessible(true);
            ParameterDumpValue parameterDumpValueAnnotation
                    = field.getDeclaredAnnotation(ParameterDumpValue.class);
            String hashmapKey = parameterDumpValueAnnotation.key();
            String hashMapValue = parsedBlock.get(hashmapKey);
            switch (parameterDumpValueAnnotation.type()) {
                case BOOLEAN:
                    field.set(this, Boolean.parseBoolean(hashMapValue));
                    break;
                case DOUBLE:
                    field.set(this, NumberUtils.createDouble(hashMapValue));
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
            field.setAccessible(false);
        }
    }
}
