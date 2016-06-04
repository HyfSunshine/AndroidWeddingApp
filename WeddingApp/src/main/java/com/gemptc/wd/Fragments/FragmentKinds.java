package com.gemptc.wd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.wedding.R;
import com.gemptc.wd.activities.kind.KindClothesActivity;
import com.gemptc.wd.activities.kind.KindHotelActivity;
import com.gemptc.wd.activities.kind.KindRentCarActivity;
import com.gemptc.wd.activities.kind.KindRingActivity;
import com.gemptc.wd.activities.kind.KindShootActivity;
import com.gemptc.wd.activities.kind.KindTravelActivity;

/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentKinds extends Fragment{
    private View view;

    private ImageButton mImgBtnShoot,mImgBtnRing,mImgBtnRentCar,
            mImgBtnClothes,mImgBtnHotel,mImgBtnTravel;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_kinds,null);
        initView();
        initListeners();
        return view;
    }


    private void initView() {
        mImgBtnClothes= (ImageButton) view.findViewById(R.id.clothes);
        mImgBtnHotel= (ImageButton) view.findViewById(R.id.hotel);
        mImgBtnRentCar= (ImageButton) view.findViewById(R.id.rentcar);
        mImgBtnRing= (ImageButton) view.findViewById(R.id.ring);
        mImgBtnShoot= (ImageButton) view.findViewById(R.id.shoot);
        mImgBtnTravel= (ImageButton) view.findViewById(R.id.travel);
    }


    private void initListeners() {
        KindListener listener=new KindListener();
        mImgBtnTravel.setOnClickListener(listener);
        mImgBtnShoot.setOnClickListener(listener);
        mImgBtnRing.setOnClickListener(listener);
        mImgBtnRentCar.setOnClickListener(listener);
        mImgBtnClothes.setOnClickListener(listener);
        mImgBtnHotel.setOnClickListener(listener);
    }
    class KindListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.clothes:
                    startActivity(new Intent(getContext(),KindClothesActivity.class));
                    break;
                case R.id.hotel:
                    startActivity(new Intent(getContext(),KindHotelActivity.class));
                    break;
                case R.id.rentcar:
                    startActivity(new Intent(getContext(), KindRentCarActivity.class));
                    break;
                case R.id.ring:
                    startActivity(new Intent(getContext(), KindRingActivity.class));
                    break;
                case R.id.shoot:
                    startActivity(new Intent(getContext(), KindShootActivity.class));
                    break;
                case R.id.travel:
                    startActivity(new Intent(getContext(), KindTravelActivity.class));
                    break;
                default:
                    break;
            }
        }
    }

}
