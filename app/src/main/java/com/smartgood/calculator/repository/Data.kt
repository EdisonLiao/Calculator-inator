package com.smartgood.calculator.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.smartgood.calculator.model.Currency
import com.smartgood.calculator.model.JsonPlaceholderApi
import com.smartgood.calculator.model.Rate
import com.smartgood.calculator.util.CurrencyDeserializer
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class Data(context: Context) {

    companion object {
        private var instance: Data? = null

        fun getInstance(context: Context): Data {
            if (instance == null) {
                synchronized(Data::class) {
                    instance = Data(context)
                }
            }
            return instance!!
        }
    }

    private val preferenceRates: SharedPreferences =
        context.getSharedPreferences("Exchange-Rates", MODE_PRIVATE)

    private var isFetching: MutableLiveData<Boolean> = MutableLiveData(false)

    private fun saveExchangeRates(currency: Currency) {
        preferenceRates.edit {
            clear()
            putString("_base", currency.base)
            putString(
                "_date",
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currency.date!!)
            )
            currency.rates?.forEach {
                putFloat(it.code, it.value)
            }
        }
    }

    fun getIsFetching(): LiveData<Boolean> {
        return isFetching
    }

    fun getExchangeRatesFromPreferences(): Currency? {
        return if (preferenceRates.getString(
                "_base",
                null
            ) == null || preferenceRates.getString("_date", null) == null
        )
            null
        else
            Currency(
                true,
                preferenceRates.getString("_base", null)!!,
                SimpleDateFormat("yyyy-MM-DD", Locale.getDefault()).parse(
                    preferenceRates.getString(
                        "_date",
                        null
                    )!!
                ),
                preferenceRates.all.entries
                    .filter { !it.key.startsWith("_") }
                    .sortedBy { it.key }
                    .map { Rate(it.key!!, (it.value as Float)) }
                    .toList()
            )
    }
}