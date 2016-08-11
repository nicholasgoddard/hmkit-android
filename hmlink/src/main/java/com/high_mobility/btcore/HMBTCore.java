package com.high_mobility.btcore;

/**
 * Created by ttiganik on 13/04/16.
 */
public class HMBTCore {

    static {
        System.loadLibrary("hmbtcore");
    }

    //Init core
    //interface is class reference what implements HMBTCoreInterface
    //TT
    public native void HMBTCoreInit(HMBTCoreInterface coreInterface);
    //Send clock beat to core
    public native void HMBTCoreClock(HMBTCoreInterface coreInterface);

    //CORE SENSING

    public native void HMBTCoreSensingReadNotification(HMBTCoreInterface coreInterface, byte[] mac);
    public native void HMBTCoreSensingReadResponse(HMBTCoreInterface coreInterface, byte[] data, int size, int offset, byte[] mac);

    public native void HMBTCoreSensingWriteResponse(HMBTCoreInterface coreInterface, byte[] mac);

    public native void HMBTCoreSensingPingNotification(HMBTCoreInterface coreInterface, byte[] mac);

    public native void HMBTCoreSensingProcessAdvertisement(HMBTCoreInterface coreInterface, byte[] mac, byte[] data, int size);
    public native void HMBTCoreSensingDiscoveryEvent(HMBTCoreInterface coreInterface, byte[] mac);
    public native void HMBTCoreSensingScanStart(HMBTCoreInterface coreInterface);

    public native void HMBTCoreSensingConnect(HMBTCoreInterface coreInterface, byte[] mac);
    public native void HMBTCoreSensingDisconnect(HMBTCoreInterface coreInterface, byte[] mac);

    //CORE LINK

    //Initialize link object in core
    //TT
    public native void HMBTCorelinkConnect(HMBTCoreInterface coreInterface, byte[] mac);
    //Delete link object in core
    //TT
    public native void HMBTCorelinkDisconnect(HMBTCoreInterface coreInterface, byte[] mac);

    //Forward link incoming data to core
    //TT
    public native void HMBTCorelinkIncomingData(HMBTCoreInterface coreInterface, byte[] data, int size, byte[] mac);

    public native void HMBTCoreSendCustomCommand(HMBTCoreInterface coreInterface, byte[] data, int size, byte[] mac);

    public native void HMBTCoreSendReadDeviceCertificate(HMBTCoreInterface coreInterface, byte[] mac, byte[] nonce, byte[] caSignature);

    //Crypto
    public native void HMBTCoreCryptoCreateKeys(byte[] privateKey, byte[] publicKey);

    public native void HMBTCoreCryptoAddSignature(byte[] data, int size, byte[] privateKey, byte[] signature);
}
