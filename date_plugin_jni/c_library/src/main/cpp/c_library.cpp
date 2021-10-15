#include "c_library.h"

/************************************************************
 * Author: George Aziz
 * Purpose: JNI Implementation of Date Plugin from Java
 * Date Last Modified: 15/10/2021
 ************************************************************/

// This construct is needed to make the C++ compiler generate C-compatible compiled code.
extern "C" 
{
    //Registers plugin name and button function callback
    JNIEXPORT void JNICALL Java_texteditor_DatePluginJNI_start(JNIEnv *env, jobject cls, jobject api)
    {
        //Setting imported API object as the field
        jclass thisClass = (*env).GetObjectClass(cls);
        jfieldID apiFieldID = (*env).GetFieldID(thisClass , "api", "LCOMP3003/Assignment02/ApplicationAPI;");
        (*env).SetObjectField(cls, apiFieldID, api);

        //Getting the field api object class
        jobject apiObj = (*env).GetObjectField(cls, apiFieldID);
        jclass apiCls = (*env).GetObjectClass(apiObj);

        //Getting method calls for registering plugin and registering button
        jmethodID regPluginName = (*env).GetMethodID(apiCls,"registerPluginName","(Ljava/lang/String;)V");
        jmethodID regButton = (*env).GetMethodID(apiCls,"registerNewButton","(Ljava/lang/String;LCOMP3003/Assignment02/ButtonFunction;)V");

        //Registers plugin and registers a new button with its label
        (*env).CallVoidMethod(apiObj, regPluginName, (*env).NewStringUTF("Date Plugin (JNI)"));
        (*env).CallVoidMethod(apiObj, regButton, (*env).NewStringUTF("Date"), cls);
    }

    //Button function implementation to set text at current caret position with localised datetime
    JNIEXPORT void JNICALL Java_texteditor_DatePluginJNI_buttonFunction(JNIEnv *env, jobject cls)
    {
        //Gets API Object and Class for method calls
        jclass thisClass = (*env).GetObjectClass(cls);
        jfieldID apiFieldID = (*env).GetFieldID(thisClass , "api", "LCOMP3003/Assignment02/ApplicationAPI;");
        jobject apiObj = (*env).GetObjectField(cls, apiFieldID);
        jclass apiCls = (*env).GetObjectClass(apiObj);

        //Gets locale object from the api
        jmethodID getLocale = (*env).GetMethodID(apiCls,"getLocale","()Ljava/util/Locale;");
        jobject locale = (*env).CallObjectMethod(apiObj, getLocale);

        //Gets the current zone datetime (ZonedDateTime datetime = ZonedDateTime.now();)
        jclass zoneDateTime = (*env).FindClass("java/time/ZonedDateTime");
        jmethodID getZoneTime = (*env).GetStaticMethodID(zoneDateTime,"now","()Ljava/time/ZonedDateTime;");
        jobject zoneTime = (*env).CallStaticObjectMethod(zoneDateTime, getZoneTime);

        //Creates the formatter for the date
        //Method calls to setup the datetime formatter
        jclass dateTimeFormatter = (*env).FindClass("java/time/format/DateTimeFormatter");
        jmethodID ofLocalizedDateTimeMethod = (*env).GetStaticMethodID(dateTimeFormatter,"ofLocalizedDateTime","(Ljava/time/format/FormatStyle;)Ljava/time/format/DateTimeFormatter;");
        jmethodID withLocaleMethod = (*env).GetMethodID(dateTimeFormatter,"withLocale","(Ljava/util/Locale;)Ljava/time/format/DateTimeFormatter;");

        //Get MEDIUM ENUM for the format style
        jclass formatStyle = (*env).FindClass("java/time/format/FormatStyle");
        jmethodID valueOfMethod = (*env).GetStaticMethodID(formatStyle,"valueOf","(Ljava/lang/String;)Ljava/time/format/FormatStyle;");
        jobject mediumLength = (*env).CallStaticObjectMethod(formatStyle, valueOfMethod, (*env).NewStringUTF("MEDIUM"));

        //(DateTimeFormatter dtf1 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);)
        jobject dtf1 = (*env).CallStaticObjectMethod(dateTimeFormatter, ofLocalizedDateTimeMethod, mediumLength);
        dtf1 = (*env).CallObjectMethod(dtf1, withLocaleMethod, locale);

        //Formats the localised datetime (String dateTimeFormat = dtf1.format(datetime);)
        jmethodID formatMethod = (*env).GetMethodID(dateTimeFormatter,"format","(Ljava/time/temporal/TemporalAccessor;)Ljava/lang/String;");
        jobject dateTimeFormat = (*env).CallObjectMethod(dtf1, formatMethod, zoneTime);

        //Set text at the current caret position
        jmethodID setText = (*env).GetMethodID(apiCls,"setTextAtCurrentCaret","(Ljava/lang/String;)V");
        (*env).CallObjectMethod(apiObj, setText, dateTimeFormat);
    }
}
