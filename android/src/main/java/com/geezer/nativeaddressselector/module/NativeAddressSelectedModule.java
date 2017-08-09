package com.geezer.nativeaddressselector.module;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.geezer.nativeaddressselector.BottomDialog;
import com.geezer.nativeaddressselector.OnAddressSelectedListener;
import com.geezer.nativeaddressselector.model.AllAreas;
import com.geezer.nativeaddressselector.model.GeneralData;
import com.geezer.nativeaddressselector.util.GsonUtils;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geezer. on 2017/8/4.
 */

public class NativeAddressSelectedModule extends ReactContextBaseJavaModule {

    private static final String REACT_CLASS = "NativeAddressSelected";
    private static final String ON_ADDRESS_SELECTED_LISTENER = "OnAddressSelectedListener";
    private OnAddressSelectedListener mAddressSelectedListener;
    private BottomDialog mDialog;

    public NativeAddressSelectedModule(ReactApplicationContext reactContext) {
        super(reactContext);
        initAddressSelectedListener();
    }

    private void initAddressSelectedListener() {

        if (mAddressSelectedListener != null) return;

        mAddressSelectedListener = new OnAddressSelectedListener() {
            @Override
            public void onAddressSelected(GeneralData province, GeneralData city, GeneralData county, GeneralData street) {
                ArrayList<GeneralData> generalDatas = new ArrayList<>();

                if (province != null) {
                    generalDatas.add(province);
                }

                if (city != null) {
                    generalDatas.add(city);
                }

                if (county != null) {
                    generalDatas.add(county);
                }

                if (street != null) {
                    generalDatas.add(street);
                }

                getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit(ON_ADDRESS_SELECTED_LISTENER, GsonUtils.toJsonString(generalDatas));

                mDialog.dismiss();
            }
        };

    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void init(String areas, String data) {
        initDialog();
        try {
            initAddressProvider(areas);
            resetAddres(data);
        } catch (JsonParseException | NullPointerException | ClassCastException e) {
            Log.e(NativeAddressSelectedModule.class.getSimpleName(), e.getMessage());
        }
    }

    @ReactMethod
    public void show() {
        if (mDialog != null) {
            mDialog.setOnAddressSelectedListener(mAddressSelectedListener);
            mDialog.show();
        }
    }


    private void initDialog() {
        mDialog = new BottomDialog(getCurrentActivity());
    }

    private void resetAddres(String data) {
        List<GeneralData> datas;
        datas = GsonUtils.fromJsonString(data, new TypeToken<List<GeneralData>>() {
        }.getType());
        mDialog.resetAddres(datas);
    }

    private void initAddressProvider(String areas) {
        AllAreas allAreas;
        NativeAddressProvider nativeAddressProvider;
        allAreas = GsonUtils.fromJsonString(areas, AllAreas.class);
        nativeAddressProvider = new NativeAddressProvider(allAreas);
        mDialog.setAddressProvider(nativeAddressProvider);
    }

}