package com.gointegro.accountmanager;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReadableMap;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.R.string;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;


public class AccountManagerModule extends ReactContextBaseJavaModule {
	private ReactApplicationContext _reactContext = null;

	public AccountManagerModule(ReactApplicationContext reactContext) {
    super(reactContext);
		_reactContext = reactContext;
	}

	@Override
  public String getName() {
    return "AccountManager";
  }

	AccountManager manager = null;

	// Naive int to account mapping so our JS can simply reference native objects
	Integer accumulator = 0;
	HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();

	private Integer indexForAccount(Account account)
	{
		for(Entry<Integer, Account> e: accounts.entrySet())
		{
			if(e.getValue().equals(account))
			{
				return e.getKey();
			}
		}

		accounts.put(accumulator, account);
		return accumulator++;
	}

	@ReactMethod
	public void getAccountsByType (String accountType, Promise promise) {
			manager = AccountManager.get(_reactContext);
			Account[] account_list = manager.getAccountsByType(accountType);
			WritableNativeArray result = new WritableNativeArray();

			for(Account account: account_list)
			{
				Integer index = indexForAccount(account);

				WritableNativeMap account_object = new WritableNativeMap();
				account_object.putInt("_index", (int)index);
				account_object.putString("name", account.name);
				account_object.putString("type", account.type);
				result.pushMap(account_object);
			}

			promise.resolve(result);
	}

	@ReactMethod
	public void addAccountExplicitly (String accountType, String userName, String password, Promise promise) {
		manager = AccountManager.get(_reactContext);
		Account account = new Account(userName, accountType);
		Integer index = indexForAccount(account);
		Bundle userdata = new Bundle();

		if(false == manager.addAccountExplicitly(account, password, userdata)){
			promise.reject("Account with username already exists!");
			return;
		}

		WritableNativeMap result = new WritableNativeMap();
		result.putInt("_index", (int)index);
		result.putString("name", account.name);
		result.putString("type", account.type);

		promise.resolve(result);
		return;
	}

	@ReactMethod
	public void removeAccount (ReadableMap accountObject, Promise promise) {
		int index = accountObject.getInt("_index");
		Account account = accounts.get(index);
		if(account == null){
			promise.reject("Invalid account");
			return;
		}

		AccountManagerFuture<Boolean> future = manager.removeAccount(account, null, null);
		try {
			if(future.getResult() == true) {
				accounts.remove(index);
				promise.resolve(new WritableNativeMap());
			} else {
				promise.reject("Failed to remove account");
			}
		}
		catch (OperationCanceledException e) {
			promise.reject("Operation canceled: " + e.getLocalizedMessage());
		}
		catch (AuthenticatorException e) {
			promise.reject("Authenticator error: " + e.getLocalizedMessage());
		}
		catch (IOException e) {
			promise.reject("IO error: " + e.getLocalizedMessage());
		}

		return;
	}

	@ReactMethod
	public void setUserData (ReadableMap accountObject, String key, String data, Promise promise) {
		Account account = accounts.get(accountObject.getInt("_index"));
		if(account == null) {
			promise.reject("Invalid account");
			return;
		}
		manager.setUserData(account, key, data);
		promise.resolve(new WritableNativeMap());
		return;
	}

	@ReactMethod
	public void getUserData (ReadableMap accountObject, String key, Promise promise) {
		Account account = accounts.get(accountObject.getInt("_index"));
		if(account == null) {
			promise.reject("Invalid account");
			return;
		}

		WritableNativeMap result = new WritableNativeMap();
		result.putString("value", manager.getUserData(account, key));
		promise.resolve(result);
		return;
	}
}
