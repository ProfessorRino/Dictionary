package com.pose.dictionary

import android.app.Application

class HelpRepository (application: Application){

    private var instructions = ""
    private var annotations = ""
    private var licenses = ""

    init {
        val text = application.resources.openRawResource(R.raw.helpabout).bufferedReader().readLines()
        var string = ""
        for (line in text) {
            if (line.startsWith("ANNOTATIONS")) {
                instructions = string
                string = ""
            }
            if (line.startsWith("LICENSES")) {
                annotations = string
                string = ""
            }
            string += line + "\r\n"
        }
        licenses = string
    }

    fun getHelpText(help: JEXViewModel.Help) : String {
        when (help) {
            JEXViewModel.Help.Instructions -> {
                return instructions
            }
            JEXViewModel.Help.Annotations -> {
                return annotations
            }
            JEXViewModel.Help.Licenses -> {
                return licenses
            }
        }
    }
}