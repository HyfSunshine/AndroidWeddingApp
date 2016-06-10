package com.gemptc.wd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.x;

import java.util.List;

/**
 * Created by C5-0 on 2016/6/8.
 */
public class ProductAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context mContext;
    List<String> mImageUrl;

    public ProductAdapter(Context context, List<String> imageurl) {
        mContext = context;
        mImageUrl=imageurl;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mImageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class ViewHolder{
        ImageView imageView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.product_item,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.product_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
      x.image().bind(viewHolder.imageView, UrlAddress.PRODUCT_IMAGE_ADDRESS+mImageUrl.get(position));
        return convertView;
    }

}
