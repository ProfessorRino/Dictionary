package com.pose.dictionary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pose.dictionary.data.preferences.PreferenceRepository
import kotlinx.coroutines.*

class JEXViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: JEXRepository = JEXRepository(application)
    private var helpRepository: HelpRepository = HelpRepository(application)
    private var prefRepository: PreferenceRepository = PreferenceRepository(application)

    internal var input = MutableLiveData<String>()
    internal var annotations = mutableListOf<List<AnnotationEntity>>()
    internal val results: MutableLiveData<MutableList<List<JEXEntity>>> by lazy {
        MutableLiveData<MutableList<List<JEXEntity>>>()
    }

    private var delayedSearch: Job = Job()

    private var prevQuery : Pair<String?, SearchMethod?>? = null

    enum class Languages {
        Jap,
        Annot,
        Eng,
        Ger
    }

    enum class SearchMethod(val value: String) {
        Match("match"),
        StartWith("startWith"),
        EndWith("endWith"),
        Includes("includes")
    }

    enum class Help {
        Instructions,
        Annotations,
        Licenses
    }

    var searchMethod = MutableLiveData<SearchMethod>()

    var jap = MutableLiveData<Boolean>()
    var annot = MutableLiveData<Boolean>()
    var en = MutableLiveData<Boolean>()
    var ger = MutableLiveData<Boolean>()

    var queryRunning = MutableLiveData<Boolean>()

    val expand = MutableLiveData<Boolean>()
    val showHelp = MutableLiveData<Boolean>()
    val help = MutableLiveData<Help>()



    fun expandHelp(expand: Boolean) {
        this.expand.postValue(expand)
    }

    fun setShowHelp(help: Help) {
        results.postValue(mutableListOf())
        this.help.postValue(help)
        expandHelp(false)
        showHelp.postValue(true)
    }

    fun getHelpText(help: Help): String {
        return helpRepository.getHelpText(help)
    }

    fun setInput(input: String) {
        this.input.postValue(input)
    }

    private fun getQueryRunning(): Boolean? {
        return this.queryRunning.value
    }

    fun initValues() {
        jap.postValue(prefRepository.loadJapanesePref())
        annot.postValue(prefRepository.loadAnnotPref())
        en.postValue(prefRepository.loadEnglishPref())
        ger.postValue(prefRepository.loadGermanPref())
        searchMethod.postValue(prefRepository.loadSearchMethod())
        queryRunning.postValue(false)

        viewModelScope.launch(Dispatchers.IO) {
            repository.setCaseSensitive()
            repository.analyze()
            repository.vacuum()
        }
    }

    fun setLanguage(lan: Languages) {
        when (lan) {
            Languages.Jap -> {
                jap.value = !jap.value!!
                prefRepository.saveJapanesePref(jap.value!!)
            }
            Languages.Annot -> {
                annot.value = !annot.value!!
                prefRepository.saveAnnotPref(annot.value!!)
            }
            Languages.Eng -> {
                en.value = !en.value!!
                prefRepository.saveEnglishPref(en.value!!)
            }
            Languages.Ger -> {
                ger.value = !ger.value!!
                prefRepository.saveGermanPref(ger.value!!)
            }
        }
    }

    fun setSearchMethod(method: SearchMethod) {
        searchMethod.value = method
        prefRepository.saveSearchMethod(method)
    }

    fun delayedSearch() {
        delayedSearch.cancel()
        delayedSearch = viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            dictionarySearch()
        }
    }

    fun dictionarySearch() = viewModelScope.launch(Dispatchers.IO) {
        val newQuery = Pair(input.value, searchMethod.value)
        if (!getQueryRunning()!!
            && !input.value.isNullOrEmpty()
            &&  newQuery!= prevQuery) {
            queryRunning.postValue(true)
            prevQuery = newQuery
            results.postValue(mutableListOf())
            val keyList = getKeyList(input.value!!.trimEnd().replace("\n", ""))
            annotations = getAnnotationsList(keyList)
            results.postValue(getResults(keyList))
            showHelp.postValue(false)
            queryRunning.postValue(false)
        }
    }

    fun getKeyList(searchword: String): List<Int> {
        return when (searchMethod.value) {
            SearchMethod.Match -> getEqualEntries(searchword)
            SearchMethod.StartWith -> getStartWithEntries(searchword)
            SearchMethod.EndWith -> getEndWithEntries(searchword)
            SearchMethod.Includes -> getIncludesEntries(searchword)
            else -> getEqualEntries(searchword)
        }
    }

    fun getResults(keyList: List<Int>): MutableList<List<JEXEntity>> {
        val resultList = mutableListOf<List<JEXEntity>>()
        for (key in keyList) {
            val wordsList = getResultWords(key)
            resultList.add(wordsList)
        }
        return resultList
    }

    fun getEqualEntries(searchword: String): List<Int> {
        return repository.getEqualEntries(searchword)
    }

    fun getStartWithEntries(searchword: String): List<Int> {
        return repository.getStartWithEntries(searchword)
    }

    fun getIncludesEntries(searchword: String): List<Int> {
        return repository.getIncludesEntries(searchword)
    }

    fun getEndWithEntries(searchword: String): List<Int> {
        return repository.getEndWithEntries(searchword)
    }

    fun getResultWords(key: Int): List<JEXEntity> {
        return repository.getResultWords(key)
    }

    fun getAnnotationsList(keyList: List<Int>): MutableList<List<AnnotationEntity>> {
        val annotationsList = mutableListOf<List<AnnotationEntity>>()
        for (key in keyList) {
            annotationsList.add(getAnnotations(key))
        }
        return annotationsList
    }

    fun getAnnotations(key: Int): List<AnnotationEntity> {
        return repository.getAnnotations(key)
    }

}