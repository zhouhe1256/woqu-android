package com.bjcathay.woqu.application;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.activity.PublishedActivity;

import java.io.IOException;
import java.util.List;

public class BaiduMapApplication extends Application
{
	public LocationClient mLocationClient = null;
//	public GeofenceClient mGeofenceClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public static String TAG = "BaiduMapApplication";
	
	private static BaiduMapApplication mInstance = null;

	@Override
	public void onCreate()
	{
		mInstance = this;
		//在些处添加
		SDKInitializer.initialize(this.getApplicationContext());
		mLocationClient = new LocationClient(this);

		/**
		 * 项目的key
		 */
	//	mLocationClient.setAK("0zY5wx1STQSzyaNv4Tvr7z0R");
		mLocationClient.registerLocationListener(myListener);
		//mGeofenceClient = new GeofenceClient(this);

		super.onCreate();
	//	Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
	}
	
	public static BaiduMapApplication getInstance()
	{
		return mInstance;
	}

	/**
	 * 停止定位
	 */
	public void stopLocationClient()
	{
		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.stop();
		} 
	}

	/**
	 * 发起定位
	 */
	public void requestLocationInfo()
	{
		setLocationOption();
		
		if (mLocationClient != null && !mLocationClient.isStarted())
		{
			mLocationClient.start();
		}

		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.requestLocation();
		} 
	}
	
	/**
	 *  设置相关参数
	 */
	private void setLocationOption()
	{
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setCoorType("bd09ll"); // 设置坐标类型
		//option.setServiceName("com.baidu.location.service_v2.9");//调用百度地图定位服务
		//option.setPoiExtraInfo(true);
		option.setAddrType("all");
		//option.setPoiNumber(10);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			if (location == null)
			{
				sendBroadCast("定位失败!");
				return;
			}

				//sendBroadCast(location.getDistrict()+","+location.getStreet());
			final String distict = location.getDistrict();
			GeoCoder mSearch = GeoCoder.newInstance();

				LatLng ptCenter = new LatLng(location.getLatitude(),
					location.getLongitude());
			    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(ptCenter));
			    mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
					@Override
					public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

					}

					@Override
					public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
						if (reverseGeoCodeResult!=null){
							List<PoiInfo> list = reverseGeoCodeResult.getPoiList();
							//Logger.i("location",list.get(0).name+","+list.get(1).name+","+list.get(3).name+","+list.get(2).name);
							if(list!=null){
								sendBroadCast(distict + "," + list.get(0).name);
							}else{
								sendBroadCast("定位失败!");
							}
						}else{
							sendBroadCast("定位失败!");
						}

					}
				});



		}

		public void onReceivePoi(BDLocation poiLocation)
		{
			if (poiLocation == null)
			{
				sendBroadCast("定位失败!");
				return;
			}
			//sendBroadCast(poiLocation.getAddrStr());
			sendBroadCast(poiLocation.getDistrict()+","+poiLocation.getStreet());
		}
		
	}
	
	/**
	 * 得到发送广播
	 * @param address
	 */
	public void sendBroadCast(String address)
	{
		stopLocationClient();
		
		Intent intent = new Intent(PublishedActivity.LOCATION_BCR);
		intent.putExtra("address", address);
		sendBroadcast(intent);
	}
}
