package com.droidstouch.iweibo.ui;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dreamerx.adapter.Asynctask;
import com.dreamerx.adapter.DreamerxAdapter;
import com.droidstouch.iweibo.R;
import com.droidstouch.iweibo.bean.listInfo;
import com.droidstouch.iweibo.util.ReFlashListView;
import com.droidstouch.iweibo.util.ReFlashListView.IReflashListener;

public class DreamerActivity extends Activity implements IReflashListener{
		private String UrlStr = "http://dreamerx.aliapp.com/servlet/FirstPageAction";
		private Button  write_bt= null;
		private ArrayList<listInfo> status = new ArrayList<listInfo>();
		private DreamerxAdapter adapter;
		private Handler myHandler;
		private ArrayList<HashMap<String, Object>> params;
		private ReFlashListView list = null;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.dreamer);
			list = (ReFlashListView)findViewById(R.id.lv_dreamer);
			list.setAdapter(adapter);
			status=null;
			new Thread()
			{
				public void run()
				{	
					String jsonStr = conn(UrlStr);
					if(jsonStr==null){
						myHandler.sendEmptyMessage(2);
					}else{
					try {
						params = Analysis(jsonStr);
						insertStatus(params);
					} catch (JSONException e) {
						e.printStackTrace();
					}
						myHandler.sendEmptyMessage(1);
					}
					
				}
			}.start();
			//////////////////////////////////////////////////////////////////////
			write_bt = (Button)findViewById(R.id.write_btn);
			write_bt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(DreamerActivity.this,PublishActivity.class);
					startActivity(intent);
				}
			});
			////////////////////////////////////////////////////////////////////////
			
			///////////////////////////////////////////////////////////////////////
			myHandler=new Handler()
			{
				public void handleMessage(android.os.Message msg)
				{
					if(msg.what==1)
					{
						showlist(status);
					}
					if(msg.what==2)
					{
						Toast.makeText(DreamerActivity.this, "请检查您的网络", Toast.LENGTH_LONG).show();
						/*adapter = new DreamerxAdapter(DreamerActivity.this,status);
						System.out.println(adapter);
						list.setAdapter(adapter);*/
					}
				};
			};
			
		}
		
		public String conn(String url){
			String jsonStr = null;
			String DreamerURL = url;
			HttpResponse response;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(DreamerURL);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("1","1"));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				response = httpClient.execute(httppost);
				// 连接超时
		         httpClient.getParams().setParameter(
		                 CoreConnectionPNames.CONNECTION_TIMEOUT, 100);
		         // 请求超时
		         httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
		                 100);
				if(response.getStatusLine().getStatusCode()==200){
					jsonStr=EntityUtils.toString(response.getEntity());
					return jsonStr;
				}
				}catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			return null;
		}
		/*解析*/
		private static ArrayList<HashMap<String, Object>> Analysis(String jsonStr)
	            throws JSONException {
	        JSONArray jsonArray = null;
	        // 初始化list数组对象
	        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	        jsonArray = new JSONArray(jsonStr);
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	            // 初始化map数组对象
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            map.put("content", jsonObject.getString("itemcontent"));
	            map.put("item_imageURL", jsonObject.getString("itempicture"));
	            map.put("txt_username", jsonObject.getString("username"));
	            map.put("image_userheadURL", jsonObject.getString("userpicture"));
	            map.put("item_id",jsonObject.getString("itemid"));
	            list.add(map);
	        }
	        return list;
	    }
		/*
		 * 将解析出来的ArrayList<HashMap<String, Object>> 装入ArrayList<Status>中
		 * */
		private void insertStatus(ArrayList<HashMap<String, Object>> params){
			
			for(int i=0;i<params.size();i++){
				listInfo statu=new listInfo();
				statu.setContent(params.get(i).get("content").toString());
				statu.setPhotoUrl(params.get(i).get("item_imageURL").toString());
				statu.setUsername(params.get(i).get("txt_username").toString());
				statu.setUserphotoUrl(params.get(i).get("image_userheadURL").toString());
				statu.setId(params.get(i).get("item_id").toString());
				status.add(statu);
			}
		}
		////////////////////////////////////////////////////////////////
		private void refreshStatus(ArrayList<HashMap<String, Object>> params){
			
			for(int i=0;i<params.size();i++){
				listInfo statu=new listInfo();
				statu.setContent(params.get(i).get("content").toString());
				statu.setPhotoUrl(params.get(i).get("item_imageURL").toString());
				statu.setUsername(params.get(i).get("txt_username").toString());
				statu.setUserphotoUrl(params.get(i).get("image_userheadURL").toString());
				statu.setId(params.get(i).get("item_id").toString());
				status.add(statu);
			}
		}

		@Override
		public void onReflash() {
			//获取最新数据
			new Thread(){
				public void run()
				{	
					String jsonStr = conn(UrlStr);
					if(jsonStr==null){
						myHandler.sendEmptyMessage(2);
					}else{
						try {
							params = Analysis(jsonStr);
							refreshStatus(params);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						myHandler.sendEmptyMessage(1);
					}
				}
			}.start();
			//通知界面显示数据
			//通知listview刷新数据完毕
			
		}
		private void showlist(ArrayList<listInfo> params){
			if(adapter == null){
				list.setinterface(this);
				System.out.println("show");
				adapter = new DreamerxAdapter(DreamerActivity.this,status);
				list.setAdapter(adapter);
				new Asynctask(DreamerActivity.this,adapter).execute();
			}else{
				adapter.notifyDataSetChanged();
				new Asynctask(DreamerActivity.this,adapter).execute();
				list.reflashComplete();
			}
		}
	
}
