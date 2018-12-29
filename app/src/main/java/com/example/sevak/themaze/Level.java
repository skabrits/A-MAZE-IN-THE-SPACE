package com.example.sevak.themaze;

import android.provider.MediaStore;

public class Level {
    String data;
    String type;
    String name;
    Level(String data, String type, String name) {
        this.data = data;
        this.type = type;
        this.name = name;
    }
}
