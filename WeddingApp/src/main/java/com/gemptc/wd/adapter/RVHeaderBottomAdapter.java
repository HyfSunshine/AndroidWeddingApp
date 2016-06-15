package com.gemptc.wd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.UrlAddress;
import com.gemptc.wd.view.CircleImageView;

import org.xutils.x;

import java.util.List;
/**
 * Created by C5-0 on 2016/6/3.
 */
public class RVHeaderBottomAdapter extends RecyclerView.Adapter implements View.OnClickListener {
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
    private HeaderViewHolder headerViewHolder;
    //声明一个这个接口的变量
    private OnMoreItemClickListener mOnItemClickListener = null;

    public RVHeaderBottomAdapter(List<ProductBean> proBeanList, Seller seller, Context context) {
        mProBeanList = proBeanList;
        mSeller = seller;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (ProductBean) v.getTag());
        }

    }
    //定义接口
    public static interface OnMoreItemClickListener {

        void onItemClick(View view, ProductBean product);
    }

    public void setmOnItemClickListener(OnMoreItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


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
       private ImageView imageView;
        private TextView textView;
        private TextView collectNumtextView;
        public ContentViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.product_name);
            imageView= (ImageView) itemView.findViewById(R.id.product_img);
            collectNumtextView= (TextView) itemView.findViewById(R.id.collectProductNum);

        }
    }
    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        // private ImageView imageView;
        private TextView sellerNameTxtView;
        private TextView sellerKindTxtView;
        private TextView sellerFansNumTxtView;
        private TextView sellerDescripeTxtView;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.cv_imageview);
            sellerNameTxtView= (TextView) itemView.findViewById(R.id.sellerNameTxt);
            sellerKindTxtView= (TextView) itemView.findViewById(R.id.sellerKindTxt);
            sellerFansNumTxtView= (TextView) itemView.findViewById(R.id.fansnum);
            sellerDescripeTxtView= (TextView) itemView.findViewById(R.id.descriptionTxt);
        }
    }
    //在onCreateViewHolder()中为每个item添加点击事件
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            View view=mLayoutInflater.inflate(R.layout.seller_rv_header_item, parent, false);
            HeaderViewHolder  vh = new HeaderViewHolder(view);
            return vh;
            //return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.seller_rv_header_item, parent, false));
        } else if (viewType == mHeaderCount) {
            View view=mLayoutInflater.inflate(R.layout.seller_rv_content_item, parent, false);
            ContentViewHolder  vh = new ContentViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return vh;
           // return new ContentViewHolder(mLayoutInflater.inflate(R.layout.seller_rv_content_item, parent, false));
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).sellerDescripeTxtView.setText(mSeller.getSellerSign());
            ((HeaderViewHolder) holder).sellerFansNumTxtView.setText(""+mSeller.getSellerFansNum());
            ((HeaderViewHolder) holder).sellerKindTxtView.setText(mSeller.getSellerType());
            ((HeaderViewHolder) holder).sellerNameTxtView.setText(mSeller.getSellerName());
            Glide.with(mContext)
                    .load(UrlAddress.SELLER_IMAGE_ADDRESS+mSeller.getSellerPicName())
                    .thumbnail(0.5f)
                    .into(((HeaderViewHolder)holder).imageView);

        } else if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).collectNumtextView.setText(mProBeanList.get(position-mHeaderCount).getPrFansNum());
            ((ContentViewHolder) holder).textView.setText(mProBeanList.get(position-mHeaderCount).getProductName());
            x.image().bind(((ContentViewHolder) holder).imageView, UrlAddress.PRODUCT_IMAGE_ADDRESS+mProBeanList.get(position-mHeaderCount ).getPrListPicName());
            ((ContentViewHolder) holder).itemView.setTag(mProBeanList.get(position-mHeaderCount));
           // ((ContentViewHolder) holder).itemView.setTag(mSeller);

        }
    }
    @Override
    public int getItemCount() {
        return mHeaderCount + getContentItemCount();
    }
}

