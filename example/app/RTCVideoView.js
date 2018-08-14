
import React, { Component } from 'react'
import {
  StyleSheet,
  View,
  Text,
  Modal,
  Dimensions,
  Alert
} from 'react-native'
import PropTypes from 'prop-types';
import IconButton from './IconButton'

import {  JMRTCViewController,JMRTCView} from 'jmrtc-react-native'


const styles = StyleSheet.create({
    callAlertContainer: {
        flex: 1,
        alignItems: 'center',
        backgroundColor: '#3E4650',
        justifyContent: 'flex-end',
        zIndex: 1000
    },
    backgroundVideo: {
        position: 'absolute',
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(52, 52, 52, 0.8)',
    },
    switchCamera: {
        position: 'absolute',
        left: 20,
        top: 20,
        width: 28,
        height: 28,
    },
    subVideoContainer: {
        flexDirection: 'row',
        alignItems: 'flex-end',
        justifyContent: 'flex-end',
        width: '100%',
    },
    subVideo: {
        height: 200,
        width: 160,
        margin: 20,
        backgroundColor: '#2E3640',
    },
    buttonContainer: {
        flexDirection: 'row',
        alignItems: 'flex-end',
        marginBottom: 60,
        // justifyContent: 'center',// ?
    },
    hangoffBtn: {
        width: 60,
        height: 60,
        margin: 10,
        marginLeft: 60
    },
    muteBtn: {
        width: 60,
        height: 60,
        margin: 10,
        marginRight: 60,
    }
  });
  
export default class RTCVideoView extends Component {
  constructor(props){
    super(props)
  }

  static getDerivedStateFromProps(props) {
    // Alert.alert('componentWillReceiveProps',JSON.stringify(props))
  }

  componentDidMount() {
    
  }
  render() {
    console.log(`video view render this.props.targetUsername ${this.props.targetUsername}   this.props.myUsername ${this.props.myUsername}`)
    setTimeout(() => {
        JMRTCViewController.setVideoView({username: '0001'})
        JMRTCViewController.setVideoView({username: '0002'})
    },1000)
    return (
        <Modal
            animationType="slide"
            transparent={true}
            visible={this.props.visible}>
                <JMRTCView style={styles.callAlertContainer} 
                    username={this.props.targetUsername}>
                        <IconButton style={styles.switchCamera} onPress={this.props.onClickSwitch} icon={require('./resource/switch.png')}/>
                    
                        <View style={styles.subVideoContainer}>
                            <JMRTCView style={styles.subVideo}
                                username={this.props.myUsername}>
                            </JMRTCView>
                        </View>
                        <View style={styles.buttonContainer}>
                            <IconButton style={styles.muteBtn} onPress={this.props.onClickMute} icon={require('./resource/mute.png')}/>
                            <IconButton style={styles.hangoffBtn} onPress={this.props.onClickHangoff} icon={require('./resource/hangoff.png')}/>
                        </View>
                </JMRTCView>
        </Modal>
      )
  }
}
RTCVideoView.propTypes = {
    targetUsername: PropTypes.string,
    myUsername: PropTypes.string,
    onClickSwitch: PropTypes.function,
    onClickMute: PropTypes.function,
    onClickHangoff: PropTypes.function,
}

RTCVideoView.defaultProps = {
    targetUsername: '',
    myUsername: '',
    onClickSwitch: () => {},
    onClickMute: () => {},
    onClickHangoff: () => {},

}

module.exports = RTCVideoView