
import React, { Component } from 'react'
import {
  StyleSheet,
  TouchableHighlight,
  View,
  Image
} from 'react-native'


const styles = StyleSheet.create({
    container: {
    },
    icon: {
      width: '100%',
      height: '100%',
    }
  });
  
export default class IconButton extends Component {
  constructor(props){
    super(props)
  }

  render() {
    console.log(`the style ${JSON.stringify(this.props.style)}`)
    return (
        <TouchableHighlight {...this.props}>
          <View style={this.props.styles}>
              <Image
                  source={this.props.icon}
                  style={styles.icon}
                  />
          </View>
        </TouchableHighlight>
      )
  }
}

module.exports = IconButton