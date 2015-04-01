package com.backelite.android.gradle.plugin;

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidVariantSelectorPlugin implements Plugin<Project> {

    def void apply(Project project) {
        project.extensions.create('androidVariantSelector', AndroidVariantSelectorPluginExtension)
        project.task('assembleVariant', type: AssembleVariantTask)
    }
}

class AndroidVariantSelectorPluginExtension {
    boolean renameOutputEnabled = false
    boolean moveOutputEnabled = false
    String outputDirectoryPath = ""
}
