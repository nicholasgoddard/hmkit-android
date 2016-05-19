
#include "hm_api_callback.h"
#include "hm_cert.h"
#include <string.h>
#include "hm_config.h"
#include "hmbtcore.h"
#include "hm_api.h"
#include "hm_bt_debug_hal.h"

void hm_api_callback_init()
{

}

void hm_api_callback_ping()
{

}

void hm_api_callback_authorised_device_added(hm_device_t *device, uint8_t error)
{

}

void hm_api_callback_authorised_device_updated(hm_device_t *device, uint8_t error)
{

}

void hm_api_callback_entered_proximity(hm_device_t *device)
{
    hm_bt_debug_hal_log("ENTERED PROXIMITY");
    hm_bt_debug_hal_log_hex(device->mac,6);
    hm_bt_debug_hal_log_hex(device->serial_number,9);
    hm_bt_debug_hal_log_hex(device->app_id,12);

    jclass cls = (*envRef)->FindClass(envRef, "com/high_mobility/btcore/HMDevice");
    jmethodID constructor = (*envRef)->GetMethodID(envRef,cls, "<init>", "()V");
    jmethodID setMac = (*envRef)->GetMethodID(envRef,cls, "setMac", "([B)V");
    jmethodID setSerial = (*envRef)->GetMethodID(envRef,cls, "setSerial", "([B)V");
    jmethodID setIsAuthenticated = (*envRef)->GetMethodID(envRef,cls, "setIsAuthenticated", "(I)V");
    jmethodID setAppId = (*envRef)->GetMethodID(envRef,cls, "setAppId", "([B)V");

    jobject obj = (*envRef)->NewObject(envRef,cls, constructor);

    jbyteArray mac_ = (*envRef)->NewByteArray(envRef,6);
    (*envRef)->SetByteArrayRegion(envRef, mac_, 0, 6, (const jint*) device->mac );

    jbyteArray serial_ = (*envRef)->NewByteArray(envRef,9);
    (*envRef)->SetByteArrayRegion(envRef, serial_, 0, 9, (const jint*) device->serial_number );

    jbyteArray appid_ = (*envRef)->NewByteArray(envRef,12);
    (*envRef)->SetByteArrayRegion(envRef, appid_, 0, 12, (const jint*) device->app_id );

    (*envRef)->CallVoidMethod(envRef, obj, setMac, mac_);
    (*envRef)->CallVoidMethod(envRef, obj, setSerial, serial_);
    (*envRef)->CallVoidMethod(envRef, obj, setIsAuthenticated, device->is_authorised);
    (*envRef)->CallVoidMethod(envRef, obj, setAppId, appid_);


    return (*envRef)->CallVoidMethod(envRef, coreInterfaceRef, interfaceMethodHMApiCallbackEnteredProximity, obj);
}

void hm_api_callback_proximity_measured(hm_device_t *device, uint8_t receiver_count, hm_receiver_t *receivers)
{

}

void hm_api_callback_exited_proximity(hm_device_t *device)
{
    hm_bt_debug_hal_log("EXITED PROXIMITY");
    hm_bt_debug_hal_log_hex(device->mac,6);
    hm_bt_debug_hal_log_hex(device->serial_number,9);
    hm_bt_debug_hal_log_hex(device->app_id,12);

    jclass cls = (*envRef)->FindClass(envRef, "com/high_mobility/btcore/HMDevice");
    jmethodID constructor = (*envRef)->GetMethodID(envRef,cls, "<init>", "()V");
    jmethodID setMac = (*envRef)->GetMethodID(envRef,cls, "setMac", "([B)V");
    jmethodID setSerial = (*envRef)->GetMethodID(envRef,cls, "setSerial", "([B)V");
    jmethodID setIsAuthenticated = (*envRef)->GetMethodID(envRef,cls, "setIsAuthenticated", "(I)V");
    jmethodID setAppId = (*envRef)->GetMethodID(envRef,cls, "setAppId", "([B)V");

    jobject obj = (*envRef)->NewObject(envRef,cls, constructor);

    jbyteArray mac_ = (*envRef)->NewByteArray(envRef,6);
    (*envRef)->SetByteArrayRegion(envRef, mac_, 0, 6, (const jint*) device->mac );

    jbyteArray serial_ = (*envRef)->NewByteArray(envRef,9);
    (*envRef)->SetByteArrayRegion(envRef, serial_, 0, 9, (const jint*) device->serial_number );

    jbyteArray appid_ = (*envRef)->NewByteArray(envRef,12);
    (*envRef)->SetByteArrayRegion(envRef, appid_, 0, 12, (const jint*) device->app_id );

    (*envRef)->CallVoidMethod(envRef, obj, setMac, mac_);
    (*envRef)->CallVoidMethod(envRef, obj, setSerial, serial_);
    (*envRef)->CallVoidMethod(envRef, obj, setIsAuthenticated, device->is_authorised);
    (*envRef)->CallVoidMethod(envRef, obj, setAppId, appid_);

    return (*envRef)->CallVoidMethod(envRef, coreInterfaceRef, interfaceMethodHMApiCallbackExitedProximity, obj);
}

