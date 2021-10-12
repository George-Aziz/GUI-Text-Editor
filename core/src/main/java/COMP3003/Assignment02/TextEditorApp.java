package COMP3003.Assignment02;

import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/********************************************************************************
 * Author: George Aziz
 * Purpose: Entry point for the text editor app
 * Date Last Modified: 09/10/2021
 * It can be invoked with the command-line parameter --locale=<value>
 *******************************************************************************/
public class TextEditorApp extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    //Entry point for program that does all setup required to get program working
    @Override
    public void start(Stage stage)
    {
        //Locale string that is passed from the console
        var localeString = getParameters().getNamed().get("locale");

        //English Locale as default unless said otherwise (If not found)
        Locale loc = new Locale("en","AU");
        ResourceBundle bundle = ResourceBundle.getBundle("bundle", loc);

        if(localeString != null)
        {
            //Current supported languages, if any new bundles get added in future can add in if statement
            if(localeString.equals("ar") || localeString.equals("en")) {
                loc = new Locale(localeString);
                bundle = ResourceBundle.getBundle("bundle", loc);
            } else {
                System.out.println("ERROR: Locale not found!! - Falling back to default locale (English) if found");
            }
        }

        //Setup for other objects to be used within program's execution
        ToolBar toolbar = new ToolBar();
        TextArea textArea = new TextArea();
        ArrayList<String> loadedPlugins = new ArrayList<>();
        ArrayList<String> loadedScripts = new ArrayList<>();
        FileIO fileIO = new FileIO();
        ApplicationAPI tools = new ApplicationAPITools(loc, toolbar, textArea, stage, loadedPlugins, loadedScripts);
        LoadSaveUI loadSaveUI = new LoadSaveUI(stage, fileIO, bundle, textArea);
        AddUI addUI = new AddUI(bundle, stage, tools, loadedPlugins, loadedScripts);

        //Loads DSL file for keybind mappings
        try
        {
            KeymapParser.parse(tools);
        } catch (ParseException | UnsupportedEncodingException | FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }

        //Start of the main user interface screen
        new MainUI(stage, loadSaveUI, addUI, bundle, toolbar, textArea).display();

    }
}
