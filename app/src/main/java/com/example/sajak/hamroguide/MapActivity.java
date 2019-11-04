package com.example.sajak.hamroguide;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.sajak.hamroguide.Agencies.AgenciesActivity;
import com.example.sajak.hamroguide.Places.PlacesActivity;
import com.example.sajak.hamroguide.news.NewsActivity;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    MapboxMap mapboxMap;
    MapView mapView;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "MapActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    Double originLat, originLon, desLat, desLong;
    String from;

    Point originPoint;
    Point destinationPoint;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token_mapbox));
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        originLat = bundle.getDouble("olat");
        originLon = bundle.getDouble("olong");
        desLat = bundle.getDouble("dlat");
        desLong = bundle.getDouble("dlong");
        from = bundle.getString("from");

        toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        findViewById(R.id.searchBtn).setVisibility(View.GONE);

        Log.e("Map Origin Lat", String.valueOf(originLat));
        Log.e("Map Origin Lon",String.valueOf(originLon));

        Log.e("point", String.valueOf(originLat));
        Log.e("point desLat",String.valueOf(desLat));

        if (originLat.equals(null) || originLon.equals(null) || desLat.equals(null) || desLong.equals(null)) {
            Log.e("Map Point", "Empty Filed");
        }
        else {
            originPoint = Point.fromLngLat(originLon, originLat);
            destinationPoint = Point.fromLngLat(desLong, desLat);
        }

        mapView = findViewById(R.id.myMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                addDestinationIconSymbolLayer(style);
                setGeoJson();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("My Position");
                IconFactory iconFactory = IconFactory.getInstance(MapActivity.this);
                Icon icon = iconFactory.fromResource(R.drawable.mymarker);
                markerOptions.icon(icon);
                markerOptions.position(new LatLng(originLat, originLon));
                mapboxMap.addMarker(markerOptions);
                destinationPoint = Point.fromLngLat(desLong, desLat);
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(desLat, desLong), 13.0));
                getRoute(originPoint, destinationPoint);
            }
        });

        findViewById(R.id.mapNavigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "Starting Navigation...", Toast.LENGTH_SHORT).show();
                boolean simulateRoute = true;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(simulateRoute)
                        .build();

                NavigationLauncher.startNavigation(MapActivity.this, options);
            }
        });

        findViewById(R.id.myLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(originLat, originLon), 13.0));
            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(from, "Agencies")) {
                    startActivity(new Intent(MapActivity.this, AgenciesActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(MapActivity.this, PlacesActivity.class));
                    finish();
                }
            }
        });
    }

    private void getRoute(Point originPoint, Point destinationPoint) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(originPoint)
                .destination(destinationPoint)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        Log.d(TAG, String.valueOf(response.code()));

                        if (response.body()==null){
                            Log.e(TAG, "No route found set user and access token well");
                            return;
                        }
                        else if (response.body().routes().size()<1){
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        if (navigationMapRoute !=null){
                            navigationMapRoute.removeRoute();
                        }
                        else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }

                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error: " + t.getMessage());

                    }
                });
    }

    private void setGeoJson() {
        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(destinationPoint);
        }
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.destinationmarker));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Objects.equals(from, "Agencies")) {
            startActivity(new Intent(MapActivity.this, AgenciesActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(MapActivity.this, PlacesActivity.class));
            finish();
        }
    }
}
