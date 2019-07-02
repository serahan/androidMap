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
                new LatLng(37.5666102, 126.9783881),
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

        // UI 설정
        // UiSettings : UI와 관련된 설정을 담당하는 클래스
        // UiSettings uiSettings = naverMap.getUiSettings();

        // 컨트롤 : 지도에 대한 정보 및 간단한 조작 기능을 제공하는 지도 위 버튼
        // compassEnabled : 나침반 활성화 여부
        // scaleBarEnabled : 축척 바 활성화 여부
        // zoomButtonEnabled : 줌 버튼 활성화 여부
        // indoorLevelPickerEnabled : 실내지도 층 피커 활성화 여부
        // locationButtonEnabled : 현위치 버튼 활성화 여부
        // uiSettings.setLocationButtonEnabled(true); // 현위치 활성화

        // uiSettings.setTiltGesturesEnabled(false); // 틸트 제스처 비활성화 ( 틸트 : 두개의 손가락으로 지도 위아래 드래그 -> 기울임 각도 변경)
        // uiSettings.setRotateGesturesEnabled(false); // 회전 제스처 비활성화

        // naverMap.setOnMapClickListener((pointF, coord) -> Toast.makeText(this, "클릭 : " + coord.latitude + ", " + coord.longitude,  Toast.LENGTH_SHORT).show());
        // naverMap.setOnMapLongClickListener(((pointF, coord) -> Toast.makeText(this, "롱 클릭 : " + coord.latitude + ", " + coord.longitude, Toast.LENGTH_SHORT).show()));

        // ############ 심볼 클릭 ################
        /* naverMap.setOnSymbolClickListener(symbol -> {
                if("군산대학교".equals(symbol.getCaption())){
                Toast.makeText(this, "군산대학교 클릭", Toast.LENGTH_SHORT).show();
                // 이벤트 소비, OnMapClick 이벤트는 발생하지 않음
                return true;
            }
            // 이벤트 전파, OnMapClick 이벤트가 발생함
            return false;
        }); */

        // ################# 마커 생성 #############
        /* Marker marker = new Marker();
        marker.setPosition(new LatLng(35.9424938, 126.683274));
        marker.setMap(naverMap);

        Marker marker2 = new Marker();
        marker2.setPosition(new LatLng(35.94310618, 126.68298307));
        marker2.setMap(naverMap);

        Marker marker3 = new Marker();
        marker3.setPosition(new LatLng(35.94298469, 126.68371497));
        marker3.setMap(naverMap); */

        // ########### 마커 이미지 #############
        // OverlayImage : 오버레이에서 사용할 수 있는 비트맵 이미지 클래스
        // drawable에 marker_icon 이라는 이름의 마커 이미지를 삽입
        // marker.setIcon(OverlayImage.fromResource(R.drawable.marker_icon));
        // marker.setWidth(50);
        // marker.setHeight(80);
        // marker.setAnchor(new PointF(1,1));

        // 마커에 색 입히기
        /*marker.setIcon(MarkerIcons.BLACK);
        marker.setIconTintColor(Color.RED);
        marker2.setIcon(MarkerIcons.BLACK);
        marker2.setIconTintColor(Color.YELLOW);
        marker3.setIconTintColor(Color.BLUE);

        // 마커의 아이콘에 원근 효과 부여
        marker.setIconPerspectiveEnabled(true);

        // 마커의 캡션 텍스트 지정
        marker.setCaptionText("Here!");

        // 마커의 캡션이 아이콘 위에 배치
        marker.setCaptionAlign(Align.Top);

        // ########### 마커가 클릭되면 '마커 클릭' 이라는 토스트 표시 #############
        marker.setOnClickListener(overlay -> {
            Toast.makeText(this, "마커 1 클릭", Toast.LENGTH_SHORT).show();
            // 이벤트 소비, OnMapClick 이벤트는 발생하지 않음
            return true;
        }); */

        // ############## 더블 탭했을 때 더블 탭된 지점의 좌표 표시. 화면 확대 X ###############
        /* naverMap.setOnMapDoubleTapListener((pointF, coord) -> {
            Toast.makeText(this,coord.latitude + ", " + coord.longitude,
                    Toast.LENGTH_SHORT).show();
            return true;
        }); */

        // ########### 지도를 두 손가락으로 탭했을 때 두 손가락 탭된 지점의 좌표를 토스트로 표시, 화면 축소 X ###################
        /* naverMap.setOnMapTwoFingerTapListener((pointF, coord) -> {
            Toast.makeText(this,coord.latitude + ", " + coord.longitude,
                    Toast.LENGTH_SHORT).show();
            return true;
        }); */

        // ############ 위치 표시 #########################
        // naverMap.setLocationSource(locationSource);
        // ############ 위치 추적 모드 ####################
        // None : 위치 추적 X
        // NoFollow : 위치 추적 활성화 O, 현위치 오버레이가 사용자 위치 따라 이동, 지도 이동 X
        // Follow : 위치 추적 활성화 O, 현위치 오버레이와 카메라의 좌표가 사용자 위치 따라 이동. 카메라 건드리면 NoFollow로 전환
        // Face : 위치 추적 활성화 O, 현위치 오버레이, 카메라의 좌표, 베어링이 사용자의 위치 및 방향 따라 이동. 카메라 건드리면 NoFollow로 전환
        // naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // #########################################################
        // 정보 창
        /* InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "Here!";
            }
        });
        infoWindow.open(marker2);

        marker.setTag("마커 1");
        marker.setOnClickListener(overlay -> {
            infoWindow.open(marker);
            return true;
        });

        // ############################################################
        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener(((pointF, latLng) -> {
            infoWindow.close();
        }));

        // 마커를 클릭하면 :
        Overlay.OnClickListener listener = overlay -> {
            Marker marker4 = (Marker)overlay;

            if(marker4.getInfoWindow() == null) {
                infoWindow.open(marker4);
            } else {
                infoWindow.close();
            }
            return true;
        };

        marker.setOnClickListener(listener);
        marker2.setOnClickListener(listener);
        marker3.setOnClickListener(listener);
        */

        // ########### 폴리라인 오버레이 #############
        //PolylineOverlay polyline = new PolylineOverlay();
        // ########### 폴리라인 오버레이 좌표열 지정 ################
        /* polyline.setCoords(Arrays.asList(
                new LatLng(37.57152, 126.97714),
                new LatLng(37.56607, 126.98268),
                new LatLng(37.56445, 126.97707),
                new LatLng(37.55855, 126.97822)
        ));
        polyline.setMap(naverMap);
        polyline.setWidth(10);
        polyline.setColor(Color.GREEN);
        polyline.setPattern(10,5);
        polyline.setCapType(PolylineOverlay.LineCap.Round);
        polyline.setJoinType(PolylineOverlay.LineJoin.Round); */

        // ########## 폴리곤 오버레이 #############
        /* PolygonOverlay polygon = new PolygonOverlay();
        polygon.setCoords(Arrays.asList(
                new LatLng(37.5640984, 126.9712268),
                new LatLng(37.5651279, 126.9767904),
                new LatLng(37.5625365, 126.9832241),
                new LatLng(37.5585305, 126.9809297),
                new LatLng(37.5590777, 126.974617)
        ));
        polygon.setMap(naverMap);

        // ############# 위치 변경 ##################
        List<LatLng> coords = new ArrayList<>();
        Collections.addAll(coords,
                new LatLng(37.5640984, 126.9712268),
                new LatLng(37.5651279, 126.9767904),
                new LatLng(37.5625365, 126.9832241),
                new LatLng(37.5585305, 126.9809297),
                new LatLng(37.5590777, 126.974617)
        );
        polygon.setCoords(coords);
        coords.set(0, new LatLng(37.5734571, 126.975335));
        // 아직 반영되지 않음
        polygon.setCoords(coords);
        // 반영됨

        // ############# 내부 홀 #################
        polygon.setHoles(Collections.singletonList(Arrays.asList(
                new LatLng(37.5612243, 126.9768938),
                new LatLng(37.5627692, 126.9795502),
                new LatLng(37.5628377, 126.976066)
        )));

        // ########## 면 색 변경 #############
        polygon.setColor(Color.GREEN);
        // ############# 테두리 두께 ############
        polygon.setOutlineWidth(5);
        // ############## 테두리 색상 #########
        polygon.setOutlineColor(Color.RED); */

        // ############ 서클 오버레이 ###############
        /* CircleOverlay circle = new CircleOverlay();
        circle.setCenter(new LatLng(37.5666102, 126.9783881));
        circle.setRadius(500);
        circle.setMap(naverMap);
        // circle.setMap(null); // 제거
        circle.setCenter(new LatLng(37.5666102, 126.9783881)); // 중심점 지정
        circle.setRadius(500); // 서클 오버레이의 반경 500미터 지정
        circle.setColor(Color.GREEN); // 색 지정
        circle.setOutlineWidth(10); // 테두리 두께
        circle.setOutlineColor(Color.BLUE); // 테두리 색상 */

        // ######################## 경로선 ######################
        /* PathOverlay path = new PathOverlay();
        path.setCoords(Arrays.asList(
                new LatLng(37.57152, 126.97714),
                new LatLng(37.56607, 126.98268),
                new LatLng(37.56445, 126.97707),
                new LatLng(37.55855, 126.97822)
        ));
        path.setMap(naverMap);

        path.setWidth(30); // 두께
        path.setOutlineWidth(5); // 테두리 두께

        // #################### 패턴 #############################
        path.setPatternImage(OverlayImage.fromResource(R.drawable.path_pattern));
        path.setPatternInterval(10); */

        // ############ 멀티파트 경로선 오버레이 ################
        /* MultipartPathOverlay multipartPath = new MultipartPathOverlay();

        multipartPath.setCoordParts(Arrays.asList(
                Arrays.asList(
                        new LatLng(37.5744287, 126.982625),
                        new LatLng(37.57152, 126.97714),
                        new LatLng(37.56607, 126.98268)
                ),
                Arrays.asList(
                        new LatLng(37.56607, 126.98268),
                        new LatLng(37.56445, 126.97707),
                        new LatLng(37.55855, 126.97822)
                )
        ));

        multipartPath.setColorParts(Arrays.asList(
                new MultipartPathOverlay.ColorPart(
                        Color.RED, Color.WHITE, Color.GRAY, Color.LTGRAY),
                new MultipartPathOverlay.ColorPart(
                        Color.GREEN, Color.WHITE, Color.DKGRAY, Color.LTGRAY)
        ));

        multipartPath.setMap(naverMap);


        // #################### 좌표열 파트 ###################
        multipartPath.setCoordParts(Arrays.asList(
                Arrays.asList(
                        new LatLng(37.5744287, 126.982625),
                        new LatLng(37.57152, 126.97714),
                        new LatLng(37.56607, 126.98268)
                ),
                Arrays.asList(
                        new LatLng(37.56607, 126.98268),
                        new LatLng(37.56445, 126.97707),
                        new LatLng(37.55855, 126.97822)
                )
        ));

        List<LatLng> coordPart1 = new ArrayList<>();
        Collections.addAll(coordPart1,
                new LatLng(37.5744287, 126.982625),
                new LatLng(37.57152, 126.97714),
                new LatLng(37.56607, 126.98268)
        );

        List<LatLng> coordPart2 = new ArrayList<>();
        Collections.addAll(coordPart2,
                new LatLng(37.56607, 126.98268),
                new LatLng(37.56445, 126.97707),
                new LatLng(37.55855, 126.97822)
        );

        List<List<LatLng>> coordParts = Arrays.asList(coordPart1, coordPart2);
        multipartPath.setCoordParts(coordParts);

        coordParts.get(0).set(0, new LatLng(37.5734571, 126.975335));
// 아직 반영되지 않음
        multipartPath.setCoordParts(coordParts);
// 반영됨

        // MultipartPathOverlay.ColorPart 객체 생성
        MultipartPathOverlay.ColorPart colorPart = new MultipartPathOverlay.ColorPart(
                Color.RED,   // 지나갈 경로선의 선 색상을 빨간색으로 지정
                Color.WHITE, // 지나갈 경로선의 테두리 색상을 흰색으로 지정
                Color.GRAY,  // 지나온 경로선의 선 색상을 회색으로 지정
                Color.LTGRAY // 지나온 경로선의 테두리 색상을 밝은 회색으로 지정
        );

        // ############# 멀티파트 경로선 오버레이에 두 개의 색상 파트 지정 ############
        multipartPath.setColorParts(Arrays.asList(
                new MultipartPathOverlay.ColorPart(
                        Color.RED, Color.WHITE, Color.GRAY, Color.LTGRAY),
                new MultipartPathOverlay.ColorPart(
                        Color.GREEN, Color.WHITE, Color.DKGRAY, Color.LTGRAY)
        )); */

        ArrowheadPathOverlay arrowheadPath = new ArrowheadPathOverlay();
        arrowheadPath.setCoords(Arrays.asList(
                new LatLng(37.568003, 126.9772503),
                new LatLng(37.5701573, 126.9772503),
                new LatLng(37.5701573, 126.9793745)
        ));
        arrowheadPath.setMap(naverMap);
        arrowheadPath.setWidth(20); // 두께
        arrowheadPath.setHeadSizeRatio(4); // 머리 크기
        arrowheadPath.setColor(Color.GREEN); // 색상
        arrowheadPath.setOutlineColor(Color.RED); // 테두리 색상
    }
}
