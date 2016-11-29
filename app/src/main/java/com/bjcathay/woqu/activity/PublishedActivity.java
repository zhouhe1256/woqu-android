package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.util.Bimp;
import com.bjcathay.woqu.util.ClickUtil;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.FileUtils;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.LoadingDialog;
import com.bjcathay.woqu.view.SelectPicPopupWindow;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PublishedActivity extends Activity implements View.OnClickListener,SelectPicPopupWindow.SelectResult{

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send;
	private TextView addressTextview;
	private EditText contentEditext;
	private ImageView backImageView;
	private ImageView addressDeleImageView;
	private ImageView addressImageView;
	private LinearLayout linear;
	private BroadcastReceiver broadcastReceiver;
	protected LoadingDialog progress;
	private ArrayList<String> list;
	private List<String> imageIds;
	private List<String> del = new ArrayList<String>();
	private String address;
	private String ids = "";
	private String content;
	private String imageId;
	private String imageUrl;
	private SelectPicPopupWindow menuWindow;

	String phoneName;
	private TopView topView;
	public static String LOCATION_BCR = "location_bcr";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectimg);
		WApplication.getInstance().addActivity(this);
		initialize();
		initView();
		initData();
		setListeners();
		WApplication.getInstance().requestLocationInfo();
	}

	private void setListeners() {
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {

				if(imageId!=null){
					if (arg2-1 == Bimp.bmp.size()) {
						menuWindow = new SelectPicPopupWindow(PublishedActivity.this, PublishedActivity.this);
						menuWindow.showAtLocation(PublishedActivity.this.findViewById(R.id.noScrollgridview),
								Gravity.BOTTOM
										| Gravity.CENTER_HORIZONTAL,
								0, 0); // 设置layout在PopupWindow中显示的位置
					} else {
						Intent intent = new Intent(PublishedActivity.this,
								PhotoActivity.class);
						intent.putExtra("ID", arg2);
						if (imageId != null) {
							intent.putExtra("imageUrl", imageUrl);
						}

						startActivity(intent);

					}
				}else {
					if (arg2 == Bimp.bmp.size()) {
						menuWindow = new SelectPicPopupWindow(PublishedActivity.this, PublishedActivity.this);
						menuWindow.showAtLocation(PublishedActivity.this.findViewById(R.id.noScrollgridview),
								Gravity.BOTTOM
										| Gravity.CENTER_HORIZONTAL,
								0, 0); // 设置layout在PopupWindow中显示的位置
					} else {
						Intent intent = new Intent(PublishedActivity.this,
								PhotoActivity.class);
						intent.putExtra("ID", arg2);
						if (imageId != null) {
							intent.putExtra("imageUrl", imageUrl);
						}
						startActivity(intent);

					}
				}
			}
		});

		addressTextview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String a = addressTextview.getText().toString().trim();
				if (a.equals("显示位置")){
					addressTextview.setText("正在定位");
					WApplication.getInstance().requestLocationInfo();
				}
			}
		});
		/**
		 * 返回键监听
		 */

		addressDeleImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				addressTextview.setText("显示位置");
				addressDeleImageView.setVisibility(View.GONE);
			}
		});

	}

	@Override
	public void resultCamera() {
		photo();
	}

	@Override
	public void resultPicture() {
		Intent intent = new Intent(PublishedActivity.this,
				SelectPicActivity.class);
		intent.putExtra("content",contentEditext.getText().toString());
		intent.putExtra("imageId",imageId);
	//	intent.setType("image/*");
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (ClickUtil.isFastClick()) {
			return;
		}
		switch (v.getId()){
			case R.id.title_back_img:
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				FileUtils.deleteDir();
				finish();
				break;
			case R.id.title_send:
				send();
				break;
		}
	}
