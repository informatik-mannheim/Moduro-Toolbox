package de.hs.mannheim.modUro.optimizer.conf.model;


import org.apache.commons.lang3.StringUtils;
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

            // some parameters may differ, so we check for a list of possible
            // parameter names that can be mapped to this field
            String hashmapAnnotationKey = parameterDumpValueAnnotation.key();
            String[] hashMapKeys = StringUtils.split(hashmapAnnotationKey.trim(), ",");
            String hashMapValue = StringUtils.EMPTY;
            for (String mapKey : hashMapKeys) {
                if (parsedBlock.containsKey(mapKey)) {
                    hashMapValue = parsedBlock.get(mapKey);
                }
            }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String value = StringUtils.EMPTY;

            ParameterDumpValue declaredAnnotation = declaredField.getDeclaredAnnotation(ParameterDumpValue.class);
            try {
                declaredField.setAccessible(true);
                Object fieldValue = declaredField.get(this);
                switch (declaredAnnotation.type()) {
                    case BOOLEAN:
                        // replace first letter with upper case
                        fieldValue = fieldValue.toString().substring(0, 1).toUpperCase()
                                + fieldValue.toString().substring(1);
                        break;
                    case STRING:
                        break;
                    case DOUBLE:
                        // some of the parameters look like "3e-05"
                        // using String.format or DecimalFormat would break this
                        fieldValue = String.valueOf(fieldValue);
                        break;
                    case INTEGER:
                        fieldValue = String.valueOf(fieldValue);
                }

                String parameterKeyString = declaredAnnotation.key();
                String[] availableParameterKeys = StringUtils.split(parameterKeyString, ",");
                if (availableParameterKeys.length == 0) {
                    throw new RuntimeException("Parameter " + declaredField.getName() +
                            " is not declared properly by ParameterDumpValueAnnotation");
                }

                // Please see ParameterDumpExecConfig class about details related to the multiple keys problem
                // We ignore this problem at this moment and we will always use the first key set in the annotation
                // todo: check if this is a legit way
                String targetKey = availableParameterKeys[0];
                sb.append(targetKey)
                        .append(": ")
                        .append(fieldValue)
                        .append("\n");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                declaredField.setAccessible(false);
            }
        }
        return sb.toString();
    }

    ParameterDumpBaseComponent() {
        System.out.println("Called Default Constructor of ParameterDumpBaseComponent()");
    }
}
