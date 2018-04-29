package com.saluchen.thisproject.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.saluchen.thisproject.HomeActivity;
import com.saluchen.thisproject.MyItem;

public class CustomRenderer<T> extends DefaultClusterRenderer implements ClusterManager.OnClusterItemClickListener<ClusterItem> {

    private MyItem currentClickedClusterItem;
    private String TAG = "CustomRenderer";

    public CustomRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        setOnClusterItemClickListener(this);
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterItem item, MarkerOptions markerOptions) {
        Log.d(TAG,"cluster rendering");
        if (currentClickedClusterItem != null && item.equals(currentClickedClusterItem)) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }

    }

    @Override
    public boolean onClusterItemClick(ClusterItem item) {
        Log.d(TAG,"clicked cluster");
        if (currentClickedClusterItem != null) {
            getMarker(item).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        else {
            getMarker(item).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        return true;
    }
}