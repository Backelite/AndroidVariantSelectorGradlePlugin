package com.backelite.android.gradle.plugin

import com.google.common.io.Files
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat

class AssembleVariantTask extends DefaultTask {

    public static final String BUILD_TYPE_PROPERTY_NAME = "BUILD_TYPE"
    public static final String FLAVOR_PROPERTY_NAME = "FLAVOR"
    def buildVariantName

    AssembleVariantTask() {
        super()

        buildVariantName = generateBuildVariantName()

        def taskName = "assemble${capitalizeFirstLetter(buildVariantName)}"
        this.dependsOn taskName

        println "Variant ${taskName} selected."
    }

    @TaskAction
    def renameAndMoveApk() {
        String outputDirectoryPath = project.androidVariantSelector.outputDirectoryPath
        boolean moveOutputEnabled = project.androidVariantSelector.moveOutputEnabled
        boolean renameOutputEnabled = project.androidVariantSelector.renameOutputEnabled

        if (project.android != null && (moveOutputEnabled || renameOutputEnabled)) {
            // Cleans output directory
            if (moveOutputEnabled) {
                def apkDeliveryDirectory = new File(project.buildDir, outputDirectoryPath)
                if (apkDeliveryDirectory.exists()) {
                    for (File file : apkDeliveryDirectory.listFiles()) {
                        if (!file.isDirectory()) {
                            file.delete()
                        }
                    }
                }
            }

            project.android.applicationVariants.all { variant ->
                if (variant.name == buildVariantName) {
                    println "Found variant ${buildVariantName}"

                    variant.outputs.each { output ->
                        println "Processing output in variant..."

                        File apkToDeliver = output.outputFile
                        String oldName = apkToDeliver.name;
                        // Generates new output name if required
                        String newName = renameOutputEnabled ? generateNewName(oldName) : oldName

                        if (apkToDeliver.exists()) {
                            if (moveOutputEnabled) {
                                def apkDeliveryDirectory = new File(project.buildDir, outputDirectoryPath)
                                if (!apkDeliveryDirectory.exists()) {
                                    apkDeliveryDirectory.mkdirs()
                                }

                                Files.copy(apkToDeliver, new File(apkDeliveryDirectory, newName))
                                println "Apk " + apkToDeliver.name + " moved to : " + apkDeliveryDirectory.absolutePath + "/" + newName
                            } else if (renameOutputEnabled) {
                                apkToDeliver.renameTo(new File(apkToDeliver.parentFile, newName));
                                println "Apk " + oldName + " renamed to : " + newName
                            }
                        }
                    }
                }
            }
        }
    }

    static String generateNewName(String oldName) {
        "${buildTime()}-${oldName}"
    }

    static String generateBuildVariantName() {
        def flavorString = System.getProperty(FLAVOR_PROPERTY_NAME);
        if (flavorString == null) {
            flavorString = "";
        }

        def buildTypeString = System.getProperty(BUILD_TYPE_PROPERTY_NAME);
        if (buildTypeString == null) {
            buildTypeString = "debug"
        }

        // Capitalize first letter of build type if flavor is not empty
        if (!flavorString?.isEmpty()) {
            buildTypeString = capitalizeFirstLetter(buildTypeString)
        }

        return "${flavorString}${buildTypeString}"
    }

    static String buildTime() {
        def df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
        return df.format(new Date())
    }

    static String capitalizeFirstLetter(String inputString) {
        def result = inputString

        def inputStringLength = inputString.length()
        if (inputStringLength > 0) {
            result = inputString.charAt(0).toUpperCase().toString()
            if (inputStringLength > 1) {
                result += inputString.substring(1)
            }
        }

        return result
    }

}
