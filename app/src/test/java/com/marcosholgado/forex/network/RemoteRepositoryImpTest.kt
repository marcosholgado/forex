package com.marcosholgado.forex.network

import com.google.gson.internal.LinkedTreeMap
import com.marcosholgado.data.model.CurrencyEntity
import com.marcosholgado.forex.mapper.CurrencyEntityMapper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class RemoteRepositoryImpTest {

    private lateinit var currencyEntityMapper: CurrencyEntityMapper
    private lateinit var forexService: ForexService
    private lateinit var remoteRepositoryImp: RemoteRepositoryImp

    @Before
    fun setup() {
        currencyEntityMapper = mock()
        forexService = mock()
        remoteRepositoryImp = RemoteRepositoryImp(currencyEntityMapper, forexService)
    }

    @Test
    fun getCurrenciesCompletes() {
        whenever(forexService.getCurrencies(anyString(), anyString()))
            .thenReturn(Flowable.just(createResponse()))

        val testObserver = remoteRepositoryImp.getCurrencies().test()
        testObserver.assertComplete()
    }

    @Test
    fun getCurrenciesReturnsData() {

        val response = createResponse()
        val responseCurrencyList = response.rates
        val currencyEntityList = createCurrencyEntityList()

        currencyEntityList.forEach {
            whenever(
                currencyEntityMapper.mapFromRemote(
                    Pair(
                        it.currency,
                        responseCurrencyList[it.currency]!!
                    )
                )
            ).thenReturn(it)
        }

        whenever(forexService.getCurrencies(anyString(), anyString()))
            .thenReturn(Flowable.just(response))

        val testObserver = remoteRepositoryImp.getCurrencies().test()
        testObserver.assertValues(currencyEntityList)
    }

    private fun createResponse(): ForexService.CurrenciesResponse {

        val linkedTreeMap = LinkedTreeMap<String, String>()
        linkedTreeMap["USD"] = "1.12345"
        linkedTreeMap["CAD"] = "2.12345"

        return ForexService.CurrenciesResponse(true, "EUR", linkedTreeMap)
    }

    private fun createCurrencyEntityList(): List<CurrencyEntity> {
        val currencyEntityList = mutableListOf<CurrencyEntity>()
        currencyEntityList.add(CurrencyEntity("USD", 1.12345f))
        currencyEntityList.add(CurrencyEntity("CAD", 2.12345f))
        return currencyEntityList
    }
}
