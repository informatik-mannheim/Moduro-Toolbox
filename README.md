ModUro-Toolbox
==============

A toolbox for the analysis of computer models on the urothelium. Please refer to [the project's homepage](http://mi.informatik.hs-mannheim.de/gumbel/en/forschung/moduro/) for more information. The ModUro-Toolbox requires simulations which are created with the [ModUro CC3D software](https://github.com/informatik-mannheim/Moduro-CC3D).

Installation for Windows
------------------------

A Java 8 runtime (JRE 1.8.0_65 or higher, see [Java download page](http://www.oracle.com/technetwork/java/javase/downloads/index.html)) is required. 
The ModUro-Toolbox itself needs the following files. We recommend to have the files 1 to 3 in the same directory.
 1. `moduro-toolbox-<version>-jar-with-dependencies.jar` where `<version>` is the current version.
 2. The settings file `moduro-toolbox-settings.xml`
 3. The toolbox starts `ImagesToVideo.exe` (see [http://en.cze.cz/Images-to-video](http://en.cze.cz/Images-to-video)) to create videos form image files. Please ensure that this executable is in the path or place it in the same directory like files 1 and 2. 
 4. The toolbox starts `mplayer.exe` (Windows Media Player) to play videos. Please ensure that this executable is in the path. 
````````
You may want to edit the settings file `moduro-toolbox-settings.xml` first and setup your project. Please start (e.g. by double-clicking) the JAR file `moduro-toolbox-<version>-jar-with-dependencies.jar`. If `ImagesToVideo.exe` is not installed, you cannot create any movies.
 