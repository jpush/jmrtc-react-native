package io.jmrtc.android;

import android.content.Context;
import android.view.Choreographer;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.jmrtc.android.event.AddSurfaceViewEvent;
import io.jmrtc.android.event.RemoveSurfaceViewEvent;
import io.jmrtc.android.utils.Logger;

public class RNJMRTCViewManager extends ViewGroupManager<VideoLayout> {

    private static final String TAG = RNJMRTCViewManager.class.getSimpleName();

    @Override
    public String getName() {
        return "RTCJMRTCView";
    }


    @Override
    protected VideoLayout createViewInstance(ThemedReactContext reactContext) {
        return new VideoLayout(reactContext);
    }

    @ReactProp(name = "username")
    public void setUsername(final VideoLayout view, String username) {
        view.username = username;
    }



    @Override
    public void onDropViewInstance(VideoLayout view) {
        super.onDropViewInstance(view);
        EventBus.getDefault().unregister(view);
    }
}



class VideoLayout extends FrameLayout {
    private static final String TAG = VideoLayout.class.getSimpleName();

    public String username;
    public VideoLayout(Context context) {
        super(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), View.MeasureSpec.EXACTLY));
                    child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
                }
                getViewTreeObserver().dispatchOnGlobalLayout();
            }
        });
    }


    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddSurfaceViewEvent event) {
        Logger.d(TAG,"AddSurfaceViewEvent: " + username + " ," + event.getUsername());
        if (username.equals(event.getUsername())) {
            SurfaceView surfaceView = JMRTCModule.surfaceViewCache.get(username);
            if (surfaceView != null) {
                removeAllViews();
                addView(surfaceView);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RemoveSurfaceViewEvent event) {
        if (username.equals(event.getUsername())) {
            if (getChildCount() > 0)
                removeAllViews();
        }
    }

}