package COMP3003.Assignment02;

import java.util.Locale;

//Plugin API interface for all plugins (Implementation in core application)
public interface ApplicationAPI
{
    String getText();
    void highlightText(int start, int end);

    Locale getLocale();

    void setTextAtCurrentCaret(String text);
    int getCaretPosition();
    void setCaretPosition(int pos);

    String createDialog(String title, String headerText);

    void registerKeyMapping(String comboStr, String changeStr, boolean isAdd, boolean isStartOfLine);
    void registerTextModification(TextModification callback);
    void registerFunctionKeys(String code, FunctionKey callback);
    void registerNewButton(String buttonLabel, ButtonFunction callback);
    void registerPluginName(String name);
    void registerScriptName(String name);
}
