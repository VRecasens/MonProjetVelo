package com.example.hb.monprojetvelo.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hb.monprojetvelo.R;
import com.example.hb.monprojetvelo.dao.DAOUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import greendao.Station;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    private Location location;
    public double latitude, longitude;
    private ArrayList<Station> alStation;
    private final int FINE_LOCATION_REQ_CODE = 0;
    private LatLngBounds bounds;
    private TextView txt;
    private Button boutonRafraichir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boutonRafraichir = (Button) findViewById(R.id.btRafraichir);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        alStation = new ArrayList<>();
        boutonRafraichir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChargementStation chargementStation = new ChargementStation();
                chargementStation.execute();
            }
        });
        ChargementStation chargementStation = new ChargementStation();
        chargementStation.execute();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        // Ajouter un marqueur à ma position
        LatLng maPosition = new LatLng(latitude, longitude);
        // Toast.makeText(MapsActivity.this, "latidude : longitude :" + latitude + longitude, Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(maPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        //mMap.addMarker(new MarkerOptions().position(toulouse).title("Marker in Toulouse"));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //On a la permission
            mMap.setMyLocationEnabled(true);
            // Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show();
        }
        else {
            //Etape 2 : On demande la permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQ_CODE);
        }

        afficherPointSurCarte();

        googleMap.setInfoWindowAdapter(this);

        //        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
        //            @Override
        //            public void onInfoWindowClick(Marker marker) {
        //                Station station = new Station();
        //                station = (Station) marker.getTag();
        //                Toast.makeText(MapsActivity.this, "informations station" + station.getAddress()
        //                        , Toast.LENGTH_SHORT).show();
        //
        //                //                LatLng latlng = marker.getPosition();
        //                //
        //                //                Intent intent = new Intent(MapsActivity.this, Main2Activity.class);
        //                //                intent.putExtra("ma station", latlng);
        //                //                startActivity(intent);
        //            }
        //        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //On a la permission
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
            // Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void afficherPointSurCarte() {
        if (mMap == null) {
            return;
        }

        mMap.clear();

        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE, minLng = Double.MAX_VALUE, maxLng = Double.MIN_VALUE;
        for (Station station : alStation) {
            if (station.getAvailable_bikes() != 0 && station.getAvailable_bike_stands() != 0) {
                Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(station
                        .getPositionLatLng()).title(station.getName() + station.getAvailable_bikes() + " " +
                        "Vélos Dispos" +
                        station.getAvailable_bike_stands() + " emplacements libres").snippet("" + station.getNumber()));

                //            Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(station
                //                    .getPositionLatLng()).title(station.getName() + station.getAvailable_bikes() + " " +
                //                    "Vélos Dispos" +
                //                    station.getAvailable_bike_stands() + " emplacements libres").snippet("" + station.getNumber()));
                marker.setTag(station);
            }
            if (station.getAvailable_bikes() == 0) {
                Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(station
                        .getPositionLatLng()).title(station.getName() + station.getAvailable_bikes() + " " +
                        "Vélos Dispos" +
                        station.getAvailable_bike_stands() + " emplacements libres").snippet("" + station.getNumber()));

                //            Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(station
                //                    .getPositionLatLng()).title(station.getName() + station.getAvailable_bikes() + " " +
                //                    "Vélos Dispos" +
                //                    station.getAvailable_bike_stands() + " emplacements libres").snippet("" + station.getNumber()));
                marker.setTag(station);
            }
            if (station.getAvailable_bike_stands() == 0) {
                Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).position(station
                        .getPositionLatLng()).title(station.getName() + station.getAvailable_bikes() + " " +
                        "Vélos Dispos" +
                        station.getAvailable_bike_stands() + " emplacements libres").snippet("" + station.getNumber()));

                //            Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(station
                //                    .getPositionLatLng()).title(station.getName() + station.getAvailable_bikes() + " " +
                //                    "Vélos Dispos" +
                //                    station.getAvailable_bike_stands() + " emplacements libres").snippet("" + station.getNumber()));
                marker.setTag(station);
            }
            if (minLat > station.getPosition().getLat()) {
                minLat = station.getPosition().getLat();
            }
            if (minLng > station.getPosition().getLng()) {
                minLng = station.getPosition().getLng();
            }
            if (maxLat < station.getPosition().getLat()) {
                maxLat = station.getPosition().getLat();
            }
            if (maxLng < station.getPosition().getLng()) {
                maxLng = station.getPosition().getLng();
            }
            //            minLat = Math.min(minLat, station.getPosition().getLat());
            //            minLng = Math.min(minLng, station.getPosition().getLng());
            //            maxLat = Math.max(maxLat, station.getPosition().getLat());
            //            maxLng = Math.max(maxLng, station.getPosition().getLng());
        }

        //        if (minLat != Double.MAX_VALUE) {
        //            bounds = new LatLngBounds(new LatLng(maxLat, maxLng), new LatLng(minLat, minLng));
        //        }
        //        else {
        //            LatLng toulouse = new LatLng(43.599998, 1.43333);
        //            bounds = new LatLngBounds(toulouse, toulouse);
        //        }
        //
        //        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
        LatLng toulouse = new LatLng(43.599998, 1.43333);

        mMap.addMarker(new MarkerOptions().position(toulouse).title("Marker in Toulouse"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toulouse, 14));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 14));
    }

    protected class ChargementStation extends AsyncTask<Void, Void, Pair<DAOUtils.FROM, ArrayList<Station>>> {

        @Override
        protected Pair<DAOUtils.FROM, ArrayList<Station>> doInBackground(Void... params) {
            return DAOUtils.getAllStation();
        }

        @Override
        protected void onPostExecute(Pair<DAOUtils.FROM, ArrayList<Station>> pair) {
            super.onPostExecute(pair);

            //
            //
            //            Gson gson = new Gson();
            //
            //            Station[] stations = gson.fromJson((String) o, Station[].class);
            //
            //
            //

            //
            if (pair.first == DAOUtils.FROM.BDD) {
                Toast.makeText(MapsActivity.this, "Erreur au chargement", Toast.LENGTH_SHORT).show();
            }

            alStation.clear();
            alStation.addAll(pair.second);
            afficherPointSurCarte();

            //    Log.w("GSON", "tab:##" + gson.toJson(stations) + "##");
            //Log.w("GSON", "tabResult1:##" + gson.toJson(new Station[]{st, st}) + "##");
            //ArrayList<Station> stations = new ArrayList<>();
            //            stations.add(st);
            //            stations.add(st);
            //            Log.w("GSON", "ALEesult1:##" + gson.toJson(stations) + "##");

            //            String monresultat = st.getName();
            //            Toast.makeText(MapsActivity.this, monresultat, Toast.LENGTH_SHORT).show();
            //            Station station = new Station();
            //            station.setId(st.getId());
            //            station.setNumber(st.getNumber());
            //            station.setName(st.getName());
            //            station.setAddress(st.getAddress());
            //            station.setPositionId(st.getPositionId());
            //            station.setBanking(st.getBanking());
            //            station.setBonus(st.getBonus());
            //            station.setStatus(st.getStatus());
            //            station.setContract_name(st.getContract_name());
            //            station.setBike_stands(st.getBike_stands());
            //            station.setAvailable_bike_stands(st.getAvailable_bike_stands());
            //            station.setAvailable_bikes(st.getAvailable_bikes());
            //            station.setLast_update(st.getLast_update());

            //    StationBDDManager.insertOrUpdate(station);

            //   Toast.makeText(BddActivity.this, "resultat", Toast.LENGTH_SHORT).show();
            //   alStation.add(station);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View view = getLayoutInflater().inflate(R.layout.activity_main2, null);

        txt = (TextView) view.findViewById(R.id.txtInfoVeloStation);
        Station station = new Station();
        station = (Station) marker.getTag();
        Toast.makeText(MapsActivity.this, "informations station" + station.getAddress(), Toast.LENGTH_SHORT).show();
        txt.setText("ma station  :" + station.getAddress() + station.getAvailable_bikes() + "nb vélos libres" + station.getAvailable_bike_stands() + " stands " +
                "libres pour la dépose");

        return view;
    }
}
