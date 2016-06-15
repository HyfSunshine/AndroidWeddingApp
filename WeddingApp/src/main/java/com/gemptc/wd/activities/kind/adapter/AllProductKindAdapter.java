package com.gemptc.wd.activities.kind.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/6/11.
 */
public class AllProductKindAdapter extends BaseAdapter {

    private Context context;
    private List<ProductBean> productList;

    public AllProductKindAdapter(Context context, List<ProductBean> productList) {
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView = View.inflate(context, R.layout.mine_items_coll_product_listview,null);
            holder.iv_productImage= (ImageView) convertView.findViewById(R.id.iv_productImage);
            holder.tv_productName= (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tv_collNum= (TextView) convertView.findViewById(R.id.tv_collNum);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        x.image().bind(holder.iv_productImage, UrlAddress.PRODUCT_IMAGE_ADDRESS+productList.get(position).getPrListPicName());
        holder.tv_productName.setText(productList.get(position).getProductName());
        holder.tv_collNum.setText(productList.get(position).getPrFansNum());
        return convertView;
    }
    class ViewHolder{
        private ImageView iv_productImage;
        private TextView tv_productName,tv_collNum;
    }

}
