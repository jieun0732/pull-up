import React from "react";
import * as SplashScreen from "expo-splash-screen";
import { WebView } from "react-native-webview";

function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function delay_splash() {
  await SplashScreen.preventAutoHideAsync();
  await sleep(1000);
  await SplashScreen.hideAsync();
}

export default class App extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    delay_splash();
    return (
      <WebView
        javaScriptCanOpenWindowsAutomatically
        source={{ uri: "https://pull-up-snowy.vercel.app/" }}
      />
    );
  }
}