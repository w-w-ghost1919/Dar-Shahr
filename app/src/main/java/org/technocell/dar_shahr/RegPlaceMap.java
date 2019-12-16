package org.technocell.dar_shahr;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import ir.map.sdk_map.annotations.IconFactory;
import ir.map.sdk_map.annotations.Marker;
import ir.map.sdk_map.annotations.MarkerOptions;
import ir.map.sdk_map.camera.CameraUpdateFactory;
import ir.map.sdk_map.geometry.LatLng;
import ir.map.sdk_map.maps.MapView;
import ir.map.sdk_map.maps.MapirMap;

public class RegPlaceMap extends AppCompatActivity {
    MapView myMapView;
    locationService locationService = new locationService();
    MapirMap map;
    private Marker Mark;
    Intent iStartService;
    private LatLng MarkLocation = new LatLng();
    SharedPreferences.Editor editor;
    SharedPreferences LocationPref ;
    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, locationService.class);
        bindService(mIntent, mConnect, BIND_AUTO_CREATE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_place_map);
        locationService=new locationService(this);
        iStartService = new Intent(RegPlaceMap.this, locationService.class);
        startService(iStartService);
        locationService.getLocation();
        init();
    }
    ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            org.technocell.dar_shahr.locationService.LocalBinder mLocalBinder = (org.technocell.dar_shahr.locationService.LocalBinder)service;
            locationService = mLocalBinder.getServerInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void init(){
        LocationPref = getApplicationContext().getSharedPreferences("RandCode", MODE_PRIVATE);
        myMapView = findViewById(R.id.myMapView);
        myMapView.getMapAsync(mapirMap -> {
            initMap(mapirMap);
            mapirMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationService.getLatitude(), locationService.getLongitude()), 14));
            RegPlaceMap.this.map = mapirMap;
            if (RegPlaceMap.this.map != null) // Checks if we were successful in obtaining the map
                //mTehran object holds marker instance for future use like remove marker from Map
                map.setOnMapLongClickListener(new MapirMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng point) {
                        MarkLocation.setLatitude(point.getLatitude());
                        MarkLocation.setLongitude(point.getLongitude());
                        Mark = map.addMarker(new MarkerOptions()
                                .position(MarkLocation)
                                .title("ثبت")
                                .setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.locmarker))
                                .snippet("شغل"));

                        new MaterialDialog.Builder(RegPlaceMap.this).content("آیا مطمن هستید؟").cancelable(false)
                                .titleGravity(GravityEnum.START).contentGravity(GravityEnum.START)
                                .title("ثبت مکان").negativeText("بازگشت").positiveText("تایید")
                                .autoDismiss(false)
                                .onPositive((dialog, which) ->{
                                    startActivity(new Intent(RegPlaceMap.this, MainActivity.class).putExtra("Lat",String.valueOf(point.getLatitude())).putExtra("Long",String.valueOf(point.getLongitude())).putExtra("Bacon","1"));
                                    Animatoo.animateSlideUp(RegPlaceMap.this);
                                })

                                .onNegative((dialog, which) ->
                                        {
                                            startActivity(new Intent(RegPlaceMap.this, RegPlaceMap.class));
                                            Animatoo.animateSlideUp(RegPlaceMap.this);
                                        }
                                ).show();
                    }
                });
        });



        Exp();
    }
    private void initMap(MapirMap mapirMap) {
        map = mapirMap;
    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(RegPlaceMap.this, MainActivity.class));
        Animatoo.animateSlideUp(RegPlaceMap.this);
    }

    private void Exp(){
        new MaterialDialog.Builder(RegPlaceMap.this).content("برای ثبت مکان لطفا انگشت خود را بر روی مکان مورد نظر نگه دارید").cancelable(false)
                .titleGravity(GravityEnum.START).contentGravity(GravityEnum.START)
                .title("ثبت مکان").negativeText("تایید")
                .autoDismiss(false)
                .onNegative((dialog, which) ->
                        {
                            dialog.dismiss();
                        }
                ).show();
    }


}
