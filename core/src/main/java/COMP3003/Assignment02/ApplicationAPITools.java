package COMP3003.Assignment02;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.*;

/**********************************************************************************
 * Author: George Aziz
 * Purpose: API implementation for external components such as plugins / scripts
 * Date Last Modified: 11/10/2021
 *********************************************************************************/
public class ApplicationAPITools implements ApplicationAPI {
    private TextArea textArea;
    private Locale locale;
    private ToolBar toolbar;
    private Stage stage;
    private ArrayList<String> loadedPlugins;
    private ArrayList<String> loadedScripts;

    public ApplicationAPITools(Locale locale, ToolBar toolBar, TextArea textArea, Stage stage, ArrayList<String> loadedPlugins, ArrayList<String> loadedScripts)
    {
        this.locale = locale;
        this.toolbar = toolBar;
        this.textArea = textArea;
        this.stage = stage;
        this.loadedPlugins = loadedPlugins;
        this.loadedScripts = loadedScripts;
    }

    @Override
    //Method for highlighting a specific range in the JavaFX textArea
    public void highlightText(int start, int end) {
        textArea.selectRange(start, end);
    }

    @Override
    //Method for obtaining current JavaFX text area's text
    public String getText() {
        return textArea.getText();
    }

    @Override
    //Method for obtaining the current locale
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    //Method for setting a specific text the current caret position
    public void setTextAtCurrentCaret(String text)
    {
        String start = textArea.getText(0, textArea.getCaretPosition());
        String end = textArea.getText(textArea.getCaretPosition(), textArea.getText().length());
        textArea.setText(start + text + end);
    }

    @Override
    //Method for obtaining caret position from JavaFX text area
    public int getCaretPosition() {
        return textArea.getCaretPosition();
    }

    @Override
    //Method for setting caret position for JavaFX text area
    public void setCaretPosition(int pos) {
        textArea.positionCaret(pos);
    }

    @Override
    //Method for displaying a text input dialog to obtain a string
    public String createDialog(String title, String headerText) {
        var dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        return dialog.showAndWait().orElse(null);
    }

    @Override
    //Method for registering a specific keybind (Used by DSL)
    public void registerKeyMapping(String comboStr, String changeStr, boolean isAdd, boolean isStartOfLine)
    {
        KeyCombination combo = KeyCombination.valueOf(comboStr);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent ->
        {
            if(combo !=null) {
                if (combo.match(keyEvent)) {
                    keybindAction(changeStr, isAdd, isStartOfLine);
                }
            }
        });

    }

    @Override
    //Method for registering a text area listener
    public void registerTextModification(TextModification callback) {
        textArea.textProperty().addListener((object, oldValue, newValue) ->
        {

            textArea.setText(callback.modifyText(newValue));
        });
    }

    @Override
    //Method for registering a function key keybind mapping
    public void registerFunctionKeys(String code, FunctionKey callback)
    {
        if(KeyCode.getKeyCode(code).isFunctionKey())
        {
            stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent ->
            {
                if(keyEvent.getCode() == KeyCode.getKeyCode(code))
                    callback.applyFunction();
            });
        }
    }

    @Override
    //Method for registering a new button in the UI
    public void registerNewButton(String buttonLabel, ButtonFunction callback)
    {
        var newBtn = new Button(buttonLabel);
        newBtn.setOnAction(event -> callback.buttonFunction());
        toolbar.getItems().add(newBtn);
    }

    @Override
    //Method for registering a script's name into the UI
    public void registerScriptName(String name)
    {
        loadedScripts.add(name);
    }

    @Override
    //Method for registering a plugin's name into the UI
    public void registerPluginName(String name)
    {
        loadedPlugins.add(name);
    }

    //Internal method to do an action for specific keybind mapping (Used by DSL)
    private void keybindAction(String changeStr, boolean isAdd, boolean isStartOfLine)
    {
        String fullTextArea = getText();
        int oldCaretPosition = getCaretPosition();

        if(isAdd && isStartOfLine) //Add at start of line
        {
            String toCaretStr = fullTextArea.substring(0, getCaretPosition());
            int lastNewLine = toCaretStr.lastIndexOf('\n');
            if(lastNewLine == -1) //If there is no new line
            {
                textArea.setText(changeStr + fullTextArea);
            }
            else
            {
                setCaretPosition(lastNewLine + 1);
                setTextAtCurrentCaret(changeStr);
            }
            setCaretPosition(oldCaretPosition + changeStr.length());
        }
        else if (isAdd && !isStartOfLine) //Add at caret location
        {
            setTextAtCurrentCaret(changeStr); //Sets text at current caret position
            setCaretPosition(oldCaretPosition + changeStr.length()); //Moves caret position as much as inputted text is
        }
        else if (!isAdd && isStartOfLine) //Delete at start of line
        {
            String toCaretStr = fullTextArea.substring(0, getCaretPosition());
            int lastNewLine = toCaretStr.lastIndexOf('\n');
            if(lastNewLine == -1) //If there is no new line
            {
                if(fullTextArea.startsWith(changeStr))
                {
                    textArea.setText(fullTextArea.substring(changeStr.length()));
                    setCaretPosition(oldCaretPosition - changeStr.length());
                }
            }
            else
            {
                setCaretPosition(lastNewLine + 1);
                String curLine = getText().substring(getCaretPosition());
                if(curLine.startsWith(changeStr)) //Checks if current line starts with the text
                {
                    textArea.setText(fullTextArea.substring(0,lastNewLine+1) + curLine.substring(changeStr.length()));
                    setCaretPosition(oldCaretPosition - changeStr.length());
                }
            }
        }
        else if (!isAdd && !isStartOfLine) //Delete at caret location (Acts as a delete key in that only looks to the right)
        {
            String fromCaretStr = fullTextArea.substring(getCaretPosition());
            if(fromCaretStr.startsWith(changeStr))
            {
                textArea.setText(fullTextArea.substring(0,getCaretPosition()) + fromCaretStr.substring(changeStr.length()));
                setCaretPosition(oldCaretPosition);
            }
        }
    }
}
