package com.highmobility.hmkit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import com.highmobility.utils.Bytes;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ttiganik on 01/06/16.
 */
public class SharedBle {
    Context ctx;

    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;

    private ArrayList<SharedBleListener> listeners = new ArrayList<>();

    SharedBle(Context context) {
        this.ctx = context;
        createAdapter();
        context.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    public void addListener(SharedBleListener listener) {
        if (listeners.contains(listener) == false) listeners.add(listener);
    }

    public void removeListener(SharedBleListener listener) {
        listeners.remove(listener);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                for (SharedBleListener listener : listeners) {
                    listener.bluetoothChangedToAvailable(false);
                }
            } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {
                for (SharedBleListener listener : listeners) {
                    listener.bluetoothChangedToAvailable(true);
                }
            }
        }
        }
    };

    public BluetoothManager getManager() {
        return mBluetoothManager;
    }

    public BluetoothAdapter getAdapter() {
        return mBluetoothAdapter;
    }

    public boolean isBluetoothSupported() {
        return ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public boolean isBluetoothOn() {
        return (getAdapter() != null && getAdapter().isEnabled() && getAdapter().getState() == BluetoothAdapter.STATE_ON);
    }

    void setRandomAdapterName() {
        String name = "HM ";
        byte[] serialBytes = new byte[3];
        new Random().nextBytes(serialBytes);
        String randomBytesString = Bytes.hexFromBytes(serialBytes);
        name += randomBytesString.substring(1);
        getAdapter().setName(name);
    }

    void createAdapter() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) ctx.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            setRandomAdapterName();
        }
    }

    void terminate() {
        ctx.unregisterReceiver(receiver);
        listeners.clear();
        ctx = null;
    }
}
