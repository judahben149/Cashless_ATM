package com.shashank.cashlessatm.data.network.iso

import android.content.Context
import com.shashank.cashlessatm.data.network.tcpip.WorldPayChannel
import com.shashank.cashlessatm.data.network.tcpip.WorldpayCustomChannel
import com.shashank.cashlessatm.domain.SessionManager
import com.shashank.cashlessatm.domain.interfaces.IsoService
import com.shashank.cashlessatm.domain.model.IsoChannelMessage
import com.shashank.cashlessatm.utils.date.DateUtils.timeAndDateFormatter
import com.shashank.cashlessatm.utils.iso.IsoUtils
import com.shashank.cashlessatm.utils.iso.WorldPayPackager
import kotlinx.coroutines.channels.Channel
import org.jpos.iso.IFE_BINARY
import org.jpos.iso.IFE_NUMERIC
import org.jpos.iso.ISOField
import org.jpos.iso.ISOMsg
import org.jpos.iso.ISOUtil
import org.jpos.iso.packager.GenericPackager
import org.jpos.util.Logger
import org.jpos.util.SimpleLogListener
import java.io.InputStream
import java.util.BitSet
import java.util.Date
import javax.inject.Inject
import com.shashank.cashlessatm.utils.logs.Logger as myLogger

class WorldPayIsoServiceImpl @Inject constructor(
    private val channel: WorldPayChannel,
    private val customChannel: WorldpayCustomChannel,
    private val context: Context,
    private val packager: WorldPayPackager,
    private val sessionManager: SessionManager,
    private val isoUtils: IsoUtils
) : IsoService {

    override suspend fun downloadKeys(channel: Channel<IsoChannelMessage>): Boolean {
        downloadMasterKey(channel)
//        unpack()
        return false
    }

    private fun downloadMasterKey(channelEvent: Channel<IsoChannelMessage>) {
        val date = Date()
        val stan = isoUtils.getNextStan()

        val tmkRequest = ISOMsg()
        tmkRequest.packager = packager

        val logger = Logger()
        logger.addListener(SimpleLogListener(System.out))
        packager.setLogger(logger, "debug")

        tmkRequest.mti = "0800"

        tmkRequest.apply {
            set("7", timeAndDateFormatter.format(date))
            set("11", stan)
            set("70", "301") //0301, 801
            set("115.0", "E000000000000000") //Bitmap showing that fields 1, 2, 3 are set
            set("115.1", "0000")
            set("115.2", "123")
            set("115.3", "012345678")
        }

        tmkRequest.dump(System.out, "")
        val packed = tmkRequest.pack()
        val bitmap = tmkRequest.getValue(-1)
        myLogger.log("BITMAP ----> $bitmap")

        val unpacked = ISOMsg()
        unpacked.packager = packager
        unpacked.unpack(packed)
        val bitmap2 = tmkRequest.getValue(-1)
        myLogger.log("BITMAP UNPACKED----> $bitmap2")

        customChannel.apply {
            connect()
            myLogger.log("Is channel connected? - $isConnected")
            send(packed)
        }

        val response = customChannel.receive()
        customChannel.disconnect()
        response.dump(System.out, "")

    }

    private fun returnField0packager(): GenericPackager {
        val packagerInputStream: InputStream = context.assets.open("worldpay_field0.xml")
        return GenericPackager(packagerInputStream)
    }

    private fun buildField0(): ByteArray {
        val field0 = ISOMsg()
        field0.packager = returnField0packager()

        field0.apply {
            set("1", "MA.")
            set("2", "0800")
            set("3", "1000001000100000000000000000000000000000000000000000000000000000")
        }

        return field0.pack()
    }

    private fun unpack() {

        val dump = "08008220000000000000040000000000001004011333551333550160000ED2C5E82000000000000000F1F0F2"


        val b = ISOUtil.hex2byte(dump.replace(" ", ""))
        println(ISOUtil.hexdump(b))

//        val packagerInputStream: InputStream = context.assets.open("worldpay.xml")
//        val packager = GenericPackager("worldpay.xml")
        val m = ISOMsg()
        val logger = Logger()
        logger.addListener(SimpleLogListener(System.out))
        m.packager = packager
        packager.setLogger(logger, "debug")
        m.unpack(b)
        m.dump(System.out, "")

        val m1 = ISOMsg()
        m1.packager = packager
        m1.unpack(m.pack())
        m.dump(System.out, "")

        if (!b.contentEquals(m.pack())) {
            println("Pack/Unpack differs")
            println(ISOUtil.hexdump(b))
            println(ISOUtil.hexdump(m.pack()))
        }
    }

    private fun constructField0 (): ByteArray? {
        val bitset = BitSet()

        //Field 0.1, length - 3, Char, Conditional
        val header = ISOUtil.asciiToEbcdic("MA.")
        bitset.set(1)

        //Field 0.2, length - 4, Numeric, Mandatory
        val mti = ISOField(4, "0800")
        val mtiPackager = IFE_NUMERIC(4, "MTI")
        bitset.set(2)
        val packedMti = mtiPackager.pack(mti)

        //Field 0.3, length - 64, Binary, Mandatory
        val primaryBitmap = ISOField(64, "1000001000100000000000000000000000000000000000000000000000000000")
        val primaryBitmapPackager = IFE_BINARY(64, "MTI")
        bitset.set(3)
        val packedPrimaryBitmap = primaryBitmapPackager.pack(primaryBitmap)

        // If bit map is used, not used here
        val bitmap = ByteArray(3)
        System.arraycopy(ISOUtil.bitSet2byte(bitset), 0, bitmap, 0, 3)

        return isoUtils.concatenateByteArrays(header, packedMti, packedPrimaryBitmap)
    }
}