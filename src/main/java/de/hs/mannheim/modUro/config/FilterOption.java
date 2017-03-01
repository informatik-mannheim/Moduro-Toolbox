package de.hs.mannheim.modUro.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data class to represent filter options.
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class FilterOption {

    public boolean steadyState = false;
    public boolean completed = false;
    public LocalDate fromDate = null;
    public LocalDate toDate = null;

    public FilterOption() {
    }

    public FilterOption(boolean steadyState, boolean completed) {
        this.steadyState = steadyState;
        this.completed = completed;
    }
}
