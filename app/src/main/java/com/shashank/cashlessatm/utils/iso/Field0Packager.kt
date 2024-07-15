package com.shashank.cashlessatm.utils.iso

import org.jpos.iso.IFA_LLCHAR
import org.jpos.iso.IFB_BITMAP
import org.jpos.iso.IFB_NUMERIC
import org.jpos.iso.ISOComponent
import org.jpos.iso.ISOMsg
import org.jpos.iso.ISOPackager
import org.jpos.iso.packager.GenericPackager
import java.io.InputStream

class Field0Packager : ISOPackager {

    private val subPackager = GenericPackager().apply {
        setFieldPackager(1, IFA_LLCHAR(3, "Terminal Application Header"))
        setFieldPackager(2, IFB_NUMERIC(4, "MTI", false))
        setFieldPackager(3, IFB_BITMAP(8, "Primary Bitmap"))
    }

    override fun pack(isoMsg: ISOComponent): ByteArray {
        return subPackager.pack(isoMsg)
    }

    override fun unpack(m: ISOComponent?, b: ByteArray?): Int {
        TODO("Not yet implemented")
    }

//    override fun unpack(isoMsg: ISOComponent, b: ByteArray) {
//        subPackager.unpack(isoMsg, b)
//    }

    override fun unpack(m: ISOComponent?, `in`: InputStream?) {
        TODO("Not yet implemented")
    }

    override fun getDescription(): String {
        TODO("Not yet implemented")
    }

    override fun getFieldDescription(m: ISOComponent?, fldNumber: Int): String {
        TODO("Not yet implemented")
    }

    override fun createISOMsg(): ISOMsg {
        TODO("Not yet implemented")
    }


}