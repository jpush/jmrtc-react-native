package io.jmrtc.android;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.jiguang.jmrtc.api.JMRtcClient;
import cn.jiguang.jmrtc.api.JMRtcListener;
import cn.jiguang.jmrtc.api.JMRtcSession;
import cn.jiguang.jmrtc.api.JMSignalingMessage;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.jmrtc.android.event.AddSurfaceViewEvent;
import io.jmrtc.android.event.RemoveSurfaceViewEvent;
import io.jmrtc.android.utils.AndroidUtils;
import io.jmrtc.android.utils.JMTRCUtils;
import io.jmrtc.android.utils.Logger;
import io.jmrtc.android.utils.ResultUtils;

public class JMRTCModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private static final String TAG = "JMRTCModule";

    private static final String CALL_OUT_GOING = "JMRTC.CallOutgoing";
    private static final String CALL_RECEIVE_INVITE = "JMRTC.CallReceiveInvite";
    private static final String CALL_CONNECTING = "JMRTC.CallConnecting";
    private static final String CALL_CONNECTED = "JMRTC.CallConnected";
    private static final String CALL_MEMBER_JOIN = "JMRTC.CallMemberJoin";
    private static final String CALL_DISCONNECT = "JMRTC.CallDisconnect";
    private static final String CALL_MEMBER_LEAVE = "JMRTC.CallMemberLeave";
    private static final String CALL_OTHER_USER_INVITED = "JMRTC.CallOtherUserInvited";
    private static final String CALL_ERROR = "JMRTC.CallError";
    private static final String CALL_USER_VIDEOSTREAM_ENABLED = "JMRTC.CallUserVideoStreamEnabled";

    private static final int ERR_CODE_PARAMETER = 1;
    private static final int ERR_CODE_PERMISSION = 2;

    private static final String ERR_MSG_PARAMETER = "Parameters error";
    private static final String ERR_MSG_PERMISSION = "Permission not granted";

    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};


    private Context mContext;
    private JMTRCUtils mJMessageUtils;
    public static HashMap<String, SurfaceView> surfaceViewCache;

    private JMRtcSession session;//通话数据元信息对象
    private boolean requestPermissionSended;

    public JMRTCModule(ReactApplicationContext reactContext, boolean shutdownToast) {
        super(reactContext);
        mJMessageUtils = new JMTRCUtils(reactContext, shutdownToast);
        mContext = reactContext;
        surfaceViewCache = new HashMap<>();
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "JMRTCModule";
    }

    @Override
    public boolean canOverrideExistingModule() {
        return true;
    }


    private Callback mInitSuccess;
    private Callback mInitFail;

    @ReactMethod
    public void initEngine(Callback success, Callback fail) {
        mInitSuccess = success;
        mInitFail = fail;
        Logger.d(TAG, "initEngine has been called");
        JMRtcClient.getInstance().initEngine(jmRtcListener);
    }

    @ReactMethod
    public void reinitEngine(Callback success, Callback fail) {
        mInitSuccess = success;
        mInitFail = fail;
        Logger.d(TAG, "reinitEngine has been called");
        JMRtcClient.getInstance().reinitEngine();
    }

    @ReactMethod
    public void releaseEngine() {
        JMRtcClient.getInstance().releaseEngine();
    }

    @ReactMethod
    public void startCallUsers(ReadableMap map, final Callback success, final Callback fail) {
        try {
            ReadableArray array = map.getArray(Constant.USERNAMES);
            final String type = map.getString(Constant.TYPE);
            Logger.d(TAG, "startCallUsers has been called type:" + type);
            if (array == null || array.size() == 0) {
                Logger.d(TAG, "Username is empty.");
                mJMessageUtils.handleError(fail, ERR_CODE_PARAMETER, "Username is empty.");
                return;
            }

            JMessageClient.getUserInfo(array.getString(0), new GetUserInfoCallback() {
                @Override
                public void gotResult(int status, String desc, UserInfo info) {
                    if (null != info) {
                        JMRtcClient.getInstance().call(Collections.singletonList(info),
                                type.equals("voice") ? JMSignalingMessage.MediaType.AUDIO : JMSignalingMessage.MediaType.VIDEO,
                                new BasicCallback() {
                                    @Override
                                    public void gotResult(int status, String desc) {
                                        Logger.d(TAG, "call send complete . status = " + status + " desc = " + desc);
                                        mJMessageUtils.handleCallback(status, desc, success, fail);
                                    }
                                });
                    } else {
                        Logger.d(TAG, "call send complete . status = " + status + " desc = " + desc);
                        mJMessageUtils.handleError(fail, status, desc);
                    }
                }
            });
//            final ArrayList<UserInfo> userInfos = new ArrayList<>();
//            for (int i = 0; i < array.size(); i++) {
//                Logger.d(TAG, "startCallUsers has been called username:"+array.getString(i));
//                JMessageClient.getUserInfo(array.getString(i), new GetUserInfoCallback() {
//                    @Override
//                    public void gotResult(int status, String desc, UserInfo userInfo) {
//                        if (status == 0) {
//                            Logger.d(TAG,userInfo.toJson());
//                            userInfos.add(userInfo);
//                        } else {
//                            mJMessageUtils.handleError(fail, status, desc);
//                        }
//                    }
//                });
//
//            }
//            if(userInfos == null || userInfos.size() == 0){
//                Logger.d(TAG, "UserInfo is empty by username.");
//                mJMessageUtils.handleError(fail, ERR_CODE_PARAMETER, "UserInfo is empty by username.");
//                return;
//            }

//            if (type.equals("video")) {
//                JMRtcClient.getInstance().call(userInfos, JMSignalingMessage.MediaType.VIDEO, new BasicCallback() {
//                    @Override
//                    public void gotResult(int status, String desc) {
//                        Logger.d(TAG, "call send complete . status = " + status + " desc = " + desc);
//                        mJMessageUtils.handleCallback(status, desc, success, fail);
//                    }
//                });
//
//            } else if (type.equals("voice")) {
//                JMRtcClient.getInstance().call(userInfos, JMSignalingMessage.MediaType.AUDIO, new BasicCallback() {
//                    @Override
//                    public void gotResult(int status, String desc) {
//                        mJMessageUtils.handleCallback(status, desc, success, fail);
//                    }
//                });
//            } else {
//                mJMessageUtils.handleError(fail, ERR_CODE_PARAMETER, ERR_MSG_PARAMETER + " : " + type);
//            }

        } catch (Exception e) {
            e.printStackTrace();
            mJMessageUtils.handleError(fail, ERR_CODE_PARAMETER, ERR_MSG_PARAMETER);
        }

    }

    @ReactMethod
    public void setVideoView(ReadableMap map) {
        try {
            String username = map.getString(Constant.USERNAME);
            if (!TextUtils.isEmpty(username)) {
                EventBus.getDefault().post(new AddSurfaceViewEvent(username));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @ReactMethod
    public void setVideoProfile(ReadableMap map) {
        try {
            String profile = map.getString(Constant.PROFILE);
            JMRtcClient.VideoProfile videoProfile = null;
            switch (profile) {
                case "240p":
                    videoProfile = JMRtcClient.VideoProfile.Profile_240P;
                    break;
                case "360p":
                    videoProfile = JMRtcClient.VideoProfile.Profile_360P;
                    break;
                case "480p":
                    videoProfile = JMRtcClient.VideoProfile.Profile_480P;
                    break;
                case "720p":
                    videoProfile = JMRtcClient.VideoProfile.Profile_720P;
                    break;
            }
            if (videoProfile == null) {
                Logger.d(TAG, "videoProfile is empty");
                return;
            }
            JMRtcClient.getInstance().setVideoProfile(videoProfile);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d(TAG, ERR_MSG_PARAMETER);
        }

    }

    @ReactMethod
    public void getVideoProfile(Callback callback) {
        JMRtcClient.VideoProfile videoProfile = JMRtcClient.getInstance().getVideoProfile();
        String profile = "";
        switch (videoProfile) {
            case Profile_240P:
                profile = "240p";
                break;
            case Profile_360P:
                profile = "360p";
                break;
            case Profile_480P:
                profile = "480p";
                break;
            case Profile_720P:
                profile = "720p";
                break;
        }
        callback.invoke(profile);
    }

    @ReactMethod
    public void switchCamera() {
        JMRtcClient.getInstance().switchCamera();
    }

    @ReactMethod
    public void accept(final Callback success, final Callback fail) {
        JMRtcClient.getInstance().accept(new BasicCallback() {
            @Override
            public void gotResult(int status, String desc) {
                mJMessageUtils.handleCallback(status, desc, success, fail);
            }
        });
    }

    @ReactMethod
    public void hangup(final Callback success, final Callback fail) {
        JMRtcClient.getInstance().hangup(new BasicCallback() {
            @Override
            public void gotResult(int status, String desc) {
                mJMessageUtils.handleCallback(status, desc, success, fail);
            }
        });
    }

    @ReactMethod
    public void refuse(final Callback success, final Callback fail) {
        JMRtcClient.getInstance().refuse(new BasicCallback() {
            @Override
            public void gotResult(int status, String desc) {
                mJMessageUtils.handleCallback(status, desc, success, fail);
            }
        });
    }

    @ReactMethod
    public void inviteUsers(ReadableMap map, final Callback success, final Callback fail) {
        try {
            final ReadableArray array = map.getArray(Constant.USERNAMES);
            final List<UserInfo> userInfos = new ArrayList<>();
            final int[] cnt = {0};
            for (int i = 0; i < array.size(); i++) {
                JMessageClient.getUserInfo(array.getString(i), new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int status, String desc, UserInfo userInfo) {
                        ++cnt[0];
                        if (status == 0) {
                            userInfos.add(userInfo);
                            if (cnt[0] == array.size()) {
                                JMRtcClient.getInstance().invite(userInfos, new BasicCallback() {
                                    @Override
                                    public void gotResult(int status, String desc) {
                                        mJMessageUtils.handleCallback(status, desc, success, fail);
                                    }
                                });
                            }
                        } else {
                            mJMessageUtils.handleError(fail, status, desc);
                        }
                    }
                });
            }



        } catch (Exception e) {
            e.printStackTrace();
            mJMessageUtils.handleError(fail, ERR_CODE_PARAMETER, ERR_MSG_PARAMETER);
        }

    }

    @ReactMethod
    public void setIsMuted(ReadableMap map) {
        try {
            boolean muted = map.getBoolean(Constant.MUTED);
            JMRtcClient.getInstance().enableAudio(!muted);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d(TAG, ERR_MSG_PARAMETER);
        }
    }

    @ReactMethod
    public void setIsSpeakerphoneEnabled(ReadableMap map) {
        try {
            boolean speakerphoneEnabled = map.getBoolean(Constant.SPEAKERPHONE_ENABLED);
            JMRtcClient.getInstance().enableSpeakerphone(speakerphoneEnabled);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d(TAG, ERR_MSG_PARAMETER);
        }
    }

    @ReactMethod
    public void setIsVideoStreamEnabled(ReadableMap map) {
        try {
            boolean videoStreamEnabled = map.getBoolean(Constant.VIDEO_STREAM_ENABLED);
            JMRtcClient.getInstance().enableVideo(videoStreamEnabled);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d(TAG, ERR_MSG_PARAMETER);
        }
    }


    JMRtcListener jmRtcListener = new JMRtcListener() {
        @Override
        public void onEngineInitComplete(final int errCode, final String errDesc) {
            super.onEngineInitComplete(errCode, errDesc);
            Logger.d(TAG, "onEngineInitComplete invoked!. errCode = " + errCode);
            if (errCode != 872105)
                mJMessageUtils.handleCallback(errCode, errDesc, mInitSuccess, mInitFail);
        }

        @Override
        public void onCallOutgoing(JMRtcSession callSession) {// 通话已播出
            super.onCallOutgoing(callSession);
            Logger.d(TAG, "onCallOutgoing invoked!. session = " + callSession);
            WritableMap map = ResultUtils.toJSObject(callSession);
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_OUT_GOING, map);
            session = callSession;
        }

        @Override
        public void onCallInviteReceived(JMRtcSession callSession) {// 收到通话邀请
            super.onCallInviteReceived(callSession);
            Logger.d(TAG, "onCallInviteReceived invoked!. session = " + callSession);
            WritableMap map = ResultUtils.toJSObject(callSession);
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_RECEIVE_INVITE, map);
            session = callSession;
        }

        @Override
        public void onCallOtherUserInvited(UserInfo fromUserInfo, List<UserInfo> invitedUserInfos, JMRtcSession callSession) {// 通话中有其他人被邀请
            super.onCallOtherUserInvited(fromUserInfo, invitedUserInfos, callSession);
            Logger.d(TAG, "onCallOtherUserInvited invoked!. session = " + callSession + " from user = " + fromUserInfo
                    + " invited user = " + invitedUserInfos);
            WritableMap map = Arguments.createMap();
            map.putArray(Constant.INVITED_USERS, ResultUtils.toJSArray(invitedUserInfos));
            map.putMap(Constant.FROM_USER, ResultUtils.toJSObject(fromUserInfo));
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_OTHER_USER_INVITED, map);
            session = callSession;
        }

        //主线程回调
        @Override
        public void onCallConnected(JMRtcSession callSession, SurfaceView localSurfaceView) {//通话连接已建立
            super.onCallConnected(callSession, localSurfaceView);
            Logger.d(TAG, "onCallConnected invoked!. session = " + callSession + " localSerfaceView = " + localSurfaceView);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            localSurfaceView.setLayoutParams(layoutParams);
            surfaceViewCache.put(JMessageClient.getMyInfo().getUserName(), localSurfaceView);
            WritableMap map = ResultUtils.toJSObject(callSession);
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_CONNECTED, map);
            session = callSession;

        }

        //主线程回调
        @Override
        public void onCallMemberJoin(UserInfo joinedUserInfo, SurfaceView remoteSurfaceView) {//有其他人加入通话
            super.onCallMemberJoin(joinedUserInfo, remoteSurfaceView);
            Logger.d(TAG, "onCallMemberJoin invoked!. joined user  = " + joinedUserInfo + " remoteSerfaceView = " + remoteSurfaceView);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            remoteSurfaceView.setLayoutParams(layoutParams);
            surfaceViewCache.put(joinedUserInfo.getUserName(), remoteSurfaceView);
            WritableMap map = ResultUtils.toJSObject(joinedUserInfo);
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_MEMBER_JOIN, map);
        }

        @Override
        public void onPermissionNotGranted(final String[] requiredPermissions) {
            Logger.d(TAG, "[onPermissionNotGranted] permission = " + requiredPermissions.length);
            try {
                AndroidUtils.requestPermission(getCurrentActivity(), requiredPermissions);
                requestPermissionSended = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCallMemberOffline(final UserInfo leavedUserInfo, JMRtcClient.DisconnectReason reason) {//有人退出通话
            super.onCallMemberOffline(leavedUserInfo, reason);
            Logger.d(TAG, "onCallMemberOffline invoked!. leave user = " + leavedUserInfo + " reason = " + reason);

            SurfaceView cachedSurfaceView = surfaceViewCache.get(leavedUserInfo.getUserName());
            if (null != cachedSurfaceView) {
                surfaceViewCache.remove(leavedUserInfo.getUserName());
            }

            WritableMap map = Arguments.createMap();
            map.putMap(Constant.USER, ResultUtils.toJSObject(leavedUserInfo));
            map.putString(Constant.REASON, reason.toString());
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_MEMBER_LEAVE, map);
            EventBus.getDefault().post(new RemoveSurfaceViewEvent(leavedUserInfo.getUserName()));

        }

        @Override
        public void onCallDisconnected(JMRtcClient.DisconnectReason reason) {//本地通话连接断开
            super.onCallDisconnected(reason);
            Logger.d(TAG, "onCallDisconnected invoked!. reason = " + reason);
            WritableMap map = Arguments.createMap();
            map.putMap(Constant.SESSION, ResultUtils.toJSObject(session));
            map.putString(Constant.REASON, reason.toString());
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_DISCONNECT, map);
            session = null;
        }

        @Override
        public void onCallError(int errorCode, String desc) {//通话发生错误
            super.onCallError(errorCode, desc);
            Logger.d(TAG, "onCallError invoked!. errCode = " + errorCode + " desc = " + desc);
            WritableMap map = ResultUtils.toJSObject(session);
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_ERROR, map);
            session = null;
        }

        @Override
        public void onRemoteVideoMuted(UserInfo remoteUser, boolean isMuted) {
            super.onRemoteVideoMuted(remoteUser, isMuted);
            Logger.d(TAG, "onRemoteVideoMuted invoked!. remote user = " + remoteUser + " isMuted = " + isMuted);
            WritableMap map = ResultUtils.toJSObject(remoteUser);
            map.putBoolean(Constant.ENABLE, isMuted);
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(CALL_USER_VIDEOSTREAM_ENABLED, map);

        }
    };


    @Override
    public void onHostResume() {
        if (requestPermissionSended) {
            if (AndroidUtils.checkPermission(getCurrentActivity(), REQUIRED_PERMISSIONS)) {
                JMRtcClient.getInstance().reinitEngine();
            } else {
                mJMessageUtils.handleError(mInitFail, ERR_CODE_PERMISSION, ERR_MSG_PERMISSION);
            }
        }
        requestPermissionSended = false;
    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        surfaceViewCache = null;
        JMRtcClient.getInstance().releaseEngine();
    }
}
