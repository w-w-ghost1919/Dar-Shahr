package org.technocell.dar_shahr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class login extends AppCompatActivity {



    Button mBtn_login;
    TextView mBtn_Register;
    TextInputEditText mEdit_txt_phone_number_login;
    private static long back_pressed;
    MaterialDialog LoadDialog;
    RequestQueue queue;
    private int Randcode=0;
    SharedPreferences.Editor editor;
    SharedPreferences RandCodePref ;
    Random Rand = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();



       TextView textview = findViewById(R.id.mBtn_Register);

        String text = "حساب کاربری ندارید؟ ثبت نام کنید.";
        SpannableString ss = new SpannableString(text);
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
       // ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
      //  BackgroundColorSpan bcsYellow = new BackgroundColorSpan(Color.YELLOW);


        ssb.setSpan(fcsRed, 20,28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  ssb.setSpan(fcsGreen, 16,20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       // ssb.setSpan(bcsYellow, 23,28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );


       // ssb.append("asdfghjkl");


       textview.setText(ssb);







        mBtn_Register.setOnClickListener(v -> {
            startActivity(new Intent(login.this, register.class));
            Animatoo.animateSlideUp(login.this);
        });
        mBtn_login.setOnClickListener(v -> {
            if(Objects.requireNonNull(mEdit_txt_phone_number_login.getText()).toString().equals("")){
                TastyToast.makeText(getApplicationContext(), "لطفا شماره تلفن خود را وارد کنید!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }else
            {
                Login();
            }

        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void Init(){
        mBtn_login = findViewById(R.id.mBtn_login);
        mBtn_Register = findViewById(R.id.mBtn_Register);
        mEdit_txt_phone_number_login  = findViewById(R.id.mEdit_txt_phone_number_login);
        Randcode = Rand.nextInt(234561 + 989776) + 234561;
        RandCodePref = getApplicationContext().getSharedPreferences("RandCode", MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);



    }

    @Override
    public void onBackPressed()
    {

    }

    private void Login(){
        LoadDialog = new MaterialDialog.Builder(login.this).title("لطفا صبر کنید").content("درحال چک کردن اطلاعات شما").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("titr.ttf","titr.ttf").cancelable(false).show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://darshahr-org.com/AppEngine/RegisterUser.php",
                response -> {
                    Log.e("ResPonse",response.trim());
                   if(response.trim().equals("701")){
                        LoadDialog.cancel();
                        TastyToast.makeText(getApplicationContext(), "اکانتی با این شماره وجود ندارد لطفا ثبت نام کنید!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }else{
                       LoadDialog.cancel();
                       editor = RandCodePref.edit();
                       editor.putString("User_Id","U_"+mEdit_txt_phone_number_login.getText().toString().trim());
                       editor.apply();
                       //SendSms();
                       startActivity(new Intent(login.this, security_code.class));
                       Animatoo.animateSlideUp(login.this);
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
                params.put("RqType","CheckLogin");
                params.put("PhoneNumber",mEdit_txt_phone_number_login.getText().toString().trim());
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
        LoadDialog = new MaterialDialog.Builder(login.this).title("لطفا صبر کنید").content("درحال پیامک به شما ").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://darshahr-org.com/AppEngine/RequestSms.php",
                response -> {
                    if (response.length() == 8) {
                        //save code
                        editor = RandCodePref.edit();
                        editor.putString("Code",String.valueOf(Randcode));
                        editor.putString("Code",String.valueOf(Randcode));
                        editor.apply();
                        LoadDialog.cancel();
                        startActivity(new Intent(login.this, security_code.class));
                        Animatoo.animateSlideUp(login.this);
                    }else
                    {
                        LoadDialog.cancel();
                        TastyToast.makeText(getApplicationContext(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
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
                params.put("RqType","SendSms");
                params.put("AuthCode",String.valueOf(Randcode));
                params.put("PhoneNumber","+98"+mEdit_txt_phone_number_login.getText().toString().trim());
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
