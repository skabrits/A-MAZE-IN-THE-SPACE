package com.example.sevak.themaze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LANServsResiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //System.out.println(intent.toString()+"----------------------------------");
        if (!Client.runningServIPcontainer.contains(intent.getStringExtra("ipv4"))) {
            try {
                Client.gotBroadCast.put("ipv4");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Client.runningServs.get(intent.getStringExtra("ipv4")) == null || Client.runningServs.get(intent.getStringExtra("ipv4"))) {
                Client.avalibleServers.put(intent.getStringExtra("ipv4"), intent.getStringExtra("name"));
                Client.runningServIPcontainer.add(intent.getStringExtra("ipv4"));
                Client.runningServs.put(intent.getStringExtra("ipv4"), true);
            }
        }
    }
}
