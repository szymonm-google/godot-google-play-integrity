/*************************************************************************/
/*  GodotGooglePlayIntegrity.java                                        */
/*************************************************************************/
/*                       This file is part of:                           */
/*                           GODOT ENGINE                                */
/*                      https://godotengine.org                          */
/*************************************************************************/
/* Copyright (c) 2007-2020 Juan Linietsky, Ariel Manzur.                 */
/* Copyright (c) 2014-2020 Godot Engine contributors (cf. AUTHORS.md).   */
/*                                                                       */
/* Permission is hereby granted, free of charge, to any person obtaining */
/* a copy of this software and associated documentation files (the       */
/* "Software"), to deal in the Software without restriction, including   */
/* without limitation the rights to use, copy, modify, merge, publish,   */
/* distribute, sublicense, and/or sell copies of the Software, and to    */
/* permit persons to whom the Software is furnished to do so, subject to */
/* the following conditions:                                             */
/*                                                                       */
/* The above copyright notice and this permission notice shall be        */
/* included in all copies or substantial portions of the Software.       */
/*                                                                       */
/* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,       */
/* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF    */
/* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.*/
/* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY  */
/* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  */
/* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE     */
/* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                */
/*************************************************************************/

package org.godotengine.godot.plugin.googleplayintegrity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import com.google.android.play.core.integrity.IntegrityTokenResponse;
import com.google.android.play.core.tasks.Task;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.Set;

public class GodotGooglePlayIntegrity extends GodotPlugin {

    private final String kLogTag = "godot_pia_demo";

    private IntegrityManager integrityManager;

    public GodotGooglePlayIntegrity(Godot godot) {
        super(godot);

        integrityManager = IntegrityManagerFactory.create(getActivity());
    }

    @UsedByGodot
    public String getString() {
        return "Google Play Integrity";
    }

    @UsedByGodot
    public void requestIntegrityToken(String nonce) {
        Log.d(kLogTag, "Requesting Play Integrity token. Nonce=" + nonce);

        Task<IntegrityTokenResponse> integrityTokenResponse =
                integrityManager
                        .requestIntegrityToken(
                                IntegrityTokenRequest.builder().setNonce(nonce).build());

        integrityTokenResponse.addOnCompleteListener(command -> {
            Log.d(kLogTag, "Token: " + command.getResult().token());
            emitSignal("request_completed", command.getResult().token());
        });
    }

    @Override
    public String getPluginName() {
        return "GodotGooglePlayIntegrity";
    }

    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = new ArraySet<>();

        signals.add(new SignalInfo("request_completed", String.class));

        return signals;
    }
}
