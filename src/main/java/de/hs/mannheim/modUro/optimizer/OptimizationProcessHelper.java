package de.hs.mannheim.modUro.optimizer;

public class OptimizationProcessHelper {

    /**
     * This method opens the CompuCell3D-Player if
     * optimization-parameters are set.
     */
     public  void openCompuCell() {
         try{
            System.out.println("Opening compucell in a new thread");
            // todo: make path configurable
             final ProcessBuilder cc3dProc =
                     new ProcessBuilder("C://Program Files (x86)//CompuCell3D//compucell3d.bat").inheritIO();
             System.out.println("starting cc3d");
            cc3dProc.start();
     } catch (Exception e) {
        System.out.println(e.getMessage());
        e.getStackTrace();
    }
}

    /**
     * This method opens the Moduro-cc3d-folder
     * for selecting an optimization-model
     */
    public  void openModel(){
        //selectModel for optimization
        try
        {
            //TODO: set relative path!!
            Runtime.getRuntime().exec("explorer.exe C:\\Users\\Station\\Desktop\\Master\\sources\\Moduro-CC3D");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

     /**
     * Default Constructor
     */
    public OptimizationProcessHelper() {
    }

}
