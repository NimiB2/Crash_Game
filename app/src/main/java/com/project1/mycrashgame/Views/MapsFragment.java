package com.project1.mycrashgame.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project1.mycrashgame.R;

public class MapsFragment extends Fragment {
    private GoogleMap googleMap;
    private Runnable onMapReadyCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps));
        supportMapFragment.getMapAsync(googleMap -> {
            this.googleMap = googleMap;
            if (onMapReadyCallback != null) {
                onMapReadyCallback.run();
                onMapReadyCallback = null;
            }
        });
        //findViews(view);

        return view;
    }

    public void setOnMapReadyCallback(Runnable callback) {
        onMapReadyCallback = callback;
    }

    public void zoom(double lat, double lon, String playerName){
        if (googleMap == null) {
            setOnMapReadyCallback(() -> zoom(lat, lon, playerName));
            return;
        }
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lon)).title(playerName));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))
                .zoom(15)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
