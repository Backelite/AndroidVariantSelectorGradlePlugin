# AndroidVariantSelector Gradle Plugin

A plugin for Gradle that aims to simplify dynamic build of android variants.
The plugin's task reads a BUILD_TYPE and a FLAVOR variable to determine the variant to build.

It also can move and/or rename your generated apk.


## Why ?
The Android plugin for Gradle lets you build different variants of an application using dynamically generated tasks (like assembleDebug and assembleRelease for example). However, you need to know those tasks in advance to build a variant.  
Consequently, when building your application via a script, you need to hardcode the variant you want to build in your script. You would need as many scripts as the number of variants you are building.  
This is especially problematic when using a continuous integration server to build your application. On Jenkins, you would need to create a job for each build variant.  

This plugin allows to select dynamically which variant you want to build in your script by  using input variables. On Jenkins for example, you simply need to add parameters to your job. Each build can then be parameterized to build the variant you need.

In order to easily retrieve the application you built, the plugin also gives you the opportunity to move the generated apk in the directory of your choice (without touching the original apk created by Android).

The last feature lets you prefix the generated apk name with the date and hour of the build, to be able to identify your application more easily.


## Usage

Apply the plugin in your `build.gradle` and configure it if needed, using the 'androidVariantSelector' closure :

```groovy
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath 'com.backelite:android-variantSelector-gradle-plugin:1.0'
  }
}

apply plugin: 'com.backelite.android.variantSelector'

androidVariantSelector {
  moveOutputEnabled true
  outputDirectoryPath "myDeliveryFolder"
  renameOutputEnabled false
}
```

You can then use the plugin with the gradle command line :

    $ gradle assembleVariant -DBUILD_TYPE=myBuildType -DFLAVOR=myFlavor


## Parameters
The `assembleVariant` task can take two properties :  
_**BUILD_TYPE :**_  
The gradle build type of the variant to assemble.  
_**FLAVOR :**_  
The gradle flavor of the variant to assemble.  
This property is optional if you don’t have any flavor in your application.

You can customize the following parameters in your `build.gradle`, using the 'androidVariantSelector' closure :  
_**renameOutputEnabled :**_  
Specifies if the final apk will be renamed. Currently, the rename function simply prefixes the apk name with the build date.  
The default value is false.  
_**moveOutputEnabled :**_  
Specifies if we need to move the generated apk outside of the defaut build directory.  
The default value is false.  
_**outputDirectoryPath :**_  
This variable is used only if moveOutputEnabled is true.  
It specifies the directory in which we will copy the final apk. The path is relative to the project build directory.


## Contributing
Contributions for bug fixing or improvements are welcomed. Feel free to submit a pull request.

## Licence
AndroidVariantSelector is available under the MIT license. See the LICENSE file for more info.

[![Analytics](https://ga-beacon.appspot.com/UA-44164731-1/android-variant-selector/readme?pixel)](https://github.com/igrigorik/ga-beacon)