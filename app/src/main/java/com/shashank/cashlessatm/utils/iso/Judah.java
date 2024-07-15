package com.shashank.cashlessatm.utils.iso;

///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 11
//DEPS org.jpos:jpos:2.1.10-SNAPSHOT,commons-codec:commons-codec:20041127.091804
//REPOS mavenCentral,jpos=https://jpos.org/maven
//SOURCES com/worldpay/eftpos/jpos/packager/*.java,com/worldpay/eftpos/common/eftpos/*.java

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;
import java.nio.ByteBuffer;


class PackagerDebug {
    private static final String dump = "08008220000000000000040000000000001004011333551333550160000ED2C5E82000000000000000F1F0F2";

    public static void main(String... args) throws Exception {
        byte[] b = ISOUtil.hex2byte(dump.replace(" ", ""));
        System.out.println (ISOUtil.hexdump(b));

        GenericPackager packager = new GenericPackager("worldpay.xml");
        ISOMsg m = new ISOMsg();
        Logger logger = new Logger();
        logger.addListener (new SimpleLogListener(System.out));
        m.setPackager (packager);
        packager.setLogger (logger, "debug");
        m.unpack (b);
        m.dump (System.out, "");

        ISOMsg m1 = new ISOMsg();
        m1.setPackager (packager);
        m1.unpack (m.pack());
        m.dump (System.out, "");

        if (!Arrays.equals(b, m.pack())) {
            System.out.println ("Pack/Unpack differs");
            System.out.println (ISOUtil.hexdump (b));
            System.out.println (ISOUtil.hexdump (m.pack()));
        }
    }
}
