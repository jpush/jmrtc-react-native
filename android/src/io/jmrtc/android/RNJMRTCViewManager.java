package io.jmrtc.android;

import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.jmrtc.android.event.AddSurfaceViewEvent;
import io.jmrtc.android.event.RemoveSurfaceViewEvent;

public class RNJMRTCViewManager extends ViewGroupManager<FrameLayout> {

    private static final String TAG = RNJMRTCViewManager.class.getSimpleName();

    private String username;

    private FrameLayout frameLayout;

    @Override
    public String getName() {
        return "RTCJMRTCView";
    }

    @Override
    protected FrameLayout createViewInstance(ThemedReactContext reactContext) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        frameLayout = new FrameLayout(reactContext, null);
        return frameLayout;
    }

    @ReactProp(name = "username")
    public void setUsername(final FrameLayout view, String username) {
        Log.i(TAG, "username:" + username);
        this.username = username;


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddSurfaceViewEvent event) {
        if (username.equals(event.getUsername())) {
            SurfaceView surfaceView = JMRTCModule.surfaceViewCache.get(username);
            if (surfaceView != null) {
                frameLayout.addView(surfaceView);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RemoveSurfaceViewEvent event) {
        if (username.equals(event.getUsername())) {
            if (frameLayout.getChildCount() > 0)
                frameLayout.removeAllViews();
        }
    }

    @Override
    public void onDropViewInstance(FrameLayout view) {
        super.onDropViewInstance(view);
        EventBus.getDefault().unregister(this);
    }
}
