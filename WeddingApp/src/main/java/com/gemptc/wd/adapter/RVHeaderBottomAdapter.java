package com.gemptc.wd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.x;

import java.util.List;

/**
 * Created by C5-0 on 2016/6/3.
 */
public class RVHeaderBottomAdapter extends RecyclerView.Adapter {
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    //数据
    public List<ProductBean> mProBeanList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    //头部View个数
    private int mHeaderCount = 1;
    private Seller mSeller;

    public RVHeaderBottomAdapter(List<ProductBean> proBeanList, Seller seller, Context context) {
        mProBeanList = proBeanList;
        mSeller = seller;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /*
    public RVHeaderBottomAdapter(List<ProductBean> mproduct, Context context) {
        this.mProBeanList = mproduct;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }*/

    //内容长度
    public int getContentItemCount() {
        return mProBeanList.size();
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else { //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    //内容ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
       private ImageButton imageButton;
        private TextView textView;
        private TextView collectNumtextView;
        public ContentViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.product_name);
            imageButton= (ImageButton) itemView.findViewById(R.id.product_img);
            collectNumtextView= (TextView) itemView.findViewById(R.id.collectProductNum);
        }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView sellerNameTxtView;
        private TextView sellerKindTxtView;
        private TextView sellerFansNumTxtView;
        private TextView sellerDescripeTxtView;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cv_imageview);
            imageView.setAlpha(100);
            sellerNameTxtView= (TextView) itemView.findViewById(R.id.sellerNameTxt);
            sellerKindTxtView= (TextView) itemView.findViewById(R.id.sellerKindTxt);
            sellerFansNumTxtView= (TextView) itemView.findViewById(R.id.fansnum);
            sellerDescripeTxtView= (TextView) itemView.findViewById(R.id.descriptionTxt);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            View view=mLayoutInflater.inflate(R.layout.seller_rv_header_item, parent, false);
            HeaderViewHolder vh=new HeaderViewHolder(view);

            return vh;
        } else if (viewType == mHeaderCount) {
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.seller_rv_content_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).sellerNameTxtView.setText(mSeller.getSellerName());
            ((HeaderViewHolder) holder).sellerKindTxtView.setText(mSeller.getSellerType());
            ((HeaderViewHolder) holder).sellerFansNumTxtView.setText(""+mSeller.getSellerFansNum());
            ((HeaderViewHolder) holder).sellerDescripeTxtView.setText(mSeller.getSellerSign());
            x.image().bind(((HeaderViewHolder) holder).imageView, UrlAddress.SELLER_IMAGE_ADDRESS+mSeller.getSellerPicName());
        } else if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).collectNumtextView.setText(mProBeanList.get(position-mHeaderCount).getPrFansNum());
            ((ContentViewHolder) holder).textView.setText(mProBeanList.get(position-mHeaderCount).getProductName());
            /*((ContentViewHolder) holder).imageButton*/
            x.image().bind(((ContentViewHolder) holder).imageButton, UrlAddress.PRODUCT_IMAGE_ADDRESS+mProBeanList.get(position-mHeaderCount ).getPrListPicName());

        }
    }

    @Override
    public int getItemCount() {
        return mHeaderCount + getContentItemCount();
    }
    //define interface

}

