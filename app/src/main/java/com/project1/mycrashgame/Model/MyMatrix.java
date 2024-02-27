package com.project1.mycrashgame.Model;

import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class MyMatrix {
    private List<LinearLayoutCompat> layoutsCloudList;
    private List<LinearLayoutCompat> layoutscauldronList;

    public MyMatrix() {

    }
    public MyMatrix(List<LinearLayoutCompat> layoutsCloud,List<LinearLayoutCompat> layoutscauldron) {
        this.layoutsCloudList=layoutsCloud;
        this.layoutsCloudList= initialization(true);
        this.layoutscauldronList=layoutscauldron;
        this.layoutscauldronList= initialization(false);
    }

    public List<LinearLayoutCompat> getLayoutsCloudList() {
        return layoutsCloudList;
    }

    public List<LinearLayoutCompat> getLayoutscauldronList() {
        return layoutscauldronList;
    }

    private List<LinearLayoutCompat> initialization(boolean isCloud){

        if(isCloud){
            for (LinearLayoutCompat cloudLayout: layoutsCloudList) {
                initializationLayout(cloudLayout);
            }
            return layoutsCloudList;
        }
        else {
            for (LinearLayoutCompat cauldronLayout: layoutscauldronList) {
                initializationLayout(cauldronLayout);
            }
            return layoutscauldronList;
        }
    }

    private void initializationLayout(LinearLayoutCompat theLayout) {
        List<ShapeableImageView> iMG_List = new ArrayList<>();

        for (int i = 0,j=0; i < theLayout.getChildCount(); i++) {
            if(theLayout.getChildAt(i) instanceof ShapeableImageView){
                ShapeableImageView temp= (ShapeableImageView) theLayout.getChildAt(i);
                int imageId =theLayout.getChildAt(i).getId();
                iMG_List.add(temp);
                iMG_List.get(j).findViewById(imageId);
                iMG_List.get(j++).setVisibility(View.INVISIBLE);
            }
        }
    }
}
