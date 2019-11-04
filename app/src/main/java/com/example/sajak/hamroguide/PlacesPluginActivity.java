package com.example.sajak.hamroguide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.sajak.hamroguide.Agencies.AgenciesActivity;
import com.example.sajak.hamroguide.news.NewsActivity;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
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
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

/**
 * Use the places plugin to take advantage of Mapbox's location search ("geocoding") capabilities. The plugin
 * automatically makes geocoding requests, has built-in saved locations, includes location picker functionality,
 * and adds beautiful UI into Android project.
 */
public class PlacesPluginActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private MapView mapView;
    double lon;
    double lat;
    private MapboxMap mapboxMap;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    Toolbar toolbar;
    Point originPoint, destinationPoint;
    DirectionsRoute currentRoute;
    NavigationMapRoute navigationMapRoute;
    private static final String TAG = "PlacesPluginActivity";
    FloatingActionButton dirBtn, navBtn;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token_mapbox));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_places_plugin);

        toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        lat = bundle.getDouble("lat");
        lon = bundle.getDouble("long");

        dirBtn = findViewById(R.id.directionBtn);
        dirBtn.setVisibility(View.GONE);

        navBtn = findViewById(R.id.navigationBtn);
        navBtn.setVisibility(View.GONE);

        Toast.makeText(this, String.valueOf(lat), Toast.LENGTH_SHORT).show();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                initSearchFab();

                // Add the symbol layer icon to map for future use
                style.addImage(symbolIconId, BitmapFactory.decodeResource(
                        PlacesPluginActivity.this.getResources(), R.drawable.destinationmarker));

                // Create an empty GeoJSON source using the empty feature collection
                setUpSource(style);

                // Set up a new symbol layer for displaying the searched location's feature coordinates
                setupLayer(style);

//                addDestinationIconSymbolLayer(style);

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

                        if (response.body() == null) {
                            Log.e(TAG, "No route found set user and access token well");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
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

    private void addDestinationIconSymbolLayer(Style loadedMapStyle) {
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

    private void initSearchFab() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("My Position");
        IconFactory iconFactory = IconFactory.getInstance(PlacesPluginActivity.this);
        Icon icon = iconFactory.fromResource(R.drawable.mymarker);
        markerOptions.icon(icon);
        markerOptions.position(new LatLng(lat, lon));
        mapboxMap.addMarker(markerOptions);
        originPoint = Point.fromLngLat(lon, lat);
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 13.0));

        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findLocation();
            }
        });

        findViewById(R.id.myLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 13.0));
            }
        });

        dirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
                if (source != null) {
                    source.setGeoJson(destinationPoint);
                }
                Toast.makeText(PlacesPluginActivity.this, "You click direction", Toast.LENGTH_SHORT).show();
                getRoute(originPoint, destinationPoint);
                navBtn.setVisibility(View.VISIBLE);

            }
        });

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(true)
                        .build();

                NavigationLauncher.startNavigation(PlacesPluginActivity.this, options);

            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlacesPluginActivity.this, NewsActivity.class));
                finish();
            }
        });
    }

    private void findLocation() {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken())
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(PlacesPluginActivity.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    // Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);

                    destinationPoint = Point.fromLngLat(((Point) selectedCarmenFeature.geometry()).longitude(),
                            ((Point) selectedCarmenFeature.geometry()).latitude());
                }
            }
            dirBtn.setVisibility(View.VISIBLE);
        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlacesPluginActivity.this, NewsActivity.class));
        finish();
    }
}
