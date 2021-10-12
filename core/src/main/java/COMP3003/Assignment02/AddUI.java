package COMP3003.Assignment02;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/************************************************************
 * Author: George Aziz
 * Purpose: Manages dialog box for adding new plugins/scripts
 * Date Last Modified: 10/10/2021
 ************************************************************/
public class AddUI
{
    private Button addBtn = new Button();
    ObservableList<String> list;
    private List<String> loadedPlugins;
    private List<String> loadedScripts;
    private ApplicationAPI api;
    private Stage stage;
    private Dialog dialog;
    private ResourceBundle bundle;

    public AddUI(ResourceBundle bundle, Stage stage, ApplicationAPI api, ArrayList<String> loadedPlugins, ArrayList<String> loadedScripts)
    {
        this.bundle = bundle;
        this.stage = stage;
        this.api = api;
        this.loadedPlugins = loadedPlugins;
        this.loadedScripts = loadedScripts;
    }

    //Method to prompt window to add plugins/scripts
    public void addPluginScript(boolean isPlugin)
    {
        dialog = new Dialog<>();
        dialog.getDialogPane().setStyle("-fx-font-family: 'arial'");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        var toolBar = new ToolBar();

        if(isPlugin) { //Plugin to be added
            var textLabel = new Text(bundle.getString("plugin_to_be_added_label"));
            var textField = new TextField();
            textField.setPromptText(bundle.getString("plugin_name_prompt_text"));
            addBtn.setText(bundle.getString("add_plugin_btn"));
            dialog.setTitle(bundle.getString("plugin_dialog_title"));
            dialog.setHeaderText(bundle.getString("plugin_header_text"));
            list = FXCollections.observableArrayList(loadedPlugins);
            addBtn.setOnAction(event -> addPlugin(textField.getText(), list));
            toolBar.getItems().addAll(textLabel, textField, addBtn);
        } else { //Script
            addBtn.setText(bundle.getString("add_script_btn"));
            dialog.setTitle(bundle.getString("dialog_script_title"));
            dialog.setHeaderText(bundle.getString("script_header_text"));
            addBtn.setOnAction(event -> addScript());
            list = FXCollections.observableArrayList(loadedScripts);
            toolBar.getItems().addAll(addBtn);
        }

        ListView<String> listView = new ListView<>(list);

        var box = new BorderPane();
        box.setTop(toolBar);
        box.setCenter(listView);
        dialog.getDialogPane().setContent(box);
        dialog.showAndWait();
    }

    //Internal method to add a plugin
    private void addPlugin(String pluginName, ObservableList<String> list)
    {
        if(pluginName == null || pluginName.isEmpty()) //Cant have empty plugin name
        {
            Alert error = new Alert(Alert.AlertType.ERROR, bundle.getString("empty_plugin_name_error"),
                    ButtonType.CLOSE);
            error.getDialogPane().setStyle("-fx-font-family: 'arial'");
            error.showAndWait();
        }
        else //Valid name
        {
            try
            {
                Class<?> pluginClass = Class.forName(pluginName); //Looks for class
                Plugin pluginObj = (Plugin) pluginClass.getConstructor().newInstance(); //Creates the class
                pluginObj.start(api); //Calls the start method of the plugin
                dialog.close(); //Closes the dialog if successfully called start method of plugin
            } catch (ReflectiveOperationException ex) { //Error in loading the plugin (Class not found / Method not found)
                Alert error = new Alert(
                        Alert.AlertType.ERROR,
                        String.format(bundle.getString("loading_plugin_error") + "\n" + ex.getClass().getName() +
                                "\n" + ex.getMessage()), ButtonType.CLOSE);
                error.getDialogPane().setStyle("-fx-font-family: 'arial'");
                error.showAndWait();
            } catch (ClassCastException | IllegalArgumentException ex) { //Error in the loaded class itself
                Alert error = new Alert(
                        Alert.AlertType.ERROR,
                        String.format(bundle.getString("loaded_plugin_error")+ "\n" + ex.getClass().getName() +
                                "\n" + ex.getMessage()), ButtonType.CLOSE);
                error.getDialogPane().setStyle("-fx-font-family: 'arial'");
                error.showAndWait();
            }

        }
    }

    //Internal method to add a script
    private void addScript()
    {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle(bundle.getString("script_file_chooser_title"));
        File f = fileDialog.showOpenDialog(stage);
        if(f != null)
        {
            try
            {
                FileIO fileIO = new FileIO();
                ScriptHandler scriptHandler = new ScriptHandler();
                String pythonScript = fileIO.load(f, StandardCharsets.UTF_8);
                scriptHandler.runScript(api, pythonScript);
                dialog.close();
            }
            catch (FileLoadingException ex)
            {
                Alert error = new Alert(
                        Alert.AlertType.ERROR,
                        String.format(bundle.getString("loading_script_error")+ "\n" + ex.getClass().getName() +
                                "\n" + ex.getMessage()), ButtonType.CLOSE);
                error.getDialogPane().setStyle("-fx-font-family: 'arial'");
                error.showAndWait();
            }
        }
    }
}
