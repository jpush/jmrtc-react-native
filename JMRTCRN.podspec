require 'json'
pjson = JSON.parse(File.read('package.json'))

Pod::Spec.new do |s|

  s.name            = "JMRTCRN"
  s.version         = pjson["version"]
  s.homepage        = "https://github.com/jpush/jmrtc-react-native"
  s.summary         = pjson["description"]
  s.license         = pjson["license"]
  s.author          = { "huminios" => "380108184@qq.com" }
  
  s.ios.deployment_target = '7.0'

  s.source          = { :git => "https://github.com/jpush/jmrtc-react-native.git", :tag => "#{s.version}" }
  s.source_files    = 'ios/RCTJMRTCModule/*.{h,m}'
  s.preserve_paths  = "*.js"
  s.frameworks      = 'UIKit','CFNetwork','CoreFoundation','CoreTelephony','SystemConfiguration','CoreGraphics','Foundation','Security','CoreLocation','CoreAudio','AudioToolbox','AVFoundation','VideoToolbox'
  s.weak_frameworks = 'UserNotifications'
  s.libraries       = 'z','resolv','sqlite3.0'
  s.vendored_frameworks = "ios/RCTJMRTCModule/*.framework"
  s.dependency 'React'
  s.dependency 'JMessageRN'
end
