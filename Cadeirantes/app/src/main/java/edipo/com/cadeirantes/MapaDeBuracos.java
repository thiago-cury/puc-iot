package edipo.com.cadeirantes;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapaDeBuracos extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ArrayList<Localizacao> locs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_de_buracos);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locs = new ArrayList<>();


        //Firebase
        FirebaseApp.initializeApp(MapaDeBuracos.this);
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference banco = db.getReference("localizacao");

        banco.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                locs.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Localizacao l = data.getValue(Localizacao.class);
                    l.setKey(data.getKey()); //Colocando key manualmente no objeto
                    locs.add(l);
                }
                onMapReady(mMap);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Mostrando localização atual no mapa
        LatLng latLng = new LatLng(-29.76796313, -53.15129235);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //Movimentando o zoom da camera no map
        mMap.moveCamera(CameraUpdateFactory.zoomTo(6));

        if (!locs.isEmpty()) {
            for (int i = 0; i < locs.size(); i++) {
                mMap.addMarker(
                    new MarkerOptions().position(
                            new LatLng(locs.get(i).getLatitude(), locs.get(i).getLongitude())).
                            title("Buraco " + (i + 1)).
                            snippet("Buraco " + (i + 1)).
                            icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_no_acess)));
            }
        }
    }
}
