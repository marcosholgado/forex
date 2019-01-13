package com.marcosholgado.data.repository

import com.marcosholgado.data.mapper.CurrencyDateMapper
import com.marcosholgado.data.mapper.CurrencyMapper
import com.marcosholgado.data.model.CurrencyDateEntity
import com.marcosholgado.data.model.CurrencyEntity
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.model.CurrencyDate
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class CurrencyDataRepositoryTest {

    private lateinit var remoteRepository: RemoteRepository
    private lateinit var currencyMapper: CurrencyMapper
    private lateinit var currencyDateMapper: CurrencyDateMapper
    private lateinit var currencyDataRepository: CurrencyDataRepository

    @Before
    fun setup() {
        remoteRepository = mock()
        currencyMapper = mock()
        currencyDateMapper = mock()
        currencyDataRepository =
                CurrencyDataRepository(remoteRepository, currencyMapper, currencyDateMapper)
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

    @Test
    fun compareCurrenciesReturnsData() {
        val currencyDateEntityList = createCurrencyDateEntity()
        val currencyList = createCurrencyDate()

        whenever(remoteRepository.compareCurrencies(anyString(), anyString())).thenReturn(
            Flowable.just(currencyDateEntityList)
        )

        whenever(currencyDateMapper.mapFromEntity(currencyDateEntityList)).thenReturn(currencyList)

        val testObserver = currencyDataRepository.compareCurrencies(anyString(), anyString()).test()
        testObserver.assertValue(currencyList)
    }

    @Test
    fun compareCurrenciesCompletes() {
        whenever(remoteRepository.compareCurrencies(anyString(), anyString())).thenReturn(
            Flowable.just(createCurrencyDateEntity())
        )

        whenever(currencyDateMapper.mapFromEntity(createCurrencyDateEntity())).thenReturn(
            createCurrencyDate()
        )
        val testObserver = currencyDataRepository.compareCurrencies(anyString(), anyString()).test()
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

    private fun createCurrencyDateEntity(): CurrencyDateEntity {
        val map = mutableMapOf<String, Float>()
        map["EUR"] = 1.1234f
        map["USD"] = 2.1234f
        return CurrencyDateEntity("2018-11-10", map)
    }

    private fun createCurrencyDate(): CurrencyDate {
        val map = mutableMapOf<String, Float>()
        map["EUR"] = 1.1234f
        map["USD"] = 2.1234f
        return CurrencyDate("2018-11-10", map)
    }
}
