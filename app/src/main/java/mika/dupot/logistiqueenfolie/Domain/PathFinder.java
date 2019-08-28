package mika.dupot.logistiqueenfolie.Domain;

import android.util.Log;


import java.util.ArrayList;




public class PathFinder {

    protected int[][] tMap;
    protected Point target;
    protected Point start;
    protected boolean found=false;

    public static String LEFT="LEFT";
    public static String RIGHT="RIGHT";
    public static String UP="UP";
    public static String DOWN="DOWN";


    protected ArrayList<Point> tPathPoint;
    private int lengthX;
    private int lengthY;

    public PathFinder(){
        tPathPoint=new ArrayList<Point>();
    }

    public void setMap(int[][] tMap_) {
        tMap=tMap_;

        lengthX=tMap[0].length;
        lengthY=tMap.length;
    }

    public void setTarget(Point target_) {
        target=target_;

    }

    public void setStart(Point start_){
        start=start_;
    }

    public boolean hasFound(){
        return found;
    }

    public void calculatePointList(String side) {


        tPathPoint=new ArrayList<Point>();
        found=false;


        addToPath(new Point( start.x,start.y));

        Point oPointUp=getPointUp(start);
        Point oPointDown=getPointDown(start);
        Point oPointLeft=getPointLeft(start);
        Point oPointRight=getPointRight(start);


        if(side.equals(this.UP) && isFree(oPointUp)){
            gotoAndCheck( oPointUp );
        }else if(side.equals(this.DOWN) && isFree(oPointDown)) {
            gotoAndCheck(oPointDown);
        }else if(side.equals(this.LEFT) && isFree(oPointLeft)){
            gotoAndCheck( oPointLeft );
        }else if(side.equals(this.RIGHT) && isFree(oPointRight)){
            gotoAndCheck( oPointRight );
        }

    }

    public Point getPointUp(Point oPoint){
        return new Point(oPoint.x,oPoint.y-1);
    }
    public Point getPointDown(Point oPoint){
        return new Point(oPoint.x,oPoint.y+1);
    }
    public Point getPointLeft(Point oPoint){
        return new Point(oPoint.x-1,oPoint.y);
    }
    public Point getPointRight(Point oPoint){
        return new Point(oPoint.x+1,oPoint.y);
    }


    private void gotoAndCheck(Point oPoint){
        addToPath(oPoint);

        if(oPoint.x==target.x && oPoint.y==target.y){


            found=true;
            return;
        }


        ArrayList<Point> pointList = new ArrayList<Point>();

        pointList.add( getPointUp(oPoint) );
        pointList.add( getPointDown(oPoint) );
        pointList.add( getPointLeft(oPoint) );
        pointList.add( getPointRight(oPoint) );

        Point minPoint=new Point(-1,-1);
        int minScore=-1;
        for( Point currentPoint : pointList){

            if(false==isFree(currentPoint)){
                continue;
            }else if (isPointAlreadyInPath(currentPoint)){
                continue;
            }


            int currentScore=calculateScoreFor(currentPoint);
            if(currentScore==minScore){

                //if 2 way with same score, we chose the way in same flow
                if(currentPoint.x==oPoint.x || currentPoint.y==oPoint.y){
                    minPoint=new Point(currentPoint.x,currentPoint.y);
                    minScore=currentScore;
                }

            }else if(currentScore < minScore || minScore==-1){
                minPoint=new Point(currentPoint.x,currentPoint.y);
                minScore=currentScore;
            }
        }

        if(minPoint.x==target.x && minPoint.y==target.y){
            addToPath(minPoint);

            found=true;
            return;
        }

        if(tPathPoint.size()>26){
            return;
        }else if(minPoint.x==-1){
            return;
        }

        gotoAndCheck(minPoint);


    }

    public int calculateScoreFor(Point oPointToCalculate){

        //calcul distance target
        int xTargetDelta=Math.abs(oPointToCalculate.x-target.x);
        int yTargetDelta=Math.abs(oPointToCalculate.y-target.y);


        return (xTargetDelta+yTargetDelta);

    }

    public boolean isPointAlreadyInPath(Point oPointToCalculate) {



        for(Point oPathPoint : tPathPoint){

            if(oPointToCalculate.x==oPathPoint.x && oPointToCalculate.y==oPathPoint.y){
                return true;
            }
        }
        return false;


    }

    public void addToPath(Point oPoint){
        tPathPoint.add(oPoint);
    }

    private boolean isFree(Point oPointToCheck){
        if(oPointToCheck.x >= lengthX || oPointToCheck.x<0){
            return false;
        }else if(oPointToCheck.y >= lengthY || oPointToCheck.y<0){
            return false;
        }

        if(tMap==null){
            System.out.println("tMap is null !");
            return false;
        }

        try {
            if (tMap[oPointToCheck.y][oPointToCheck.x] == 0) {
                return true;
            }

        }catch (Exception e){
            System.out.println("not found in tMap x:"+Integer.toString(oPointToCheck.x)+" y:"+Integer.toString(oPointToCheck.y));
            return false;
        }
        return false;
    }


    public ArrayList<Point> getPointList() {
        return tPathPoint;
    }


}
