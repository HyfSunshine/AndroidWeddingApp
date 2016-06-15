package com.gemptc.wd.activities.mine.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class ColleProductAdapter extends BaseAdapter{

    private List<ProductBean> productList;
    private Context context;

    public ColleProductAdapter(Context context, List<ProductBean> productList) {
        this.context=context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView = View.inflate(context,R.layout.mine_items_coll_product_listview,null);
            holder.iv_productImage= (ImageView) convertView.findViewById(R.id.iv_productImage);
            holder.tv_productName= (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tv_collNum= (TextView) convertView.findViewById(R.id.tv_collNum);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        x.image().bind(holder.iv_productImage,UrlAddress.PRODUCT_IMAGE_ADDRESS+productList.get(position).getPrListPicName());
        holder.tv_productName.setText(productList.get(position).getProductName());
        holder.tv_collNum.setText(productList.get(position).getPrFansNum());
        return convertView;
    }
    class ViewHolder{
        private ImageView iv_productImage;
        private TextView tv_productName,tv_collNum;
    }
}
