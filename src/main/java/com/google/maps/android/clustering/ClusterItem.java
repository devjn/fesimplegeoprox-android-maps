package com.google.maps.android.clustering;

import com.google.android.gms.maps.model.LatLng;

//This class is intended only for proper serialization on common module
public interface ClusterItem {
    LatLng getPosition();

    String getTitle();

    String getSnippet();
}