void hm_api_callback_command_incoming(hm_device_t *device, uint8_t *data, uint16_t *length, uint8_t *error)
{
    jclass cls = (*envRef)->FindClass(envRef, "com/high_mobility/btcore/HMDevice");
    jmethodID constructor = (*envRef)->GetMethodID(envRef,cls, "<init>", "()V");
    jmethodID setMac = (*envRef)->GetMethodID(envRef,cls, "setMac", "([B)V");
    jmethodID setSerial = (*envRef)->GetMethodID(envRef,cls, "setSerial", "([B)V");
    jmethodID setIsAuthenticated = (*envRef)->GetMethodID(envRef,cls, "setIsAuthenticated", "(I)V");
    jmethodID setAppId = (*envRef)->GetMethodID(envRef,cls, "setAppId", "([B)V");

    jobject obj = (*envRef)->NewObject(envRef,cls, constructor);

    jbyteArray mac_ = (*envRef)->NewByteArray(envRef,6);
    (*envRef)->SetByteArrayRegion(envRef, mac_, 0, 6, (const jint*) device->mac );

    jbyteArray serial_ = (*envRef)->NewByteArray(envRef,9);
    (*envRef)->SetByteArrayRegion(envRef, serial_, 0, 9, (const jint*) device->serial_number );

    jbyteArray appid_ = (*envRef)->NewByteArray(envRef,12);
    (*envRef)->SetByteArrayRegion(envRef, appid_, 0, 12, (const jint*) device->app_id );

    (*envRef)->CallVoidMethod(envRef, obj, setMac, mac_);
    (*envRef)->CallVoidMethod(envRef, obj, setSerial, serial_);
    (*envRef)->CallVoidMethod(envRef, obj, setIsAuthenticated, device->is_authorised);
    (*envRef)->CallVoidMethod(envRef, obj, setAppId, appid_);

    jbyteArray data_ = (*envRef)->NewByteArray(envRef,255);
    (*envRef)->SetByteArrayRegion(envRef, data_, 0, *length, (const jbyte*) data );

    jintArray length_ = (*envRef)->NewIntArray(envRef,1);
    (*envRef)->SetIntArrayRegion(envRef, length_, 0, 1, (const jint*) length );

    jintArray error_ = (*envRef)->NewIntArray(envRef,1);
    (*envRef)->SetIntArrayRegion(envRef, error_, 0, 1, (const jint*) error );

    (*envRef)->CallVoidMethod(envRef, coreInterfaceRef, interfaceMethodHMApiCallbackCustomCommandIncoming, obj,data_,length_,error_);

    jint* length_array = (*envRef)->GetIntArrayElements(envRef, length_, NULL);
    *length = length_array[0];

    jint* error_array = (*envRef)->GetIntArrayElements(envRef, error_, NULL);
    *error = error_array[0];

    jbyte* data_array = (*envRef)->GetByteArrayElements(envRef, data_, NULL);
    memcpy(data,data_array,*length);
}

void hm_api_callback_command_response(hm_device_t *device, uint8_t *data, uint16_t length)
{
    jclass cls = (*envRef)->FindClass(envRef, "com/high_mobility/btcore/HMDevice");
    jmethodID constructor = (*envRef)->GetMethodID(envRef,cls, "<init>", "()V");
    jmethodID setMac = (*envRef)->GetMethodID(envRef,cls, "setMac", "([B)V");
    jmethodID setSerial = (*envRef)->GetMethodID(envRef,cls, "setSerial", "([B)V");
    jmethodID setIsAuthenticated = (*envRef)->GetMethodID(envRef,cls, "setIsAuthenticated", "(I)V");
    jmethodID setAppId = (*envRef)->GetMethodID(envRef,cls, "setAppId", "([B)V");

    jobject obj = (*envRef)->NewObject(envRef,cls, constructor);

    jbyteArray mac_ = (*envRef)->NewByteArray(envRef,6);
    (*envRef)->SetByteArrayRegion(envRef, mac_, 0, 6, (const jint*) device->mac );

    jbyteArray serial_ = (*envRef)->NewByteArray(envRef,9);
    (*envRef)->SetByteArrayRegion(envRef, serial_, 0, 9, (const jint*) device->serial_number );

    jbyteArray appid_ = (*envRef)->NewByteArray(envRef,12);
    (*envRef)->SetByteArrayRegion(envRef, appid_, 0, 12, (const jint*) device->app_id );

    (*envRef)->CallVoidMethod(envRef, obj, setMac, mac_);
    (*envRef)->CallVoidMethod(envRef, obj, setSerial, serial_);
    (*envRef)->CallVoidMethod(envRef, obj, setIsAuthenticated, device->is_authorised);
    (*envRef)->CallVoidMethod(envRef, obj, setAppId, appid_);

    jbyteArray data_ = (*envRef)->NewByteArray(envRef,255);
    (*envRef)->SetByteArrayRegion(envRef, data_, 0, length, (const jbyte*) data );

    (*envRef)->CallVoidMethod(envRef, coreInterfaceRef, interfaceMethodHMApiCallbackCustomCommandResponse, obj,data_,length);
}

