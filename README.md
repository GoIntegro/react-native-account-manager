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

#### With React Native 0.27+

```shell
react-native link react-native-account-manager
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
