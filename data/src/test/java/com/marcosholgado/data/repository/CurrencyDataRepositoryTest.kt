package com.marcosholgado.data.repository

import com.marcosholgado.data.mapper.CurrencyMapper
import com.marcosholgado.data.model.CurrencyEntity
import com.marcosholgado.domain.model.Currency
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyDataRepositoryTest {

    private lateinit var remoteRepository: RemoteRepository
    private lateinit var currencyMapper: CurrencyMapper
    private lateinit var currencyDataRepository: CurrencyDataRepository

    @Before
    fun setup() {
        remoteRepository = mock()
        currencyMapper = mock()
        currencyDataRepository = CurrencyDataRepository(remoteRepository, currencyMapper)
    }

    @Test
    fun getCurrenciesReturnsData() {
        val currencyEntityList = createCurrencyEntityList()
        val currencyList = createCurrencyList()

        whenever(remoteRepository.getCurrencies()).thenReturn(
            Flowable.just(currencyEntityList)
        )

        currencyEntityList.forEachIndexed { index, it ->
            whenever(currencyMapper.mapFromEntity(it)).thenReturn(currencyList[index])
        }

        val testObserver = currencyDataRepository.getCurrencies().test()
        testObserver.assertValue(currencyList)
    }

    @Test
    fun getCurrenciesCompletes() {
        whenever(remoteRepository.getCurrencies()).thenReturn(
            Flowable.just(createCurrencyEntityList())
        )
        val testObserver = currencyDataRepository.getCurrencies().test()
        testObserver.assertComplete()
    }

    private fun createCurrencyEntityList(): List<CurrencyEntity> {
        val currencyEntityList = mutableListOf<CurrencyEntity>()
        currencyEntityList.add(CurrencyEntity("EUR", 1.12345f))
        currencyEntityList.add(CurrencyEntity("USD", 2.12345f))
        return currencyEntityList
    }

    private fun createCurrencyList(): List<Currency> {
        val currencyEntityList = mutableListOf<Currency>()
        currencyEntityList.add(Currency("EUR", 1.12345f))
        currencyEntityList.add(Currency("USD", 2.12345f))
        return currencyEntityList
    }
}
