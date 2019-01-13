package com.marcosholgado.forex.home

import com.marcosholgado.forex.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.forex.utils.RecyclerViewMatcher
import com.marcosholgado.forex.utils.TestApplication
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrowseActivityTest {

    @Rule
    @JvmField
    val activity = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)

    @Test
    fun activityLaunches() {
        stubRepositoryGetCurrencies(Flowable.just(createCurrencyList()))
        activity.launchActivity(null)
    }

    private fun stubRepositoryGetCurrencies(list: Flowable<List<Currency>>) {
        whenever(TestApplication.appComponent().currencyRepository().getCurrencies()).thenReturn(
            list
        )
    }

    @Test
    fun displaysCurrencies() {
        stubRepositoryGetCurrencies(Flowable.just(createCurrencyList()))
        activity.launchActivity(null)
        onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerView).atPosition(0))
            .check(matches(hasDescendant(withText("USD"))))
        onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerView).atPosition(1))
            .check(matches(hasDescendant(withText("CAD"))))
        onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerView).atPosition(0))
            .check(matches(hasDescendant(withText("1.12345"))))
        onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerView).atPosition(1))
            .check(matches(hasDescendant(withText("2.12345"))))

    }

    @Test
    fun updatesCurrencies() {
        stubRepositoryGetCurrencies(Flowable.just(createCurrencyList()))
        activity.launchActivity(null)
        onView(withId(R.id.value)).perform(clearText(), typeText("2"))

        onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerView).atPosition(0))
            .check(matches(hasDescendant(withText("2.2469"))))
        onView(RecyclerViewMatcher.withRecyclerView(R.id.recyclerView).atPosition(1))
            .check(matches(hasDescendant(withText("4.2469"))))
    }

    private fun createCurrencyList(): List<Currency> {
        val currencyEntityList = mutableListOf<Currency>()
        currencyEntityList.add(Currency("USD", 1.12345f))
        currencyEntityList.add(Currency("CAD", 2.12345f))
        return currencyEntityList
    }
}