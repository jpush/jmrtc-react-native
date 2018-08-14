
import React, { Component } from 'react'
import {
  StyleSheet,
  View,
  Text,
  Modal,
  Image
} from 'react-native'
import PropTypes from 'prop-types';
import IconButton from './IconButton'

const styles = StyleSheet.create({
    callAlertContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },

    callAlert: {
        backgroundColor: '#3E4650',
        width: 300,
        height: 300,
        justifyContent: 'center',
        alignItems: 'center',
    },
    avatar: {
        width: 100,
        height: 100,
        marginTop: 20,
    },
    username: {
        fontSize: 30,
        color: 'white',
        margin: 30,
    },
    buttonContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
    },
    acceptBtn: {
        width: 60,
        height: 60,
        margin: 8,
    },
    rejectBtn: {
        width: 60,
        height: 60,
        margin: 8,
    }
  });
  
export default class CallAlert extends Component {
  constructor(props){
    super(props)
  }

  render() {
    return (
        <Modal
            animationType="slide"
            transparent={true}
            // visible={this.state.isBeCalling}>
            visible={this.props.visible}>
            <View style={styles.callAlertContainer}>
                <View style={styles.callAlert}>

                    <IconButton style={styles.avatar} onPress={this.props.onClickAvatar} icon={require('./resource/default-avatar.png')}/>
                    <Text style={styles.username}>{this.props.callingUserName}</Text>
                    <View style={styles.buttonContainer}>
                        <IconButton style={styles.rejectBtn} onPress={this.props.onClickReject} icon={require('./resource/reject.png')}/>
                        <IconButton style={styles.acceptBtn} onPress={this.props.onClickAccept} icon={require('./resource/accept.png')}/>
                    </View>
                </View>
            </View>
        </Modal>
      )
  }
}
CallAlert.propTypes = {
    onClickReject: PropTypes.function,
    onClickAccept: PropTypes.function,
    onClickAvatar: PropTypes.function,
    callingUserName: PropTypes.string
}

CallAlert.defaultProps = {
    callingUserName: '',
    onClickReject: () => {},
    onClickAccept: () => {},
    onClickAvatar: () => {}
}

module.exports = CallAlert