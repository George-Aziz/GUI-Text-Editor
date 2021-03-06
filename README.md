# GUI Text Editor
**Date created:** 04/10/2021

**Date last modified:** 15/10/2021

## Purpose:
To create an internationalised text editor with a GUI in JavaFX with the ability to load a DSL on startup and load plugins and python scripts at runtime

## Functionality:
* To compile and run use `./gradlew clean build` first and then `./gradlew run`

* 2 languages supported so far - English (Australia) & Arabic with the default language being English
  * To run the program with arabic do `./gradlew run --args'--locale=ar'`
  * To run the program with english do `./gradlew run --args'--locale=en'`

* There are 3 plugins provided: DatePlugin, FindPlugin & DatePluginJNI (Functions the same as DatePlugin but implemented in JNI)
* There is 1 python script provided in the root directory of the program: emoji.py