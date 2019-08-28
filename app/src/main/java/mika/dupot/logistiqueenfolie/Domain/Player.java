package mika.dupot.logistiqueenfolie.Domain;

import java.util.ArrayList;

public class Player {

    protected int item;
    protected int x=1;
    protected int y=1;


    protected ArrayList<Point> targetPointList;
    protected int targetPointList_i;
    protected int targetX=1;
    protected int targetY=1;
    protected int ticMovingTo=0;

    protected int rowUsing;
    protected int colUsing;

    public static final int SPRITE_STABLE=0;
    public static final int SPRITE_RIGHT=1;
    public static final int SPRITE_LEFT=2;
    public static final int SPRITE_DOWN=3;
    public static final int SPRITE_UP=4;
    protected int ticSprite;

    public Player(){
        targetPointList=new ArrayList<Point>();
    }


    public void setX(int x_){
        x=x_;
        targetX=x_;
    }
    public void setY(int y_){
        y=y_;
        targetY=y_;
    }

    public void setItem(int item_){
        item=item_;
    }

    public int getItem(){
        return item;
    }

    public void empty(){
        item=0;
    }

    public boolean isFull(){
        if(item!=0){
            return true;
        }
        return false;
    }

    public void analyzeGameContext(){


        GamePlay.getInstance().processGameForPlayer(this);


    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setTargetPath(ArrayList<Point> targetPointList_) {
        targetPointList=targetPointList_;
        targetPointList_i=0;
    }

    public ArrayList<Point> getPathPointList(){
        return targetPointList;
    }
    public void increasePathPointIndex(){
        targetPointList_i++;
    }

    public void setTarget(int x_,int y_){
        targetX=x_;
        targetY=y_;

        enableTicMovingTo();
    }
    public void resetTicMovingTo(){
        ticMovingTo=0;
    }
    public void enableTicMovingTo(){
        ticMovingTo=4;
    }
    public void decreaseTicMovingTo(){
        ticMovingTo-=1;
        if(ticMovingTo<0){
            ticMovingTo=0;
        }
    }
    public int getTicMovingTo(){
        return ticMovingTo;
    }

    public int getTargetX() {
        return targetX;
    }
    public int getTargetY() {
        return targetY;
    }

    public void processPathPointList() {

        if(shouldProcessPathList()) {
            targetPointList_i++;

            if (targetPointList_i >= targetPointList.size()) {
                targetPointList = new ArrayList<>();
                targetPointList_i = 0;

                rowUsing = SPRITE_STABLE;
                return;
            } else {


                Point targetPoint = targetPointList.get(targetPointList_i);
                setTarget(targetPoint.x, targetPoint.y);

                if (x > targetPoint.x) {
                    rowUsing = SPRITE_LEFT;
                } else if (x < targetPoint.x) {
                    rowUsing = SPRITE_RIGHT;
                } else if (y > targetPoint.y) {
                    rowUsing = SPRITE_UP;
                } else if (y < targetPoint.y) {
                    rowUsing = SPRITE_DOWN;
                }
            }
        }
    }

    public boolean shouldGoToTargetX(){
        if(x!=targetX ){
            return true;
        }
        return false;
    }
    public boolean shouldGoToTargetY(){
        if(y!=targetY ){
            return true;
        }
        return false;
    }

    public void processGoToTargetX(){
        if(  x < targetX ) {
            if(ticMovingTo==0) {
                x += 1;

                analyzeGameContext();
            }
        }else if(  x > targetX ){
            if(ticMovingTo==0) {
                x -= 1;

                analyzeGameContext();
            }
        }
    }

    public void processGoToTargetY(){
        if(  y < targetY ) {
            if(ticMovingTo==0) {
                y += 1;

                analyzeGameContext();
            }
        }else if(  y > targetY ){
            if(ticMovingTo==0) {
                y -= 1;

                analyzeGameContext();
            }
        }
    }

    public void processBeforeDraw(){
        if(shouldProcessPathList()==true){
            if(x==targetX && y==targetY) {

                processPathPointList();
            }else if(shouldGoToTargetX() ){
                processGoToTargetX();
            }else if(shouldGoToTargetY() ) {
                processGoToTargetY();
            }
        }
    }

    public void processAfterDraw(){
        decreaseTicMovingTo();
    }

    public int getColUsing() {
        return colUsing;
    }
    public int getRowUsing() {
        return rowUsing;
    }

    public boolean shouldProcessPathList() {
        if(targetPointList==null || targetPointList.size()==0){
            return false;
        }
        return true;
    }
}
