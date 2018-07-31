/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  TextInput
} from 'react-native';
import {  JMRTCViewController,JMRTCView} from 'jmrtc-react-native'
import JMessage from 'jmessage-react-plugin'
import {FormButton} from './LoginPage'

export default class RTCComponent extends Component {

  constructor(props) {
    super(props)
    this.state = {
        username: ''
    }
    
  }

  componentDidMount() {

    JMRTCViewController.initEngine((res) => {
        console.log(`engine init success ${JSON.stringify(res)}`)
    }, (err) => {
        console.log('engine init fail')
    })

    // JMRTCViewController.accept(((res) => {
    //     console.log(`engine init success ${JSON.stringify(res)}`)
    // }, (err) => {
    //     console.log('engine init fail')
    // }))
  }

  render() {
    return (

      <View style={styles.container}>
        <View style={styles.inputView}>
            <TextInput
                placeholder = "输入用户名"
                onChangeText = { (e) => { this.setState({username: e}) } }>
            </TextInput>
            <FormButton
                title="视频"
                onPress={ () => {
                    JMRTCViewController.startCallUsers({usernames: [this.state.username], type: 'video'}, (res) => {
                        console.log(`startCallUsers success ${JSON.stringify(res)}`)
                        JMRTCViewController.setVideoView({username: this.props.myUsername})
                        JMRTCViewController.setVideoView({username: this.state.username})
                    }, (err) => {
                        console.log(`startCallUsers fail ${JSON.stringify(err)}`)
                    })
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
        <View style={styles.rctContainer}>
            <JMRTCView style={styles.welcome}
                username={this.props.myUsername}
            />
            <JMRTCView style={styles.welcome} 
                username={this.state.username}/>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
    inputView: {
        margin: 10,
        width: 200,
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
