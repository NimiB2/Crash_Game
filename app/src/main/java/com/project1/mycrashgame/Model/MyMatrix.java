package com.project1.mycrashgame.Model;

import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class MyMatrix {
    private List<LinearLayoutCompat> layoutsCloudList;


    public MyMatrix() {

    }
    public MyMatrix(List<LinearLayoutCompat> layoutsCloud) {
        this.layoutsCloudList=layoutsCloud;
        this.layoutsCloudList=initializationLayouts();
    }

    public List<LinearLayoutCompat> getLayoutsCloudList() {
        return layoutsCloudList;
    }

    private List<LinearLayoutCompat> initializationLayouts(){
        for (LinearLayoutCompat cloudLayout: layoutsCloudList) {
            initializationClouds(cloudLayout);
        }
        return layoutsCloudList;
    }

    private void initializationClouds(LinearLayoutCompat cloudLayout) {
        List<ShapeableImageView> clouds_IMG_List = new ArrayList<>();

        for (int i = 0,j=0; i < cloudLayout.getChildCount(); i++) {
            if(cloudLayout.getChildAt(i) instanceof ShapeableImageView){
                ShapeableImageView temp= (ShapeableImageView) cloudLayout.getChildAt(i);
                int cloudImageId =cloudLayout.getChildAt(i).getId();
                clouds_IMG_List.add(temp);
                clouds_IMG_List.get(j).findViewById(cloudImageId);
                clouds_IMG_List.get(j++).setVisibility(View.INVISIBLE);
            }
        }
    }
}
