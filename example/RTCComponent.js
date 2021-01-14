/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  TextInput,
  Alert,
  Platform
} from 'react-native';
import {  JMRTCViewController} from 'jmrtc-react-native-ys'
import {FormButton} from './LoginPage'

import CallAlert from './CallAlert'
import RTCVideoView from './RTCVideoView'
export default class RTCComponent extends Component {

    isMuted = false;

  constructor(props) {
    super(props)
    this.state = {
        username: '',
        isBeCalling: false,
        isCalling: false,
        callingUserName: '',
        targetUsername: '',
        myUsername: '',
    }

  }

  startCallUser(){
    JMRTCViewController.releaseEngine()
    JMRTCViewController.initEngine( (res) => {
        console.log('initEngine ' + JSON.stringify(res))

        JMRTCViewController.startCallUsers(
            { usernames: [this.state.username], type: "video" },
            res => {
                console.log("rtccomponent username" + this.props.myUsername + "targetUsername" + this.state.username);
                this.setState({
                targetUsername: this.state.username,
                isCalling: true
                });
            },
            err => {
                console.log(`startCallUsers fail ${JSON.stringify(err)}`);
            }
            );                    

    }, () => {
        console.log('initEngine ' + JSON.stringify(res))
    });
  }

  componentDidMount() {    
    // JMRTCViewController.addCallOutgoingListener()

    JMRTCViewController.releaseEngine()
    JMRTCViewController.initEngine( (res) => {
        console.log('initEngine ' + JSON.stringify(res))
    }, () => {
        console.log('initEngine ' + JSON.stringify(res))
    });
    
    JMRTCViewController.addCallReceiveInviteListener((inviteArr) => {
        console.log('addCallReceiveInviteListener' + JSON.stringify(inviteArr));
        this.setState({targetUsername: inviteArr.joinedMembers[0].username});
        this.setState({isBeCalling: true})
    })

    // JMRTCViewController.addCallConnectingListener(callback)

    JMRTCViewController.addCallConnectedListener(() =>{
        console.log('addCallConnectedListener');
        setTimeout(() => {
            JMRTCViewController.setVideoView({ username: this.props.myUsername });    
        }, 1000);
        
    })

    JMRTCViewController.addCallMemberJoinListener((map) => {
        console.log('addCallMemberJoinListener ' + JSON.stringify(map));
        JMRTCViewController.setVideoView({ username: map.username });      
    });

    JMRTCViewController.addCallDisconnectListener(() => {
        console.log('addCallDisconnectListener ');
        this.setState({isCalling: false, isBeCalling: false})  
    })

    JMRTCViewController.addCallMemberLeaveListener( () => {
        JMRTCViewController.hangup(() => {
            this.setState({isCalling: false, isBeCalling: false})  
              
        }, () => {
            this.setState({ isCalling: false ,isBeCalling: false});  
            Alert.alert('hangup', 'error')
        })
        
    } )
    
    // JMRTCViewController.addCallOtherUserInvitedListener(callback)

    // JMRTCViewController.addCallErrorListener(callback)

    // JMRTCViewController.addCallUserVideoStreamEnabledListener(callback)
    
  }

  render() {
    return (
      <View style={styles.container}>
      
      <CallAlert visible={this.state.isBeCalling}
        callingUserName={this.state.callingUserName}
        onClickAvatar={() => { 

         } }
        onClickAccept={ () => { 
            JMRTCViewController.accept(() => {
                this.setState({isCalling: true, isBeCalling: false})
            }, (err) => {
                console.error(JSON.stringify(err))
            })
         } }
        onClickReject={ () => { 
            JMRTCViewController.refuse(() => {
                this.setState({isCalling: false, isBeCalling: false})
            }, (err) => {
                console.error(JSON.stringify(err))
            })
         }}
        />

        <RTCVideoView visible={this.state.isCalling}
            targetUsername={this.state.targetUsername}
            myUsername={this.props.myUsername}
            onClickSwitch={ () => {
                JMRTCViewController.switchCamera()
            } }
            onClickMute={ () => {
                // JMRTCViewController.isMuted((res) => {
                    console.log("muted:" + this.isMuted);
                    this.isMuted = !this.isMuted;
                    JMRTCViewController.setIsMuted({muted: this.isMuted})
                // })
            } }
            onClickHangoff={ () => {
                JMRTCViewController.hangup(() => {
                    this.setState({isCalling: false})                        
                }, () => {
                    Alert.alert('hangup', 'error')
                })
            } }>
        </RTCVideoView>

        <View style={styles.inputView}>
            <TextInput style ={{flex: 1}}
                placeholder = "输入用户名"
                onChangeText = { (e) => { this.setState({username: e}) } }>
            </TextInput>
            <FormButton
                title="视频"
                onPress={ () => {
                    this.startCallUser()
                } }
            />

            <FormButton
                title="音频"
                onPress={ () => {
                    this.setState({
                        isCalling: true
                        });
                    // JMRTCViewController.startCallUsers({usernames: [this.state.username], type: 'voice'}, (res) => {
                    //     console.log(`startCallUsers success ${JSON.stringify(res)}`)
                    // }, (err) => {
                    //     console.log(`startCallUsers success ${JSON.stringify(err)}`)
                    // })
                } }
            />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
    inputView: {
        margin: 10,
        width: 300,
        flexDirection: 'row',
    },
    container: {
        backgroundColor: '#F5FCFF',
    },
    rctContainer: {
        flexDirection: 'row',
    },
    welcome: {
        margin: 10,
        width: 100,
        height: 100,
    },
});
