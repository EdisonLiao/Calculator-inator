package com.smartgood.calculator.repository

import androidx.lifecycle.LiveData
import com.smartgood.calculator.model.History

class HistoryRepository(private val historyDao: HistoryDao) {
    val allHistory: LiveData<List<History>> = historyDao.getHistory()

    suspend fun insert(history: History) {
        historyDao.insert(history)
    }

    suspend fun deleteAll() {
        historyDao.deleteAll()
    }

    suspend fun delete(id: Int) {
        historyDao.delete(id)
    }


}