# react-native-js-bridge

Get the webview nativeid or tag and pass to addJsBridge. It will add JS bridge on webview object create by `react-native-webview`

## Installation

```sh
npm install react-native-js-bridge

yarn add react-native-js-bridge
```

## Usage


```js
import { addJsBridge } from 'react-native-js-bridge';

// ...

const checkandAddJsBridge = (tag: number) => {
    const nativeTag = findNodeHandle(tag);
    if (nativeTag) addJsBridge(nativeTag);
};
```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
