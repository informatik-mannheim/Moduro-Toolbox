package de.hs.mannheim.modUro.config;

/**
 * Data class to represent filter options.
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class FilterOption {

    public boolean steadyState = false;
    public boolean completed = false;

    public FilterOption() {
    }

    public FilterOption(boolean steadyState, boolean completed) {
        this.steadyState = steadyState;
        this.completed = completed;
    }
}
