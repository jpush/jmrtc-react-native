package io.jmrtc.android;

import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class RNJMRTCViewManager extends ViewGroupManager<FrameLayout> {

    private static final String TAG = RNJMRTCViewManager.class.getSimpleName();

    @Override
    public String getName() {
        return "RNJMRTCViewManager";
    }

    @Override
    protected FrameLayout createViewInstance(ThemedReactContext reactContext) {
        return new FrameLayout(reactContext);
    }

    @ReactProp(name = "username")
    public void setUsername(final FrameLayout view, String username) {
        Log.i(TAG,"username:"+username);
        SurfaceView surfaceView = JMRTCModule.surfaceViewCache.get(username);
        if(surfaceView != null){
            view.addView(surfaceView);
        }

    }

    @Override
    public void onDropViewInstance(FrameLayout view) {
        super.onDropViewInstance(view);
    }
}
