package com.highmobility.hmkit;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.highmobility.btcore.HMDevice;
import com.highmobility.hmkit.Error.LinkError;
import com.highmobility.utils.ByteUtils;
import com.highmobility.value.Bytes;
import com.highmobility.value.DeviceSerial;

import java.util.Calendar;

import static com.highmobility.hmkit.Broadcaster.TAG;

/**
 * Created by ttiganik on 17/08/16.
 */
public class Link {
    BluetoothDevice btDevice;
    HMDevice hmDevice;
    DeviceSerial serial;

    public enum State {
        DISCONNECTED, CONNECTED, AUTHENTICATED
    }

    /**
     * CommandCallback is used to notify the user about the command result.
     */
    public interface CommandCallback {
        /**
         * Invoked when the command was successfully sent.
         */
        void onCommandSent();

        /**
         * Invoked when there was an issue with the command.
         *
         * @param error The command error.
         */
        void onCommandFailed(LinkError error);
    }

    State state = State.CONNECTED;
    SentCommand sentCommand;
    LinkListener listener;
    long connectionTime;

    Manager manager;

    Link(Manager manager, BluetoothDevice btDevice) {
        this.btDevice = btDevice;
        this.manager = manager;
        connectionTime = Calendar.getInstance().getTimeInMillis();
    }

    /**
     * @return The Links state.
     */
    public State getState() {
        return state;
    }

    void setState(State state) {
        if (this.state != state) {
            final State oldState = this.state;
            if (state == State.AUTHENTICATED && Manager.loggingLevel.getValue() >= Manager
                    .LoggingLevel.DEBUG.getValue()) {
                Log.d(TAG, "authenticated in " + (Calendar.getInstance().getTimeInMillis() -
                        connectionTime) + "ms");
            }

            this.state = state;

            if (listener != null) {
                final Link linkPointer = this;
                manager.postToMainThread(new Runnable() {
                    @Override public void run() {
                        if (linkPointer.listener == null) return;
                        linkPointer.listener.onStateChanged(linkPointer, oldState);
                    }
                });
            }
        }
    }

    /**
     * @return the name of the Link's bluetooth peripheral.
     */
    public String getName() {
        return btDevice.getName();
    }

    /**
     * @return Link's serial.
     */
    public DeviceSerial getSerial() {
        return serial;
    }

    /**
     * Send command to the Link.
     *
     * @param bytes    The command bytes that will be sent to the link.
     * @param callback A {@link CommandCallback} object that is invoked with the command result.
     */
    public void sendCommand(final Bytes bytes, CommandCallback callback) {
        if (bytes.getLength() > Constants.MAX_COMMAND_LENGTH) {
            LinkError error = new LinkError(LinkError.Type.COMMAND_TOO_BIG, 0,
                    "Command size is bigger than " + Constants.MAX_COMMAND_LENGTH + " bytes");
            callback.onCommandFailed(error);
            return;
        }

        if (state != State.AUTHENTICATED) {
            if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.ALL.getValue())
                Log.d(TAG, "not authenticated");
            callback.onCommandFailed(new LinkError(LinkError.Type.UNAUTHORIZED, 0, "not " +
                    "authenticated"));
            return;
        }

        if (sentCommand != null && sentCommand.finished == false) {
            if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.ALL.getValue())
                Log.d(TAG, "custom command in progress");

            callback.onCommandFailed(new LinkError(LinkError.Type.COMMAND_IN_PROGRESS, 0, "custom" +
                    " command in progress"));
            return;
        }

        if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue())
            Log.d(TAG, "send command " + bytes
                    + " to " + ByteUtils.hexFromBytes(hmDevice.getMac()));

        sentCommand = new SentCommand(callback, manager.mainHandler);

        manager.workHandler.post(new Runnable() {
            @Override
            public void run() {
                manager.core.HMBTCoreSendCustomCommand(manager.coreInterface, bytes.getByteArray
                        (), bytes.getLength(), getAddressBytes());
            }
        });
    }

    void setHmDevice(HMDevice hmDevice) {
        this.hmDevice = hmDevice;
        if (serial == null || serial.equals(hmDevice.getSerial()) == false) {
            serial = new DeviceSerial(hmDevice.getSerial());
        }

        if (hmDevice.getIsAuthenticated() == 0) {
            setState(State.CONNECTED);
        } else {
            setState(State.AUTHENTICATED);
        }
    }

    void onCommandReceived(final byte[] bytes) {
        if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue())
            Log.d(TAG, "did receive command " + ByteUtils.hexFromBytes(bytes)
                    + " from " + ByteUtils.hexFromBytes(hmDevice.getMac()));

        if (listener == null) {
            Log.d(TAG, "can't dispatch notification: no listener set");
            return;
        }

        manager.postToMainThread(new Runnable() {
            @Override public void run() {
                if (listener == null) return;
                listener.onCommandReceived(Link.this, new Bytes(bytes));
            }
        });
    }

    void onCommandResponseReceived(final byte[] data) {
        if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue())
            Log.d(TAG, "did receive command response " + ByteUtils.hexFromBytes(data)
                    + " from " + ByteUtils.hexFromBytes(hmDevice.getMac()) + " in " +
                    (Calendar.getInstance().getTimeInMillis() - sentCommand.commandStartTime) +
                    "ms");

        if (sentCommand == null) {
            if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue())
                Log.d(TAG, "can't dispatch command response: sentCommand = null");
            return;
        }

        sentCommand.dispatchResult(data);
    }

    byte[] getAddressBytes() {
        return ByteUtils.bytesFromMacString(btDevice.getAddress());
    }
}
