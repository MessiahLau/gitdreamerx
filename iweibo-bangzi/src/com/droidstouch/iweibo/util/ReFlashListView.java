package com.droidstouch.iweibo.util;


import com.droidstouch.iweibo.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
public class ReFlashListView extends ListView implements OnScrollListener{
	
	View header;
	int height;
	int firstVisibleItem;//当前可视页面的第一个list的Item
	int visibleItemCount;//当前可视的个数
	int totalItemCount;//总的item个数
	int scrollState;//listview滚动状态
	boolean isRemark;//标记当前的listview是否在最顶端摁下的
	int startY;//开始滚动的位置
	IReflashListener iReflashListener;//定义接口
	int state;//状态
	final int NONE =0;//正常状态
	final int PULL =1;//下拉状态
	final int RELESE =2;//释放状态
	final int REFLASHING =3;//正在刷新状态
	public ReFlashListView(Context context) {
		super(context);
		initView(context);
	}

	public ReFlashListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ReFlashListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	/*
	 * 初始化
	 * */
	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.header, null);
		measureView(header);
		height=header.getMeasuredHeight();
		topPadding(-height);
		this.addHeaderView(header);
		this.setOnScrollListener(this);
		
	}
	/*
	 * 通知父部件header的高度
	 * */
	private  void measureView(View view){
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if(p==null){
			p=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight=p.height;
		if(tempHeight>0){
			height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
		}else{
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		System.out.println("height="+height);
		view.measure(width, height);
	}
	
	/*
	 * 设置高度
	 * */
	private void topPadding(int topPadding){
		header.setPadding(header.getPaddingLeft(),topPadding,
				header.getPaddingRight(),header.getPaddingBottom());
		header.invalidate();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState=scrollState;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			System.out.println("action_down");
			if(firstVisibleItem == 0|| totalItemCount ==0){
				isRemark = true;
				System.out.println("action_down1");
				startY = (int)ev.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			System.out.println("move");
			onMove(ev);
			reflashViewByState();
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("ACTION_UP");
			if(state ==RELESE){
				state = REFLASHING;
				//刷新数据
				reflashViewByState();
				iReflashListener.onReflash(); 
			}else if(state == PULL){
				state = NONE;
				isRemark =false;
				reflashViewByState();
			}
			break;
			
		}
		return super.onTouchEvent(ev);
	}
	/*
	 * 监听动作
	 * */
	private void onMove(MotionEvent ev){
		if(!isRemark){
			return;
		}
		int tempY = (int)ev.getY();
		System.out.println(tempY);
		int space = tempY-startY;
		System.out.println(space);
		int topPadding = space - height;
		switch(state){
		case NONE:
			if(space>0){
				System.out.println("onmove_none");
				state = PULL;
				reflashViewByState();
			}
			break;
		case PULL:
			System.out.println("onmove_pull");
			System.out.println("scrollState="+scrollState);
			topPadding(topPadding);
			if(space>height+30&&
					(scrollState==SCROLL_STATE_TOUCH_SCROLL||totalItemCount==0)){
				state = RELESE;
				System.out.println("onmove_pull1");
				reflashViewByState();
			}
			break;
		case RELESE:
			System.out.println("onmove_relese");
			topPadding(topPadding);
			if(space<height+30){
				System.out.println("onmove_relese1");
				state = PULL;
				reflashViewByState();
			}else if(space<=0){
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		}
	}
	/*
	 * 下拉时界面的变化
	 * */
	private void reflashViewByState(){
		TextView tip = (TextView)header.findViewById(R.id.tip);
		ImageView arrow = (ImageView)header.findViewById(R.id.arrow);
		ProgressBar progress = (ProgressBar)header.findViewById(R.id.progress);
		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF,0.5f,
				RotateAnimation.RELATIVE_TO_SELF,0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF,0.5f,
				RotateAnimation.RELATIVE_TO_SELF,0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch(state){
		case NONE:
			topPadding(-height);
			arrow.clearAnimation();
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			arrow.clearAnimation();
			arrow.setAnimation(anim);
			break;
		case RELESE:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("下拉可以刷新");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case REFLASHING:
			topPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			tip.setText("松开可以刷新");
			arrow.clearAnimation();
			break;
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public void reflashComplete(){
		state = NONE;
		isRemark = false;
		reflashViewByState();
		/*TextView lastupdatetime = (TextView) header.findViewById(R.id.lastupdate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastupdatetime.setText(time);*/
	}
	public void setinterface(IReflashListener iReflashListener){
		this.iReflashListener=iReflashListener;
	}
	/*正在刷新接口*/
	public interface IReflashListener{
		public void onReflash();
	}
}