public void send(){
	list = new ArrayList<String>();
	for (int i = 0; i < Bimp.drr.size(); i++) {
		String Str = Bimp.drr.get(i).substring(
				Bimp.drr.get(i).lastIndexOf("/") + 1,
				Bimp.drr.get(i).lastIndexOf("."));
		list.add(FileUtils.SDPATH+Str+".JPEG");
	}
	// 高清的压缩图片全部就在  list 路径里面了
	// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
	// 完成上传服务器后 .........
	//FileUtils.deleteDir();

	if(list.size()>0||imageId!=null){

		String content = contentEditext.getText().toString().trim();
		if(content.equals("")||content==null){
			content = "超幸运,我中奖啦...";
		}
		if(!content.equals("")||content!=null){
			if (progress == null) {
				progress = new LoadingDialog(this);
			}
			progress.show();
			if(addressTextview.getText().toString().trim().equals("显示位置")){
				address = null;
			}
			WApplication.getInstance().send_=true;


			WApplication.getInstance().startService(content,address,list,imageId);
			finish();
			overridePendingTransition(0, R.anim.base_slide_right_out);
		}else {
			DialogUtil.showMessage("请添写内容");
		}

	}else {
		DialogUtil.showMessage("至少上传一张图片");
	}




}

	private void initView() {
		topView=ViewUtil.findViewById(this,R.id.top_title);
		topView.setTitleBackVisiable();
		topView.setTitleSendVisiable();
		topView.setTitleText("晒单");
		linear = (LinearLayout) findViewById(R.id.published_linear);
		//activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
		addressTextview = (TextView) findViewById(R.id.address);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		contentEditext = (EditText) findViewById(R.id.content);
		addressDeleImageView = (ImageView) findViewById(R.id.address_dele);
		addressImageView = (ImageView) findViewById(R.id.address_image);
		if(getIntent().getStringExtra("imageId")!=null&&!getIntent().getStringExtra("imageId").equals("0")){
			imageId = getIntent().getStringExtra("imageId");
			imageUrl = getIntent().getStringExtra("imageUrl");
		}
		content = getIntent().getStringExtra("content");
		if(content!=null){
			contentEditext.setText(content);
		}
	}



	private void initialize() {
		registerBroadCastReceiver();
	}


	public void initData() {
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);

	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {



			if(imageId==null){
				return (Bimp.bmp.size() + 1)>9?9:(Bimp.bmp.size() + 1);
			}else{
				return (Bimp.bmp.size() + 2)>9?9:(Bimp.bmp.size() + 2);
			}

		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(final int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.delImage = (RelativeLayout) convertView
						.findViewById(R.id.item_del_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(imageId!=null){
				if(position==0&&Bimp.bmp.size()!= position-1){
					ImageViewAdapter.adapt(holder.image,imageUrl,true);
				}else{

				if(position-1 == Bimp.bmp.size()){
					holder.image.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.icon_addpic_unfocused));
					holder.delImage.setVisibility(View.GONE);
					if (position == 9) {
						holder.image.setVisibility(View.GONE);
						holder.delImage.setVisibility(View.GONE);

					}
				}else {
					holder.delImage.setVisibility(View.VISIBLE);
					if (Bimp.bmp.size() != 0) {
						holder.image.setImageBitmap(Bimp.bmp.get(position - 1));
					}
				}
				}
			}else{
			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				holder.delImage.setVisibility(View.GONE);
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
					holder.delImage.setVisibility(View.GONE);

					//todo
				}
			} else {
				holder.delImage.setVisibility(View.VISIBLE);
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}}
				holder.delImage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						if(imageId!=null){
							if(position==0){
								imageId=null;
							}else{
								String newStr = Bimp.drr.get(position-1).substring(
										Bimp.drr.get(position-1).lastIndexOf("/") + 1,
										Bimp.drr.get(position-1).lastIndexOf("."));

								Bimp.bmp.remove(position-1);
								Bimp.drr.remove(position-1);
								Bimp.max--;
								FileUtils.delFile(newStr + ".JPEG");
							}
							adapter.notifyDataSetChanged();
						}else{
						if (Bimp.drr.size() == 1) {
							Bimp.bmp.clear();
							Bimp.drr.clear();
							Bimp.max = 0;
							FileUtils.deleteDir();
						} else {
							String newStr = Bimp.drr.get(position).substring(
									Bimp.drr.get(position).lastIndexOf("/") + 1,
									Bimp.drr.get(position).lastIndexOf("."));

							Bimp.bmp.remove(position);
							Bimp.drr.remove(position);
							Bimp.max--;
							FileUtils.delFile(newStr + ".JPEG");
						}
						adapter.notifyDataSetChanged();
					}
					}
				});
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
			public RelativeLayout delImage;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {

						if (Bimp.drr.size()<=0||Bimp.drr == null||Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}



	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void photo() {
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/woqu/");
		if (!f.exists()){
			f.mkdirs();
		}
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(f, String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();

		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {
				case TAKE_PICTURE:
					if(imageId!=null){
						if (Bimp.drr.size() < 8 && resultCode == -1) {
							Bimp.drr.add(path);
						}
					}else{
					if (Bimp.drr.size() < 9 && resultCode == -1) {
						Bimp.drr.add(path);
					}
					}
			break;
		}
	}
	/**
	 * 注册一个广播，监听定位结果
	 */
	private void registerBroadCastReceiver()
	{
		broadcastReceiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				 address = intent.getStringExtra("address");
				if(address==null||address.equals("定位失败!")){
					DialogUtil.showMessage("请打开手机定位权限");
					addressTextview.setText("显示位置");
				}else{
				    addressTextview.setText(address);
					addressDeleImageView.setVisibility(View.VISIBLE);
				}
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction(LOCATION_BCR);
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Bimp.bmp.clear();
		Bimp.drr.clear();
		Bimp.max = 0;
		finish();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE );
				if (imm != null ) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					linear.requestFocus();
				}
			}
			return super .dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true ;
		}
		return onTouchEvent(ev);
	}
	public  boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			//获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false ;
			} else {
				return true ;
			}
		}
		return false ;
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("发布晒页面");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("发布晒页面");
		MobclickAgent.onPause(this);
	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		Bimp.bmp.clear();
//		Bimp.drr.clear();
//		Bimp.max = 0;
//	//	FileUtils.deleteDir();
//		finish();
//		return false;
//	}


}
