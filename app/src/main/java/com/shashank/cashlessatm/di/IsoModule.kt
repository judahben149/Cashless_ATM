package com.shashank.cashlessatm.di

import android.content.Context
import com.shashank.cashlessatm.data.network.iso.WorldPayIsoServiceImpl
import com.shashank.cashlessatm.data.network.tcpip.WorldPayChannel
import com.shashank.cashlessatm.domain.SessionManager
import com.shashank.cashlessatm.utils.PreferencesHelper
import com.shashank.cashlessatm.utils.iso.IsoUtils
import com.shashank.cashlessatm.utils.iso.WorldPayPackager
import com.shashank.cashlessatm.utils.system.SystemUtils
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
    ): WorldPayPackager {
        SystemUtils.fixXmlParserIssue()
        val packagerInputStream: InputStream = context.assets.open("worldpay_packager_test.xml")
        return WorldPayPackager(packagerInputStream)
    }

    @Provides
    @Singleton
    fun providesGenericPackager(worldPayPackager: WorldPayPackager): GenericPackager {
        return worldPayPackager
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
        packager: WorldPayPackager,
        sm: SessionManager,
        isoUtils: IsoUtils
    ): WorldPayIsoServiceImpl {
        return WorldPayIsoServiceImpl(channel, packager, sm, isoUtils)
    }

    @Provides
    @Singleton
    fun providesIsoUtils(preferencesHelper: PreferencesHelper): IsoUtils {
        return IsoUtils(preferencesHelper)
    }
}