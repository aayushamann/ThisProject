package com.saluchen.thisproject;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;
import com.saluchen.thisproject.models.PlaceInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<MyItem>, ClusterManager.OnClusterInfoWindowClickListener<MyItem>,
        ClusterManager.OnClusterItemClickListener<MyItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private AutoCompleteTextView mSearchText;
    private ImageView mGps, mInfo, mPlacePicker;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    android.widget.Button btn_gmap,btn_call,btn_whatsapp;
    android.support.design.widget.BottomSheetDialog dialog;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutoCompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private static final float DEFAULT_ZOOM = 15f;
    private PlaceInfo noPlaceInfo = null;
    private Marker mMarker;
    private String noTitle = "";
    private PlaceInfo mPlace;
    private String TAG = "HomeActivity";
    private FirebaseAuth mAuth;
    private ImageView dehaze;

    private TextView request_text;
    private TextView respond_text;
    private TextView status_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setBottomNavBar();

        dehaze = (ImageView) findViewById(R.id.ic_dehaze);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dehaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        View headerView = navigationView.getHeaderView(0);
        final TextView navHeaderText1 = headerView.findViewById(R.id.nav_header_text1);
        final TextView navHeaderText2 = headerView.findViewById(R.id.nav_header_text2);

        database.child("user").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                navHeaderText1.setText(userProfile.name);
                navHeaderText2.setText(userProfile.phone);
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        //mInfo = (ImageView) findViewById(R.id.place_info);
        //mPlacePicker = (ImageView) findViewById(R.id.place_picker);

        getLocationPermission();
        //initMap();
        //setUpClusterer();

    }

    private void setBottomNavBar(){
        request_text = (TextView) findViewById(R.id.request_text);
        respond_text = (TextView) findViewById(R.id.respond_text);
        status_text = (TextView) findViewById(R.id.status_text);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.request_action:
                                Toast.makeText(HomeActivity.this,"Request",Toast.LENGTH_SHORT).show();
                                request_text.setVisibility(View.VISIBLE);
                                respond_text.setVisibility(View.GONE);
                                status_text.setVisibility(View.GONE);
                                break;
                            case R.id.respond_action:
                                Toast.makeText(HomeActivity.this,"Respond",Toast.LENGTH_SHORT).show();
                                request_text.setVisibility(View.GONE);
                                respond_text.setVisibility(View.VISIBLE);
                                status_text.setVisibility(View.GONE);
                                break;
                            case R.id.status_action:
                                Toast.makeText(HomeActivity.this,"Status",Toast.LENGTH_SHORT).show();
                                request_text.setVisibility(View.GONE);
                                respond_text.setVisibility(View.GONE);
                                status_text.setVisibility(View.VISIBLE);
                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_sign_out) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this,
                    SignInActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(HomeActivity.this);
        Log.d(TAG, "initMap: Map initialized");
    }


    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            //getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        /*the default gps icon because is removed because that space will be used by search bar
        and we will make our own gps icon.
         */

        }
        mMap.setPadding(0,0,0,100);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(HomeActivity.this));
        init();
        setUpCircle();
    }

    private void setUpCircle(){
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(26.0000000, 82.0000000))
                .radius(1000)
                .strokeWidth(0)
                .strokeColor(Color.argb(128, 135, 206, 250))
                .fillColor(Color.argb(128, 135, 206, 250))
                .clickable(true));

        LatLngBounds bounds = toBounds(new LatLng(26.0000000, 82.0000000),1000);
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 10);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //Your code where exception occurs goes here...
                mMap.animateCamera(cameraUpdate);
            }
        });


        Log.d(TAG,bounds.northeast.toString());
        Log.d(TAG,bounds.southwest.toString());

        // mMap.addMarker(new MarkerOptions().position(bounds.northeast).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        //mMap.addMarker(new MarkerOptions().position(bounds.southwest).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        Toast.makeText(this, "Circle Drawn", Toast.LENGTH_SHORT).show();

        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

            @Override
            public void onCircleClick(Circle circle) {
                int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                circle.setStrokeColor(strokeColor);
            }
        });
        setUpClusterer();
        init_modal_bottomsheet();
    }

    private void setUpClusterer() {
        // Position the map.(26.2848686, 82.0805832)
        //getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.2848686, 82.0805832), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        // Add cluster items (markers) to the cluster manager.
        addItems();
        mClusterManager.cluster();
        MarkerManager.Collection x = mClusterManager.getClusterMarkerCollection();
        Log.d(TAG,x.toString());
        Toast.makeText(this, x.getMarkers().toString(), Toast.LENGTH_SHORT).show();
        MarkerManager.Collection y = mClusterManager.getMarkerCollection();
        Toast.makeText(this,y.getMarkers().toString(),Toast.LENGTH_SHORT).show();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 26.0000000;
        double lng = 82.0000000;

        //String title = "This is the title";
        //String snippet = "and this is the snippet.";

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 100; i++) {
            double offset = i / 6000d;
            lat = lat + offset;
            lng = lng + offset;
            String title = "This is the title";
            String snippet = "and this is the snippet.";
            // Create a cluster item for the marker and set the title and snippet using the constructor.
            MyItem infoWindowItem = new MyItem(lat, lng, title, snippet);
            // Add the cluster item (marker) to the cluster manager.
            mClusterManager.addItem(infoWindowItem);
        }
    }

    private GoogleMap getMap() {
        return mMap;
    }


    public void init_modal_bottomsheet() {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);

        dialog = new android.support.design.widget.BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        btn_call = (android.widget.Button) modalbottomsheet.findViewById(R.id.btn_call);
        btn_whatsapp = (android.widget.Button) modalbottomsheet.findViewById(R.id.btn_whatsapp);
        btn_gmap = (android.widget.Button) modalbottomsheet.findViewById(R.id.btn_gmap);
    }

    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

    private void init(){
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(keyEvent == null) {
                    Toast.makeText(HomeActivity.this, "Choose Appropriate Location", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(keyEvent!=null && actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

/*        mInfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: clicked place info");
            try{
                if(mMarker.isInfoWindowShown()){
                    mMarker.hideInfoWindow();
                }else{
                    Log.d(TAG, "onClick: place info: " + mPlace.toString());
                    mMarker.showInfoWindow();
                }
            }catch (NullPointerException e){
                Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
            }
        }
    });

    mPlacePicker.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.e(TAG, "onClick: GooglePlayServicesNotAvailableException: " + e.getMessage() );
            }
        }
    });*/

        hideSoftKeyboard();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            if(location!=null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM,
                                        "My Location", noPlaceInfo);
                            }
                            else{
                                Toast.makeText(HomeActivity.this, "Allow Location", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Log.d(TAG, "onCoplete: current location is null");
                            Toast.makeText(HomeActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
        catch (NullPointerException e){
            Toast.makeText(HomeActivity.this, "Allow Location", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title, PlaceInfo placeInfo){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //mMap.clear();

        if(placeInfo != null){
            try{
                Toast.makeText(this, "PlaceInfo Wala", Toast.LENGTH_SHORT).show();
                //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhonenumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);

                mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            Toast.makeText(this, "No PlaceInfo", Toast.LENGTH_SHORT).show();
            //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
            String snippet = "";
            if(!title.equals("My Location")){
                Toast.makeText(this, "Not my location", Toast.LENGTH_SHORT).show();
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                mMap.addMarker(options);
            }
            else{
                Toast.makeText(this, "My location", Toast.LENGTH_SHORT).show();
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .snippet("")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(options);
            }
        }

        hideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(HomeActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0),noPlaceInfo);
        }
        else{
            Toast.makeText(this, "Location Unavailable",Toast.LENGTH_SHORT).show();
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatLng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhonenumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());
                Log.d(TAG, "onResult: place: " + mPlace.toString());

            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude),DEFAULT_ZOOM,noTitle, mPlace);

            places.release();
        }
    };



    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onClusterClick(com.google.maps.android.clustering.Cluster<MyItem> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().getTitle();
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (com.google.maps.android.clustering.ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(com.google.maps.android.clustering.Cluster<MyItem> cluster) {
        // Does nothing, but you could go to a list of the users.

        Log.d(TAG, "onClusterInfoWindowClick");
        Toast.makeText(HomeActivity.this, "onClusterInfoWindowClick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onClusterItemClick(MyItem item) {
        Log.d(TAG, "onClusterItemClick");
        // Does nothing, but you could go into the user's profile page, for example.
        // Toast.makeText(MapActivity.this, item.getTitle()+"onClusterItemClick", Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem item) {

        final double lat = item.getPosition().latitude;
        final double lng = item.getPosition().longitude;
        final String task_title = item.getTitle();

        if(dialog.isShowing()){
            dialog.dismiss();
        }
        else{
            dialog.show();
        }

        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+"7052469479"));
                intent.setPackage("com.whatsapp");
                startActivity(intent);
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "7052469479"));
                startActivity(intent);
            }
        });

        btn_gmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + task_title + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        Log.d(TAG, "onClusterItemInfoWindowClick");
        // Does nothing, but you could go into the user's profile page, for example.
        Toast.makeText(HomeActivity.this, item.getPosition()+"onClusterItemInfoWindowClick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}