package org.ferriludium.simplegeoprox;

import com.google.maps.android.clustering.ClusterItem;

public abstract class MapClusterItem<T> implements ClusterItem {
    public final T clientObject;

    protected MapClusterItem(T clientObject) {
        this.clientObject = clientObject;
    }

    public T getObject() {
        return clientObject;
    }

    /**
     * Returns a verbose listing of the GeoObject (key, location, and contents) - NOTE
     * that this means the user-supplied contents should support a useful toString method.
     * @return String representation of the GeoObject
     */
    @Override
    public String toString() {
        return clientObject.toString() + " @" + getPosition().toString();
    }
}
