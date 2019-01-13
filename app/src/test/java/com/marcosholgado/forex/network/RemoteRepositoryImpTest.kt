package com.marcosholgado.forex.network

import com.google.common.truth.Truth.assertThat
import com.google.gson.internal.LinkedTreeMap
import com.marcosholgado.data.model.CurrencyDateEntity
import com.marcosholgado.data.model.CurrencyEntity
import com.marcosholgado.forex.mapper.CurrencyDateEntityMapper
import com.marcosholgado.forex.mapper.CurrencyEntityMapper
import com.nhaarman.mockitokotlin2.any
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
    private lateinit var currencyDateEntityMapper: CurrencyDateEntityMapper
    private lateinit var forexService: ForexService
    private lateinit var remoteRepositoryImp: RemoteRepositoryImp
    private val string: String = "test"

    @Before
    fun setup() {
        currencyEntityMapper = mock()
        currencyDateEntityMapper = mock()
        forexService = mock()
        remoteRepositoryImp =
                RemoteRepositoryImp(
                    currencyEntityMapper,
                    currencyDateEntityMapper,
                    forexService,
                    string,
                    string
                )
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
                        responseCurrencyList!![it.currency]!!
                    )
                )
            ).thenReturn(it)
        }

        whenever(forexService.getCurrencies(anyString(), anyString()))
            .thenReturn(Flowable.just(response))

        val testObserver = remoteRepositoryImp.getCurrencies().test()
        testObserver.assertValues(currencyEntityList)
    }

    @Test
    fun getCurrenciesReturnsEmptyList() {

        val response = createNullListResponse()
        whenever(forexService.getCurrencies(anyString(), anyString()))
            .thenReturn(Flowable.just(response))

        val testObserver = remoteRepositoryImp.getCurrencies().test()
        testObserver.assertValues(emptyList())
    }

    @Test
    fun getCurrenciesReturnsError() {

        val response = createErrorResponse()
        whenever(forexService.getCurrencies(anyString(), anyString()))
            .thenReturn(Flowable.just(response))

        val testObserver = remoteRepositoryImp.getCurrencies().test()
        assertThat(testObserver.errorCount()).isEqualTo(1)
        testObserver.assertErrorMessage("this is an error")
    }

    @Test
    fun compareCurrenciesCompletes() {
        whenever(forexService.compareCurrencies(anyString(), anyString(), anyString()))
            .thenReturn(Flowable.just(createDateResponse()))
        whenever(
            currencyDateEntityMapper.mapFromRemote(createDateResponse())
        ).thenReturn(createCurrencyDateEntity())

        val testObserver =
            remoteRepositoryImp.compareCurrencies("", "").test()
        testObserver.assertComplete()
    }

    @Test
    fun compareCurrenciesReturnsError() {
        val response = createErrorDateResponse()
        whenever(forexService.compareCurrencies(anyString(), anyString(), anyString()))
            .thenReturn(Flowable.just(response))

        val testObserver = remoteRepositoryImp.compareCurrencies("", "").test()
        assertThat(testObserver.errorCount()).isEqualTo(1)
        testObserver.assertErrorMessage("this is an error")
    }

    @Test
    fun compareCurrenciesReturnsData() {
        val response = createDateResponse()
        val currencyEntityList = createCurrencyDateEntity()

        whenever(
            currencyDateEntityMapper.mapFromRemote(createDateResponse())
        ).thenReturn(currencyEntityList)

        whenever(forexService.compareCurrencies(anyString(), anyString(), anyString()))
            .thenReturn(Flowable.just(response))

        val testObserver = remoteRepositoryImp.compareCurrencies("", "").test()
        testObserver.assertValues(currencyEntityList)
    }

    private fun createResponse(): ForexService.CurrenciesResponse {
        val linkedTreeMap = LinkedTreeMap<String, String>()
        linkedTreeMap["USD"] = "1.12345"
        linkedTreeMap["CAD"] = "2.12345"
        return ForexService.CurrenciesResponse(true, "EUR", linkedTreeMap, null)
    }

    private fun createNullListResponse(): ForexService.CurrenciesResponse {
        return ForexService.CurrenciesResponse(true, "EUR", null, null)
    }

    private fun createErrorResponse(): ForexService.CurrenciesResponse {
        return ForexService.CurrenciesResponse(
            true,
            null,
            null,
            ForexService.CurrencyError("this is an error")
        )
    }

    private fun createCurrencyEntityList(): List<CurrencyEntity> {
        val currencyEntityList = mutableListOf<CurrencyEntity>()
        currencyEntityList.add(CurrencyEntity("USD", 1.12345f))
        currencyEntityList.add(CurrencyEntity("CAD", 2.12345f))
        return currencyEntityList
    }

    private fun createDateResponse(): ForexService.CurrenciesDateResponse {
        val linkedTreeMap = LinkedTreeMap<String, String>()
        linkedTreeMap["USD"] = "1.12345"
        linkedTreeMap["CAD"] = "2.12345"
        return ForexService.CurrenciesDateResponse(true, "EUR", "10-11-2018", linkedTreeMap, null)
    }

    private fun createCurrencyDateEntity(): CurrencyDateEntity {
        val map = mutableMapOf<String, Float>()
        map["USD"] = 1.12345f
        map["CAD"] = 2.12345f
        return CurrencyDateEntity("CAD", map)
    }

    private fun createErrorDateResponse(): ForexService.CurrenciesDateResponse {
        return ForexService.CurrenciesDateResponse(
            true,
            null,
            null,
            null,
            ForexService.CurrencyError("this is an error")
        )
    }
}
