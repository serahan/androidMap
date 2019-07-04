package serahan.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.naver.maps.map.CameraUpdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private int whileMax = 4;

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
                new LatLng(35.945371, 126.682160),
                8,     // 줌 레벨
                0,     // 기울임 각도
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




        // ############ 마커 리스트로 구현 ###################
        List<Marker> markers = new ArrayList<>();       // 선언
        String[] Tags = {"군산대학교", "군산시청", "원광대학교", "전북대학교"};
        LatLng[] locations = {  new LatLng(35.945371, 126.682160),
                                new LatLng(35.967612, 126.736825),
                                new LatLng(35.969381, 126.957475),
                                new LatLng(35.846695, 127.129278) };

        for(int i=0;i<whileMax;i++)
        {
            markers.add(new Marker(locations[i]));
            markers.get(i).setTag(Tags[i]);
            markers.get(i).setMap(naverMap);
        }

        // ################ 정보창 #####################
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });

        for(int i=0;i<whileMax;i++)
        {
            int finalI = i;
            markers.get(i).setOnClickListener(overlay -> {
                infoWindow.open(markers.get(finalI));
                return true;
            });
        }

        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(
                markers.get(0).getPosition(),
                markers.get(1).getPosition(),
                markers.get(2).getPosition(),
                markers.get(3).getPosition()
        ));
        polyline.setMap(naverMap);

        // ################### 마커 4개의 중심부에서 카메라 시작 #####################
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i=0;i<whileMax;i++)
        {
            builder.include(markers.get(i).getPosition());
        }
        LatLngBounds latLngBounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdate.fitBounds(latLngBounds, 20);
        naverMap.moveCamera(cameraUpdate);

        // ########### 버튼 클릭 이벤트 ##############
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoWindow.isVisible()) {
                    infoWindow.setVisible(false);           // 정보창
                    for(int i=0;i<whileMax;i++)
                    {
                        markers.get(i).setMap(null);
                    }
                    polyline.setMap(null);                  // 셰이드
                } else if(!infoWindow.isVisible()){
                    for(int i=0;i<whileMax;i++)
                    {
                        markers.get(i).setMap(naverMap);
                    }
                    infoWindow.setVisible(true);            // 정보창
                    polyline.setMap(naverMap);              // 셰이드
                }
            }
        });
    }
}