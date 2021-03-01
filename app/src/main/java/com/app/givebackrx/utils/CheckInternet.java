package com.app.givebackrx.utils;

import android.content.Context;

import com.app.givebackrx.di.ApplicationContext;
import com.app.givebackrx.utils.service.ConnectivityReceiver;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class CheckInternet {

    private Context mContext;

    @Inject
    public CheckInternet(@ApplicationContext Context context) {
        this.mContext = context;
    }

    public boolean checkConnectionActivity() {
        //        String message = "No Internet Connection";
        return ConnectivityReceiver.isConnected(mContext);
    }

}
