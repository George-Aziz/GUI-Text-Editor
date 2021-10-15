#include <jni.h>
#include <stdio.h>

/* Header for class texteditor_DatePluginJNI */

#ifndef _Included_texteditor_DatePluginJNI
#define _Included_texteditor_DatePluginJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     texteditor_DatePluginJNI
 * Method:    start
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_texteditor_DatePluginJNI_start
  (JNIEnv *, jobject, jobject);

/*
 * Class:     texteditor_DatePluginJNI
 * Method:    buttonFunction
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_texteditor_DatePluginJNI_buttonFunction
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
