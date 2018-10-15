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
import {  JMRTCViewController} from 'jmrtc-react-native'
import {FormButton} from './LoginPage'

import CallAlert from './CallAlert'
import RTCVideoView from './RTCVideoView'
export default class RTCComponent extends Component {

    // callStatues =  'none' | calling
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

  componentDidMount() {



    // JMRTCViewController.addCallOutgoingListener()
    
    JMRTCViewController.addCallReceiveInviteListener((inviteArr) => {
        this.setState({isBeCalling: true})
    })

    // JMRTCViewController.addCallConnectingListener(callback)

    // JMRTCViewController.addCallConnectedListener(callback)

    // JMRTCViewController.addCallMemberJoinListener(callback)

    // JMRTCViewController.addCallDisconnectListener(callback)

    JMRTCViewController.addCallMemberLeaveListener( () => {
        JMRTCViewController.hangup(() => {
            this.setState({isCalling: false})  
              
        }, () => {
            this.setState({ isCalling: false });  
            Alert.alert('hangup', 'error')
        })
        
    } )
    
    // JMRTCViewController.addCallOtherUserInvitedListener(callback)

    // JMRTCViewController.addCallErrorListener(callback)

    // JMRTCViewController.addCallUserVideoStreamEnabledListener(callback)
    JMRTCViewController.initEngine( (res) => {
      console.log(JSON.stringify(res))
    }, () => {

    });
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
                JMRTCViewController.isMuted((res) => {
                    JMRTCViewController.setIsMuted({muted: !res})
                })
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
                    if (Platform.OS === "android") {
                        // JMRTCViewController.reinitEngine(
                        // res => {
                        //     console.log(`engine init success ${JSON.stringify(res)}`);
                            JMRTCViewController.startCallUsers(
                            { usernames: [this.state.username], type: "video" },
                            res => {
                                JMRTCViewController.setVideoView({ username: "0001" });
                                JMRTCViewController.setVideoView({ username: "0002" });
                                console.log("targetUsername" + this.props.myUsername);
                                this.setState({
                                targetUsername: this.state.username,
                                isCalling: true
                                });
                            },
                            err => {
                                console.log(`startCallUsers fail ${JSON.stringify(err)}`);
                            }
                            );
                        // },
                        // err => {
                        //     console.log("engine init fail");
                        // }
                        // );
                    }else{
                        JMRTCViewController.initEngine(res => {
                            console.log(`engine init success ${JSON.stringify(res)}`);
                            JMRTCViewController.startCallUsers({ usernames: [this.state.username], type: "video" }, res => {
                                JMRTCViewController.setVideoView(
                                  { username: "0001" }
                                );
                                JMRTCViewController.setVideoView(
                                  { username: "0002" }
                                );
                                console.log("targetUsername" + this.props.myUsername);
                                this.setState({
                                  targetUsername: this
                                    .state.username,
                                  isCalling: true
                                });
                              }, err => {
                                console.log(`startCallUsers fail ${JSON.stringify(err)}`);
                              });
                          }, err => {
                            console.log("engine init fail");
                          });
                    }
                    

                } }
            />

            <FormButton
                title="音频"
                onPress={ () => {
                    JMRTCViewController.startCallUsers({usernames: [this.state.username], type: 'voice'}, (res) => {
                        console.log(`startCallUsers success ${JSON.stringify(res)}`)
                    }, (err) => {
                        console.log(`startCallUsers success ${JSON.stringify(err)}`)
                    })
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
