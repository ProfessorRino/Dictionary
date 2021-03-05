package com.pose.dictionary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider

private lateinit var jexModel: JEXViewModel


class DictionaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jexModel = ViewModelProvider(this).get(JEXViewModel::class.java)
        jexModel.initValues()
        setContent {
            MaterialTheme {
                Column {
                    InputRow()
                    HelpMenu()
                    ControlRow()
                    SearchResults()
                    LoadingScreen()
                    HelpScreen()
                }
            }
        }
    }
}

@Composable
fun InputRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val input: String by jexModel.input.observeAsState("")
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = input,
            onValueChange = {
                jexModel.setInput(it)
                jexModel.delayedSearch()
            },
            keyboardActions = KeyboardActions(onDone = {
                jexModel.dictionarySearch()
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            label = { Text("case sensitive query") },
        )
        TextButton(onClick = { jexModel.setInput("") }, modifier = Modifier.width(30.dp)) {
            if (input.isNotEmpty()) {
                Text(text = "X")
            }
        }
        TextButton(onClick = {
            jexModel.expandHelp(true)
        }, modifier = Modifier.width(30.dp)) {
            Text(text = "?")
        }
    }
}

@Composable
fun ControlRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Button(
                onClick = { jexModel.dictionarySearch() },
                modifier = Modifier.padding(3.dp)
            ) {
                Text(text = "\uD83D\uDD0D", style = MaterialTheme.typography.button)
            }
        }
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val japaneseCheck: Boolean by jexModel.jap.observeAsState(true)
                val annotationsCheck: Boolean by jexModel.annot.observeAsState(true)
                val englishCheck: Boolean by jexModel.en.observeAsState(true)
                val germanCheck: Boolean by jexModel.ger.observeAsState(true)

                Checkbox(
                    checked = japaneseCheck,
                    onCheckedChange = {
                        jexModel.setLanguage(JEXViewModel.Languages.Jap)
                    }
                )
                Text("日本語", modifier = Modifier.padding(2.dp))
                Checkbox(
                    checked = annotationsCheck,
                    onCheckedChange = {
                        jexModel.setLanguage(JEXViewModel.Languages.Annot)
                    }
                )
                Text("{注}", modifier = Modifier.padding(2.dp))

                Checkbox(
                    checked = englishCheck,
                    onCheckedChange = { jexModel.setLanguage(JEXViewModel.Languages.Eng) }
                )
                Text("英語", modifier = Modifier.padding(2.dp))

                Checkbox(
                    checked = germanCheck,
                    onCheckedChange = { jexModel.setLanguage(JEXViewModel.Languages.Ger) }
                )
                Text("ドイツ語", modifier = Modifier.padding(2.dp))
            }
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                val radioOptions = mapOf(
                    " ☆  " to JEXViewModel.SearchMethod.Match,
                    "☆…" to JEXViewModel.SearchMethod.StartWith,
                    "…☆" to JEXViewModel.SearchMethod.EndWith,
                    "…☆…" to JEXViewModel.SearchMethod.Includes
                )
                val searchMethod: JEXViewModel.SearchMethod by jexModel.searchMethod.observeAsState(
                    JEXViewModel.SearchMethod.StartWith
                )
                radioOptions.forEach { option ->
                    RadioButton(
                        selected = (option.value == searchMethod),
                        onClick = {
                            jexModel.setSearchMethod(option.value)
                            jexModel.dictionarySearch()
                        }
                    )
                    Text(text = option.key, modifier = Modifier.padding(6.dp))
                }
            }
        }
    }
}

