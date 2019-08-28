package mika.dupot.logistiqueenfolie.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class Picture {

    protected int gridWidth;


    protected int spriteWidth;
    protected int spriteHeight;

    protected Bitmap image;


    public Picture( Bitmap image, int gridWidth_, float scaledDensity) {
        this.image=image;

        gridWidth=gridWidth_;

        spriteWidth= (int) (80*scaledDensity);
        spriteHeight= (int) (80*scaledDensity);

    }

    public void drawCoord(Canvas canvas, int x, int y){
        Bitmap bitmap = image;

        int x2=x*gridWidth;
        int y2 = (y * gridWidth) ;

        canvas.drawBitmap(bitmap,new Rect(0,0,spriteWidth,spriteHeight),new Rect(0+x2,0+y2,gridWidth+x2,gridWidth+y2),null);

    }

    public void drawHalfCoord(Canvas canvas, int x, int y){
        Bitmap bitmap = image;

        int halfWidth=gridWidth/2;

        int x2=x*halfWidth ;
        int y2 = (y * halfWidth)+(halfWidth/2)  ;//- (this.caseHeight/4);

        canvas.drawBitmap(bitmap,new Rect(0,0,halfWidth,halfWidth),new Rect(x2,y2,halfWidth+x2,halfWidth+y2),null);

    }


}

