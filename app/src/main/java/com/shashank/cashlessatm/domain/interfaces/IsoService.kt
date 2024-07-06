package com.shashank.cashlessatm.domain.interfaces

import com.shashank.cashlessatm.domain.model.IsoChannelMessage
import kotlinx.coroutines.channels.Channel

interface IsoService {

    suspend fun downloadKeys(channel: Channel<IsoChannelMessage>): Boolean { return false }

    suspend fun downloadTerminalParameters(): Boolean { return false }
}