package mika.dupot.logistiqueenfolie.Display;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mika.dupot.logistiqueenfolie.Domain.Draw;
import mika.dupot.logistiqueenfolie.Domain.GamePlay;
import mika.dupot.logistiqueenfolie.Domain.Message;

import mika.dupot.logistiqueenfolie.GameoverActivity;
import mika.dupot.logistiqueenfolie.R;


public class Game extends SurfaceView implements SurfaceHolder.Callback , Runnable{

    public static Game instance;

    private SurfaceHolder surfaceHolder;
    private Thread drawThread;

    private boolean isSurfaceReady = false;
    private boolean isDrawingActive = false;

    private static final int GAME_FPS=24;
    private static final int MAX_FRAME_TIME = (int) (1000.0 / GAME_FPS);
    private int ticFps=0;

    //-----

    public static float scaledDensity;

    private Rack oCameraRack;
    private Rack oTabletRack;
    private Rack oUsbkeyRack;
    private Rack oEmptyRack;

    private Picture oCase;
    private Picture oPath;
    private Picture oDont;
    private AnimatedPicture oBelt;
    private Picture oLifeUp;
    private Picture oLifeDown;
    private Picture oBoxFilled;

    private AnimatedPicture oArrow;
    private AnimatedPicture oArrowPull;
    private AnimatedPicture oArrowPush;

    private Animation oAnimBoxSuccess;
    private Animation oAnimBoxFailed;


    private Box oBox;
    private Order Order;
    private Player oPlayer;

    private float screenWidth;
    private int gridWidth;

    public static boolean isAnimationRunning=false;
    private ArrayList<Draw> previousDrawList;


    public Game(Context context) {
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

    }

    public void gameOver(){
        drawThread.interrupt();
        isSurfaceReady =false;

        getContext().startActivity(new Intent(getContext(),GameoverActivity.class));
    }


    public void render(Canvas canvas) {
        try {


            if(GamePlay.getInstance().animationIsRunning()) {

                Draw drawObject= GamePlay.getInstance().calculRenderAnimation();

                ArrayList<Draw> newDrawList=new ArrayList<Draw>();
                for(Draw previousDraw : previousDrawList){
                    if( (previousDraw.getType()==Draw.TYPE_BOX || previousDraw.getType()==Draw.TYPE_ARROWPUTINBOX) && previousDraw.getX()==drawObject.getX() && previousDraw.getY()==drawObject.getY()){
                        //previousDrawList.remove(previousDraw);
                    }else if(previousDraw.getType()==Draw.TYPE_PLAYER){
                        //previousDrawList.remove(previousDraw);

                    }else{
                        newDrawList.add( previousDraw);
                    }

                }
                newDrawList.add(GamePlay.getInstance().getRenderPlayer());

                super.draw(canvas);
                printDrawList(canvas, newDrawList);


                switch (drawObject.getType()) {
                    case Draw.TYPE_ANIMATION_BOX_SUCCESS:
                        if(!oAnimBoxSuccess.draw(canvas,drawObject.getX(), drawObject.getY() ) ){
                            GamePlay.getInstance().disableAnimation();
                        }
                        break;
                    case Draw.TYPE_ANIMATION_BOX_FAILED:
                        if(!oAnimBoxFailed.draw(canvas,drawObject.getX(), drawObject.getY() ) ){
                            GamePlay.getInstance().disableAnimation();
                        }
                        break;

                }


            }else {

                boolean oneSec=false;
                ticFps += 1;
                if (ticFps >= GAME_FPS) {
                    oneSec=true;
                    ticFps = 0;
                }

                if (!GamePlay.getInstance().cycleBeforeDrawing(oneSec) ) {
                    gameOver();
                }

                super.draw(canvas);
                ArrayList<Draw> drawList = GamePlay.getInstance().calculRender();
                printDrawList(canvas, drawList);


                previousDrawList=drawList;


            }

            printScore(canvas,GamePlay.getInstance().getScore());


        } catch (Exception e) {
            Log.e("GameSurface", "Error:" + e.getMessage()+" cause:"+e.getCause());
            Log.e("GameSurface",Log.getStackTraceString(e));
        }
    }

