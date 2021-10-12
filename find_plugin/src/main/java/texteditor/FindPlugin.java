package texteditor;

import COMP3003.Assignment02.*;
import java.text.Normalizer;

/***********************************************************************************************************************
 * Author: George Aziz
 * Purpose: Plugin for GUI Text Editor application that finds a user specified text in the text area of the text editor
 * Date Last Modified: 11/10/2021
 **********************************************************************************************************************/
public class FindPlugin implements Plugin, ButtonFunction, FunctionKey
{
    private ApplicationAPI api;

    @Override
    //Method that does the setup for the plugin into the core application
    public void start(ApplicationAPI api) {
        this.api = api;
        api.registerPluginName("Find Plugin");
        api.registerNewButton("Find", this);
        api.registerFunctionKeys("F3", this);
    }

    @Override
    //Implementation of the button function
    public void buttonFunction() {
        pluginFunction();
    }

    @Override
    //Implementation of the function key function
    public void applyFunction() {
        pluginFunction();
    }

    //The implementation of the function itself to be used in registering for button/function key callback
    private void pluginFunction()
    {
        //Uses api to create a dialog to get the text to be searched
        String findStr = api.createDialog("Find Plugin", "Find a term");
        if(findStr != null) //Does not enter if it is null which means no text was inputted
        {
            String textAreaStr = api.getText(); //Gets the text of the current text area of the text editor
            String searchableStr = textAreaStr.substring(api.getCaretPosition()); //Only searches for text after the caret position

            //Normalizes text to ensure all strings work as expected
            String normFindStr = Normalizer.normalize(findStr, Normalizer.Form.NFKC);
            String normSearchableStr = Normalizer.normalize(searchableStr, Normalizer.Form.NFKC);
            if(normSearchableStr.contains(normFindStr)) //Checks if user specified string exists
            {
                //Creates the range of text to be highlighted in the text area and highlights
                int strLength = normFindStr.length();
                int startIdx = normSearchableStr.indexOf(normFindStr);
                api.highlightText(api.getCaretPosition() + startIdx, api.getCaretPosition() + startIdx + strLength);
            }
        }
    }
}
