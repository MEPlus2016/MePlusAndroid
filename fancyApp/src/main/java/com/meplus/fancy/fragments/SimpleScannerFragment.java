package com.meplus.fancy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.meplus.fancy.events.ScannerEvent;

import org.greenrobot.eventbus.EventBus;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerFragment extends BaseFragment implements ZXingScannerView.ResultHandler {
    private static final String TAG = SimpleScannerFragment.class.getSimpleName();
    private ZXingScannerView mScannerView;

    public static SimpleScannerFragment newInstance() {
        final SimpleScannerFragment fragment = new SimpleScannerFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        final String content = rawResult.getText();
        Log.i(TAG, content);
        final ScannerEvent scannerEvent = new ScannerEvent(content);
        scannerEvent.setType(ScannerEvent.TYPE_CAMERA);
        EventBus.getDefault().post(scannerEvent);
    }

    public void resumeScanner() {
        mScannerView.resumeCameraPreview(SimpleScannerFragment.this);
    }

}
