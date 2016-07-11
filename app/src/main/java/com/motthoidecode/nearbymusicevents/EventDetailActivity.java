package com.motthoidecode.nearbymusicevents;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import model.MusicEvent;
import utils.Config;

public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 14;

    private TextView tvEventTitle, tvLocation, tvDate, tvEventDetails, tvEventVenue, tvMore;
    private Button btnGetTickets;
    private ImageView ivEventDetail;
    private MapView mMapView;
    private GoogleMap mMap;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvEventTitle = (TextView) findViewById(R.id.tvEventTitle);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvEventDetails = (TextView) findViewById(R.id.tvEventDetails);
        tvEventVenue = (TextView) findViewById(R.id.tvEventVenue);
        tvMore = (TextView) findViewById(R.id.tvMore);
        btnGetTickets = (Button) findViewById(R.id.btnGetTickets);
        ivEventDetail = (ImageView) findViewById(R.id.ivEventDetail);
        mCurrentLocation = new Location("");

        MusicEvent musicEvent = new MusicEvent();
        Intent i = getIntent();
        musicEvent.setId(i.getStringExtra(Config.KEY_ID));
        musicEvent.setTitle(i.getStringExtra(Config.KEY_TITLE));
        musicEvent.setDescription(i.getStringExtra(Config.KEY_DESCRIPTION));
        musicEvent.setUrl(i.getStringExtra(Config.KEY_URL));
        musicEvent.setStart_time(i.getStringExtra(Config.KEY_START_TIME));
        musicEvent.setStop_time(i.getStringExtra(Config.KEY_STOP_TIME));
        musicEvent.setVenue_id(i.getStringExtra(Config.KEY_VENUE_ID));
        musicEvent.setVenue_name(i.getStringExtra(Config.KEY_VENUE_NAME));
        musicEvent.setVenue_address(i.getStringExtra(Config.KEY_VENUE_ADDRESS));
        musicEvent.setLongitude(i.getDoubleExtra(Config.KEY_LONGITUDE, 0));
        musicEvent.setLatitude(i.getDoubleExtra(Config.KEY_LATITUDE, 0));

        actionBar.setTitle(musicEvent.getTitle());

        tvEventTitle.setText(musicEvent.getTitle());
        tvLocation.setText(musicEvent.getVenue_address());
        tvDate.setText(musicEvent.getStart_time());
        tvEventDetails.setText(musicEvent.getDescription());
        tvEventVenue.setText(musicEvent.getVenue_name());
        if (musicEvent.getImageUrl() == null) {
            ivEventDetail.setBackgroundColor(Color.parseColor(MusicEventsNearbyActivity.ORANGE));
        } else
            Picasso.with(this).load(musicEvent.getImageUrl()).into(ivEventDetail);

        mCurrentLocation.setLatitude(musicEvent.getLatitude());
        mCurrentLocation.setLongitude(musicEvent.getLongitude());

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this);
        mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
        // Move the captureImage
        LatLng mLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, DEFAULT_ZOOM));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}