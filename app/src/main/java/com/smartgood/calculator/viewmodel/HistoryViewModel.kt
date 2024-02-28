package com.smartgood.calculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartgood.calculator.model.History
import com.smartgood.calculator.repository.AppDatabase
import com.smartgood.calculator.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository
    val allHistory: LiveData<List<History>>

    private val mutableClickedHistory: MutableLiveData<History> = MutableLiveData()
    val clickedHistory: LiveData<History> = mutableClickedHistory

    init {
        val dao = AppDatabase.getDatabase(application).getHistoryDao()
        repository = HistoryRepository(dao)
        allHistory = repository.allHistory
    }

    fun insertHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(history)
    }

    fun deleteAllHistory() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteHistoryById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(id)
    }

    fun setClickedExpression(history: History) {
        mutableClickedHistory.value = history
    }
}