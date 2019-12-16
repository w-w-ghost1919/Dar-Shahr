package org.technocell.dar_shahr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.textfield.TextInputEditText;
import com.sdsmdg.tastytoast.TastyToast;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class security_code extends AppCompatActivity {
    TextInputEditText mEdit_txt_security;
    CardView cardView_two;
    Button mBtn_login_security_code;
    SharedPreferences SecCodePref ;
    String SecCode;
    MaterialDialog LoadDialog;
    TextView CountDown;
    ImageView resendbtn;
    int count = 60;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_code);
        Counter();
        init();
        mBtn_login_security_code.setOnClickListener(v -> {

       //     if(mEdit_txt_security.getText().toString().equals("")){
            //    TastyToast.makeText(getApplicationContext(), "لطفا کد ارسالی را وارد کنید.", TastyToast.LENGTH_LONG, TastyToast.WARNING);
        //    }else {
          //     CheckCode();
       //     }
            startActivity(new Intent(security_code.this, MainActivity.class));
            Animatoo.animateSlideUp(security_code.this);
        });

        resendbtn.setOnClickListener(v -> {
            if (flag) {
                startActivity(new Intent(security_code.this, login.class));
                Animatoo.animateSlideUp(security_code.this);
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    private void init(){
        mBtn_login_security_code = findViewById(R.id.mBtn_login_security_code);
        mEdit_txt_security= findViewById(R.id.mEdit_txt_security);
        cardView_two= findViewById(R.id.cardView_two);
        resendbtn = findViewById(R.id.resendbtn);
        CountDown = findViewById(R.id.CountDown);
        SecCodePref = getApplicationContext().getSharedPreferences("RandCode", MODE_PRIVATE);
        SecCode  = SecCodePref.getString("Code","");
    }
    @Override
    public void onBackPressed() {
    }
    private void CheckCode(){
        LoadDialog = new MaterialDialog.Builder(security_code.this).title("لطفا صبر کنید").content("درحال بررسی کد... ").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
        if(SecCode.equals(mEdit_txt_security.getText().toString().trim())){
            LoadDialog.cancel();
            startActivity(new Intent(security_code.this, MainActivity.class));
            Animatoo.animateSlideUp(security_code.this);
            TastyToast.makeText(getApplicationContext(), "خوش آمدید", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }else {
            LoadDialog.cancel();
            TastyToast.makeText(getApplicationContext(), "کد وارد شده اشتباه است!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }
    private void Counter() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                CountDown.setText(Integer.toString(count));
                count = count - 1;
            }

            public void onFinish() {
                CountDown.setVisibility(View.GONE);
                resendbtn.setVisibility(View.VISIBLE);
                flag = true;
            }
        }.start();
    }

}