@Composable
fun SearchResults() {
    val results: List<List<JEXEntity>> by jexModel.results.observeAsState(listOf())
    val japaneseCheck: Boolean by jexModel.jap.observeAsState(true)
    val annotationsCheck: Boolean by jexModel.annot.observeAsState(true)
    val englishCheck: Boolean by jexModel.en.observeAsState(true)
    val germanCheck: Boolean by jexModel.ger.observeAsState(true)

    LazyColumn {
        itemsIndexed(results) { index, list ->
            Card(modifier = Modifier.fillMaxWidth().padding(5.dp), elevation = 8.dp) {
                Column {
                    val cardNo = index + 1
                    Row {
                        Text("($cardNo)", style = TextStyle(color = Color.Gray))
                    }
                    var kanji = ""
                    var reading = ""
                    var annot = ""
                    var english = ""
                    var german = ""
                    val sep = "; "
                    val annotSep = ", "
                    var key: Int? = 0
                    list.forEach {
                        key = it.key
                        when (it.lan) {
                            "K" -> kanji = addResult(kanji, it.resultword, sep)
                            "R" -> reading = addResult(reading, it.resultword, sep)
                            "eng" -> english = addResult(english, it.resultword, sep)
                            "ger" -> german = addResult(german, it.resultword, sep)
                        }
                    }

                    if (japaneseCheck) {
                        if (annotationsCheck) {
                            for (annotList in jexModel.annotations) {
                                for (annotation in annotList) {
                                    if (annotation.key != key) {
                                        break
                                    } else {
                                        annot = addResult(annot, annotation.tagstring, annotSep)
                                    }
                                }
                            }
                        }
                        kanji = "[J] " + kanji.dropLast(sep.length)
                        val annotatedKanji = AnnotatedString.Builder(kanji)
                            .apply {
                                addStyle(SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal), 4,kanji.length)
                            }
                        reading = "【" + reading.dropLast(sep.length) + "】"
                        val annotatedReading = AnnotatedString.Builder(reading)
                            .apply {
                                addStyle(SpanStyle(fontSize = 20.sp,fontWeight = FontWeight.Medium,
                                    letterSpacing = -1.5.sp), 0, reading.length)
                            }
                        annot = "{" + annot.dropLast(annotSep.length) + "}."
                        val annotatedAnnot = AnnotatedString.Builder(annot)
                            .apply {
                                addStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color.Gray),
                                    0, annot.length)
                            }
                        Row(verticalAlignment = Alignment.Bottom) {
                            if (annotationsCheck) {
                                Text(
                                    annotatedKanji.toAnnotatedString()
                                            + annotatedReading.toAnnotatedString()
                                            + annotatedAnnot.toAnnotatedString()
                                )
                            } else {
                                Text(annotatedKanji.toAnnotatedString()
                                        + annotatedReading.toAnnotatedString()
                                )
                            }
                        }
                    }
                    if (englishCheck && english.isNotEmpty()) {
                        Row {
                            Text("[E] " + english.dropLast(sep.length) + ".")
                        }
                    }
                    if (germanCheck && german.isNotEmpty()) {
                        Row {
                            Text("[G] " + german.dropLast(sep.length) + ".")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HelpMenu(){
    val expand: Boolean by jexModel.expand.observeAsState(false)
    if (expand) {
        Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopEnd)) {
            DropdownMenu(expanded = expand,
                onDismissRequest = { jexModel.expandHelp(false) }) {
                DropdownMenuItem(
                    onClick = { jexModel.setShowHelp(JEXViewModel.Help.Instructions) }) {
                    Text("About")
                }
                DropdownMenuItem(
                    onClick = { jexModel.setShowHelp(JEXViewModel.Help.Annotations) }) {
                    Text("Annotations")
                }
                DropdownMenuItem(
                    onClick = { jexModel.setShowHelp(JEXViewModel.Help.Licenses) }) {
                    Text("Licenses")
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    val loading: Boolean by jexModel.queryRunning.observeAsState(false)
    if (loading) {
        Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun HelpScreen() {
    val showHelp: Boolean by jexModel.showHelp.observeAsState(false)
    val help: JEXViewModel.Help by jexModel.help.observeAsState(JEXViewModel.Help.Instructions)
    if (showHelp) {
        LazyColumn {
            itemsIndexed(listOf(jexModel.getHelpText(help))) { _, text ->
                Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                    Text(text, modifier = Modifier.padding(7.dp))
                }
            }
        }
    }
}

private fun addResult(thisResult: String, nextResult: String?, sep: String): String {
    var newString = thisResult
    if (!thisResult.contains(nextResult.toString())) {
        newString += nextResult + sep
    }
    return newString
}
