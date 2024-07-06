package com.shashank.cashlessatm.di

import com.shashank.cashlessatm.data.network.iso.WorldPayIsoServiceImpl
import com.shashank.cashlessatm.domain.usecase.KeyExchangeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesKeyExchangeUseCase(
        worldPayIsoServiceImpl: WorldPayIsoServiceImpl
    ): KeyExchangeUseCase {
        return KeyExchangeUseCase(worldPayIsoServiceImpl)
    }
}