package com.shashank.cashlessatm.domain.usecase

import com.shashank.cashlessatm.data.network.iso.WorldPayIsoServiceImpl
import com.shashank.cashlessatm.domain.model.IsoChannelMessage
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class KeyExchangeUseCase @Inject constructor(
    private val worldPayIsoServiceImpl: WorldPayIsoServiceImpl
) {

    suspend fun downloadKeys(channelMessage: Channel<IsoChannelMessage>) {
        worldPayIsoServiceImpl.downloadKeys(channelMessage)
    }
}