package com.shashank.cashlessatm.domain.model

sealed class IsoChannelMessage {

    data class MasterKeyDownloaded(val content: String = "Master key is downloaded"): IsoChannelMessage()
    data class PinKeyDownloaded(val content: String = "Pin key is downloaded"): IsoChannelMessage()
    data class SessionKeyDownloaded(val content: String = "Session key is downloaded"): IsoChannelMessage()


}