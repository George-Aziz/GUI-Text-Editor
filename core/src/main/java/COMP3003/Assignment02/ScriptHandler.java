package COMP3003.Assignment02;

import org.python.core.*;
import org.python.util.*;

/**********************************************
 * Author: George Aziz
 * Purpose: Script handler to execute scripts
 * Date Last Modified: 10/10/2021
 *********************************************/
public class ScriptHandler {
    public void runScript(ApplicationAPI api, String pythonScript)
    {
        // Initialise the interpreter
        PythonInterpreter interpreter = new PythonInterpreter();
        // Bind the API to the script environment
        interpreter.set("api", api);
        // Run the script
        interpreter.exec(pythonScript);
    }
}
