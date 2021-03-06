/*
 * The MIT License
 *
 * Copyright (c) 2014- High-Mobility GmbH (https://high-mobility.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.highmobility.hmkit;

import android.bluetooth.BluetoothDevice;

import java.util.Calendar;

import javax.annotation.Nullable;

import static com.highmobility.hmkit.HMLog.d;
import static com.highmobility.hmkit.HMLog.e;

/**
 * The ConnectedLink is a representation of the connection between the Broadcaster and a Device that
 * has connected to it. The ConnectedLink is created by the other Device via discovering and
 * connecting to the Broadcaster.
 * <p>
 * The ConnectedLink inherits from Link, which provides the ability to send and receive commands.
 * ConnectedLink is used to provide the authorization callbacks.
 */
public class ConnectedLink extends Link {
    ConnectedLink(Core core, ThreadManager threadManager, BluetoothDevice btDevice) {
        super(core, threadManager, btDevice);
    }

    /**
     * In order to receive ConnectedLink events, a listener must be set.
     *
     * @param listener The listener instance to receive ConnectedLink events.
     */
    public void setListener(@Nullable ConnectedLinkListener listener) {
        this.listener = listener;
    }

    private int pairingResponse = -1;

    int didReceivePairingRequest() {
        if (listener == null) {
            e("link listener not set");
            return 1;
        }

        final ConnectedLink reference = this;
        pairingResponse = -1;

        threadManager.postToMain(new Runnable() {
            @Override public void run() {
                if (listener == null) {
                    pairingResponse = 1;
                    return;
                }

                ((ConnectedLinkListener) listener).onAuthenticationRequest(reference, new
                        ConnectedLinkListener.AuthenticationRequestCallback() {
                            @Override
                            public void approve() {
                                pairingResponse = 0;
                            }

                            @Override
                            public void decline() {
                                pairingResponse = 1;
                            }
                        });
            }
        });

        Calendar c = Calendar.getInstance();
        int startSeconds = c.get(Calendar.SECOND);

        while (pairingResponse < 0) {
            int passedSeconds = Calendar.getInstance().get(Calendar.SECOND);
            if (passedSeconds - startSeconds > Constants.registerTimeout) {
                if (listener != null) {
                    threadManager.postToMain(new Runnable() {
                        @Override public void run() {
                            if (listener == null) return;
                            ((ConnectedLinkListener) listener).onAuthenticationRequestTimeout(reference);
                        }
                    });

                    d("pairing timer exceeded");
                    return 1; // TOD1O: use correct code
                }
            }
        }

        return pairingResponse;
    }
}
