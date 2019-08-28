package mika.dupot.logistiqueenfolie.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimatedPicture {
    protected int gridWidth;


    protected int spriteWidth;
    protected int spriteHeight;

    protected int imageWidth;
    protected int imageHeight;

    protected Bitmap image;

    protected int ticSprite;
    protected int colUsing=0;
    private Bitmap[] spritesList;

    protected boolean isRunning=true;

    public AnimatedPicture( Bitmap image,  int nbCol_, int gridWidth_, float scaledDensity) {
        this.image=image;

        gridWidth=gridWidth_;

        spriteWidth= (int) (80*scaledDensity);
        spriteHeight= (int) (80*scaledDensity);

        imageWidth=image.getWidth()/nbCol_;
        imageHeight=image.getHeight();

        spritesList = new Bitmap[nbCol_];
        for (int col = 0; col < nbCol_; col++) {

            spritesList[col] = this.createSubImageAt( col);
        }

    }

    public void play(){
        isRunning=true;
    }
    public void stop(){
        isRunning=false;
    }

    public void run(){

        if(isRunning==false){
            return;
        }

        ticSprite +=1;
        if(ticSprite>4){

            ticSprite=0;
            colUsing++;

            if(colUsing>2){
                colUsing=0;
            }

        }
    }

    public void drawCoord(Canvas canvas, int x, int y){
        Bitmap bitmap = image;

        int x2=x*gridWidth;
        int y2 = (y * gridWidth) ;

        canvas.drawBitmap(getCurrentMoveBitmap(),new Rect(0,0,spriteWidth,spriteHeight),new Rect(0+x2,0+y2,gridWidth+x2,gridWidth+y2),null);

        run();

    }

    public void drawHalfCoord(Canvas canvas, int x, int y){
        Bitmap bitmap = image;

        int halfWidth=gridWidth/2;

        int x2=x*halfWidth ;
        int y2 = (y * halfWidth)+(halfWidth/2)  ;//- (this.caseHeight/4);

        canvas.drawBitmap(getCurrentMoveBitmap(),new Rect(0,0,halfWidth,halfWidth),new Rect(x2,y2,halfWidth+x2,halfWidth+y2),null);

        run();
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
