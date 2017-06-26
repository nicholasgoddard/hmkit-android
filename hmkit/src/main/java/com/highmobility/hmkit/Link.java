package com.highmobility.hmkit;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.highmobility.btcore.HMDevice;
import com.highmobility.hmkit.Error.LinkError;

import java.util.Calendar;

/**
 * Created by ttiganik on 17/08/16.
 */
public class Link {
    BluetoothDevice btDevice;
    HMDevice hmDevice;

    public enum State {
        DISCONNECTED, CONNECTED, AUTHENTICATED
    }

    public interface CommandCallback {
        void onCommandSent();
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
     *
     * @return the Link's state
     */
    public State getState() {
        return state;
    }

    void setState(State state) {
        if (this.state != state) {
            final State oldState = this.state;
            if (state == State.AUTHENTICATED && Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue()) {
                Log.d(Broadcaster.TAG, "authenticated in " + (Calendar.getInstance().getTimeInMillis() - connectionTime) + "ms");
            }

            this.state = state;

            if (listener != null) {
                final Link linkPointer = this;
                Runnable callback = new Runnable() {
                    @Override
                    public void run() {
                    linkPointer.listener.onStateChanged(linkPointer, oldState);
                    }
                };
                
                manager.mainHandler.post(callback);
            }
        }
    }

    /**
     *
     * @return the name of the Link's bluetooth peripheral
     */
    public String getName() {
        return btDevice.getName();
    }

    /**
     *
     * @return Link's serial
     */
    public byte[] getSerial() {
        return hmDevice != null ? hmDevice.getSerial() : null;
    }

    /**
     * Send command to the ConnectedLink inside a secure container.
     *
     * @param bytes       The bytes that will be sent inside the secure container.
     * @param callback    callback that is invoked with the command result
     *                    onCommandSent is invoked if command was sent successfully
     *                    onCommandFailed is invoked if something went wrong.
     */
    public void sendCommand(final byte[] bytes, CommandCallback callback) {
        if (state != State.AUTHENTICATED) {
            if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.ALL.getValue())
                Log.d(Broadcaster.TAG, "not authenticated");
            callback.onCommandFailed(new LinkError(LinkError.Type.UNAUTHORIZED, 0, "not authenticated"));
            return;
        }

        if (sentCommand != null && sentCommand.finished == false) {
            if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.ALL.getValue())
                Log.d(Broadcaster.TAG, "custom command in progress");

            callback.onCommandFailed(new LinkError(LinkError.Type.COMMAND_IN_PROGRESS, 0, "not authenticated"));
            return;
        }

        if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue())
            Log.d(Broadcaster.TAG, "send command " + ByteUtils.hexFromBytes(bytes)
                    + " to " + ByteUtils.hexFromBytes(hmDevice.getMac()));

        sentCommand = new SentCommand(callback, manager.mainHandler);

        manager.workHandler.post(new Runnable() {
            @Override
            public void run() {
                manager.core.HMBTCoreSendCustomCommand(manager.coreInterface, bytes, bytes.length, getAddressBytes());
            }
        });
    }

    void setHmDevice(HMDevice hmDevice) {
        this.hmDevice = hmDevice;

        if (hmDevice.getIsAuthenticated() == 0) {
            setState(State.CONNECTED);
        }
        else {
            setState(State.AUTHENTICATED);
        }
    }

    void onCommandReceived(final byte[] bytes) {
        if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue())
            Log.d(Broadcaster.TAG, "did receive command " + ByteUtils.hexFromBytes(bytes)
                    + " from " + ByteUtils.hexFromBytes(hmDevice.getMac()));

        if (listener == null) {
            Log.d(Broadcaster.TAG, "can't dispatch notification: no listener set");
            return;
        }
        manager.mainHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onCommandReceived(Link.this, bytes);
            }
        });
    }

    void onCommandResponseReceived(final byte[] data) {
        if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue())
            Log.d(Broadcaster.TAG, "did receive command response " + ByteUtils.hexFromBytes(data)
                    + " from " + ByteUtils.hexFromBytes(hmDevice.getMac()) + " in " +
                    (Calendar.getInstance().getTimeInMillis() - sentCommand.commandStartTime) + "ms");

        if (sentCommand == null) {
            if (Manager.loggingLevel.getValue() >= Manager.LoggingLevel.DEBUG.getValue())
                Log.d(Broadcaster.TAG, "can't dispatch command response: sentCommand = null");
            return;
        }

        sentCommand.dispatchResult(data);
    }

    byte[] getAddressBytes() {
        return ByteUtils.bytesFromMacString(btDevice.getAddress());
    }
}