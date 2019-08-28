package mika.dupot.logistiqueenfolie.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Rack {


    protected int spriteWidth;
    protected int spriteHeight;


    protected int gridWidth;

    protected Bitmap image;

    public Rack( Bitmap image, int gridWidth_, float scaledDensity){

        this.image=image;

        gridWidth=gridWidth_;

        spriteWidth= (int) (80*scaledDensity);
        spriteHeight= (int)(160*scaledDensity);
    }

    public void drawCoord(Canvas canvas, int x, int y){
        Bitmap bitmap = image;

        int x2=x*gridWidth;


        int decalageY=gridWidth;

        int y2 = (y * gridWidth)-decalageY;

        int y3 = ((y-1) * gridWidth)+gridWidth-decalageY;



        canvas.drawBitmap(bitmap,new Rect(0,0,spriteWidth,spriteHeight),new Rect(0+x2,0+y2,gridWidth+x2,(gridWidth*2)+y3),null);

    }

}
