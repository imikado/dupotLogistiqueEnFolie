package mika.dupot.logistiqueenfolie.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.util.ArrayMap;
import java.util.ArrayList;


public class Box extends AbstractDraw {

    private ArrayMap<Integer,Bitmap> itemBitmapList;

    private int ticLeft;


    public Box(Bitmap image_, ArrayMap<Integer,Bitmap> itemBitmapList_, int gridWith_, float scaledDensity_){

        super(  image_,   gridWith_,   scaledDensity_);

        itemBitmapList=itemBitmapList_;


    }

    public void setTicLeft(int ticLeft_){
        ticLeft=ticLeft_;
    }

    public int getMovingDelta(){
        return (ticLeft*(gridWidth/4));
    }

    public void drawCoord(Canvas canvas_, int x_, int y_, int orderId_, ArrayList<Integer> itemList_, int timeLeft_, int timeTotal_, int ticLeft_, boolean isArrived_){

        setTicLeft(ticLeft_);

        drawImageCoordPlusDelta(canvas_,image,x_,y_,getMovingDelta());

        int i=0;
        int j=0;
        for( int item : itemList_){
            Bitmap bitmap=itemBitmapList.get(item);

            int x2=(x_*gridWidth)+((i*gridWidth/6))+getMovingDelta();
            int y2=(y_ * gridWidth)+((j*gridWidth/8));
            drawImageCoordWithPrecision(canvas_,bitmap,x2,y2);

             i++;

             if(i>2){
                 i=0;
                 j=1;
             }
        }

        drawTextCoordPlusDelta(canvas_,"#"+Integer.toString(orderId_),x_,y_,getMovingDelta());

        if(isArrived_) {
            drawTimeLeftBarPlusDelta(canvas_, x_, y_, timeLeft_, timeTotal_, getMovingDelta());
        }
    }

    private void drawTimeLeftBarPlusDelta(Canvas canvas,int x_,int y_,int timeLeft,int timeTotal, int deltaX_) {

        int timeLeftGraphic=calculateWidthRatio(gridWidth,timeLeft,timeTotal  );


        int x2=x_*gridWidth+deltaX_;
        int y2=(y_*gridWidth)+gridWidth;

        drawRectRed(canvas,x2,y2,gridWidth,gridWidth/4);

        drawRectGreen(canvas,x2,y2,timeLeftGraphic,gridWidth/4);

        drawRectStrokeWhite(canvas,x2,y2,gridWidth,gridWidth/4);

    }

    private int calculateWidthRatio(int width_,int step_,int total_){

        float ratio=( (float)step_/(float)total_);
        return (int)( width_*ratio);
    }


}
