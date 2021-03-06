package ca.bcit.studybuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private String TAG = "LandingActivity";
    private GoogleMap mMap;
    private Map<Marker, Map<String, Object>> markers = new HashMap<>();
    private ArrayList<Map> locationsByDist;
    private ArrayAdapter<String> libraryListAdapter;
    private ListView libraryListView;
    private DrawerLayout drawer;
    private double currentX = 0.0;
    private double currentY = 0.0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static GoogleSignInAccount acct;
    public User user;
    private int previousRequestSize = 0;
    Bundle bundle;

    public LandingActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        System.out.println("HELLLOTEST");
        acct = GoogleSignIn.getLastSignedInAccount(this);
        libraryListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        libraryListView = (ListView) findViewById(R.id.library_list);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Hamburger menu bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#DE7070"));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set user img and name in the navigationView
        View headerView = navigationView.getHeaderView(0);
        ImageView userImage = headerView.findViewById(R.id.user_img);
        TextView userName = headerView.findViewById(R.id.user_name);
        Glide.with(this).load(acct.getPhotoUrl().toString()).into(userImage);
        userName.setText(acct.getDisplayName());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        realtimeProfileUpdater(acct.getId());

    }

    public void realtimeProfileUpdater(String googID) {
        try {
            db.collection("students").document(googID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }
                            if (snapshot != null && snapshot.exists()) {
                                Log.d(TAG, "Current data: " + snapshot.getData());
                                Map<String, Object> data = snapshot.getData();

                                user = new User(
                                        (String) data.get("name"),
                                        (String) data.get("location"),
                                        (String) data.get("major"),
                                        (String) data.get("phone"),
                                        (String) data.get("pk"),
                                        (String) data.get("school"),
                                        (ArrayList<String>) data.get("friends"),
                                        (ArrayList<String>) data.get("requests"),
                                        (ArrayList<String>) data.get("sentRequests"),
                                        (String) data.get("photoUrl")
                                );


                                if (user.requests != null && user.requests.size() > previousRequestSize) {
                                    previousRequestSize = user.requests.size();
                                    Toast.makeText(getBaseContext(), "New Request!", Toast.LENGTH_SHORT).show();
                                }
                                Log.d(TAG, user.toString());
                            } else {
                                Log.d(TAG, "Current data: null");
                            }
                        }
                    });
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }


    /**
     * When map is ready zooms in on current location.
     *
     * @param googleMap
     */
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
     * To forward to the clicked location intent.
     */
    public void onBottomLocationSelection(String locationName, String locationAddress, String locationPk) {
        //this is where next intent on location selection
//        Log.d(TAG, (String) locationsByDist.get(index).get("name"));

        CheckinActivity checkFrag = new CheckinActivity();
        bundle = new Bundle();
        bundle.putString("locationName", locationName);
        bundle.putString("locationAddress", locationAddress);
        bundle.putString("locationPk", locationPk);
        checkFrag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, checkFrag).commit();
    }

    /**
     * Sets the bottom bar with the closest locations.
     */
    public void setBottomBarLocations() {
        locationsByDist = new ArrayList<Map>(markers.values());
        Collections.sort(locationsByDist, new Comparator<Map>() {
            public int compare(Map o1, Map o2) {
                return (int) ((double) o1.get("distance") - (double) o2.get("distance"));
            }
        });
        libraryListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        for (int i = 0; i < locationsByDist.size() && i < 15; i++) {
            Log.d(TAG, locationsByDist.get(i).get("distance") + " " + locationsByDist.get(i).get("name"));
            libraryListAdapter.add((String) locationsByDist.get(i).get("name") + "\n" + locationsByDist.get(i).get("address"));
        }

        libraryListView = (ListView) findViewById(R.id.library_list);
        libraryListView.setAdapter(libraryListAdapter);
        libraryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String locationName = locationsByDist.get(position).get("name").toString();
                String locationAddress = locationsByDist.get(position).get("address").toString();
                String locationPk = locationsByDist.get(position).get("pk").toString();
                onBottomLocationSelection(locationName, locationAddress, locationPk);
            }
        });

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

    /**
     * Sets Map marker locations then calls setBottomBarLocaitons().
     */
    public void setLocations() {

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
//                CheckoutFragment checkOutFragment = new CheckoutFragment();
//                checkOutFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, checkOutFragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheckoutFragment()).commit();
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
     * Send a friend request.
     *
     * @param receiverGoogID
     */
    public void sendRequest(String receiverGoogID) {
        db.collection("students").document(user.pk)
                .update("sentRequests", FieldValue.arrayUnion(receiverGoogID));
        db.collection("students").document(receiverGoogID)
                .update("requests", FieldValue.arrayUnion(user.pk));

    }

    /**
     * Accept a friend request.
     *
     * @param googID
     */
    public void acceptRequest(String googID) {
        //remove request
        db.collection("students").document(user.pk)
                .update("requests", FieldValue.arrayRemove(googID));
        //remove sent request just incase
        db.collection("students").document(user.pk)
                .update("sentRequests", FieldValue.arrayRemove(googID));
        //add friend
        db.collection("students").document(user.pk)
                .update("friends", FieldValue.arrayUnion(googID));
        //remove request
        db.collection("students").document(googID)
                .update("sentRequests", FieldValue.arrayRemove(user.pk));
        //add friend
        db.collection("students").document(googID)
                .update("friends", FieldValue.arrayUnion(user.pk));

    }
    /**
     * Decline a friend request.
     *
     * @param googID
     */
    public void declineRequest(String googID) {
        //remove request
        db.collection("students").document(user.pk)
                .update("requests", FieldValue.arrayRemove(googID));
        //add friend
//        db.collection("students").document(user.pk)
//                .update("friends", FieldValue.arrayUnion(accepteGoogID));
        //remove request
        db.collection("students").document(googID)
                .update("sentRequests", FieldValue.arrayRemove(user.pk));
        //add friend
//        db.collection("students").document(accepteGoogID)
//                .update("friends", FieldValue.arrayUnion(user.pk));

    }
    /**
     * check into location
     *
     * @param locationID
     */
    public void checkIn(String locationID) {
        if(!user.location.equals(""))
            checkOut();
        db.collection("locations").document(locationID)
                .update("students", FieldValue.arrayUnion(acct.getId()));
        db.collection("students").document(acct.getId())
                .update("location", locationID);
        user.location = locationID;
    }

    /**
     * check out of location
     */
    public void checkOut() {
        db.collection("locations").document(user.location)
                .update("students", FieldValue.arrayRemove(user.pk));
        db.collection("students").document(user.pk)
                .update("location", "");
    }

}
