package com.high_mobility.HMLink.Scanning;

import android.util.Log;

import com.high_mobility.HMLink.Broadcasting.LocalDevice;
import com.high_mobility.HMLink.Device;
import com.high_mobility.HMLink.Scanning.ExternalDeviceManager;
import com.high_mobility.btcore.HMBTCoreInterface;
import com.high_mobility.btcore.HMDevice;
import com.high_mobility.HMLink.LinkException;
import com.high_mobility.HMLink.AccessCertificate;

/**
 * Created by ttiganik on 03/06/16.
 */
public class BTCoreInterface implements HMBTCoreInterface {
    ExternalDeviceManager manager;

    BTCoreInterface(ExternalDeviceManager manager) {
        this.manager = manager;
    }

    @Override
    public int HMBTHalInit() {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMBTHalScanStart() {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());

        return 0;
    }

    @Override
    public int HMBTHalScanStop() {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());

        return 0;
    }

    @Override
    public int HMBTHalAdvertisementStart(byte[] issuer, byte[] appID) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMBTHalAdvertisementStop() {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMBTHalConnect(byte[] mac) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMBTHalDisconnect(byte[] mac) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMBTHalServiceDiscovery(byte[] mac) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMBTHalWriteData(byte[] mac, int length, byte[] data) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMBTHalReadData(byte[] mac, int offset) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMPersistenceHalgetSerial(byte[] serial) {
        copyBytesToJNI(manager.getSerialNumber(), serial);
        return 0;
    }

    @Override
    public int HMPersistenceHalgetLocalPublicKey(byte[] publicKey) {
        copyBytesToJNI(manager.getPublicKey(), publicKey);
        return 0;
    }

    @Override
    public int HMPersistenceHalgetLocalPrivateKey(byte[] privateKey) {
        copyBytesToJNI(manager.getPrivateKey(), privateKey);
        return 0;
    }

    @Override
    public int HMPersistenceHalgetDeviceCertificate(byte[] cert) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMPersistenceHaladdPublicKey(byte[] serial, byte[] publicKey, byte[] startDate, byte[] endDate, int commandSize, byte[] command) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMPersistenceHalgetPublicKey(byte[] serial, byte[] publicKey, byte[] startDate, byte[] endDate, int[] commandSize, byte[] command) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMPersistenceHalgetPublicKeyByIndex(int index, byte[] serial, byte[] publicKey, byte[] startDate, byte[] endDate, int[] commandSize, byte[] command) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 1;
    }

    @Override
    public int HMPersistenceHalgetPublicKeyCount(int[] count) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 0;
    }

    @Override
    public int HMPersistenceHalremovePublicKey(byte[] serial) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 1;
    }

    @Override
    public int HMPersistenceHaladdStoredCertificate(byte[] cert, int size) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 1;
    }

    @Override
    public int HMPersistenceHalgetStoredCertificate(byte[] serial, byte[] cert, int[] size) {
        return 0;
    }

    @Override
    public int HMPersistenceHaleraseStoredCertificate(byte[] serial) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 1;
    }

    @Override
    public void HMApiCallbackEnteredProximity(HMDevice device) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Override
    public void HMApiCallbackExitedProximity(HMDevice device) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Override
    public void HMApiCallbackCustomCommandIncoming(HMDevice device, byte[] data, int[] length, int[] error) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Override
    public void HMApiCallbackCustomCommandResponse(HMDevice device, byte[] data, int length) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Override
    public int HMApiCallbackGetDeviceCertificateFailed(HMDevice device, byte[] nonce) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        // TODO: Sensing: should ask for CA sig for the nonce
        // if ret false getting the sig start failed
        // if ret true started acquiring signature
        return 0;
    }

    @Override
    public int HMApiCallbackPairingRequested(HMDevice device) {
        Log.d(ExternalDeviceManager.TAG, new Object(){}.getClass().getEnclosingMethod().getName());
        return 1;
    }

    private void copyBytesToJNI(byte[] from, byte[] to) {
        for (int i = 0; i < from.length; i++) {
            to[i] = from[i];
        }
    }
}