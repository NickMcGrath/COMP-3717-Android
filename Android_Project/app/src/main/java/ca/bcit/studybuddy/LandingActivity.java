package ca.bcit.studybuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private GoogleMap mMap;
    String TAG = "LandingPage";
    private Map<Marker, Map<String, Object>> markers = new HashMap<>();
    private DrawerLayout drawer;
    double currentX = 0.0;
    double currentY = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Hamburger menu bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "in onMapReady");
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (gpsLocation != null) {
                    currentX = gpsLocation.getLatitude();
                    currentY = gpsLocation.getLongitude();
                    LatLng currentLocation = new LatLng(currentX, currentY);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
                    googleMap.animateCamera(cameraUpdate);
                } else if (netLocation != null) {
                    currentX = netLocation.getLatitude();
                    currentY = netLocation.getLongitude();
                    LatLng currentLocation = new LatLng(currentX, currentY);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
                    googleMap.animateCamera(cameraUpdate);
                }
            }
        }
        setLocations();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Map dataModel = (Map) markers.get(marker);
                String title = (String) dataModel.get("pk");
                Log.d(TAG, title);
                return false;
            }
        });

    }

    /**
     * Sets the bottom bar with the closest locations.
     *
     * @// TODO: 2020-03-13 actually makes the bottom show LOL
     */
    public void setBottomBarLocations() {
        ArrayList<Map> markersByDist = new ArrayList<Map>(markers.values());
        Collections.sort(markersByDist, new Comparator<Map>() {
            public int compare(Map o1, Map o2) {
                return (int) ((double) o1.get("distance") - (double) o2.get("distance"));
            }
        });
        for (Map map : markersByDist) {
            Log.d(TAG, map.get("distance") + " " + map.get("name"));
        }

    }

    /**
     * Returns the distance between 2 points.
     */
    public double distanceFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        int meterConversion = 1609;
        return new Double(dist * meterConversion).floatValue();
    }

    public void setLocations() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                data.put("pk", document.getId());
                                data.put("distance", distanceFrom((double) data.get("x"), (double) data.get("y"), currentX, currentY));
                                //data values:
                                // document.getId(), (String) data.get("name"), (String) data.get("address"), (String) data.get("type"), (double) data.get("x"), (double) data.get("y"), (ArrayList) data.get("students")
                                LatLng point = new LatLng((double) data.get("x"), (double) data.get("y"));

                                if (((ArrayList) data.get("students")).size() > 0) {
                                    Log.d(TAG, document.getId() + " => " + "has: " + ((ArrayList) data.get("students")).size() + " students");
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(point).title((String) data.get("name") + "\nWith" + ((ArrayList) data.get("students")).size() + "Students!"));
                                    markers.put(marker, data);
                                } else {
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(point).title((String) data.get("name")));
                                    markers.put(marker, data);
                                    Log.d(TAG, document.getId() + " => " + "has: " + 0 + " students");
                                }
                            }
                            setBottomBarLocations();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(LandingActivity.this, LandingActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_your_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new YourProfileFragment()).commit();
                break;
            case R.id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
                break;
            case R.id.nav_study_tools:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudyToolsFragment()).commit();
                break;
            case R.id.nav_checkout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheckoutFragment()).commit();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "you have successfully logged out.", Toast.LENGTH_LONG).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This method is a little bit jank and should not be here LOL
     * It is here because getBaseContext() needs to be passed in from an activity.
     * For initially setting up all the libraries and schools found in the json files under app/assets
     */
    private void initFirebaseWithPositions() {
        DatabaseQueries.PositionDataPoint[] schools = DatabaseQueries.getSchoolsJSON(getBaseContext());
        DatabaseQueries.PositionDataPoint[] libraries = DatabaseQueries.getLibrariesJSON(getBaseContext());
        for (DatabaseQueries.PositionDataPoint school : schools) {
            DatabaseQueries.addToFireStoreCollection("locations", DatabaseQueries.DataPointToMap(school));
        }
        for (DatabaseQueries.PositionDataPoint library : libraries) {
            DatabaseQueries.addToFireStoreCollection("locations", DatabaseQueries.DataPointToMap(library));
        }
    }
}
