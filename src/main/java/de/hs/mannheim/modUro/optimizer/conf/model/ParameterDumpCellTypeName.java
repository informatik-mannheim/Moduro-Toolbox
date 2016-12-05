package de.hs.mannheim.modUro.optimizer.conf.model;


public enum ParameterDumpCellTypeName {
    MEDIUM("Medium"),
    UMBRELLA("Umbrella"),
    INTERMIDATE("Intermediate"),
    STEM("Stem"),
    BASAL("Basal"),
    BASALMEMBRAN("BasalMembrane");

    private String parameterDumpNameValue;

    ParameterDumpCellTypeName(String name) {
        parameterDumpNameValue = name;
    }

    @Override
    public String toString() {
        return this.parameterDumpNameValue;
    }
}
