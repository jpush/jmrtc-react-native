package io.jmrtc.android;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.jmrtc.android.utils.Logger;

public class JMRTCReactPackage implements ReactPackage {

    private boolean mShutdownToast;

    public JMRTCReactPackage(boolean shutdownToast, boolean shutdownLog) {
        Logger.SHUTDOWNTOAST = shutdownToast;
        Logger.SHUTDOWNLOG = shutdownLog;
        mShutdownToast = shutdownToast;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> result = new ArrayList<>();
        result.add(new JMRTCModule(reactContext, mShutdownToast));
        return result;
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        List<ViewManager> viewManagers = new ArrayList<>();
        viewManagers.add(new RNJMRTCViewManager());
        return viewManagers;
    }
}
