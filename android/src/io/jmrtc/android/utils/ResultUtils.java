package io.jmrtc.android.utils;



import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jiguang.jmrtc.api.JMRtcSession;
import cn.jiguang.jmrtc.api.JMSignalingMessage;
import cn.jmessage.support.google.gson.JsonElement;
import cn.jmessage.support.google.gson.JsonObject;
import cn.jmessage.support.google.gson.JsonParser;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.LocationContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.ChatRoomInfo;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupBasicInfo;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import io.jmrtc.android.Constant;

public class ResultUtils {


    public static Map<String, String> fromMap(ReadableMap extras) {
        Map<String, String> map = new HashMap<String, String>();

        ReadableMapKeySetIterator keysItr = extras.keySetIterator();
        while (keysItr.hasNextKey()) {
            String key = keysItr.nextKey();
            String value = extras.getString(key);
            map.put(key, value);
        }
        return map;
    }

    public static WritableMap toJSObject(Map<String, String> map) {
        Iterator<String> iterator = map.keySet().iterator();

        WritableMap object = Arguments.createMap();
        while (iterator.hasNext()) {
            String key = iterator.next();
            object.putString(key, map.get(key));
        }
        return object;
    }

    public static WritableMap toJSObject(final JMRtcSession session){
        final WritableMap result = Arguments.createMap();
        if(session == null){
            return result;
        }
        result.putString(Constant.CHANNEL_ID,session.getChannelKey());
        result.putString(Constant.MEDIA_TYPE,session.getMediaType()== JMSignalingMessage.MediaType.AUDIO?"voice":"video");
        session.getInviterUserInfo(new RequestCallback<UserInfo>() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if(userInfo!=null)
                    result.putMap(Constant.INVITER,toJSObject(userInfo));
            }
        });
        session.getInvitingUserInfos(new RequestCallback<List<UserInfo>>() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> userInfos) {
                if(userInfos!= null)
                    result.putArray(Constant.INVITING_MEMBERS,toJSArray(userInfos));
            }
        });
        session.getJoiendMembers(new RequestCallback<List<UserInfo>>() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> userInfos) {
                if(userInfos!=null){
                    result.putArray(Constant.JOINED_MEMBERS,toJSArray(userInfos));
                }
            }
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result ;
    }


    public static WritableMap toJSObject(final UserInfo userInfo) {
        if (userInfo == null) {
            return Arguments.createMap();
        }
        final WritableMap result = Arguments.createMap();
        result.putString(Constant.TYPE, Constant.TYPE_USER);
        if (null != userInfo.getGender()) {
            result.putString(Constant.GENDER, userInfo.getGender().toString());
        } else {
            result.putString(Constant.GENDER, "unknown");
        }
        result.putString(Constant.USERNAME, userInfo.getUserName());
        result.putString(Constant.APP_KEY, userInfo.getAppKey());
        result.putString(Constant.NICKNAME, userInfo.getNickname());

        if (userInfo.getAvatarFile() != null) {
            result.putString(Constant.AVATAR_THUMB_PATH, userInfo.getAvatarFile().getAbsolutePath());
        } else {
            result.putString("avatarThumbPath", "");
        }
        if (userInfo.getExtras() != null && userInfo.getExtras().size() > 0) {
            result.putMap(Constant.EXTRAS, toJSObject(userInfo.getExtras()));
        }
        result.putDouble(Constant.BIRTHDAY, userInfo.getBirthday());
        result.putString(Constant.REGION, userInfo.getRegion());
        result.putString(Constant.SIGNATURE, userInfo.getSignature());
        result.putString(Constant.ADDRESS, userInfo.getAddress());
        result.putString(Constant.NOTE_NAME, userInfo.getNotename());
        result.putString(Constant.NOTE_TEXT, userInfo.getNoteText());
        result.putBoolean(Constant.IS_NO_DISTURB, userInfo.getNoDisturb() == 1);
        result.putBoolean(Constant.IS_IN_BLACKLIST, userInfo.getBlacklist() == 1);
        result.putBoolean(Constant.IS_FRIEND, userInfo.isFriend());
        return result;
    }

    public static WritableMap toJSObject(GroupInfo groupInfo) {
        WritableMap result = Arguments.createMap();

        result.putString(Constant.TYPE, Constant.TYPE_GROUP);
        result.putString(Constant.ID, String.valueOf(groupInfo.getGroupID()));
        result.putString(Constant.NAME, groupInfo.getGroupName());
        result.putString(Constant.DESC, groupInfo.getGroupDescription());
        result.putInt(Constant.LEVEL, groupInfo.getGroupLevel());
        result.putString(Constant.OWNER, groupInfo.getGroupOwner());
        if (groupInfo.getAvatarFile() != null) {
            result.putString(Constant.AVATAR_THUMB_PATH, groupInfo.getAvatarFile().getAbsolutePath());
        }
        result.putString(Constant.OWNER_APP_KEY, groupInfo.getOwnerAppkey());
        result.putInt(Constant.MAX_MEMBER_COUNT, groupInfo.getMaxMemberCount());
        result.putBoolean(Constant.IS_NO_DISTURB, groupInfo.getNoDisturb() == 1);
        result.putBoolean(Constant.IS_BLOCKED, groupInfo.isGroupBlocked() == 1);
        return result;
    }

    public static WritableMap toJSObject(GroupBasicInfo groupInfo) {
        WritableMap result = Arguments.createMap();

        result.putString(Constant.TYPE, Constant.TYPE_GROUP);
        result.putString(Constant.ID, String.valueOf(groupInfo.getGroupID()));
        result.putString(Constant.NAME, groupInfo.getGroupName());
        result.putString(Constant.DESC, groupInfo.getGroupDescription());
        result.putInt(Constant.LEVEL, groupInfo.getGroupLevel());
        result.putString(Constant.AVATAR_THUMB_PATH, groupInfo.getAvatar());
        result.putInt(Constant.MAX_MEMBER_COUNT, groupInfo.getMaxMemberCount());
        return result;
    }

    public static WritableMap toJSObject(Message msg) {
        WritableMap result = Arguments.createMap();
        try {
            result.putString(Constant.ID, String.valueOf(msg.getId()));
            result.putString(Constant.SERVER_ID, String.valueOf(msg.getServerMessageId()));
            result.putMap(Constant.FROM, toJSObject(msg.getFromUser()));

            switch (msg.getTargetType()) {
                case group:
                    result.putMap(Constant.TARGET, toJSObject((GroupInfo) msg.getTargetInfo()));
                    break;
                case single:
                    if (msg.getDirect() == MessageDirect.send) {
                        result.putMap(Constant.TARGET, toJSObject((UserInfo) msg.getTargetInfo()));
                    } else {
                        result.putMap(Constant.TARGET, toJSObject(JMessageClient.getMyInfo()));
                    }
                    break;
                case chatroom:
                    result.putMap(Constant.TARGET, toJSObject((ChatRoomInfo) msg.getTargetInfo()));
                    break;
            }

            MessageContent content = msg.getContent();
            if (content.getStringExtras() != null) {
                result.putMap(Constant.EXTRAS, toJSObject(content.getStringExtras()));
            }

            result.putDouble(Constant.CREATE_TIME, msg.getCreateTime());
            result.putInt(Constant.UNRECEIPT_COUNT, msg.getUnreceiptCnt());
            switch (msg.getContentType()) {
                case text:
                    result.putString(Constant.TYPE, Constant.TEXT);
                    result.putString(Constant.TEXT, ((TextContent) content).getText());
                    break;
                case image:
                    result.putString(Constant.TYPE, Constant.IMAGE);
                    ImageContent imageContent = (ImageContent) content;
                    result.putString(Constant.THUMB_PATH, imageContent.getLocalThumbnailPath());
                    result.putString(Constant.LOCAL_PATH, imageContent.getLocalPath());
                    break;
                case voice:
                    result.putString(Constant.TYPE, Constant.VOICE);
                    VoiceContent voiceContent = (VoiceContent) content;
                    result.putString(Constant.PATH, voiceContent.getLocalPath());
                    result.putInt(Constant.DURATION, ((VoiceContent) content).getDuration());
                    break;
                case file:
                    result.putString(Constant.TYPE, Constant.FILE);
                    FileContent fileContent = (FileContent) content;
                    result.putString(Constant.PATH, fileContent.getLocalPath());
                    result.putString(Constant.FILE_NAME, fileContent.getFileName());
                    break;
                case custom:
                    result.putString(Constant.TYPE, Constant.CUSTOM);
                    Map<String, String> customObject = ((CustomContent) content).getAllStringValues();
                    result.putMap(Constant.CUSTOM_OBJECT, toJSObject(customObject));
                    break;
                case location:
                    result.putString(Constant.TYPE, Constant.LOCATION);
                    result.putDouble(Constant.LATITUDE, ((LocationContent) content).getLatitude().doubleValue());
                    result.putDouble(Constant.LONGITUDE, ((LocationContent) content).getLongitude().doubleValue());
                    result.putString(Constant.ADDRESS, ((LocationContent) content).getAddress());
                    result.putDouble(Constant.SCALE, ((LocationContent) content).getScale().doubleValue());
                    break;
                case eventNotification:
                    result.putString(Constant.TYPE, "event");
                    List usernameList = ((EventNotificationContent) content).getUserNames();
                    result.putArray(Constant.USERNAMES, toJSArray(usernameList));
                    switch (((EventNotificationContent) content).getEventNotificationType()) {
                        case group_member_added:
                            //群成员加群事件
                            result.putString(Constant.EVENT_TYPE, "group_member_added");
                            break;
                        case group_member_removed:
                            //群成员被踢事件
                            result.putString(Constant.EVENT_TYPE, "group_member_removed");
                            break;
                        case group_member_exit:
                            //群成员退群事件
                            result.putString(Constant.EVENT_TYPE, "group_member_exit");
                            break;
                        case group_info_updated:
                            result.putString(Constant.EVENT_TYPE, "group_info_updated");
                            break;
                        case group_member_keep_silence:
                            result.putString(Constant.EVENT_TYPE, "group_member_keep_silence");
                            break;
                        case group_member_keep_silence_cancel:
                            result.putString(Constant.EVENT_TYPE, "group_member_keep_silence_cancel");
                            break;
                        case group_keeper_added:
                            result.putString(Constant.EVENT_TYPE, "group_keeper_added");
                            break;
                        case group_keeper_removed:
                            result.putString(Constant.EVENT_TYPE, "group_keeper_removed");
                            break;
                        case group_dissolved:
                            //解散群组事件
                            result.putString(Constant.EVENT_TYPE, "group_dissolved");
                            break;
                        case group_owner_changed:
                            //移交群组事件
                            result.putString(Constant.EVENT_TYPE, "group_owner_changed");
                            break;
                        case group_type_changed:
                            //移交群组事件
                            result.putString(Constant.EVENT_TYPE, "group_type_changed");
                            break;
                    }
                    break;
                default:
                    result.putString(Constant.TYPE, Constant.UNKNOW);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static WritableMap toJSObject(Conversation conversation) {
        WritableMap map = Arguments.createMap();

        try {
            map.putString(Constant.TITLE, conversation.getTitle());
            map.putString(Constant.CONVERSATION_TYPE, conversation.getType().name());
            map.putInt(Constant.UNREAD_COUNT, conversation.getUnReadMsgCnt());

            if (conversation.getLatestMessage() != null) {
                map.putMap(Constant.LATEST_MESSAGE, toJSObject(conversation.getLatestMessage()));
            }

            if (!TextUtils.isEmpty(conversation.getExtra())) {
                WritableMap extrasMap = Arguments.createMap();
                String extras = conversation.getExtra();
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(extras).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    extrasMap.putString(entry.getKey(), entry.getValue().toString());
                }
                map.putMap(Constant.EXTRAS, extrasMap);
            }

            if (conversation.getType() == ConversationType.single) {
                UserInfo targetInfo = (UserInfo) conversation.getTargetInfo();
                map.putMap(Constant.TARGET, toJSObject(targetInfo));

            } else if (conversation.getType() == ConversationType.group) {
                GroupInfo targetInfo = (GroupInfo) conversation.getTargetInfo();
                map.putMap(Constant.TARGET, toJSObject(targetInfo));
            } else {
                ChatRoomInfo chatRoomInfo = (ChatRoomInfo) conversation.getTargetInfo();
                map.putMap(Constant.TARGET, toJSObject(chatRoomInfo));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static WritableMap toJSObject(ChatRoomInfo chatRoomInfo) {
        final WritableMap map = Arguments.createMap();
        try {
            map.putString(Constant.ROOM_ID, String.valueOf(chatRoomInfo.getRoomID()));
            map.putString(Constant.TYPE, Constant.TYPE_CHAT_ROOM);
            map.putString(Constant.ROOM_NAME, chatRoomInfo.getName());
            map.putString(Constant.APP_KEY, chatRoomInfo.getAppkey());
            map.putInt(Constant.MAX_MEMBER_COUNT, chatRoomInfo.getMaxMemberCount());
            map.putString(Constant.DESCRIPTION, chatRoomInfo.getDescription());
            map.putInt(Constant.MEMBER_COUNT, chatRoomInfo.getTotalMemberCount());
            map.putInt(Constant.CREATE_TIME, chatRoomInfo.getCreateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static WritableArray toJSArray(List list) {
        WritableArray array = Arguments.createArray();
        if (list != null) {
            for (Object object : list) {
                if (object instanceof UserInfo) {
                    array.pushMap(toJSObject((UserInfo) object));
                } else if (object instanceof GroupInfo) {
                    array.pushMap(toJSObject((GroupInfo) object));
                } else if (object instanceof GroupBasicInfo) {
                    array.pushMap(toJSObject((GroupBasicInfo) object));
                } else if (object instanceof Message) {
                    array.pushMap(toJSObject((Message) object));
                } else if (object instanceof Conversation) {
                    array.pushMap(toJSObject((Conversation) object));
                } else if (object instanceof ChatRoomInfo) {
                    array.pushMap(toJSObject((ChatRoomInfo) object));
                } else {
                    array.pushString(object.toString());
                }
            }
        }

        return array;
    }

    public static JSONObject toJSObject(String eventName, JSONObject value) {
        JSONObject result = new JSONObject();
        try {
            result.put("eventName", eventName);
            result.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
