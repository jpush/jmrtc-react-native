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
  Modal,
  Keyboard
} from 'react-native';
import {  JMRTCViewController,JMRTCView} from 'jmrtc-react-native'
import JMessage from 'jmessage-react-plugin'

import LoginPage from './LoginPage'
import RTCComponent from './RTCComponent'

export default class App extends Component {

  constructor(props) {
    super(props)
    this.state = {
      isLogin: false,
      myUsername: ''
    }
  }
  componentDidMount() {
    JMessage.init({appkey: '4f7aef34fb361292c566a1cd'})
    JMessage.getMyInfo((userInfo) => {
      if (userInfo.username) {
        this.setState({isLogin: true, myUsername: userInfo.username})    
      } else {
        this.setState({isLogin: false, myUsername: ''})    
      }
    })
  }

  render() {
    JMRTCViewController.initEngine( (res) => {
      console.log(JSON.stringify(res))
    }, () => {

    });

    JMRTCViewController.releaseEngine()
    return (
      <View style={styles.container}>
        { this.state.isLogin ?
          <RTCComponent style={styles.welcome} 
            myUsername={this.state.myUsername}
          />
          :
          <LoginPage onLoginSuccess = { () => {
            this.setState(this.setState({isLogin: true}))
          }} />
        }
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    margin: 10,
    width: 100,
    height: 100,
  },
});
