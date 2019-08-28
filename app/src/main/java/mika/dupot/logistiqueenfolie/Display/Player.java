package mika.dupot.logistiqueenfolie.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.ArrayMap;

import java.util.ArrayList;


public class Player {

    protected int imageWidth;
    protected int imageHeight;

    protected int decalageY;
    protected Game game;

    protected int gridWidth;

    protected int spriteWidth;
    protected int spriteHeight;


    protected Bitmap image;
    protected Bitmap imageUp;

    private ArrayMap<Integer,Bitmap> itemBitmapList;


    protected int rowUsing=0;
    protected int colUsing=0;

    protected int ticSprite=0;

    public static final int SPRITE_STABLE=0;
    public static final int SPRITE_RIGHT=1;
    public static final int SPRITE_LEFT=2;
    public static final int SPRITE_DOWN=3;
    public static final int SPRITE_UP=4;

    private Bitmap[][] tSprites;



    public Player(Bitmap image, Bitmap imageUp, ArrayMap<Integer,Bitmap> itemBitmapList_, int gridWidth_, float scaledDensity) {
        this.image=image;
        this.imageUp=imageUp;

        itemBitmapList=itemBitmapList_;

        imageWidth=image.getWidth()/4;
        imageHeight=image.getHeight()/5;

        gridWidth=gridWidth_;

        spriteWidth= (int) (80*scaledDensity);
        spriteHeight= (int) (80*scaledDensity);

        decalageY=(int)(gridWidth/4);

        tSprites = new Bitmap[5][4];
        for(int row=0;row < 5;row ++) {
            for (int col = 0; col < 4; col++) {

                tSprites[row][col] = this.createSubImageAt(row, col);
            }
        }

    }

    public void setColUsing(int colUsing_){
        colUsing=colUsing_;
    }
    public void setRowUsing(int rowUsing_){
        rowUsing=rowUsing_;
    }

    protected Bitmap createSubImageAt(int row, int col)  {
        // createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(image, col* imageWidth, row* imageHeight ,imageWidth,imageHeight);
        return subImage;
    }

    public Bitmap getCurrentMoveBitmap() {

        return tSprites[this.rowUsing][this.colUsing];
    }



    public void drawCoord(Canvas canvas, int x, int y,int spriteMoveX_,int spriteMoveY_){

        int x2=x*gridWidth   + spriteMoveX_;
        int y2 = (y * gridWidth)-decalageY+spriteMoveY_;


        Bitmap imageSprite=getCurrentMoveBitmap();

        canvas.drawBitmap(imageSprite,new Rect(0,0,spriteWidth,spriteHeight),new Rect(0+x2,0+y2,gridWidth+x2,gridWidth+y2),null);


        ticSprite++;

        if(ticSprite>4){

            ticSprite=0;
            colUsing++;

            if(colUsing>3){
                colUsing=0;
            }

        }


    }



    public void drawImageCoord(Canvas canvas,Bitmap bitmap,int x, int y,int spriteMoveX_,int spriteMoveY_){
        int x2=x*gridWidth;

        if(rowUsing==SPRITE_LEFT){
            x2-=decalageY;
        }else if(rowUsing==SPRITE_RIGHT){
            x2+=decalageY;
        }

        int y2 = (y * gridWidth)+decalageY/4;

        if(colUsing==1 || colUsing==3){
            y2+=decalageY/6;
        }


        x2+=spriteMoveX_;
        y2+=spriteMoveY_;


        canvas.drawBitmap(bitmap,new Rect(0,0,spriteWidth,spriteHeight),new Rect(0+x2,0+y2,gridWidth+x2,gridWidth+y2),null);

    }


    public void draw(Canvas canvas,int x_,int y_,int targetX_,int targetY_,int ticMovingTo_,int rowUsing_,int item_) {

        setRowUsing(rowUsing_);

        ArrayList<Integer> spriteMoveList=  calculateSpriteMoveList( x_,  y_,  targetX_,   targetY_,  ticMovingTo_);
        int spriteMoveX=spriteMoveList.get(0);
        int spriteMoveY=spriteMoveList.get(1);

        if(rowUsing!=SPRITE_UP) {
            drawCoord(canvas, x_, y_,spriteMoveX,spriteMoveY);
        }


        if(itemBitmapList.containsKey(item_)) {
            drawImageCoord(canvas, itemBitmapList.get(item_), x_, y_, spriteMoveX, spriteMoveY);
        }


        if(rowUsing==SPRITE_UP) {
            drawCoord(canvas, x_, y_,spriteMoveX,spriteMoveY);
        }

    }


    public ArrayList<Integer> calculateSpriteMoveList(int x_,int y_,int targetX_, int targetY_,int ticMovingTo_){
        int spriteMoveX=0;
        int spriteMoveY=0;

        ArrayList<Integer> spriteList=new ArrayList<Integer>();

        if(ticMovingTo_>0){

            int spriteMove=gridWidth-( ticMovingTo_ *gridWidth/4);

            if(targetX_ != x_){
                if(targetX_ > x_){
                    spriteMoveX=spriteMove;
                }else{
                    spriteMoveX=spriteMove*-1;
                }
            }else if(targetY_ != y_){
                if(targetY_ > y_){
                    spriteMoveY=spriteMove;
                }else{
                    spriteMoveY=spriteMove*-1;
                }
            }

            spriteList.add(spriteMoveX);
            spriteList.add(spriteMoveY);

            return spriteList;


        }

        spriteList.add(0);
        spriteList.add(0);

        return spriteList;

    }




}
