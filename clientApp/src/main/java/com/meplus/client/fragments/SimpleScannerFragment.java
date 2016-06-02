package com.meplus.client.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meplus.client.events.ScannerEvent;
import com.meplus.events.EventUtils;
import com.meplus.fragments.BaseFragment;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerFragment extends BaseFragment implements ZBarScannerView.ResultHandler {
    private static final String TAG = SimpleScannerFragment.class.getSimpleName();
    private ZBarScannerView mScannerView;

    public static SimpleScannerFragment newInstance() {
        final SimpleScannerFragment fragment = new SimpleScannerFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZBarScannerView(getActivity());
        return mScannerView;
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
        final String content;
        content = rawResult.getContents();

        Log.i(TAG, content);
        EventUtils.postEvent(new ScannerEvent(content));
    }

    public void resumeScanner() {
        mScannerView.resumeCameraPreview(SimpleScannerFragment.this);
    }

}
