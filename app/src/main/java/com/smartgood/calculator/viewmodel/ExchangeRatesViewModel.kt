package com.smartgood.calculator.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartgood.calculator.model.Currency
import com.smartgood.calculator.repository.Data

class ExchangeRatesViewModel(application: Application) : AndroidViewModel(application) {
    private val currencyLiveData: MutableLiveData<Currency> = MutableLiveData<Currency>()

    fun getExchangeRates(context: Context): LiveData<Currency> {
        currencyLiveData.value = Data.getInstance(context).getExchangeRatesFromPreferences()
        return currencyLiveData
    }

    fun isFetching(context: Context): LiveData<Boolean> {
        return Data.getInstance(context).getIsFetching()
    }
}