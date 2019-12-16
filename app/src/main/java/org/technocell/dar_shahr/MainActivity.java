package org.technocell.dar_shahr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import ir.map.sdk_map.annotations.IconFactory;
import ir.map.sdk_map.annotations.Marker;
import ir.map.sdk_map.annotations.MarkerOptions;
import ir.map.sdk_map.camera.CameraUpdateFactory;
import ir.map.sdk_map.geometry.LatLng;
import ir.map.sdk_map.maps.MapView;
import ir.map.sdk_map.maps.MapirMap;
import ir.map.sdk_map.maps.OnMapReadyCallback;
import life.sabujak.roundedbutton.RoundedButton;

public class MainActivity extends AppCompatActivity {
    List<MainStroy_Items> itemlist;
    RecyclerView MainRecycler;
    static ViewPager viewPager;
    MainStory_Adapter mainStory_adapter;
    ImageView mIc_place_registration_menu, home;
    static RequestQueue queue,Queues;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    static   MapView myMapView;
    static   MapirMap map;
    private Uri filePath;
    static locationService locationService;
    static  private final int PICK_IMAGE_REQUEST = 71;
    ImageView mBtn_stoty_page_main;
    static   String SiteResponse;
    Bundle extras ;
    Intent iStartService;
    static String AdLat,AdLong;
    Bitmap bitmap;
    static  SharedPreferences.Editor editor;
    static JSONObject jObject,StoryJob;
   static JSONArray JArayobject = null;
   ImageView mIc_supporters_menu,mIc_discount_menu,mIc_grouping_menu,mIc_video_menu;
  static   SharedPreferences RandCodePref,DetailShared ;
    public static ImageView Pic_1,Pic_2,Pic_3,Pic_4,Pic_5;
    public static String Pic_1_String="Null",Pic_2_String="Null",Pic_3_String="Null",Pic_4_String="Null",Pic_5_String="Null",User_Id;
    public static TextView Pic_2_text,Pic_3_text,Pic_4_text,Pic_5_text;
    public static int ImageChoaser;
    static int Randcode=0;
    static   MaterialDialog LoadDialog;
    static Random Rand = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mIc_supporters_menu = findViewById(R.id.mIc_supporters_menu);
        mIc_discount_menu = findViewById(R.id.mIc_discount_menu);
        mIc_grouping_menu = findViewById(R.id.mIc_grouping_menu);
        mIc_video_menu = findViewById(R.id.mIc_video_menu);

