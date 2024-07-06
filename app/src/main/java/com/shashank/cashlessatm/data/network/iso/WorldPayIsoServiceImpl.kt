package com.shashank.cashlessatm.data.network.iso

import com.shashank.cashlessatm.data.network.tcpip.WorldPayChannel
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
import java.util.BitSet
import java.util.Date
import javax.inject.Inject

class WorldPayIsoServiceImpl @Inject constructor(
    private val channel: WorldPayChannel,
    private val packager: WorldPayPackager,
    private val sessionManager: SessionManager,
    private val isoUtils: IsoUtils
) : IsoService {

    override suspend fun downloadKeys(channel: Channel<IsoChannelMessage>): Boolean {
        downloadMasterKey(channel)
        return false
    }

    private fun downloadMasterKey(channel: Channel<IsoChannelMessage>) {
        val date = Date()
        val stan = isoUtils.getNextStan()

        val tmkRequest = ISOMsg()
        tmkRequest.packager = packager

        tmkRequest.apply {
            set("0.1", "MA.")
            set("0.2", "0800")
            set("0.3", "0800") // Primary Bitmap- To replace with calculated Bitmap after all fields are set
            set("1", "0000010000000000000000000000000000000000000000000000000000000000") // Secondary bitmap
            set("7", timeAndDateFormatter.format(date))
            set("11", stan)
            set("70", "301") //0301, 801
            // To set - Field 115 - Has subfields
        }


        tmkRequest.pack()


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