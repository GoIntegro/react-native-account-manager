# react-native-account-manager

[![npm version](https://badge.fury.io/js/react-native-account-manager.svg)](https://badge.fury.io/js/react-native-account-manager)

Access to Android AccountManager for [React Native](https://github.com/facebook/react-native) inspired by [cordova-android-accountmanager plugin](https://github.com/polychrom/cordova-android-accountmanager)
It currently only supports explicit account handling (programatically adding/removing/settin/getting account details to the AccountManager).
Only the following methods are available;
  * AccountManager.addAccountExplicitly('account_type', 'userName', 'userPassword');
  * AccountManager.removeAccount(account);
  * AccountManager.getAccountsByType('account_type');
  * AccountManager.getUserData(account, 'key');
  * AccountManager.setUserData(account, 'key', 'value')

## Install

```shell
npm install --save react-native-account-manager
```

## Automatically link

#### With React Native 0.61+

No addiontional steps required.

#### With React Native 0.27+

```shell
react-native link react-native-account-manager
```

## Configuration

1. In your AndroidManifest file, add this service in `<application />`
```
<service android:name="com.gointegro.accountmanager.AuthenticatorService">
   <intent-filter>
       <action android:name="android.accounts.AccountAuthenticator" />
   </intent-filter>
   <meta-data android:name="android.accounts.AccountAuthenticator" android:resource="@xml/authenticator" />
</service>
```

2. Create an `authenticator.xml` file in the `res/xml` folder and add this code
```
<?xml version="1.0" encoding="utf-8"?>
<account-authenticator
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:accountType="{your app package name}"
    android:icon="@mipmap/ic_launcher_round"
    android:smallIcon="@mipmap/ic_launcher_round"
    android:label="@string/app_name"/>
```

## Manually link

### Android

- in `android/app/build.gradle`:

```diff
dependencies {
    ...
    implementation project(':react-native-account-manager')
}
```

- in `android/settings.gradle`:

```diff
...
include ':app'
+ include ':react-native-account-manager''
+ project(':react-native-account-manager').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-account-manager/android')
```

#### With React Native 0.29+

- in `MainApplication.java`:

```diff
+ import com.gointegro.accountmanager.AccountManagerPackage;

  public class MainApplication extends Application implements ReactApplication {
    //......

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
+         new AccountManagerPackage(),
          new MainReactPackage()
      );
    }

    ......
  }
```

## Basic Usage (Creating, removing an account)
```
import AccountManager from 'react-native-account-manager';

AccountManager.addAccountExplicitly('accountName', 'userName', 'userPassword')
.then((account) => {
  // console.log('account successfully added', account)
  AccountManager.removeAccount(account).then(() => {
    // console.log('account successfully removed');
    })
}).catch((e) => {
  // console.log('fail to add account', e);
});
```
## Basic Usage (Get accounts, get account data, set account data)

```
import AccountManager from 'react-native-account-manager';

AccountManager.getAccountsByType('accountName').then((accounts) => {
  // console.log('available accounts', accounts);
  let [firstAccount] = accounts;

  AccountManager.getUserData(firstAccount, 'storedKey').then((storedData) => {
    // console.log('stored data for storeKey', storedData);

    AccountManager.setUserData(account, 'storedKey', JSON.stringify({foo: "bar"})).then(() => {
      // console.log('data successfully stored');
      })
    });
  })

```

# Troubleshooting

### `uid XXXX cannot explicitly add accounts of type: xxxx`

This means you're trying to access an account created with the same type but by an app with a different signature. The signature is defined in the signing of the app, defined by the keystore or upload key uploaded to the Play Store. You cannot solve this issue, all apps that share an account **must** have the same signature. [Learn more](https://developer.android.com/reference/android/accounts/AccountManager)
