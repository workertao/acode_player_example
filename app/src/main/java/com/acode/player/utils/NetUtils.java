package com.acode.player.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * user:yangtao
 * date:2018/5/151041
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class NetUtils {
    public static final int TYPE_NONE = -1;
    public static final int TYPE_WIFI = 0;
    public static final int TYPE_MOBILE = 1;
    //网络监听
    private NetworkBroadcastReceiver mNetworkBroadcastReceiver;

    //上下文
    private Context context;

    //网络回调监听
    private OnNetBackListener onNetBackListener;

    public NetUtils setOnNetBackListener(OnNetBackListener onNetBackListener) {
        this.onNetBackListener = onNetBackListener;
        return this;
    }

    public interface OnNetBackListener {
        void onNetWork(int type);
    }

    public NetUtils(Context context) {
        this.context = context;
    }

    /***
     * 注册广播监听
     */
    public void registerReceiverNet() {
        if (mNetworkBroadcastReceiver == null) {
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mNetworkBroadcastReceiver = new NetworkBroadcastReceiver();
            context.registerReceiver(mNetworkBroadcastReceiver, intentFilter);
        }
    }

    /***
     * 取消广播监听
     */
    public void unNetworkBroadcastReceiver() {
        if (mNetworkBroadcastReceiver != null) {
            context.unregisterReceiver(mNetworkBroadcastReceiver);
        }
        mNetworkBroadcastReceiver = null;
    }


    /***
     * 网络监听类
     ***/
    private final class NetworkBroadcastReceiver extends BroadcastReceiver {
        long is = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                //获取ConnectivityManager对象对应的NetworkInfo对象
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    onNetBackListener.onNetWork(TYPE_WIFI);
                } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                    onNetBackListener.onNetWork(TYPE_WIFI);
                } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    onNetBackListener.onNetWork(TYPE_MOBILE);
                } else {
                    onNetBackListener.onNetWork(TYPE_NONE);
                }
            } else {
                //API大于23
                String action = intent.getAction();
                if (null != action && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    assert mConnectivityManager != null;
                    NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                    if (netInfo == null) {
                        if (onNetBackListener == null) {
                            return;
                        }
                        onNetBackListener.onNetWork(TYPE_NONE);
                        return;
                    }
                    if (netInfo != null && netInfo.isAvailable()) {
                        //移动网络
                        if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            //3g网络
                            if (System.currentTimeMillis() - is > 500) {
                                is = System.currentTimeMillis();
                            }
                            onNetBackListener.onNetWork(TYPE_MOBILE);
                            return;
                        }

                        //wifi
                        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            //WIFI网络播放
                            if (System.currentTimeMillis() - is > 500) {
                                is = System.currentTimeMillis();
                            }
                            onNetBackListener.onNetWork(TYPE_WIFI);
                            return;
                        }
                    }
                }
            }
        }
    }
}
