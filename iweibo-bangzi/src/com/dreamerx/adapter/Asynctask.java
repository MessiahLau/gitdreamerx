package com.dreamerx.adapter;

import com.droidstouch.iweibo.bean.listInfo;
import com.droidstouch.iweibo.util.Request;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


public class Asynctask extends AsyncTask<Void, Void, Void> {
    private DreamerxAdapter adapter;
    // 初始化
    public Asynctask(Context context, DreamerxAdapter adapter) {
 	   this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 0; i < adapter.getCount(); i++) {
           listInfo bean = (listInfo) adapter.getItem(i);
           Bitmap bitmap = BitmapFactory
                  .decodeStream(Request
                          .HandlerData(bean.getPhotoUrl()));
           bean.setPhoto(bitmap);
           Bitmap userphoto = BitmapFactory
                   .decodeStream(Request
                           .HandlerData(bean.getUserphotoUrl()));
           bean.setUserphoto(userphoto);
           publishProgress(); // 通知去更新UI
        }

        return null;
    }

    public void onProgressUpdate(Void... voids) {
        if (isCancelled())
           return;
        // 更新UI
        adapter.notifyDataSetChanged();
    }
 }