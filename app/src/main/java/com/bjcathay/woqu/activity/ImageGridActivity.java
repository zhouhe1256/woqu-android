package com.bjcathay.woqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.adapter.ImageGridAdapter;
import com.bjcathay.woqu.model.ImageItem;
import com.bjcathay.woqu.util.AlbumHelper;
import com.bjcathay.woqu.util.Bimp;
import com.bjcathay.woqu.adapter.ImageGridAdapter.TextCallback;
import com.bjcathay.woqu.util.ViewUtil;
import com.bjcathay.woqu.view.TopView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ImageGridActivity extends Activity implements OnClickListener{
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	private String content;
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt;
	private TopView topView;
	private String imageId;
	private int num;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					Toast.makeText(ImageGridActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);
		Intent intent=getIntent();
		imageId=intent.getStringExtra("imageId");

		num=(imageId==null?9:8);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
					/*Intent intent = new Intent(ImageGridActivity.this,
							PublishedActivity.class);
					intent.putExtra("content",content);
					startActivity(intent);*/
					Bimp.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < num) {
						Bimp.drr.add(list.get(i));
					}
				}
				finish();
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_back_img:
				finish();
				break;
		}
	}

	private void initView() {
		topView= ViewUtil.findViewById(this,R.id.top_title);
		topView.setTitleBackVisiable();
		topView.setTitleText("选择图片");
		content = getIntent().getStringExtra("content");
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler,num);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				adapter.notifyDataSetChanged();
			}

		});

	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("选择相册页面");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("选择相册页面");
		MobclickAgent.onPause(this);
	}
}
