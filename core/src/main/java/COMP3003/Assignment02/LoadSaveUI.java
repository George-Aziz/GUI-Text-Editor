package COMP3003.Assignment02;


import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ResourceBundle;


/*********************************************************************************************************
 * Author: George Aziz
 * Purpose: Controls those parts of the user interface relating to loading/saving text area content files
 * Date Last Modified: 15/10/2021
 * NOTE: This class has been provided by the unit and adjusted to fit the needs of the current program
 ********************************************************************************************************/
public class LoadSaveUI
{
    private static final int SPACING = 8;
    
    private Stage stage;
    private FileIO fileIO;
    private FileChooser fileDialog = new FileChooser();
    private Dialog<String> encodingDialog;
    private ResourceBundle bundle;
    private TextArea textArea;
    
    public LoadSaveUI(Stage stage, FileIO fileIO, ResourceBundle bundle, TextArea textArea)
    {
        this.stage = stage;
        this.fileIO = fileIO;
        this.bundle = bundle;
        this.textArea = textArea;
    }

    // Internal method for displaying the encoding dialog and retrieving the name of the chosen encoding
    private String getEncoding()
    {
        if(encodingDialog == null)
        {
            var encodingComboBox = new ComboBox<String>();
            var content = new FlowPane();
            encodingDialog = new Dialog<>();
            encodingDialog.getDialogPane().setStyle("-fx-font-family: 'arial'");
            encodingDialog.setTitle(bundle.getString("encoding_dialog"));
            encodingDialog.getDialogPane().setContent(content);
            encodingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);        
            encodingDialog.setResultConverter(
                btn -> (btn == ButtonType.OK) ? encodingComboBox.getValue() : null);
            
            content.setHgap(SPACING);
            content.getChildren().setAll(new Label(bundle.getString("encoding_label")), encodingComboBox);
            
            encodingComboBox.getItems().setAll("UTF-8", "UTF-16", "UTF-32");
            encodingComboBox.setValue("UTF-8"); //By default the encoding will be UTF-8 unless changed
        }        
        return encodingDialog.showAndWait().orElse(null);
    }

    // Asks the user to choose a file to load, then an encoding, then loads the file contents and updates the text area
    public void load()
    {
        fileDialog.setTitle(bundle.getString("load_file_dialog"));

        File f = fileDialog.showOpenDialog(stage);
        if(f != null)
        {
            String encoding = getEncoding();
            if(encoding != null)
            {
                try
                {
                    Charset selectedEncoding = Charset.forName(encoding);
                    textArea.setText(fileIO.load(f, selectedEncoding));
                }
                catch(FileLoadingException  ex)
                {
                    System.out.println(ex.getMessage());
                    Alert error = new Alert(
                        Alert.AlertType.ERROR, 
                        String.format(bundle.getString("load_error") + "\n" + ex.getClass().getName() +
                                "\n" + ex.getMessage()), ButtonType.CLOSE);
                    error.getDialogPane().setStyle("-fx-font-family: 'arial'");
                    error.showAndWait();
                }                
            }
        }
    }

    // Asks the user to choose a filename to save the content of text area under, then an encoding, then saves the
    // contents to the chosen file in the chosen encoding
    public void save()
    {
        fileDialog.setTitle(bundle.getString("save_file_dialog"));
        File f = fileDialog.showSaveDialog(stage);
        if(f != null)
        {
            String encoding = getEncoding();
            if(encoding != null)
            {
                try
                {
                    Charset selectedEncoding = Charset.forName(encoding);
                    fileIO.save(f, selectedEncoding, textArea);
                }
                catch(IOException ex)
                {
                    Alert error = new Alert(
                            Alert.AlertType.ERROR,
                            String.format(bundle.getString("save_error")+ "\n" + ex.getClass().getName() +
                                    "\n" + ex.getMessage()), ButtonType.CLOSE);
                    error.getDialogPane().setStyle("-fx-font-family: 'arial'");
                    error.showAndWait();
                }
            }
        }
    }
}
