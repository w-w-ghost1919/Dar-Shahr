package org.technocell.dar_shahr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class job_details extends AppCompatActivity {
    TextView mDescription_job_det,mNumber_job_det,mInstagram_job_det,mTelegram_job_det,mAddress_job_det;
    String AdLat,AdLong;
    MaterialDialog LoadDialog;
    RequestQueue queue;
    Bundle extras ;
    static JSONObject jObject;
    static JSONArray JArayobject = null;
    String SiteResponse;
    static SharedPreferences DetailShared;
    ImageView mJob_marker_back;
    Slider slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        init();

        mJob_marker_back.setOnClickListener(v -> {
            startActivity(new Intent(job_details.this, MainActivity.class));
            Animatoo.animateSlideUp(job_details.this);
        });

    }
    private void init(){
        slider= findViewById(R.id.slider);

        mJob_marker_back = findViewById(R.id.mJob_marker_back);
        DetailShared = getApplicationContext().getSharedPreferences("DetailShared", MODE_PRIVATE);
        AdLat = DetailShared.getString("Lat","0");
        AdLong = DetailShared.getString("Long","0");
        extras = getIntent().getExtras();
        mDescription_job_det = findViewById(R.id.mDescription_job_det);
        mNumber_job_det = findViewById(R.id.mNumber_job_det);
        mInstagram_job_det = findViewById(R.id.mInstagram_job_det);
        mTelegram_job_det = findViewById(R.id.mTelegram_job_det);
        mAddress_job_det = findViewById(R.id.mAddress_job_det);
        queue = Volley.newRequestQueue(this);
            GetDetails();
    }

    private void GetPics(String Pic1,String Pic2,String Pic3,String Pic4,String Pic5){
        List<Slide> slideList = new ArrayList<>();
        if(!Pic1.equals("Null")){
            slideList.add(new Slide(0,Pic1 , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        }

        if(!Pic2.equals("Null")){
            slideList.add(new Slide(1,Pic2, getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        }

        if(!Pic3.equals("Null")){
            slideList.add(new Slide(2,Pic3, getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));

        }if(!Pic4.equals("Null")){
            slideList.add(new Slide(3,Pic4 , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        }

        if(!Pic5.equals("Null")){
            slideList.add(new Slide(4,Pic5 , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
        }
        slider.addSlides(slideList);
    }

    private void GetDetails(){
        LoadDialog = new MaterialDialog.Builder(job_details.this).title("لطفا صبر کنید").content("درحال بارگیری اطلاعات").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://darshahr-org.com/AppEngine/PlaceRegistration.php",
                response -> {
                    Log.e("DResponse",response);
                    if (response.equals("1093")) {
                        LoadDialog.cancel();
                        TastyToast.makeText(getApplicationContext(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }else
                    {
                        try {
                            JArayobject = new JSONArray(response);
                                try {
                                    SiteResponse = JArayobject.getString(0);
                                    jObject= new JSONObject(SiteResponse);
                                    mDescription_job_det.setText(jObject.getString("context"));
                                    mNumber_job_det.setText(jObject.getString("phoneNumber"));
                                    mInstagram_job_det.setText(jObject.getString("Instagram"));
                                    mTelegram_job_det.setText(jObject.getString("Telegram")) ;
                       //             mAddress_job_det.setText(jObject.getString(""));
                                    GetPics(jObject.getString("Pic_1"),jObject.getString("Pic_2"),jObject.getString("Pic_3"),jObject.getString("Pic_4"),jObject.getString("Pic_5"));
                                    //geo

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            LoadDialog.cancel();
                        }catch (Exception err){
                            err.printStackTrace();
                        }
                    }
                },
                error -> {
                    TastyToast.makeText(getApplicationContext(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    LoadDialog.cancel();
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("RqType","GetPlaceDetails");
                params.put("latitude",AdLat);
                params.put("longitude",AdLong);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

}
