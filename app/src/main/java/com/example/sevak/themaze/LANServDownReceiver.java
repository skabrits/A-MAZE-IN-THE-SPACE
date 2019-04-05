package com.example.sevak.themaze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class LANServDownReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        Client.avalibleServers.remove(intent.getStringExtra("ipv4"));
        Client.runningServIPcontainer.remove(intent.getStringExtra("ipv4"));
        Client.runningServs.replace(intent.getStringExtra("ipv4"), false);
    }
}
