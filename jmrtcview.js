
import React, {Component} from 'react';
import ReactNative from 'react-native';
import PropTypes from 'prop-types';
import {ViewPropTypes} from 'react-native';

// var {
//   Component,
// } = React;

var {
  requireNativeComponent,
} = ReactNative;

var RTCJMRTCView = requireNativeComponent('RTCJMRTCView', JMRTCView);

export default class JMRTCView extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <RTCJMRTCView 
                {...this.props}                 
            />);
    }
}

JMRTCView.propTypes = {
    username: PropTypes.string.isRequired
}