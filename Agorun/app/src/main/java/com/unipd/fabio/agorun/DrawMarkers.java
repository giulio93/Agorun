package com.unipd.fabio.agorun;

import android.os.AsyncTask;
import android.util.Log;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by fabio on 14/06/17.
 */

public class DrawMarkers extends AsyncTask<ListIterator, Void, Void> {

    MapsActivity mapsActivity;
    String sid;
    double lat;
    double lng;
    String[] session_point;
    Map<LatLng, String> map = new HashMap<>();
    String addrS;

    List<String> sids = new ArrayList<>();
    List<String[]> session_points = new ArrayList<>();


    @Override
    protected Void doInBackground(ListIterator... params) {
        mapsActivity = MapsActivity.getMapsData();

        String result = "";
        ListIterator it = params[0];

        while (params[0].hasNext()) {
            while (it.hasNext()) {

                result = result + (it.next());
                //System.out.println("RESULT = "+result);
                if (result.equals("Problems selecting activities")) {
                    System.out.println("NO ACTIVITIES");
                } else if (!(result.charAt(0) == 'C')) {  // Connection failed

                    mapsActivity.setConnections(0);

                    session_point = result.split(";");
                    session_points.add(session_point);

                    sids.add(session_point[0]);
                    Log.d("info", session_point[0]);
                    //                Log.d("sid", sid);

                    lat = Double.parseDouble(session_point[1]);
                    lng = Double.parseDouble(session_point[2]);
                    addrS = mapsActivity.geoLocateStart(lat, lng);
                    map.put(new LatLng(lat, lng), addrS);

                    for (String info : session_point) {
                        Log.d("info",info);
                    }

                    result = "";
                }
            }
        }
        publishProgress();

        return null;
    }


    @Override
    protected void onProgressUpdate(Void... params) {
        //System.out.println("RICHIAMO!!!!");

        int i = 0;
        for (LatLng latLng : map.keySet()) {

            Marker marker = mapsActivity.addMarkerToMap(false, sids.get(i), latLng.latitude, latLng.longitude, map.get(latLng), "");

            if (mapsActivity.getTempMarker() == null) {

                if (session_points.get(i).length == 4) {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
            }
            i++;
        }
    }
}
