package org.technocell.dar_shahr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.textfield.TextInputEditText;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class register extends AppCompatActivity {

    TextView mTxt_city;
    ArrayList<String> items=new ArrayList<>();
    SpinnerDialog spinnerDialog;
    TextInputEditText NameInput,SurnameInput,PhoneInput;
    Button StateInput,RegiSterBtn;
    MaterialDialog LoadDialog;
    RequestQueue queue;
    SharedPreferences.Editor editor;
    SharedPreferences RandCodePref ;
    Random Rand = new Random();
    private int Randcode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       init();
        StateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });
        RegiSterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register();
            }
        });

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    private void init(){
        Randcode = Rand.nextInt(234561 + 989776) + 234561;
        queue = Volley.newRequestQueue(this);
        RandCodePref = getApplicationContext().getSharedPreferences("RandCode", MODE_PRIVATE);
        NameInput = findViewById(R.id.NameInput);
        SurnameInput = findViewById(R.id.SurnameInput);
        NameInput = findViewById(R.id.NameInput);
        PhoneInput = findViewById(R.id.PhoneInput);
        StateInput = findViewById(R.id.StateInput);
        RegiSterBtn = findViewById(R.id.RegiSterBtn);
        spinnerDialog = new SpinnerDialog(register.this,items,"شهر خود را انتخاب نمایید","استان");
        mTxt_city = findViewById(R.id.mTxt_city);
        items.add("کرمانشاه");
        // items.add("استان تهران");
        //  items.add("استان خراسان رضوی");
        //  items.add("استان اصفهان");
        //  items.add("استان فارس");
        //  items.add("استان خوزستان");
        //   items.add("استان آذربایجان شرقی");
        //  items.add("استان مازندران");
        //   items.add("استان آذربایجان غربی");
        //   items.add("استان کرمان");
        // items.add("استان سیستان و بلوچستان");
        //  items.add("استان البرز");
        //    items.add("استان گیلان");
        //   items.add("استان گلستان");
        //   items.add("استان هرمزگان");
        //   items.add("استان لرستان");
        //   items.add("استان همدان");
        //   items.add("استان کردستان");
        //   items.add("استان مرکزی");
        //    items.add("استان قم");
        //    items.add("استان قزوین");
        //   items.add("استان اردبیل");
        //   items.add("استان بوشهر");
        //   items.add("استان یزد");
        //   items.add("استان زنجان");
        //   items.add("استان چهارمحال و بختیاری");
        //   items.add("استان خراسان شمالی");
        //   items.add("استان خراسان جنوبی");
        //   items.add("استان کهگیلویه و بویراحمد");
        //  items.add("استان سمنان");
        //  items.add("استان ایلام");

        spinnerDialog=new SpinnerDialog(register.this,items,"استان خود را انتخاب کنید","بستن");// With No Animation
        spinnerDialog=new SpinnerDialog(register.this,items,"استان خود را انتخاب کنید",R.style.DialogAnimations_SmileWindow,"بستن");// With 	Animation
        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                mTxt_city.setText(item);
            }
        });
    }

    private void Register(){
        LoadDialog = new MaterialDialog.Builder(register.this).title("لطفا صبر کنید").content("درحال چک کردن اطلاعات شما").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://darshahr-org.com/AppEngine/RegisterUser.php",
                response -> {
                    Log.e("ResPonse",response.trim());
                    if (response.trim().equals("651")) {
                        LoadDialog.cancel();
                        TastyToast.makeText(getApplicationContext(), "یک حساب با این شماره وجود دارد.لطفا وارد شوید!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }else if(response.trim().equals("1093")){
                        LoadDialog.cancel();
                        TastyToast.makeText(getApplicationContext(), "مشکلی پیش امده است,لطفا دوباره امتحان کنید!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }else if(response.trim().equals("116")){
                        LoadDialog.cancel();
                        TastyToast.makeText(getApplicationContext(), "حساب کابری شما با موفقیت ساخته شد لطفا وارد شوید!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        startActivity(new Intent(register.this, login.class));
                        Animatoo.animateSlideUp(register.this);
                    }
                },
                error -> {
                    TastyToast.makeText(getApplicationContext(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    Log.e("ERR",error.getMessage());
                    LoadDialog.cancel();
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("RqType","CheckUser");
                params.put("PhoneNumber",PhoneInput.getText().toString().trim());
                params.put("firstName",NameInput.getText().toString().trim());
                params.put("Surname",SurnameInput.getText().toString().trim());
                params.put("City",mTxt_city.getText().toString().trim())    ;
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }
    private void SendSms(){
        Log.e("RandCode",String.valueOf(Randcode));
        LoadDialog = new MaterialDialog.Builder(register.this).title("لطفا صبر کنید").content("درحال پیامک به شما ").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://darshahr-org.com/AppEngine/RequestSms.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() == 8) {
                            //save code
                            editor = RandCodePref.edit();
                            editor.putString("Code",String.valueOf(Randcode));
                            editor.apply();
                            LoadDialog.cancel();
                            startActivity(new Intent(register.this, security_code.class));
                            Animatoo.animateSlideUp(register.this);
                        }else
                        {
                            LoadDialog.cancel();
                            TastyToast.makeText(getApplicationContext(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TastyToast.makeText(getApplicationContext(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        LoadDialog.cancel();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("RqType","SendSms");
                params.put("AuthCode",String.valueOf(Randcode));
                params.put("PhoneNumber","+98"+PhoneInput.getText().toString().trim());
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