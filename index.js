import { 
  DeviceEventEmitter, 
  NativeModules,
  Platform
 } from 'react-native'

import JMRTCView from './jmrtcview';

const JMRTCModule = NativeModules.JMRTCModule

const CallOutgoing = 'JMRTC.CallOutgoing'
const CallReceiveInvite = 'JMRTC.CallReceiveInvite'
const CallConnecting = 'JMRTC.CallConnecting'
const CallConnected = 'JMRTC.CallConnected'
const CallMemberJoin = 'JMRTC.CallMemberJoin'
const CallDisconnect = 'JMRTC.CallDisconnect'
const CallMemberLeave = 'JMRTC.CallMemberLeave'
const CallOtherUserInvited = 'JMRTC.CallOtherUserInvited'
const CallError = 'JMRTC.CallError'
const CallUserVideoStreamEnabled = 'JMRTC.CallUserVideoStreamEnabled'

const listeners = {}

export default class JMRTCViewController {
  /**
   * 初始化音视频引擎，初始化完成会回调 cb 函数。
   * @param {Function} success = (res) => { }
   * @param {Function} fail = (error) => { }
   */
  static initEngine(success, fail) {
    JMRTCModule.initEngine(success,fail)
  }

  /**
   * 释放音视频引擎
   */
  static releaseEngine() {
    JMRTCModule.releaseEngine()
  }
  
  /**
   * @abstract 发起一个通话
   * @param params = {usernames: [String], type = 'video' | 'voice'} 
   * @param success = () => {} 成功回调
   * @param fail = () => {} 失败回调
   */
  static startCallUsers(params, success, fail) {
    JMRTCModule.startCallUsers(params, success, fail)
  }

  /**
   * 设置实时视频的展示视图
   * @param {Object} params = {username: string}
   */
  static setVideoView(params) {
    JMRTCModule.setVideoView(params)
  }
  
  /**
    * @abstract 设置视频输出的编码属性
    *
    * @param params = {profile: '240p' | '360p' | '480p' | '720p'} 视频编码属性,不调用这个接口默认是 '360p'
    *
    * ### 建议在发起通话之前，调用此接口设置，避免在通话过程中调用此接口
    *
    * @discussion 注意：多人视频通话期间设置此接口可能会失效
    */
   static setVideoProfile(params) {
    JMRTCModule.setVideoProfile(params)
  }

  /**
   * @abstract 设置视频输出的编码属性
   * callback = (res) => {} , res = {profile: '240p' | '360p' | '480p' | '720p'}
   */
  static getVideoProfile(callback) {
    JMRTCModule.getVideoProfile(callback)
  }


// 实例方法
  /**
   * @abstract 接听来电
   * @param success = () => {} 成功回调
   * @param fail = () => {} 失败回调
   */
  static accept(success, fail) {
    JMRTCModule.accept(success, fail)
  }

  /**
   * @abstract 挂断通话
   * @param success = () => {} 成功回调
   * @param fail = () => {} 失败回调
   */
  static hangup(success, fail) {
    JMRTCModule.hangup(success, fail)
  }

  /**
   * @abstract 拒绝通话
   * @param success = () => {} 成功回调
   * @param fail = () => {} 失败回调
   */
  static refuse(success, fail) {
    JMRTCModule.refuse(success, fail)
  }

  /**
   * @abstract 邀请其他用户加入通话
   * @param params = {usernames: [String]} 
   * @param success = () => {} 成功回调
   * @param fail = () => {} 失败回调
   */
  static inviteUsers(params, success, fail) {
    JMRTCModule.inviteUsers(params, success, fail)
  }
  /**
   * 
   * @param {Function} callback = (boolean) => {}
   */
  static isMuted(callback) {
    JMRTCModule.isMuted(callback)
  }

  static setIsMuted(params) {
    JMRTCModule.setIsMuted(params)
  }


  /**
   * 
   * @param {Function} callback = (boolean) => {}
   */
  static isSpeakerphoneEnabled(callback) {
    JMRTCModule.isSpeakerphoneEnabled(callback)
  }

  static setIsSpeakerphoneEnabled(params) {
    JMRTCModule.setIsSpeakerphoneEnabled(params)
  }

  /**
   * 
   * @param {Function} callback = (boolean) => {}
   */
  static isVideoStreamEnabled(callback) {
    JMRTCModule.isVideoStreamEnabled(callback)
  }

  static setIsVideoStreamEnabled(params) {
    JMRTCModule.setIsVideoStreamEnabled(params)
  }

//  事件
  static addCallOutgoingListener(callback) {
    listeners[callback] = DeviceEventEmitter.addListener(CallOutgoing, map => {
      callback(map)
    })
  }

  static addCallReceiveInviteListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallReceiveInvite, map => {
      callback(map)
    }) 
  }

  static addCallConnectingListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallConnecting, map => {
      callback(map)
    }) 
  }

  static addCallConnectedListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallConnected, map => {
      callback(map)
    }) 
  }

  static addCallMemberJoinListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallMemberJoin, map => {
      callback(map)
    }) 
  }

  static addCallDisconnectListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallDisconnect, map => {
      callback(map)
    }) 
  }

  static addCallMemberLeaveListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallMemberLeave, map => {
      callback(map)
    }) 
  }
  

  static addCallOtherUserInvitedListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallOtherUserInvited, map => {
      callback(map)
    }) 
  }

  static addCallErrorListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallError, map => {
      callback(map)
    }) 
  }

  static addCallUserVideoStreamEnabledListener(callback) {
    if (!listeners[callback]) {
      console.warn('函数被重复用于监听，可能会出现未预期的问题！')
    }

    listeners[cb] = DeviceEventEmitter.addListener(CallUserVideoStreamEnabled, map => {
      callback(map)
    }) 
  }

  static removeListener(callback) {
    if (!listeners[callback]) {
      return
    }
    listeners[callback].remove()
    listeners[callback] = null
  }
}

module.exports = {
  JMRTCViewController,
  JMRTCView
}
