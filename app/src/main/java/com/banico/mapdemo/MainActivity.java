package com.banico.mapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapView.setActivated(true);
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(MainActivity.this).build();
                            final LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions.builder(MainActivity.this, style).
                                    locationComponentOptions(locationComponentOptions)
                                    .useDefaultLocationEngine(true)
                                    .build();
                            LocationComponent locationComponent = mapboxMap.getLocationComponent();
                            locationComponent.activateLocationComponent(locationComponentActivationOptions);
                            locationComponent.setLocationComponentEnabled(true);
                            locationComponent.setRenderMode(RenderMode.COMPASS);
                            locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);

                            getRouting();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 9001);
                        }
                    }

                    private void getRouting() {
                        final MapboxNavigation mapboxNavigation = new MapboxNavigation(getApplicationContext(), "pk.eyJ1IjoieWFzYW1hbmkiLCJhIjoiY2szbGFpbWlwMDUxczNkczM2eGtjZ3pjaSJ9.5EKXazzgnvCpIS2L2SzqFA");
                        Point origin = Point.fromLngLat(35.6892, 51.3890);
                        Point destination = Point.fromLngLat(35.6880, 51.3890);
                        NavigationRoute.builder(MainActivity.this)
                                .accessToken("pk.eyJ1IjoieWFzYW1hbmkiLCJhIjoiY2szbGFpbWlwMDUxczNkczM2eGtjZ3pjaSJ9.5EKXazzgnvCpIS2L2SzqFA")
                                .origin(origin)
                                .destination(destination)
                                .build().getRoute(new Callback<DirectionsResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    DirectionsResponse directions = response.body();
                                    DirectionsRoute root = directions.routes().get(0);
                                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                            .waynameChipEnabled(true)
                                            .directionsRoute(root)
                                            .shouldSimulateRoute(false)
                                            .build();
                                    NavigationLauncher.startNavigation(MainActivity.this, options);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable t) {

                            }
                        });
                    }
                });
                mapboxMap.getUiSettings().setAllGesturesEnabled(true);
                mapboxMap.getUiSettings().setDoubleTapGesturesEnabled(true);
                mapboxMap.getUiSettings().setZoomGesturesEnabled(true);
                mapboxMap.getUiSettings().setQuickZoomGesturesEnabled(true);
                mapboxMap.getUiSettings().setAttributionEnabled(true);
                mapboxMap.getUiSettings().setCompassEnabled(true);
                mapboxMap.getUiSettings().setLogoEnabled(true);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 90001) {
            for (String permission :
                    permissions) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
