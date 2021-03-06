/*
 * Aurora Droid
 * Copyright (C) 2019, Rahul Kumar Patel <whyorean@gmail.com>
 *
 * Aurora Droid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aurora Droid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aurora Droid.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aurora.adroid.activity;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.aurora.adroid.manager.RepoListManager;
import com.aurora.adroid.model.Repo;
import com.aurora.adroid.util.Log;
import com.aurora.adroid.util.Util;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        checkPermissions();
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        mScannerView.stopCameraPreview();
        mScannerView.stopCamera();
        if (rawResult.getText().contains("fingerprint") || rawResult.getText().contains("FINGERPRINT")) {
            try {
                String[] ss = rawResult.getText().split("\\?");
                Repo repo = new Repo();
                repo.setRepoName(Util.getDomainName(ss[0]));
                repo.setRepoId(String.valueOf(repo.getRepoName().hashCode()));
                repo.setRepoUrl(ss[0]);
                ss[1] = ss[1].replace("fingerprint=", "");
                ss[1] = ss[1].replace("FINGERPRINT=", "");
                repo.setRepoFingerprint(ss[1]);
                RepoListManager.addRepoToCustomList(this, repo);
                Toast.makeText(this, "Repo Added Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to add repo", Toast.LENGTH_SHORT).show();
                Log.d(e.getMessage());
            }
        } else {
            Toast.makeText(this, "Unsupported QR", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void checkPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA
                },
                1337);
    }
}
