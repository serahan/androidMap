package serahan.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.ArrowheadPathOverlay;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.MultipartPathOverlay;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.overlay.PolygonOverlay;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ############### 위치 표시 ##################
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if(mapFragment == null) {
            NaverMapOptions options = new NaverMapOptions().zoomControlEnabled(false);
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        // ####### Zoom 레이아웃 변경 : 기본 줌 컨트롤을 비활성화하고 오른쪽 아래에 별도의 줌 컨트롤을 배치하는 예제 #######
        /* mapFragment.getMapAsync(naverMap -> {
            ZoomControlView zoomControlView = findViewById(R.id.zoom);
            zoomControlView.setMap(naverMap);
        }); */
    }

    // ############### 위치 표시 ################
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                    return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        // LatLng : 지도상의 위도, 경도 값을 저장하는 클래스
        // LatLng ( double latitude, double longitude )
        // LatLng location = new LatLng(35.9424938, 126.683274); // 군산대학교 아카데미홀 위치

        // 카메라 위치와 줌 조절 (숫자가 클수록 확대)
        //CameraPosition cameraPosition = new CameraPosition(location,17);
        //naverMap.setCameraPosition(cameraPosition);

        CameraPosition cameraPosition = new CameraPosition(
                //new LatLng(35.9424938, 126.683274), // 군산대학교 아카데미홀
                new LatLng(35.9437857, 126.681656),
                16,     // 줌 레벨
                45,     // 기울임 각도
                0     // 베어링 각도
        );
        naverMap.setCameraPosition(cameraPosition);

        // 줌 범위 제한
        naverMap.setMinZoom(5.0);
        naverMap.setMaxZoom(18.0);

        // 카메라 영역 제한
        LatLng northWest = new LatLng(31.43, 122.37);
        LatLng southEast = new LatLng(44.35, 132);
        naverMap.setExtent(new LatLngBounds(northWest, southEast));


        // ############### 군산대학교 마커 ####################
        Marker marker_kunsanUni = new Marker();  // 군산대
        marker_kunsanUni.setPosition(new LatLng(35.945371, 126.682160));
        marker_kunsanUni.setMap(naverMap);

        Marker marker_kunsanCenter = new Marker();  // 군산시청
        marker_kunsanCenter.setPosition(new LatLng(35.967612, 126.736825));
        marker_kunsanCenter.setMap(naverMap);

        Marker marker_WongoangUni = new Marker(); // 원광대
        marker_WongoangUni.setPosition(new LatLng(35.969381, 126.957475));
        marker_WongoangUni.setMap(naverMap);

        Marker marker_JunbukUni = new Marker(); // 전북대
        marker_JunbukUni.setPosition(new LatLng(35.846695, 127.129278));
        marker_JunbukUni.setMap(naverMap);
    }
}
