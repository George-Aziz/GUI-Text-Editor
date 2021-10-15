package texteditor;

import COMP3003.Assignment02.ApplicationAPI;
import COMP3003.Assignment02.ButtonFunction;
import COMP3003.Assignment02.Plugin;

/*********************************************************************
 * Author: George Aziz
 * Purpose: Method declarations for JNI for the Date Plugin
 * Date Last Modified: 11/10/2021
 ********************************************************************/
public class DatePluginJNI implements Plugin, ButtonFunction
{
    private ApplicationAPI api;

    //Import of c code implementation of the java methods
    static
    {
        System.loadLibrary("c_library");
    }

    /* METHOD HEADERS */

    //Method that does the setup for the plugin into the core application
    @Override
    public native void start(ApplicationAPI api);

    @Override
    //Button function callback
    public native void buttonFunction();


}
