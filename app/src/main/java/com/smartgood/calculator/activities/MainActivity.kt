package com.smartgood.calculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.smartgood.calculator.R
import com.smartgood.calculator.adapters.ViewPagerAdapter
import com.smartgood.calculator.databinding.ActivityMainBinding
import com.smartgood.calculator.extensions.isLandscape
import com.smartgood.calculator.extensions.smallestScreenWidthDp
import com.smartgood.calculator.fragments.CalculatorFragment
import com.smartgood.calculator.fragments.ConverterFragment
import com.smartgood.calculator.viewmodel.HistoryViewModel


class MainActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    val topAppBar get() = binding.topAppBar
    val historyBar get() = binding.historyBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Calculator)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        setUpViews()
    }

    private fun setUpViews() {
        val pagerAdapter = ViewPagerAdapter(this)
        pagerAdapter.apply {
            addFragment(CalculatorFragment())
            addFragment(ConverterFragment())
        }
        binding.pager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
        }

        binding.tabLayout.apply {
            TabLayoutMediator(
                this, binding.pager
            ) { _: TabLayout.Tab, _: Int -> }.attach()

            getTabAt(0)?.setIcon(R.drawable.ic_calculator_filled)
            getTabAt(1)?.setIcon(R.drawable.ic_converter_outline)
            getTabAt(2)?.setIcon(R.drawable.ic_currency_outline)

            addOnTabSelectedListener(object : OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> {
                            tab.setIcon(R.drawable.ic_calculator_filled)
                            // Hide keyboard
                            getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(
                                this@apply.windowToken,
                                0
                            )
                        }

                        1 -> tab.setIcon(R.drawable.ic_converter_filled)

                        2 -> tab.setIcon(R.drawable.ic_currency_filled)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.setIcon(
                        when (tab.position) {
                            0 -> R.drawable.ic_calculator_outline

                            1 -> R.drawable.ic_converter_outline

                            2 -> R.drawable.ic_currency_outline

                            else -> R.drawable.ic_calculator_outline
                        }
                    )
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
        enableViewPagerSwipe()
    }

    fun enableViewPagerSwipe() {
        binding.pager.isUserInputEnabled = smallestScreenWidthDp > 400 || isLandscape
    }

    fun disableViewPagerSwipe() {
        binding.pager.isUserInputEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_history -> {
                showDeleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage(resources.getString(R.string.delete_message))
            .setTitle(resources.getString(R.string.delete_title))
            .setIcon(R.drawable.ic_delete_history)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                historyViewModel.deleteAllHistory()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}