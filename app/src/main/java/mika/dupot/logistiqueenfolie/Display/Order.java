package mika.dupot.logistiqueenfolie.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.ArrayMap;

import java.util.ArrayList;


public class Order extends AbstractDraw {

    private ArrayMap<Integer,Bitmap> itemBitmapList;


    public Order(Bitmap image_, ArrayMap<Integer,Bitmap> itemBitmapList_, int gridWith_, float scaledDensity_){

        super(  image_,   gridWith_,   scaledDensity_);

        itemBitmapList=itemBitmapList_;

    }

    public void drawCoord(Canvas canvas, int x2, int y2, int orderId, ArrayList<Integer> tItem){



        int i=0;
        for( int item : tItem){

            int x3=x2+(i*(gridWidth/4));


            Bitmap bitmap=itemBitmapList.get(item);
            drawImageCoordWithPrecision(canvas,bitmap,x3,y2);

            i++;
        }


        int yText=y2+ (int)(gridWidth*0.8);
        int xText=x2;

        drawTextCoordWhiteWithPrecision(canvas,"#"+Integer.toString(orderId),xText,yText);
    }




}
