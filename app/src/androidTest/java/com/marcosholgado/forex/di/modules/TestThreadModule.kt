package com.marcosholgado.forex.di.modules

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Module
object TestThreadModule {
    @Provides
    @JvmStatic
    fun provideMainThread(): Scheduler = Schedulers.trampoline()
}