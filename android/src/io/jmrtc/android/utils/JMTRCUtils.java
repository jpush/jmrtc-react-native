package io.jmrtc.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;
import io.jmrtc.android.Constant;
import io.jmrtc.android.HandleResponseCode;


public class JMTRCUtils {

    private Context mContext;
    private boolean mShutdownToast;

    public JMTRCUtils(Context context, boolean shutdownToast) {
        this.mContext = context;
        this.mShutdownToast = shutdownToast;
    }

    public Conversation getConversation(ReadableMap map) {
        Conversation conversation = null;
        String type = map.getString(Constant.TYPE);
        if (type.equals(Constant.TYPE_SINGLE)) {
            String username = map.getString(Constant.USERNAME);
            String appKey = "";
            if (map.hasKey(Constant.APP_KEY)) {
                appKey = map.getString(Constant.APP_KEY);
                conversation = Conversation.createSingleConversation(username, appKey);
            } else {
                conversation = Conversation.createSingleConversation(username);
            }
        } else if (type.equals(Constant.TYPE_GROUP)) {
            String groupId = map.getString(Constant.GROUP_ID);
            conversation = Conversation.createGroupConversation(Long.parseLong(groupId));
        } else {
            String roomId = map.getString(Constant.ROOM_ID);
            conversation = Conversation.createChatRoomConversation(Long.parseLong(roomId));
        }
        return conversation;
    }

    public void sendMessage(ReadableMap map, MessageContent content,
                            final Callback success, final Callback fail) {
        if (map.hasKey(Constant.EXTRAS)) {
            content.setExtras(ResultUtils.fromMap(map.getMap(Constant.EXTRAS)));
        }
        Conversation conv = getConversation(map);
        final Message msg = conv.createSendMessage(content);
        msg.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int status, String desc) {
                handleCallbackWithObject(status, desc, success, fail, ResultUtils.toJSObject(msg));
            }
        });
        if (map.hasKey(Constant.SENDING_OPTIONS)) {
            MessageSendingOptions options = new MessageSendingOptions();
            ReadableMap optionMap = map.getMap(Constant.SENDING_OPTIONS);
            options.setShowNotification(optionMap.getBoolean("isShowNotification"));
            options.setRetainOffline(optionMap.getBoolean("isRetainOffline"));

            if (optionMap.hasKey("isCustomNotificationEnabled")) {
                options.setCustomNotificationEnabled(
                        optionMap.getBoolean("isCustomNotificationEnabled"));
            }
            if (optionMap.hasKey("notificationTitle")) {
                options.setNotificationTitle(optionMap.getString("notificationTitle"));
            }
            if (optionMap.hasKey("notificationText")) {
                options.setNotificationText(optionMap.getString("notificationText"));
            }
            JMessageClient.sendMessage(msg, options);
        }
        JMessageClient.sendMessage(msg);
    }

    public String storeImage(Bitmap bitmap, String filename, String pkgName) {
        File avatarFile = new File(getAvatarPath(pkgName));
        if (!avatarFile.exists()) {
            avatarFile.mkdirs();
        }

        String filePath = getAvatarPath(pkgName) + filename + ".png";
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return filePath;
    }

    public String getAvatarPath(String pkgName) {
        return  Environment.getExternalStorageDirectory() + "/" + pkgName + "/images/avatar/";
    }

    public void handleCallback(int status, String desc, Callback success, Callback fail) {
        if (status == 0) {
            success.invoke(0);
        } else {
            handleError(fail, status, desc);
        }
    }

    public void handleCallbackWithValue(int status, String desc, Callback success, Callback fail,
                                        String value) {
        if (status == 0) {
            success.invoke(value);
        } else {
            handleError(fail, status, desc);
        }
    }

    public void handleCallbackWithObject(int status, String desc, Callback success, Callback fail,
                                         WritableMap map) {
        if (status == 0) {
            success.invoke(map);
        } else {
            handleError(fail, status, desc);
        }
    }

    public void handleCallbackWithArray(int status, String desc, Callback success, Callback fail,
                                        WritableArray array) {
        if (status == 0) {
            success.invoke(array);
        } else {
            handleError(fail, status, desc);
        }
    }

    public void handleError(Callback error, int code, String message) {
        HandleResponseCode.onHandle(mContext, code, false, mShutdownToast);
        WritableMap result = Arguments.createMap();
        result.putInt(Constant.CODE, code);
        result.putString(Constant.DESCRIPTION, message);
        error.invoke(result);
    }
}
