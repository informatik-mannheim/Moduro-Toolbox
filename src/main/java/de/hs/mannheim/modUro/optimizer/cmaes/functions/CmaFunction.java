package de.hs.mannheim.modUro.optimizer.cmaes.functions;

import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;


public abstract class CmaFunction {

    /** The very well-known Rosenbrock objective function to be minimized.**/

    public static class Rosenbrock implements IObjectiveFunction { // meaning implements methods valueOf and isFeasible
        public double valueOf (double[] x) {
            double res = 0;
            for (int i = 0; i < x.length-1; ++i)
                res += 100 * (x[i]*x[i] - x[i+1]) * (x[i]*x[i] - x[i+1]) +
                        (x[i] - 1.) * (x[i] - 1.);
            return res;
        }
        public boolean isFeasible(double[] x) {return true; } // entire R^n is feasible
    }

   public static class UroFunction implements IObjectiveFunction { // meaning implements methods valueOf and isFeasible
       public double valueOf(double[] x) {
           double res = 0;
           for (int i = 0; i < x.length - 1; ++i)
               res += (x[i] + x[i]) / 2;
           return res;
       }

       public boolean isFeasible(double[] x) {
           return true;
       } // entire R^n is feasible
   }

   /*
   Random Function for initializing UroFunc
    */
   public class RandomFunction implements IObjectiveFunction{
       public double valueOf(double[] x){
           double res =0;
           for (int i = 0; i < x.length - 1; ++i)
               res += x[i];
           return res;
       }
       public boolean isFeasible(double[] x){
           return true;
       }
   }

}

