package com.high_mobility.HMLink;

import com.high_mobility.HMLink.Broadcasting.LocalDevice;
import com.high_mobility.btcore.HMBTCore;

import java.util.Random;

/**
 * Created by ttiganik on 26/05/16.
 */
public class Crypto {
    public static KeyPair createKeypair() {
        HMBTCore core = new HMBTCore();
        byte[] privateKey = new byte[32];
        byte[] publicKey = new byte[64];
        core.HMBTCoreCryptoCreateKeys(privateKey, publicKey);

        return new KeyPair(privateKey, publicKey);
    }

    public static byte[] createSerialNumber() {
        byte[] serialBytes = new byte[9];
        new Random().nextBytes(serialBytes);
        return serialBytes;
    }
}