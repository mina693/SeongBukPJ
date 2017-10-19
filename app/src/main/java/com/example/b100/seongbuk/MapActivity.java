package com.example.b100.seongbuk;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static com.example.b100.seongbuk.R.id.map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    final private int MY_PERMISSION_REQUEST_LOCATION = 100;

    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    PolylineOptions polylineOption;
    static Button btn;
    /*Geocoder geocoder = new Geocoder(this, Locale.KOREA);*/
    GoogleMap gMap;
    private LocationListener mLocationListener;
    double latitude = 0.0;
    double longitude = 0.0;
    float precision = 0.0f;
    Button btn2;
    private ArrayList<LatLng> arraypoints;
    ArrayList<Double> array_lat;
    ArrayList<Double> array_long;
    String lon;
    String lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        btn = (Button) findViewById(R.id.mylocation_btn);
        polylineOption = new PolylineOptions();
        isPermissionGranted();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new MyConnectionCallBack())
                .addOnConnectionFailedListener(new MyOnConnectionFailedListener())
                .addApi(LocationServices.API)
                .build();

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                myLocation();
            }
        });

    }

    private boolean isPermissionGranted() {
        String[] PERMISSIONS_STORAGE = {    // 요청할 권한 목록을 설정
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MapActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                    PERMISSIONS_STORAGE,        // 요청할 권한 목록을 설정한 String 배열
                    MY_PERMISSION_REQUEST_LOCATION);   // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            return false;
        } else
            return true;
    }


    private class MyConnectionCallBack implements GoogleApiClient.ConnectionCallbacks {
        public void onConnected(Bundle bundle) {
            if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            precision = mCurrentLocation.getAccuracy();
            updateUI();

        } // 연결 성공시 호출


        public void onConnectionSuspended(int i) {
        } // 일시 연결 해제시 호출
    }

    private class MyOnConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    public void onMapReady(GoogleMap gMap) {

        LatLng location = new LatLng(latitude, longitude);
        EditText editText = (EditText) findViewById(R.id.edit);
        String stext = editText.getText().toString();

        gMap.addMarker(new MarkerOptions().position(location).title(stext));

        gMap.addMarker(new MarkerOptions().position(new LatLng(37.581217, 127.008056)).title("쓰레기통").icon(BitmapDescriptorFactory.fromResource(R.drawable.trash)).snippet("놀이광장 성벽 : 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.583051, 127.007093)).title("쓰레기통 & 포토존").icon(BitmapDescriptorFactory.fromResource(R.drawable.photo)).snippet("혜화방향 쉼터 : 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.582416, 127.006704)).title("쓰레기통 & 포토존").icon(BitmapDescriptorFactory.fromResource(R.drawable.photo)).snippet("제2전망광장 : 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.581190, 127.007085)).title("포토존").icon(BitmapDescriptorFactory.fromResource(R.drawable.photo)).snippet("제 1 전망광장 사거리 : 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.580187, 127.007603)).title("쓰레기통").icon(BitmapDescriptorFactory.fromResource(R.drawable.trash)).snippet("낙산 전시관: 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.579783, 127.008369)).title("포토존").icon(BitmapDescriptorFactory.fromResource(R.drawable.photo)).snippet("배드민턴장 : 숨겨진 QR쿠폰코드를 찾아보세요!"));

        gMap.addMarker(new MarkerOptions().position(new LatLng(37.579857, 127.007835)).title("쓰레기통 & 포토존").icon(BitmapDescriptorFactory.fromResource(R.drawable.photo)).snippet("숨겨진 QR쿠폰코드를 찾아보세요!"));

        gMap.addMarker(new MarkerOptions().position(new LatLng(37.585925, 127.020875)).title("쓰레기통 & 포토존").icon(BitmapDescriptorFactory.fromResource(R.drawable.photo)).snippet("보문3교 안암동 다리 밑 : 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.5899839, 127.0175895)).title("포토존").icon(BitmapDescriptorFactory.fromResource(R.drawable.photo)).snippet("인촌로 7길 오리동상: 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.589977, 127.016784)).title("쓰레기통").icon(BitmapDescriptorFactory.fromResource(R.drawable.trash)).snippet("인촌로 5길 조형물 앞: 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.591312, 127.013817)).title("쓰레기통").icon(BitmapDescriptorFactory.fromResource(R.drawable.trash)).snippet("성북천: 숨겨진 QR쿠폰코드를 찾아보세요!"));
        gMap.addMarker(new MarkerOptions().position(new LatLng(37.589934, 127.011090)).title("포토존").icon(BitmapDescriptorFactory.fromResource(R.drawable.photo)).snippet("동소문로 10길: 숨겨진 QR쿠폰코드를 찾아보세요!"));

        gMap.setOnInfoWindowClickListener(infoWindowClickListener);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.585925, 127.020875), 15));


    }

    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            lon = String.valueOf(marker.getPosition().longitude);
            lat = String.valueOf(marker.getPosition().latitude);

            AlertDialog.Builder ad = new AlertDialog.Builder(MapActivity.this);

            ad.setTitle(marker.getTitle());       // 제목 설정
            ad.setMessage(marker.getSnippet());   // 내용 설정

            ad.setPositiveButton("길찾기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse("https://www.google.co.kr/maps/place/" + lat + "," + lon);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    dialog.dismiss();     //닫기
                    // Event
                }

            });

            ad.setNeutralButton("QR코드인식", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MapActivity.this, CouponActivity.class);
                    startActivity(intent);
                    dialog.dismiss();     //닫기
                    // Event
                }
            });

            ad.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                    // Event
                }
            });
            ad.show();
        }
    };


    private void updateUI() {
        latitude = mCurrentLocation.getLatitude();
        longitude = mCurrentLocation.getLongitude();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

    }

    private void myLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        updateUI();
    }

}
