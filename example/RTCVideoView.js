
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
        width:'100%',
        height:'100%',
        backgroundColor: '#FF0000',
        position: 'absolute',
    },
    backgroundVideo: {
        position: 'absolute',
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(52, 52, 52, 0.8)',
    },
    switchCamera: {
        left: 20,
        top: 50,
        width: 28,
        height: 28,
    },
    subVideoContainer: {
        top: '50%',
        width: '100%',
        height: 200,
    },
    subVideo: {
        position: 'absolute',
        right: 0,
        height: '100%',
        width: 160,
        backgroundColor: '#FFFF00',
    },
    buttonContainer: {
        position: 'absolute',
        width: '100%',
        backgroundColor: 'rgba(52, 52, 52, 0.8)',
        flexDirection: 'row',
        alignItems: 'flex-end',
        top: '85%',
        justifyContent: 'center',// ?
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
    return ( 
        <Modal
            animationType={"slide"}
            transparent={true}
            visible={this.props.visible}>

            <View style={{flex: 1, backgroundColor: 'powderblue'}}>
                <JMRTCView style={styles.callAlertContainer} 
                            username={this.props.targetUsername} />
                <IconButton style={styles.switchCamera} onPress={this.props.onClickSwitch} icon={require('./resource/switch.png')}/>

                <View style={styles.subVideoContainer}>
                <JMRTCView style={styles.subVideo} username={this.props.myUsername}/>    
                </View>
                
                <View style={styles.buttonContainer}>
                    <IconButton style={styles.muteBtn} onPress={this.props.onClickMute} icon={require('./resource/mute.png')}/>
                    <IconButton style={styles.hangoffBtn} onPress={this.props.onClickHangoff} icon={require('./resource/hangoff.png')}/>
                </View> 
            </View>
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