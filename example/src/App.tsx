import { addJsBridge } from 'react-native-js-bridge';
import { findNodeHandle } from 'react-native';
import { WebView } from 'react-native-webview';

export default function App() {
  const checkandAddJsBridge = (tag: number) => {
    const nativeTag = findNodeHandle(tag);
    if (nativeTag) addJsBridge(nativeTag);
  };

  return (
    <WebView
      source={{ uri: 'https://discoverpilgrim.com/' }}
      onLoad={({ nativeEvent }) => {
        checkandAddJsBridge(nativeEvent?.target);
      }}
    />
  );
}
