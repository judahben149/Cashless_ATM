package com.shashank.cashlessatm.data.network.tcpip

import com.shashank.cashlessatm.domain.SessionManager
import org.jpos.iso.BaseChannel
import org.jpos.iso.ISOException
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.GenericPackager
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.SocketException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.jvm.Throws

class WorldpayCustomChannel @Inject constructor(
    ipAddress: String,
    port: Int,
    private val worldPayPackager: GenericPackager?,
    sessionManager: SessionManager
) : BaseChannel(ipAddress, port, worldPayPackager) {

    private var myHeader = ""

    init {
        // SSL setup if required by Worldpay
        val sslSocketFactory = TcpSslConnection()
        sslSocketFactory.setPassword("")
        sslSocketFactory.setKeyStore("")
        setSocketFactory(sslSocketFactory)
        setTimeOut(sessionManager.getTimeout())
    }

    @Throws(IOException::class)
    override fun sendMessageLength(len: Int) {
//        serverOut.write(len shr 8)
//        serverOut.write(len)
        serverOut.write(0) // The length is already concatenated in the custom header being constructed and sent
    }

    @Throws(IOException::class, ISOException::class)
    override fun getMessageLength(): Int = -1


    override fun sendMessageHeader(m: ISOMsg, len: Int) {
        try {
            m.packager = packager
            val messageOriginator = "BT"
            val messageLength = m.pack().size
            val echoData = "100000100010000"

            myHeader = myHeader + messageOriginator + messageLength + echoData

            print("HEADER >>>>>> $header")

            logRawData("Header sent --> ", myHeader.toByteArray(Charsets.UTF_8))
            serverOut.write(myHeader.toByteArray(Charsets.UTF_8))
        } catch (e: ISOException) {
            print(e.message)
        }
    }

    /**
     * * @param header Hex representation of header
     */
//    override fun setHeader(header: String) {
//        super.setHeader(
//            ISOUtil.hex2byte(myHeader.toByteArray(Charsets.UTF_8), 0, myHeader.length)
//        )
//    }

    override fun receive(): ISOMsg {
        return try {
            val rawData = readRawData(serverIn)
            logRawData("Received Message -->", rawData)

            // Remove header structure from stream
            val headerLength = 21
            val data = rawData.copyOfRange(headerLength, rawData.size)

            val message = ISOMsg()
            message.packager = worldPayPackager
            message.unpack(data)
            message
//        return super.receive() // To try too
        } catch (e: SocketException) {
            println("SocketException: ${e.message}")
            throw ISOException("Error receiving message", e)
        } catch (e: IllegalArgumentException) {
            println("IllegalArgumentException: ${e.message}")
            throw ISOException("Illegal Argument Exception - Likely empty raw data?", e)
        }
    }

    private fun readRawData(input: InputStream): ByteArray {
        val baos = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        var bytesRead: Int
        return try {
            while (input.read(buffer).also { bytesRead = it } != -1) {
                baos.write(buffer, 0, bytesRead)
            }
            baos.toByteArray()
        } catch (e: SocketException) {
            println("SocketException while reading: ${e.message}")
            baos.toByteArray() // Return whatever has been read so far
        }
    }

    private fun logRawData(prefix: String, data: ByteArray) {
        val hexString = data.joinToString(" ") { String.format("%02X", it) }
        println("$prefix Raw Data: $hexString")
    }

    private fun setTimeOut(timeout: Int) {
        try {
            super.setTimeout(timeout)
        } catch (ex: SocketException) {
            throw TimeoutException("Failed to apply timeout (${timeout}s)...")
        }
    }
}
