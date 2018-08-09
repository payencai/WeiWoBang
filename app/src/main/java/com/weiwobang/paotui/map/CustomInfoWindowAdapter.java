package com.weiwobang.paotui.map;

import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.Marker;

public class CustomInfoWindowAdapter implements AMap.InfoWindowAdapter {
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
