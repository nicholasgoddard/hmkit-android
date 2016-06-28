package com.high_mobility.HMLink.Broadcasting;

import android.util.Log;

import com.high_mobility.HMLink.Device;
import com.high_mobility.HMLink.Scanning.ExternalDeviceManager;
import com.high_mobility.btcore.HMBTCoreInterface;
import com.high_mobility.btcore.HMDevice;
import com.high_mobility.HMLink.LinkException;
import com.high_mobility.HMLink.AccessCertificate;

import java.util.Arrays;

/**
 * Created by ttiganik on 20/04/16.
 */
public class BTCoreInterface implements HMBTCoreInterface {
    LocalDevice device;

    BTCoreInterface(LocalDevice device) {
        this.device = device;
    }

    @Override
    public int HMBTHalInit() {
        return 0;
    }

    @Override
    public int HMBTHalScanStart() {
        return 0;
    }

    @Override
    public int HMBTHalScanStop() {
        return 0;
    }

    @Override
    public int HMBTHalAdvertisementStart(byte[] issuer, byte[] appID) {
        return 0;
    }

    @Override
    public int HMBTHalAdvertisementStop() {
//        device.stopBroadcasting(); // stop broadcasting disconnects all the current connections and is not usable atm
        return 0;
    }

    @Override
    public int HMBTHalConnect(byte[] mac) {
        return 0;
    }

    @Override
    public int HMBTHalDisconnect(byte[] mac) {
        return 0;
    }

    @Override
    public int HMBTHalServiceDiscovery(byte[] mac) {
        return 0;
    }

    @Override
    public int HMBTHalWriteData(byte[] mac, int length, byte[] data) {
        device.writeData(mac, data);
        return 0;
    }

    @Override
    public int HMBTHalReadData(byte[] mac, int offset) {
        return 0;
    }

    @Override
    public int HMPersistenceHalgetSerial(byte[] serial) {
        copyBytesToJNI(device.getCertificate().getSerial(), serial);
        return 0;
    }

    @Override
    public int HMPersistenceHalgetLocalPublicKey(byte[] publicKey) {
        copyBytesToJNI(device.getCertificate().getPublicKey(), publicKey);
        return 0;
    }

    @Override
    public int HMPersistenceHalgetLocalPrivateKey(byte[] privateKey) {
        copyBytesToJNI(device.privateKey, privateKey);
        return 0;
    }

    @Override
    public int HMPersistenceHalgetDeviceCertificate(byte[] cert) {
        copyBytesToJNI(device.getCertificate().getBytes(), cert);
        return 0;
    }

