package com.gointegro.accountmanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service
{
    private Authenticator auth;

    @Override
    public void onCreate()
    {
    	auth = new Authenticator(this);
    }

    @Override
    public void onDestroy()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return auth.getIBinder();
    }
}