        mIc_supporters_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(MainActivity.this, "بزودی...", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
            }
        });
        mIc_discount_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(MainActivity.this, "بزودی...", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
            }
        });
        mIc_grouping_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(MainActivity.this, "بزودی...", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
            }
        });
        mIc_video_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(MainActivity.this, "بزودی...", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            switch(ImageChoaser) {
                case 1:
                    SetImages(filePath,Pic_1,Pic_2,Pic_2_text);
                    Pic_1_String = encodedString;
                    break;
                case 2:
                    SetImages(filePath,Pic_2,Pic_3,Pic_3_text);
                    Pic_2_String = encodedString;

                    break;
                case 3:
                    SetImages(filePath,Pic_3,Pic_4,Pic_4_text);
                    Pic_3_String = encodedString;
                    break;
                case 4:
                    SetImages(filePath,Pic_4,Pic_5,Pic_5_text);
                    Pic_4_String = encodedString;
                    break;
                case 5:
                    SetImages(filePath,Pic_5,Pic_5,Pic_2_text);
                    Pic_5_String = encodedString;
                    break;
                default:

                    break;
            }


        }
        LoadDialog.cancel();
    }

    ServiceConnection mConnect = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            org.technocell.dar_shahr.locationService.LocalBinder mLocalBinder = (org.technocell.dar_shahr.locationService.LocalBinder)service;
            locationService = mLocalBinder.getServerInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        iStartService = new Intent(this, locationService.class);
        bindService(iStartService, mConnect, Context.BIND_AUTO_CREATE);
        startService(iStartService);

    }

    private void FetchStory(){
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://darshahr-org.com/AppEngine/StoryRequest.php",
                response -> {
                    if (response.equals("1093")) {
                        LoadDialog.cancel();
                        TastyToast.makeText(getApplicationContext(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }else
                    {
                        try {
                            JArayobject = new JSONArray(response);
                            for (int c =0;c<JArayobject.length();c++){
                                SiteResponse = JArayobject.getString(c);
                                StoryJob= new JSONObject(SiteResponse);
                                Log.e("Pics", StoryJob.getString("Story-PicUrl"));
                                    itemlist.add(
                                            new MainStroy_Items(
                                                    StoryJob.getString("Story-PicUrl")
                                            ));

                            }
                            mainStory_adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("RqType","FetchStory");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Queues.add(postRequest);
    }

    @Override
    public void onBackPressed() {
    }

    private void init(){
        itemlist = new ArrayList<>();
        MainRecycler = findViewById(R.id.MainRecycler);
        MainRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MainRecycler.setLayoutManager(layoutManager);
        mainStory_adapter = new MainStory_Adapter(MainActivity.this,itemlist);
        MainRecycler.setAdapter(mainStory_adapter);
        RandCodePref = getApplicationContext().getSharedPreferences("RandCode", MODE_PRIVATE);
        DetailShared = getApplicationContext().getSharedPreferences("DetailShared", MODE_PRIVATE);
        User_Id  = RandCodePref.getString("User_Id","");
        locationService=new locationService(this);
        mBtn_stoty_page_main = findViewById(R.id.mBtn_stoty_page_main);
        locationService.getLocation();
        viewPager = findViewById(R.id.ViewPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        mIc_place_registration_menu = findViewById(R.id.mIc_place_registration_menu);
        home = findViewById(R.id.home);
        queue = Volley.newRequestQueue(this);
        Queues = Volley.newRequestQueue(this);
        extras = getIntent().getExtras();
        if(extras == null){
            viewPager.setCurrentItem(0);
        }else {
            AdLat = extras.getString("Lat");
            AdLong = extras.getString("Long");
            String Bacon = extras.getString("Bacon");
            if(Bacon.equals("1")){
                viewPager.setCurrentItem(1);
            }else {
                viewPager.setCurrentItem(0);
            }
        }


        mIc_place_registration_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegPlaceMap.class));
                Animatoo.animateSlideUp(MainActivity.this);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        mBtn_stoty_page_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, profile.class));
                Animatoo.animateSlideUp(MainActivity.this);
            }
        });
        FetchStory();
    }

    private static void SetImages(Uri filePath,ImageView ImageShow,ImageView Img,TextView Txt){
        Picasso.get().load(filePath).into(ImageShow);
        Img.setVisibility(View.VISIBLE);
        Txt.setVisibility(View.VISIBLE);
    }
    private static void picimage(Activity activity){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
    }

    public static class PlaceholderFragment extends Fragment implements MapirMap.OnMarkerClickListener

    {
        private static final String ARG_SECTION_NUMBER = "section_number";

        EditText mEdit_txt_explain_store_reg,mEdit_txt_phone_number_store_reg,mEdit_txt_Cyberspace_instagram_store_reg,mEdit_txt_Cyberspace_telgram_store_reg;
        View rootView = null;
        ImageView CurrentLoc;
        Button mBtn_box_video;
        RoundedButton mBtn_confirmation_store_reg;
        private static String RQ_URL="http://darshahr-org.com/AppEngine/PlaceRegistration.php";


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        public PlaceholderFragment() {
        }


        private void initMap(MapirMap mapirMap) {
            // here Map created and you can use it
            map = mapirMap;
            map.setOnMarkerClickListener(this);

        }


        @Override
        public void onStart() {
            super.onStart();
            myMapView.onStart();
        }
        @Override
        public void onResume() {
            super.onResume();
            myMapView.onResume();
        }
        @Override
        public void onPause() {
            super.onPause();
            myMapView.onPause();
        }
        @Override
        public void onStop() {
            super.onStop();
            myMapView.onStop();
        }
        @Override
        public void onLowMemory() {
            super.onLowMemory();
            myMapView.onLowMemory();
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            myMapView.onDestroy();
        }
        @Override
        public void onSaveInstanceState(@NotNull Bundle outState) {
            super.onSaveInstanceState(outState);
            myMapView.onSaveInstanceState(outState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
            {
                rootView=inflater.inflate(R.layout.fragment_map, container, false);
                myMapView = rootView.findViewById(R.id.myMapView);
                CurrentLoc = rootView.findViewById(R.id.CurrentLoc);
                myMapView.onCreate(savedInstanceState);
                myMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapirMap mapirMap) {
                        initMap(mapirMap);
                        mapirMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationService.getLatitude(), locationService.getLongitude()), 14));
                        LoadDialog = new MaterialDialog.Builder(getActivity()).title("لطفا صبر کنید").content("درحال دریافت اطلاعات ").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
                        StringRequest postRequest = new StringRequest(Request.Method.POST,RQ_URL ,
                        response -> {
                      try {
                          if (response.trim().equals("null")){
                              LoadDialog.cancel();
                          }else {
                              JArayobject = new JSONArray(response);
                              for (int s = 0;s<=JArayobject.length();s++){
                                  try {
                                      SiteResponse = JArayobject.getString(s);
                                      jObject= new JSONObject(SiteResponse);
                                      map.addMarker(new MarkerOptions().setPosition(new LatLng(Double.parseDouble(jObject.getString("latitude")), Double.parseDouble(jObject.getString("longitude")))).setTitle(String.valueOf(s)).setIcon(IconFactory.getInstance(getContext()).fromResource(R.drawable.locmarker)));
                                  } catch (JSONException e) {
                                      e.printStackTrace();
                                  }
                              }
                              LoadDialog.cancel();
                          }

                    }catch (Exception err){
                        err.printStackTrace();
                    }
                        },
                        error -> {
                            TastyToast.makeText(getActivity(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            LoadDialog.cancel();
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("RqType","GetPlaceData");
                        return params;
                    }
                };
                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(postRequest);
                CurrentLoc.setOnClickListener(v -> map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationService.getLatitude(), locationService.getLongitude()), 14)));
                    }
                });

            }else if(getArguments().getInt(ARG_SECTION_NUMBER)==2) {
                Randcode = Rand.nextInt(234561 + 989776) + 234561;
                rootView=inflater.inflate(R.layout.fragment_store_registration, container, false);
                mEdit_txt_explain_store_reg  = rootView.findViewById(R.id.mEdit_txt_explain_store_reg);
                mEdit_txt_phone_number_store_reg = rootView.findViewById(R.id.mEdit_txt_phone_number_store_reg);
                mEdit_txt_Cyberspace_instagram_store_reg = rootView.findViewById(R.id.mEdit_txt_Cyberspace_instagram_store_reg);
                mEdit_txt_Cyberspace_telgram_store_reg = rootView.findViewById(R.id.mEdit_txt_Cyberspace_telgram_store_reg);
                mBtn_confirmation_store_reg = rootView.findViewById(R.id.mBtn_confirmation_store_reg);
                Pic_1 = rootView.findViewById(R.id.Pic_1);
                Pic_2 = rootView.findViewById(R.id.Pic_2);
                Pic_3 = rootView.findViewById(R.id.Pic_3);
                Pic_4 = rootView.findViewById(R.id.Pic_4);
                Pic_5 = rootView.findViewById(R.id.Pic_5);
                Pic_2_text = rootView.findViewById(R.id.Pic_2_text);
                Pic_3_text = rootView.findViewById(R.id.Pic_3_text);
                Pic_4_text = rootView.findViewById(R.id.Pic_4_text);
                Pic_5_text = rootView.findViewById(R.id.Pic_5_text);
                mBtn_box_video = rootView.findViewById(R.id.mBtn_box_video);
                    Pic_1.setOnClickListener(v1 -> {
                        LoadDialog = new MaterialDialog.Builder(getActivity()).title("لطفا صبر کنید").content("درحال بارگیری اطلاعات").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
                        picimage(getActivity());
                        ImageChoaser = 1;
                    });
                    Pic_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoadDialog = new MaterialDialog.Builder(getActivity()).title("لطفا صبر کنید").content("درحال بارگیری اطلاعات").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
                            picimage(getActivity());
                            ImageChoaser = 2;
                        }
                    });
                    Pic_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoadDialog = new MaterialDialog.Builder(getActivity()).title("لطفا صبر کنید").content("درحال بارگیری اطلاعات").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
                            picimage(getActivity());
                            ImageChoaser = 3;
                        }
                    });
                    Pic_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoadDialog = new MaterialDialog.Builder(getActivity()).title("لطفا صبر کنید").content("درحال بارگیری اطلاعات").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
                            picimage(getActivity());
                            ImageChoaser = 4;
                        }
                    });
                    Pic_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoadDialog = new MaterialDialog.Builder(getActivity()).title("لطفا صبر کنید").content("درحال بارگیری اطلاعات").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();
                            picimage(getActivity());
                            ImageChoaser = 5;
                        }
                    });
                    mBtn_box_video.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("Clicked Videos1","True");
                        }
                    });
                    mBtn_confirmation_store_reg.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            LoadDialog = new MaterialDialog.Builder(getActivity()).title("لطفا صبر کنید").content("درحال چک کردن اطلاعات شما").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("titr.ttf","titr.ttf").cancelable(false).show();

                            if(mEdit_txt_explain_store_reg.getText().toString().equals("")||mEdit_txt_phone_number_store_reg.getText().toString().equals("")||mEdit_txt_Cyberspace_instagram_store_reg.getText().toString().equals("")||mEdit_txt_Cyberspace_telgram_store_reg.getText().toString().equals("")){

                                TastyToast.makeText(getActivity(), "لطفا اطلاعات درخواستی را پر کنید.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }else {
                                StringRequest postRequest = new StringRequest(Request.Method.POST, "http://darshahr-org.com/AppEngine/PlaceRegistration.php",
                                        response -> {
                                    Log.e("Responsei",response);
                                            if (response.trim().equals("116")) {

                                                TastyToast.makeText(getActivity(), "درخواست شما با موفقیت ثبت شد!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                                LoadDialog.cancel();
                                                viewPager.setCurrentItem(0);
                                            }else if(response.trim().equals("1093"))
                                            {
                                                TastyToast.makeText(getActivity(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                                LoadDialog.cancel();
                                            }
                                        },error -> TastyToast.makeText(getActivity(), "مشکلی پیش آمده است لطفا دوباره امتحان کنید.", TastyToast.LENGTH_LONG, TastyToast.WARNING)
                                ) {
                                    @Override
                                    protected Map<String, String> getParams()
                                    {
                                        Map<String, String>  params = new HashMap<String, String>();
                                        params.put("RqType","RegisterPlace");
                                        params.put("U_Id",User_Id);
                                        params.put("Ad_Id","Ad_"+Randcode);
                                        params.put("context",mEdit_txt_explain_store_reg.getText().toString());
                                        params.put("phoneNumber",mEdit_txt_phone_number_store_reg.getText().toString());
                                        params.put("Instagram",mEdit_txt_Cyberspace_instagram_store_reg.getText().toString());
                                        params.put("Telegram",mEdit_txt_Cyberspace_telgram_store_reg.getText().toString());
                                        params.put("Pic_1",Pic_1_String);
                                        params.put("Pic_2",Pic_2_String);
                                        params.put("Pic_3",Pic_3_String);
                                        params.put("Pic_4",Pic_4_String);
                                        params.put("Pic_5",Pic_5_String);
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
                    });
            }
            // Inflate the layout for this fragment
            return rootView;
        }


        @Override
        public boolean onMarkerClick(@NonNull Marker marker) {
            LoadDialog = new MaterialDialog.Builder(getActivity()).title("لطفا صبر کنید").content("درحال بارگیری اطلاعات").progress(true, 0).titleGravity(GravityEnum.END).contentGravity(GravityEnum.END).typeface("IRANSansMobile.ttf","IRANSansMobile.ttf").cancelable(false).show();

            editor = DetailShared.edit();
            editor.putString("Lat", String.valueOf(marker.getPosition().getLatitude()));
            editor.putString("Long", String.valueOf(marker.getPosition().getLongitude()));
            editor.apply();
            startActivity(new Intent(getActivity(), job_details.class));
            Animatoo.animateSlideUp(Objects.requireNonNull(getActivity()));
            return false;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

}


