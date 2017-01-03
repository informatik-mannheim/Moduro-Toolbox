package de.hs.mannheim.modUro.optimizer.cmaes;

import de.hs.mannheim.modUro.optimizer.conf.model.ParameterDump;
import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

public interface CmaesOptimizer {
    ParameterDump optimizeParameterDump(ParameterDump originParameterDump, IObjectiveFunction cmaFunction,
                                        String cmaesPropertiesPath);

    ParameterDump optimizeParameterDump(ParameterDump originParameterDump, IObjectiveFunction cmaFunction);

    double[] calculateOptimum(IObjectiveFunction optimizeFunction, double[] originParameters);
}
