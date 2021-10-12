package COMP3003.Assignment02;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ResourceBundle;

/******************************************************************************************************
 * Author: George Aziz
 * Purpose: Controls the main window, displaying a text area, a toolbar (with 'load', 'save' and 'add'
 *          buttons for plugins/scripts)
 * Date Last Modified: 10/10/2021
 *****************************************************************************************************/
public class MainUI
{
    private Stage stage;
    private LoadSaveUI loadSaveUI;
    private AddUI addUI;
    private ToolBar toolBar;
    private TextArea textArea;
    private ResourceBundle bundle;
    
    public MainUI(Stage stage, LoadSaveUI loadSaveUI, AddUI addUI, ResourceBundle bundle, ToolBar toolbar, TextArea textArea)
    {
        this.stage = stage;
        this.loadSaveUI = loadSaveUI;
        this.addUI = addUI;
        this.bundle = bundle;
        this.textArea = textArea;
        this.toolBar = toolbar;
    }

    //Display method for the main part of the user interface
    public void display()
    {
        stage.setTitle(bundle.getString("app_title"));
        stage.setWidth(700);
        stage.setHeight(400);

        // Create toolbar and button event handlers
        var loadBtn = new Button(bundle.getString("load_btn"));
        var saveBtn = new Button(bundle.getString("save_btn"));
        var pluginBtn = new Button(bundle.getString("plugin_btn"));
        var scriptBtn = new Button(bundle.getString("script_btn"));
        toolBar.getItems().addAll(loadBtn, saveBtn, new Separator(), pluginBtn, scriptBtn,
                new Separator(), new Text(bundle.getString("loaded_plugins_label")));
        loadBtn.setOnAction(event -> loadSaveUI.load());
        saveBtn.setOnAction(event -> loadSaveUI.save());
        pluginBtn.setOnAction(event -> addUI.addPluginScript(true));
        scriptBtn.setOnAction(event -> addUI.addPluginScript(false));

        //TextArea Setup
        textArea.setStyle("-fx-font-family: 'Times New Roman'");
        textArea.prefWidthProperty().bind(stage.widthProperty());
        textArea.prefHeightProperty().bind(stage.heightProperty());
        textArea.setWrapText(true);

        // Add the main parts of the UI to the window
        var mainBox = new BorderPane();
        mainBox.setTop(toolBar);
        mainBox.setCenter(textArea);
        Scene scene = new Scene(mainBox);
        scene.getRoot().setStyle("-fx-font-family: 'arial'");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
