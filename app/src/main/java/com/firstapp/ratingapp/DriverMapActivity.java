package com.firstapp.ratingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverMapActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private DatabaseReference driversAvailableRef;
    private GeoFire geoFire;
    private Marker driverMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        driversAvailableRef = FirebaseDatabase.getInstance().getReference("driversAvailable");
        geoFire = new GeoFire(driversAvailableRef);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Adaugă aici logica de solicitare a permisiunilor, dacă nu au fost acordate
            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this); // Adaugă un ascultător pentru a permite mutarea marker-ului
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            mLastLocation = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            if (driverMarker != null) {
                driverMarker.remove(); // Șterge marker-ul anterior
            }

            driverMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Your Location")
                    .draggable(true)); // Activează mutarea marker-ului

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
        } else {
            Toast.makeText(this, "Nu s-a găsit locație.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Acțiuni atunci când se face o apăsare prelungită pe hartă
        if (driverMarker != null) {
            driverMarker.setPosition(latLng); // Mută marker-ul la noua locație
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            geoFire.setLocation(userId, new GeoLocation(latLng.latitude, latLng.longitude));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Adaugă aici logica de solicitare a permisiunilor, dacă nu au fost acordate
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Dacă conexiunea este întreruptă, poți adăuga logica corespunzătoare aici
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Dacă există o problemă de conectare, poți adăuga logica corespunzătoare aici
    }

    @Override
    protected void onStop() {
        super.onStop();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        geoFire.removeLocation(userId);
    }
}
