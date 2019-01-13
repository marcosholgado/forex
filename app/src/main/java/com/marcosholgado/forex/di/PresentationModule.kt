package com.marcosholgado.forex.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcosholgado.forex.comparison.ComparisonCurrenciesViewModel
import com.marcosholgado.forex.home.GetCurrenciesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(GetCurrenciesViewModel::class)
    abstract fun bindGetCurrenciesViewModel(viewModel: GetCurrenciesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComparisonCurrenciesViewModel::class)
    abstract fun bindComparisonCurrenciesViewModel(viewModel: ComparisonCurrenciesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}