uint32_t hm_api_callback_get_device_certificate_failed(hm_device_t *device, uint8_t *nonce)
{
    jclass cls = (*envRef)->FindClass(envRef, "com/high_mobility/btcore/HMDevice");
    jmethodID constructor = (*envRef)->GetMethodID(envRef,cls, "<init>", "()V");
    jmethodID setMac = (*envRef)->GetMethodID(envRef,cls, "setMac", "([B)V");
    jmethodID setSerial = (*envRef)->GetMethodID(envRef,cls, "setSerial", "([B)V");
    jmethodID setIsAuthenticated = (*envRef)->GetMethodID(envRef,cls, "setIsAuthenticated", "(I)V");
    jmethodID setAppId = (*envRef)->GetMethodID(envRef,cls, "setAppId", "([B)V");

    jobject obj = (*envRef)->NewObject(envRef,cls, constructor);

    jbyteArray mac_ = (*envRef)->NewByteArray(envRef,6);
    (*envRef)->SetByteArrayRegion(envRef, mac_, 0, 6, (const jint*) device->mac );

    jbyteArray serial_ = (*envRef)->NewByteArray(envRef,9);
    (*envRef)->SetByteArrayRegion(envRef, serial_, 0, 9, (const jint*) device->serial_number );

    jbyteArray appid_ = (*envRef)->NewByteArray(envRef,12);
    (*envRef)->SetByteArrayRegion(envRef, appid_, 0, 12, (const jint*) device->app_id );

    (*envRef)->CallVoidMethod(envRef, obj, setMac, mac_);
    (*envRef)->CallVoidMethod(envRef, obj, setSerial, serial_);
    (*envRef)->CallVoidMethod(envRef, obj, setIsAuthenticated, device->is_authorised);
    (*envRef)->CallVoidMethod(envRef, obj, setAppId, appid_);

    jbyteArray nonce_ = (*envRef)->NewByteArray(envRef,9);
    (*envRef)->SetByteArrayRegion(envRef, nonce_, 0, 9, (const jbyte*) nonce );

    jint ret = (*envRef)->CallIntMethod(envRef, coreInterfaceRef, interfaceMethodHMApiCallbackGetDeviceCertificateFailed, obj,nonce_);

    jbyte* nonce_array = (*envRef)->GetByteArrayElements(envRef, nonce_, NULL);
    memcpy(nonce,nonce_array,9);

    return ret;
}

void hm_api_callback_access_certificate_registered(hm_device_t *device, uint8_t *public_key, uint8_t error)
{

}

uint32_t hm_api_callback_pairing_requested(hm_device_t *device){

    jclass cls = (*envRef)->FindClass(envRef, "com/high_mobility/btcore/HMDevice");
    jmethodID constructor = (*envRef)->GetMethodID(envRef,cls, "<init>", "()V");
    jmethodID setMac = (*envRef)->GetMethodID(envRef,cls, "setMac", "([B)V");
    jmethodID setSerial = (*envRef)->GetMethodID(envRef,cls, "setSerial", "([B)V");
    jmethodID setIsAuthenticated = (*envRef)->GetMethodID(envRef,cls, "setIsAuthenticated", "(I)V");
    jmethodID setAppId = (*envRef)->GetMethodID(envRef,cls, "setAppId", "([B)V");

    jobject obj = (*envRef)->NewObject(envRef,cls, constructor);

    jbyteArray mac_ = (*envRef)->NewByteArray(envRef,6);
    (*envRef)->SetByteArrayRegion(envRef, mac_, 0, 6, (const jint*) device->mac );

    jbyteArray serial_ = (*envRef)->NewByteArray(envRef,9);
    (*envRef)->SetByteArrayRegion(envRef, serial_, 0, 9, (const jint*) device->serial_number );

    jbyteArray appid_ = (*envRef)->NewByteArray(envRef,12);
    (*envRef)->SetByteArrayRegion(envRef, appid_, 0, 12, (const jint*) device->app_id );

    (*envRef)->CallVoidMethod(envRef, obj, setMac, mac_);
    (*envRef)->CallVoidMethod(envRef, obj, setSerial, serial_);
    (*envRef)->CallVoidMethod(envRef, obj, setIsAuthenticated, device->is_authorised);
    (*envRef)->CallVoidMethod(envRef, obj, setAppId, appid_);

    jint ret = (*envRef)->CallIntMethod(envRef, coreInterfaceRef, interfaceMethodHMApiCallbackPairingRequested, obj);

    return ret;
}