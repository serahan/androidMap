package serahan.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if(mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        // LatLng : 지도상의 위도, 경도 값을 저장하는 클래스
        // LatLng ( double latitude, double longitude )
        LatLng location = new LatLng(35.9424938, 126.683274); // 군산대학교 아카데미홀 위치

        // 카메라 위치와 줌 조절 (숫자가 클수록 확대)
        CameraPosition cameraPosition = new CameraPosition(location,17);
        naverMap.setCameraPosition(cameraPosition);

        // 줌 범위 제한
        naverMap.setMinZoom(5.0);
        naverMap.setMaxZoom(18.0);

        // 카메라 영역 제한
        LatLng northWest = new LatLng(31.43, 122.37);
        LatLng southEast = new LatLng(44.35, 132);
        naverMap.setExtent(new LatLngBounds(northWest, southEast));

        // 일반 지도
        // naverMap.setMapType(NaverMap.MapType.Basic);
        // 차량용 내비게이션 지도
        // naverMap.setMapType(NaverMap.MapType.Navi);
        // 위성 지도
        // naverMap.setMapType(NaverMap.MapType.Satellite);
        // 하이브리드 지도 (위성 사진 + 도로 + 심벌)
        // naverMap.setMapType(NaverMap.MapType.Hybrid);
        // 지형도
        // naverMap.setMapType(NaverMap.MapType.Terrain);
    }
}
