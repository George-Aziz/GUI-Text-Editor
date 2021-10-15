package texteditor;

import COMP3003.Assignment02.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/*******************************************************************************************************************
 * Author: George Aziz
 * Purpose: Plugin for GUI Text Editor application that inserts a date into the text editor when a button is pressed
 * Date Last Modified: 11/10/2021
 *******************************************************************************************************************/
public class DatePlugin implements Plugin, ButtonFunction
{
    private ApplicationAPI api;

    @Override
    //Method that does the setup for the plugin into the core application
    public void start(ApplicationAPI api) {
        this.api = api;
        api.registerPluginName("Date Plugin");
        api.registerNewButton("Date", this);
    }

    @Override
    //Implementation of the button function
    public void buttonFunction() {
        //Gets the current locale of the program and creates a date
        Locale locale = api.getLocale();
        ZonedDateTime datetime = ZonedDateTime.now();
        DateTimeFormatter dtf1 = DateTimeFormatter.
                ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(locale);
        String dateTimeFormat = dtf1.format(datetime);
        api.setTextAtCurrentCaret(dateTimeFormat); //Sets the text at the current caret position with the localised date
    }
}