    @Override
    public int HMPersistenceHaladdPublicKey(byte[] serial, byte[] publicKey, byte[] startDate, byte[] endDate, int commandSize, byte[] command) {
        AccessCertificate cert = new AccessCertificate(serial, publicKey, device.getCertificate().getSerial(), startDate, endDate, command);

        try {
            device.storage.storeCertificate(cert);
        } catch (LinkException e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    @Override
    public int HMPersistenceHalgetPublicKey(byte[] serial, byte[] publicKey, byte[] startDate, byte[] endDate, int[] commandSize, byte[] command) {
        AccessCertificate certificate = device.storage.certWithGainingSerial(serial);
        if (certificate == null) {
            return 1;
        }
        copyBytesToJNI(certificate.getGainerPublicKey(), publicKey);
        copyBytesToJNI(certificate.getStartDateBytes(), startDate);
        copyBytesToJNI(certificate.getEndDateBytes(), endDate);
        byte[] permissions = certificate.getPermissions();
        copyBytesToJNI(permissions, command);
        commandSize[0] = permissions.length;

        return 0;
    }

    @Override
    public int HMPersistenceHalgetPublicKeyByIndex(int index, byte[] serial, byte[] publicKey, byte[] startDate, byte[] endDate, int[] commandSize, byte[] command) {
        AccessCertificate[] certificates = device.storage.getCertificatesWithProvidingSerial(device.getCertificate().getSerial());

        if (certificates.length >= index) {
            AccessCertificate certificate = certificates[index];
            copyBytesToJNI(certificate.getGainerPublicKey(), publicKey);
            copyBytesToJNI(certificate.getStartDateBytes(), startDate);
            copyBytesToJNI(certificate.getEndDateBytes(), endDate);
            byte[] permissions = certificate.getPermissions();
            copyBytesToJNI(permissions, command);
            commandSize[0] = permissions.length;

            return 0;
        }

        return 1;
    }

    @Override
    public int HMPersistenceHalgetPublicKeyCount(int[] count) {
        count[0] = device.storage.getCertificatesWithProvidingSerial(device.getCertificate().getSerial()).length;

        return 0;
    }

    @Override
    public int HMPersistenceHalremovePublicKey(byte[] serial) {
        if (device.storage.deleteCertificateWithGainingSerial(serial)) return 0;
        else return 1;
    }

    @Override
    public int HMPersistenceHaladdStoredCertificate(byte[] cert, int size) {
        AccessCertificate certificate = new AccessCertificate(cert);
        try {
            device.storage.storeCertificate(certificate);
        } catch (LinkException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    @Override
    public int HMPersistenceHalgetStoredCertificate(byte[] serial, byte[] cert, int[] size) {
        AccessCertificate[] storedCerts = device.storage.getCertificatesWithoutProvidingSerial(device.getCertificate().getSerial());

        for (AccessCertificate storedCert : storedCerts) {
            if (Arrays.equals(storedCert.getProviderSerial(), serial)) {
                copyBytesToJNI(storedCert.getBytes(), cert);
                size[0] = storedCert.getBytes().length;
                if (Device.loggingLevel.getValue() >= Device.LoggingLevel.All.getValue())
                    Log.d(LocalDevice.TAG, "Returned stored cert for serial " + ByteUtils.hexFromBytes(serial));
                return 0;
            }
        }

        if (Device.loggingLevel.getValue() >= Device.LoggingLevel.All.getValue())
            Log.d(LocalDevice.TAG, "No stored cert for serial" + ByteUtils.hexFromBytes(serial));

        return 1;
    }

    @Override
    public int HMPersistenceHaleraseStoredCertificate(byte[] serial) {
        AccessCertificate[] storedCerts = device.storage.getCertificatesWithoutProvidingSerial(device.getCertificate().getSerial());

        for (AccessCertificate cert : storedCerts) {
            if (Arrays.equals(cert.getProviderSerial(), serial)) {
                if (device.storage.deleteCertificate(cert)) {
                    if (Device.loggingLevel.getValue() >= Device.LoggingLevel.All.getValue())
                        Log.d(LocalDevice.TAG, "Erased stored cert for serial " + ByteUtils.hexFromBytes(serial));

                    return 0;
                }
                else {
                    if (Device.loggingLevel.getValue() >= Device.LoggingLevel.All.getValue())
                        Log.d(LocalDevice.TAG, "Could not erase cert for serial " + ByteUtils.hexFromBytes(serial));
                    return 1;
                }
            }
        }
        if (Device.loggingLevel.getValue() >= Device.LoggingLevel.All.getValue())
            Log.d(LocalDevice.TAG, "No cert to erase for serial " + ByteUtils.hexFromBytes(serial));

        return 1;
    }

    @Override
    public void HMApiCallbackEnteredProximity(HMDevice device) {
        // this means core has finished identification of the device (might me authenticated or not) - show device info on screen
        // always update the device with this, auth state might have changed later with this callback as well
        this.device.didResolveDevice(device);
    }

    @Override
    public void HMApiCallbackExitedProximity(HMDevice device) {
        if (Device.loggingLevel.getValue() >= Device.LoggingLevel.All.getValue())
            Log.d(LocalDevice.TAG, "HMCtwExitedProximity");
        this.device.didLoseLink(device);
    }

    @Override
    public void HMApiCallbackCustomCommandIncoming(HMDevice device, byte[] data, int[] length, int[] error) {
        byte[] cutBytes = new byte[length[0]];

        for (int i = 0; i < length[0]; i++) {
            cutBytes[i] = data[i];
        }

        byte[] response = this.device.onCommandReceived(device, cutBytes);
        if (response != null) {
            copyBytesToJNI(response, data);
            length[0] = response.length;
            error[0] = 0;
        }
        else {
            length[0] = 0;
            error[0] = 0;
        }
    }

    @Override
    public void HMApiCallbackCustomCommandResponse(HMDevice device, byte[] data, int length) {
        byte[] cutBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            cutBytes[i] = data[i];
        }

        this.device.onCommandResponseReceived(device, cutBytes);
    }

    @Override
    public int HMApiCallbackGetDeviceCertificateFailed(HMDevice device, byte[] nonce) {
        // Sensing: should ask for CA sig for the nonce
        // if ret false getting the sig start failed
        // if ret true started acquiring signature
        return 0;
    }

    @Override
    public int HMApiCallbackPairingRequested(HMDevice device) {
        int response = this.device.didReceivePairingRequest(device);
        return response;
    }

    private void copyBytesToJNI(byte[] from, byte[] to) {
        for (int i = 0; i < from.length; i++) {
            to[i] = from[i];
        }
    }
}