    private void printDrawList(Canvas canvas, ArrayList<Draw> drawList) {
        for (Draw drawObject : drawList) {

            switch (drawObject.getType()) {
                case Draw.TYPE_CASE:
                    oCase.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;

                case Draw.TYPE_BOX:
                    oBox.drawCoord(
                            canvas,
                            drawObject.getX(),
                            drawObject.getY(),
                            (int) drawObject.getParam(Draw.PARAM_ORDERID),
                            (ArrayList<Integer>) drawObject.getParam(Draw.PARAM_ITEMLIST),
                            (int) drawObject.getParam(Draw.PARAM_TIMELEFT),
                            (int) drawObject.getParam(Draw.PARAM_TIMETOTAL),
                            (int) drawObject.getParam(Draw.PARAM_TICMOVINGTO),

                            (boolean) drawObject.getParam(Draw.PARAM_ISARRIVED)


                    );
                    break;

                case Draw.TYPE_BELT:


                    oBelt.drawCoord(canvas, drawObject.getX(), drawObject.getY());

                    if ((boolean) drawObject.getParam(Draw.PARAM_ISRUNNING) == true) {
                        oBelt.play();
                    } else {
                        oBelt.stop();
                    }
                    break;

                case Draw.TYPE_CAMERA_RACK:
                    oCameraRack.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;
                case Draw.TYPE_TABLET_RACK:
                    oTabletRack.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;
                case Draw.TYPE_USBKEY_RACK:
                    oUsbkeyRack.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;
                case Draw.TYPE_EMPTY_RACK:
                    oEmptyRack.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;
                case Draw.TYPE_PATH:
                    oPath.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;

                case Draw.TYPE_ORDER:
                    Order.drawCoord(
                            canvas,
                            drawObject.getX() * (gridWidth / 4),
                            drawObject.getY(),
                            (int) drawObject.getParam(Draw.PARAM_ORDERID),
                            (ArrayList) drawObject.getParam(Draw.PARAM_ITEMLIST)
                    );
                    break;

                case Draw.TYPE_PLAYER:
                    oPlayer.draw(canvas, drawObject.getX(), drawObject.getY(),
                            (int) drawObject.getParam(Draw.PARAM_TARGETX),
                            (int) drawObject.getParam(Draw.PARAM_TARGETY),
                            (int) drawObject.getParam(Draw.PARAM_TICMOVINGTO),

                            (int) drawObject.getParam(Draw.PARAM_ROWUSING),
                            (int) drawObject.getParam(Draw.PARAM_ITEM)
                    );
                    break;


                case Draw.TYPE_ARROWTAKEFROMRACK:
                    oArrow.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;
                case Draw.TYPE_ARROWPUTONRACK:
                    oArrowPull.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;
                case Draw.TYPE_ARROWPUTINBOX:
                    oArrowPush.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;

                case Draw.TYPE_DONT:
                    oDont.drawCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;

                case Draw.TYPE_LIFEUP:
                    oLifeUp.drawHalfCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;
                case Draw.TYPE_LIFEDOWN:
                    oLifeDown.drawHalfCoord(canvas, drawObject.getX(), drawObject.getY());
                    break;
            }


        }
    }

    private void printScore(Canvas canvas,int score_){

        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(22 * scaledDensity);
        paint.setStyle(Paint.Style.FILL);


        int x2=0;
        int y2=8*gridWidth+(gridWidth/2);

        String text=String.format("%05d",score_ );


        canvas.drawText(text,x2,y2,paint);

    }




    public Bitmap getBitmapImage(int id_){
        return BitmapFactory.decodeResource(this.getResources(), id_);
    }

    public void toast(String text_){


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.toast, null);

        TextView oText=layout.findViewById(R.id.myText);
        oText.setText(text_);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void toastError(String text_){


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.toast_error, null);

        TextView oText=layout.findViewById(R.id.myText);
        oText.setText(text_);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public int convertToGridCoord(float screenCoord_){
        return (int)(screenCoord_/gridWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Message oMessage= GamePlay.getInstance().onTouchCoord( convertToGridCoord(event.getX()),convertToGridCoord(event.getY()) );

                if(oMessage.getType()==Message.TYPE_TOAST){
                    toast(oMessage.getArgument());
                }else if(oMessage.getType()==Message.TYPE_TOAST_ERROR){
                    toastError(oMessage.getArgument());
                }

                /*else if(oMessage.getType()==Message.TYPE_ANIMATION){
                    isAnimationRunning=true;
                }*/



                return true;
            }

        }catch(Exception e){

            Log.e("GameSurface","onTouchEvent: "+e.getMessage());

            return false;
        }

