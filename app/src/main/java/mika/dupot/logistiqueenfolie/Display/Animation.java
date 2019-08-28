package mika.dupot.logistiqueenfolie.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Animation {

    private final int spriteWidth;
    private final int spriteHeight;

    private final int imageWidth;
    private final int imageHeight;


    protected Bitmap image;

    protected int ticSprite;
    protected int colUsing=0;

    protected int nbCol=0;

    private Bitmap[] spritesList;

    public Animation(Bitmap image, int nbCol_, float screenWidth_,  float scaledDensity) {
        this.image=image;

        spriteWidth=  (int)screenWidth_;// (80*5*scaledDensity);
        spriteHeight= (int) (80*7*scaledDensity);

        imageWidth=image.getWidth()/nbCol_;
        imageHeight=image.getHeight();

        nbCol=nbCol_;

        spritesList = new Bitmap[nbCol_];
        for (int col = 0; col < nbCol_; col++) {

            spritesList[col] = this.createSubImageAt( col);
        }

    }

    public Animation(Bitmap image, int nbCol_, float gridWith_) {
        this.image=image;

        spriteWidth=  (int)gridWith_;
        spriteHeight= (int) gridWith_;

        imageWidth=image.getWidth()/nbCol_;
        imageHeight=image.getHeight();

        nbCol=nbCol_;

        spritesList = new Bitmap[nbCol_];
        for (int col = 0; col < nbCol_; col++) {

            spritesList[col] = this.createSubImageAt( col);
        }

    }


    public boolean draw(Canvas canvas,int x_,int y_){
        canvas.drawBitmap(getCurrentMoveBitmap(),new Rect(0,0,imageWidth,imageHeight),new Rect(x_*spriteWidth,y_*spriteWidth,(x_+1)*spriteWidth,(y_+1)*spriteHeight),null);

        ticSprite++;

        if(ticSprite>3){
            colUsing++;
            ticSprite=0;
        }

        if(colUsing>= nbCol){
            colUsing=0;
            ticSprite=0;

            return false;
        }

        return true;
    }


    public boolean draw(Canvas canvas){
        canvas.drawBitmap(getCurrentMoveBitmap(),new Rect(0,0,imageWidth,imageHeight),new Rect(0,0,spriteWidth,spriteHeight),null);

        ticSprite++;

        if(ticSprite>3){
            colUsing++;
            ticSprite=0;
        }

        if(colUsing>= nbCol){
           colUsing=0;
           ticSprite=0;

           return false;
        }

        return true;
    }

    protected Bitmap createSubImageAt(int col)  {
        // createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(image, col* imageWidth, 0 ,imageWidth,imageHeight);
        return subImage;
    }

    public Bitmap getCurrentMoveBitmap() {

        return spritesList[this.colUsing];
    }
}
