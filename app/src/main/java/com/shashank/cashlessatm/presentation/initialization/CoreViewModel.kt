package com.shashank.cashlessatm.presentation.initialization

import com.shashank.cashlessatm.domain.BaseViewModel
import com.shashank.cashlessatm.domain.model.IsoChannelMessage
import com.shashank.cashlessatm.domain.usecase.KeyExchangeUseCase
import com.shashank.cashlessatm.utils.logs.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoreViewModel @Inject constructor(
    private val keyExchangeUseCase: KeyExchangeUseCase
): BaseViewModel() {

    private val channel = Channel<IsoChannelMessage>()

    fun initialize() {
        with(uiScope) {

            // listen for channel messages
            launch(ioScope) {
                for (message in channel) {
                    val messageContent = when (message) {
                        is IsoChannelMessage.MasterKeyDownloaded -> message.content
                        is IsoChannelMessage.PinKeyDownloaded -> message.content
                        is IsoChannelMessage.SessionKeyDownloaded -> message.content
                    }

                    Logger.log("doKeyExchange: $messageContent")
                }
            }

            launch(ioScope) {
                keyExchangeUseCase.downloadKeys(channel)
            }
        }
    }
}