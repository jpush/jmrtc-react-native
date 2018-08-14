/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Platform,
  StyleSheet,
  View,
  TextInput,
  Button,
  Alert
} from 'react-native';
import JMessage from 'jmessage-react-plugin'


var styles = StyleSheet.create({
    signin: {
      marginLeft: 10,
      marginRight: 10
    },
    button: {
      backgroundColor: '#FF3366',
      borderColor: '#FF3366'
    }
  })
  
  export class FormButton extends Component {
      
      
      static propTypes = {
          title: PropTypes.string,
          onPress: PropTypes.func
      }
  
      constructor(props) {
          super(props);
          this.title = props.title
          this.style = props.style 
          this.onPress = this.onPress.bind(this);
      }
  
      onPress() {
          if (!this.props.onPress) {
              return;
            }
          this.props.onPress();
      }

      render () {
          return (
              <Button
              style={this.style}
              color="#FF3366"
              title={this.title}
              onPress = {this.onPress}
              />
          )
      }
  }

export default class LoginPage extends Component {

    constructor() {
        super()
        this.state = {
            username: "",
            password: "",
        }
        this.onPress = this.onPress.bind(this);
        this.register = this.register.bind(this);
    }

    onPress() {

        JMessage.login({username: this.state.username, password: this.state.password}, () => {
            Alert.alert('login success')
            this.props.onLoginSuccess()
        }, (error) => {
            Alert.alert('login fail', JSON.stringify(error))
        })
    }

    register() {
        JMessage.register({username: this.state.username, password: this.state.password}, () => {
            Alert.alert('register success')
        }, (error) => {
            Alert.alert('register fail', JSON.stringify(error))
        })
    }

    render() {
        return (
        <View style={styles.container}>
                <TextInput
                    style={styles.inputView}
                    placeholder = "用户名"
                    onChangeText = { (e) => { this.setState({username: e}) } }>
                </TextInput>
                <TextInput
                    style={styles.inputView}
                    placeholder = "密码"
                    onChangeText = { (e) => { this.setState({password: e}) } }>
                </TextInput>
                <FormButton 
                    style={styles.loginBtn}
                    title="登录"
                    onPress={this.onPress}
                    >
                </FormButton>
                <Button
                    style={{width: 100, height: 20, position: "absolute", left: 0, top: 1000}}
                    onPress={this.register}
                    title="注册">
                </Button>
        </View>
        );
    }
}

const styles = StyleSheet.create({
    inputView: {
        margin: 10,
    },
    loginBtn: {
        color: "#ffffff",
        height: 30,
        backgroundColor: "#cccccc",
    },
    button: {
        backgroundColor: '#FF3366',
        borderColor: '#FF3366'
    },
});
