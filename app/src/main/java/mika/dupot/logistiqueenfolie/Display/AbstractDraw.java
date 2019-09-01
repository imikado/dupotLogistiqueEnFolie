package mika.dupot.logistiqueenfolie.Display;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public abstract class AbstractDraw {


        protected int gridWidth;

        protected int spriteWidth;
        protected int spriteHeight;

        protected Bitmap image;


        protected float scaledDensity;

        public AbstractDraw( Bitmap image_, int gridWith_, float scaledDensity_){

            gridWidth=gridWith_;

            scaledDensity=scaledDensity_;


            spriteWidth= (int) (80*scaledDensity);
            spriteHeight= spriteWidth;

            image=image_;

         }



        public void drawImageCoordPlusDelta(Canvas canvas, Bitmap image, int x, int y, int deltaX_){

            int x2=x*gridWidth+deltaX_;
            int y2 = (y * gridWidth);

            canvas.drawBitmap(image,new Rect(0,0,spriteWidth,spriteHeight),new Rect(0+x2,0+y2,gridWidth+x2,gridWidth+y2),null);

        }

        public void drawImageCoordWithPrecision(Canvas canvas, Bitmap image, int x2, int y2){


            int y3 = y2;


            canvas.drawBitmap(image,new Rect(0,0,spriteWidth,spriteHeight),new Rect(0+x2,0+y2,gridWidth+x2,gridWidth+y3),null);

        }


        public void drawTextCoordPlusDelta(Canvas canvas, String text_, int x_, int y_, int deltaX_) {

            Paint paint=new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(22 * scaledDensity);
            paint.setStyle(Paint.Style.FILL);

            int x2=x_*gridWidth+deltaX_;
            int y2=y_*gridWidth;

            canvas.drawText(text_,x2,y2,paint);

        }


        public void drawTextCoordWhiteWithPrecision(Canvas canvas, String text_, int x2, int y2) {

            Paint paint=new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(22 * scaledDensity);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawText(text_,x2,y2,paint);



        }



        public void drawRectRed(Canvas canvas,int x2, int y2, int width_, int height_) {
            Paint oPaint=new Paint();
            oPaint.setColor(Color.RED);
            oPaint.setStyle(Paint.Style.FILL);



            canvas.drawRect(x2,y2    ,x2+width_,y2+height_,oPaint);

        }

        public void drawRectGreen(Canvas canvas,int x2, int y2, int width_, int height_) {
            Paint oPaint=new Paint();
            oPaint.setColor(Color.GREEN);
            oPaint.setStyle(Paint.Style.FILL);



            canvas.drawRect(x2,y2    ,x2+width_,y2+height_,oPaint);

        }

        public void drawRectStrokeWhite(Canvas canvas,int x2, int y2, int width_, int height_) {
            Paint oPaint=new Paint();
            oPaint.setColor(Color.WHITE);
            oPaint.setStyle(Paint.Style.STROKE);
            oPaint.setStrokeWidth(2*scaledDensity);



            canvas.drawRect(x2,y2    ,x2+width_,y2+height_,oPaint);

        }


}


