package com.shashank.cashlessatm.utils.iso

import org.jpos.iso.ISOComponent
import org.jpos.iso.ISOException
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.GenericPackager
import java.io.InputStream
import java.util.BitSet
import javax.inject.Inject

class WorldPayPackager @Inject constructor(inputStream: InputStream): GenericPackager(inputStream) {

//    override fun pack(c: ISOComponent): ByteArray {
//        try {
//            if (c is ISOMsg) {
//                // Calculate the bitmap based on which fields are present
//                val bitset = BitSet(64)
//                for (i in 1..64) {
//                    if (c.hasField(i)) {
//                        bitset.set(i - 1)
//                    }
//                }
//
//                // Convert BitSet to byte array
//                var bitmapBytes = bitset.toByteArray()
//
//                // Ensure the bitmap is exactly 8 bytes
//                if (bitmapBytes.size < 8) {
//                    val paddedBitmap = ByteArray(8)
//                    System.arraycopy(bitmapBytes, 0, paddedBitmap, 0, bitmapBytes.size)
//                    bitmapBytes = paddedBitmap
//                }
//
//                // Ensure field 0 exists
//                if (!c.hasField(0)) {
//                    c.set(0, ISOMsg(0))
//                }
//
//                // Set the calculated bitmap in field 0.3
//                (c.getComponent(0) as ISOMsg).set(3, bitmapBytes)
//            }
//
//            return super.pack(c)
//        } catch (e: ISOException) {
//            println("Error packing ISO message: ${e.message}")
//            e.printStackTrace()
//            return ByteArray(0)
//        }
//    }
}