package com.shashank.cashlessatm.data.network.iso

import com.shashank.cashlessatm.data.network.tcpip.WorldPayChannel
import com.shashank.cashlessatm.domain.SessionManager
import com.shashank.cashlessatm.domain.interfaces.IsoService
import com.shashank.cashlessatm.domain.model.IsoChannelMessage
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class WorldPayIsoServiceImpl @Inject constructor(
    channel: WorldPayChannel,
    sessionManager: SessionManager
) : IsoService {


    override suspend fun downloadKeys(channel: Channel<IsoChannelMessage>): Boolean {



        return false
    }
}