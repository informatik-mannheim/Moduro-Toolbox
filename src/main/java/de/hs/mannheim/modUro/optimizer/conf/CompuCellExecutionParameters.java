package de.hs.mannheim.modUro.optimizer.conf;

// possible execution parameters are descriped in the manual of CompuCell3D:
// http://www.compucell3d.org/BinDoc/cc3d_binaries/Manuals/CompuCell3D_Reference_Manual_v.3.7.2.pdf

// value of consoleParam is the string used as parameter when executing  cc3d via cmd
public enum CompuCellExecutionParameters {

    NO_OUTPUT("noOutput"),
    EXIT_WHEN_DONE("exitWhenDone"),
    HELP("help");

    private final String consoleParam;
    CompuCellExecutionParameters(final String s) {
        this.consoleParam = s;
    }

    @Override
    public String toString() {
        return this.consoleParam;
    }
}
