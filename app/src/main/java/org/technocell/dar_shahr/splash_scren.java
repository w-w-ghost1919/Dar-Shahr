package org.technocell.dar_shahr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class splash_scren extends AppCompatActivity {
    RequestQueue queue;
    locationService locationService;
    Intent iStartService;
    SharedPreferences RandCodePref;
    String User;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scren);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    private void init(){
        RandCodePref = getApplicationContext().getSharedPreferences("RandCode", MODE_PRIVATE);
        User = RandCodePref.getString("User_Id","Null");
        Log.e("InitStarted","True");
        locationService=new locationService(this);
        iStartService = new Intent(splash_scren.this, locationService.class);
        startService(iStartService);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        queue = Volley.newRequestQueue(this);
        authorization();

    }
    private void authorization(){
        Log.e("Auth","True");
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://darshahr-org.com/AppEngine/authorizationRequest.php",
                response -> {
                    if (response.equals("1")) {
                        new MaterialDialog.Builder(splash_scren.this).content("دسترسی غیرمجاز.").cancelable(false)
                                .titleGravity(GravityEnum.START).contentGravity(GravityEnum.START)
                                .title("غیرمجاز").negativeText("خروج")
                                .autoDismiss(false)
                               .onNegative((dialog, which) ->
                                       {
                                           finish();
                                       }
                                       ).show();
                    }else
                    {
                        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            CheckPremission();
                        }else {
                            CheckPremission();
                        }

                    }
                },
                error -> TastyToast.makeText(getApplicationContext(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR)
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("RqType","CheckAuth");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }
    private void CheckPremission() {
        Log.e("CheckPrem","True");
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.e("if","True");
            CheckIsRegistered();
        }else {
            Log.e("else","True");

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    Log.e("mostafa","damaulavaaa");

                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_PERMISSIONS);
                }else {
                    CheckIsRegistered();
                }
            }

        }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CheckIsRegistered();
                } else {
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void CheckIsRegistered(){
        if(User.equals("Null")){
            startActivity(new Intent(splash_scren.this, login.class));
            Animatoo.animateSlideUp(splash_scren.this);
        }else {
            startActivity(new Intent(splash_scren.this, MainActivity.class));
            Animatoo.animateSlideUp(splash_scren.this);
        }
    }
    }
