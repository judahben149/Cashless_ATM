package com.shashank.cashlessatm.di

import android.content.Context
import com.shashank.cashlessatm.data.network.iso.WorldPayIsoServiceImpl
import com.shashank.cashlessatm.data.network.tcpip.WorldPayChannel
import com.shashank.cashlessatm.domain.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.jpos.iso.packager.GenericPackager
import java.io.InputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IsoModule {

    @Provides
    @Singleton
    fun providesWorldPayPackager(
        context: Context
    ): GenericPackager {
        val packagerInputStream: InputStream = context.assets.open("worldpay_packager.xml")
        return GenericPackager(packagerInputStream)
    }

    @Provides
    @Singleton
    fun providesWorldPayChannel(
        worldPayPackager: GenericPackager,
        sm: SessionManager
    ): WorldPayChannel {
        return WorldPayChannel(sm.getIp(), sm.getPort(), worldPayPackager, sm)
    }

    @Provides
    @Singleton
    fun providesWorldPayIsoServiceImpl(
        channel: WorldPayChannel,
        sm: SessionManager
    ): WorldPayIsoServiceImpl {
        return WorldPayIsoServiceImpl(channel, sm)
    }
}