        return false;
    }


    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder_) {

        scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        screenWidth=getWidth();
        gridWidth =(int)(screenWidth/GamePlay.max);


        surfaceHolder = surfaceHolder_;


        ArrayMap<Integer,Bitmap> playerItemBitmapList=new ArrayMap<>();
        playerItemBitmapList.put(GamePlay.ITEM_CAMERA,this.getBitmapImage( R.drawable.player_camera));
        playerItemBitmapList.put(GamePlay.ITEM_TABLET,this.getBitmapImage( R.drawable.player_tablet));
        playerItemBitmapList.put(GamePlay.ITEM_USBKEY,this.getBitmapImage( R.drawable.player_usbkey));
        this.oPlayer =new Player(this.getBitmapImage( R.drawable.player_sprite),this.getBitmapImage( R.drawable.player_up), playerItemBitmapList,gridWidth, scaledDensity);


        this.oCameraRack=new Rack(this.getBitmapImage( R.drawable.etageres_camera), gridWidth, scaledDensity);
        this.oTabletRack=new Rack(this.getBitmapImage( R.drawable.etageres_tablets), gridWidth, scaledDensity);
        this.oUsbkeyRack=new Rack(this.getBitmapImage( R.drawable.etageres_usbkey), gridWidth, scaledDensity);
        this.oEmptyRack=new Rack(this.getBitmapImage( R.drawable.etageres_vide), gridWidth, scaledDensity);

        this.oCase=new Picture(this.getBitmapImage( R.drawable.case_back), gridWidth, scaledDensity);

        this.oPath=new Picture(this.getBitmapImage( R.drawable.path), gridWidth, scaledDensity);

        this.oArrow=new AnimatedPicture(this.getBitmapImage( R.drawable.arrow),3, gridWidth, scaledDensity);
        this.oArrowPush=new AnimatedPicture(this.getBitmapImage( R.drawable.arrow_push),3, gridWidth, scaledDensity);
        this.oArrowPull=new AnimatedPicture(this.getBitmapImage( R.drawable.arrow_pull),3, gridWidth, scaledDensity);

        this.oDont=new Picture(this.getBitmapImage( R.drawable.dont), gridWidth, scaledDensity);

        this.oBelt=new AnimatedPicture(this.getBitmapImage( R.drawable.belt),4, gridWidth, scaledDensity);
        oBelt.stop();

        this.oLifeUp=new Picture(this.getBitmapImage(R.drawable.life_up),gridWidth,scaledDensity);
        this.oLifeDown=new Picture(this.getBitmapImage(R.drawable.life_down),gridWidth,scaledDensity);

        ArrayMap<Integer,Bitmap> itemBitmapList=new ArrayMap<>();
        itemBitmapList.put(GamePlay.ITEM_CAMERA,this.getBitmapImage( R.drawable.box_camera));
        itemBitmapList.put(GamePlay.ITEM_TABLET,this.getBitmapImage( R.drawable.box_tablet));
        itemBitmapList.put(GamePlay.ITEM_USBKEY,this.getBitmapImage( R.drawable.box_usbkey));
        this.oBox =new Box(this.getBitmapImage( R.drawable.box), itemBitmapList,gridWidth, scaledDensity);


        this.oBoxFilled=new Picture(this.getBitmapImage(R.drawable.box_filled),gridWidth,scaledDensity);

        ArrayMap<Integer,Bitmap> itemOrderBitmapList=new ArrayMap<>();
        itemOrderBitmapList.put(GamePlay.ITEM_CAMERA,this.getBitmapImage( R.drawable.order_camera));
        itemOrderBitmapList.put(GamePlay.ITEM_TABLET,this.getBitmapImage( R.drawable.order_tablet));
        itemOrderBitmapList.put(GamePlay.ITEM_USBKEY,this.getBitmapImage( R.drawable.order_usbkey));
        this.Order =new Order(this.getBitmapImage( R.drawable.box),itemOrderBitmapList, gridWidth, scaledDensity);

        //this.oAnimBoxSuccess=new Animation(this.getBitmapImage(R.drawable.animation_box_success),4,screenWidth,scaledDensity);


        this.oAnimBoxSuccess=new Animation(this.getBitmapImage(R.drawable.box_filled_sprites),7,gridWidth);

        this.oAnimBoxFailed=new Animation(this.getBitmapImage(R.drawable.box_failed_sprites),7,gridWidth);


        if (drawThread != null){
            isDrawingActive = false;
            try{
                drawThread.join();
            } catch (InterruptedException e){}
        }

        isSurfaceReady = true;
        startDrawThread();
    }

    public void startDrawThread(){
        if (isSurfaceReady && drawThread == null){
            drawThread = new Thread(this, "Draw thread");
            isDrawingActive = true;
            drawThread.start();
        }
    }

    @Override
    public void run() {
        long frameStartTime;
        long frameTime;


        /*
         * In order to work reliable on Nexus 7, we place ~500ms delay at the start of drawing thread
         * (AOSP - Issue 58385)
         */
        if (Build.BRAND.equalsIgnoreCase("google") && Build.MANUFACTURER.equalsIgnoreCase("asus") && Build.MODEL.equalsIgnoreCase("Nexus 7")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }

        while (isDrawingActive) {
            if (surfaceHolder == null) {
                return;
            }

            frameStartTime = System.nanoTime();
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    synchronized (surfaceHolder) {
                        //tick();
                        render(canvas);
                    }
                } finally {

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            // calculate the time required to draw the frame in ms
            frameTime = (System.nanoTime() - frameStartTime) / 1000000;

            if (frameTime < MAX_FRAME_TIME){
                try {
                    Thread.sleep(MAX_FRAME_TIME - frameTime);
                } catch (InterruptedException e) {
                    // ignore
                }
            }

        }
    }



    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder_, int format, int width, int height) {

    }


    /**
     * Stops the drawing thread
     */
    public void stopDrawThread(){
        if (drawThread == null){
            return;
        }
        isDrawingActive = false;
        while (true){
            try{
                drawThread.join(5000);
                break;
            } catch (Exception e) {
                Log.e("Game", "Could not join with draw thread");
            }
        }
        drawThread = null;
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder_) {
        // Surface is not used anymore - stop the drawing thread
        stopDrawThread();
        // and release the surface
        surfaceHolder_.getSurface().release();

        this.surfaceHolder = null;
        isSurfaceReady = false;
    }

}
