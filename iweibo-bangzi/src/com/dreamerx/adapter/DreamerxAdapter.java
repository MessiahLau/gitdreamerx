package com.dreamerx.adapter;
import java.util.ArrayList;
import java.util.List;

import com.droidstouch.iweibo.R;
import com.droidstouch.iweibo.bean.listInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class DreamerxAdapter extends BaseAdapter{
	private Context context;//上下文
	private LayoutInflater inflater; //布局填充器
	private ViewHolder holder;
	private  List<listInfo> status;
	Bitmap bitmap =null;
	Bitmap userpic = null;
	Handler myHandler;

	public DreamerxAdapter(Context context,List<listInfo> status){
		if(null != status){
			this.status=status;
		}else{
			System.out.println("传进来了");
			this.status = new ArrayList<listInfo>();
		}
		
		this.context=context;
		inflater =LayoutInflater.from(context);	
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return status==null?0:status.size();
	}

	@Override
	public Object getItem(int arg0) {
		return status==null?null:status.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(null==status){
			return null;
		}else{
		holder = new ViewHolder();
		if(null == arg1){
			arg1=inflater.inflate(R.layout.dreamer_item_template, null);
			holder.content = (TextView) arg1.findViewById(R.id.item_content);
			holder.txt_username = (TextView)arg1.findViewById(R.id.txt_username);
			holder.image_userhead = (ImageView)arg1.findViewById(R.id.image_userhead);
			holder.item_image = (ImageView)arg1.findViewById(R.id.item_image);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		listInfo statu = status.get(arg0);
		holder.content.setText(statu.getContent());
		holder.txt_username.setText(statu.getUsername());
		if(statu.getUserphoto()!=null)
		{
			holder.image_userhead.setImageBitmap(statu.getUserphoto());
		}else{
			holder.image_userhead.setImageResource(R.drawable.ic_launcher);
		}
		if(statu.getPhoto()!=null)
		{
			holder.item_image.setImageBitmap(statu.getPhoto());
		}else{
			holder.item_image.setImageResource(R.drawable.ic_launcher);
		}
			return arg1;
		}
		
	}
	
	private class ViewHolder{
		private ImageView item_image;
		private ImageView image_userhead;
		private TextView txt_username;
		private TextView content;
	}

}
