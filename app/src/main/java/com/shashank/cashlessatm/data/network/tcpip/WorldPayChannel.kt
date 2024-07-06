package com.shashank.cashlessatm.data.network.tcpip

import com.shashank.cashlessatm.domain.SessionManager
import org.jpos.iso.BaseChannel
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.GenericPackager
import java.net.SocketException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class WorldPayChannel @Inject constructor(
    ipAddress: String,
    port: Int,
    packager: GenericPackager?,
    sessionManager: SessionManager
) : BaseChannel(ipAddress, port, packager) {

    init {
        // SSL setup if required by Worldpay
        val sslSocketFactory = TcpSslConnection()
        sslSocketFactory.setPassword("")
        sslSocketFactory.setKeyStore("")
        setSocketFactory(sslSocketFactory)
        setTimeOut(sessionManager.getTimeout())
    }

    private fun setTimeOut(timeout: Int) {
        try {
            super.setTimeout(timeout)
        } catch (ex: SocketException) {
            throw TimeoutException("Transaction timed out!")
        }
    }

    override fun getHeaderLength(): Int {
        return super.getHeaderLength()
    }

    override fun sendMessageHeader(m: ISOMsg, len: Int) {
        val header = constructWorldpayHeader(len)
        serverOut.write(header.toByteArray())
    }

    override fun getMessageLength(): Int {
        val headerBytes = ByteArray(6) // 2 for originator + 4 for length
        serverIn.readFully(headerBytes)
        return String(headerBytes.copyOfRange(2, 6)).toInt()
    }

    private fun constructWorldpayHeader(len: Int): String {
        val messageOriginator = "BT" // This should be configurable
        val messageLength = len.toString().padStart(4, '0')
        val echoData = "000000000000000" // This should be filled with actual echo data

        return "$messageOriginator$messageLength$echoData"
    }

//    override fun sendMessage(m: ISOMsg, len: Int) {
//        sendMessageHeader(m, len)
//        super.sendMessage(m, 0, len)  // CHECK parameters -- OFFSET
//    }

    override fun receive(): ISOMsg {
        val headerBytes = ByteArray(21) // Full header length
        serverIn.readFully(headerBytes)
        parseWorldpayHeader(headerBytes)
        return super.receive()
    }

    private fun parseWorldpayHeader(header: ByteArray) {
        val headerString = String(header)
        val messageOriginator = headerString.substring(0, 2)
        val messageLength = headerString.substring(2, 6).toInt()
        val echoData = headerString.substring(6, 21)

        // You can store these values or use them as needed
    }
}