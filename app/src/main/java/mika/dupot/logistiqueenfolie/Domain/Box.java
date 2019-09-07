package mika.dupot.logistiqueenfolie.Domain;

import java.util.ArrayList;

public class Box {

    private final int orderId;
    protected boolean bExist=true;

    private int timeLeft;
    private int timeTotal;

    protected ArrayList<Integer> boxItemList;
    protected ArrayList<Integer> orderItemList;

    protected int x;
    protected int y;
    protected int targetX;

    protected int ticLeft=4;
    protected int ticLeftNb=5;

    public Box(int orderId_,ArrayList<Integer> orderItemList_, int x_,int y_,int targetX_){
        
        orderId=orderId_;
        boxItemList =new ArrayList<Integer>();

        orderItemList=orderItemList_;

        x=x_;
        y=y_;
        targetX=targetX_;

        timeTotal=10+(5*orderItemList.size());
        timeLeft=timeTotal;
    }

    public int getX(){
        return x;
    }
    public void setTargetX(int targetX_){

        targetX=targetX_;
        ticLeft=0;
    }

    public int getTicLeft(){
        return ticLeft;
    }

    public void setTicLeftNb(int ticLeftNb_){
        ticLeftNb=ticLeftNb_;
    }

    public void processBeforeDrawing() {
        if(x<targetX){
            ticLeft++;
            if(ticLeft>=ticLeftNb){
                ticLeft=0;
                x++;

                GamePlay.getInstance().processGameForPlayer();
            }

        }

        if(x>targetX){
            x=targetX;
        }

    }

    public void render(){

    }

    public boolean isArrived(){
        if(targetX==x) {
            return true;
        }
        return false;
    }


    public void decreaseTimeLeft(){

        if(isArrived()) {
            timeLeft -= 1;

            if (timeLeft < 0) {
                timeLeft = 0;
            }

        }
    }
    public int getTimeLeft(){
        return timeLeft;
    }

    public int getTimeTotal(){
        return timeTotal;
    }

    public void doesntExist(){
        bExist=false;
    }

    public boolean exist(){
        return bExist;
    }

    public void addItem(int item){
        boxItemList.add(item);
    }


    public ArrayList<Integer> getBoxItemList(){
        return boxItemList;
    }


    public int getOrderId() {
        return orderId;
    }

    public boolean shouldAccept(int itemToPull) {
        if(orderItemList.contains(itemToPull) && !boxItemList.contains(itemToPull)){
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if(boxItemList.size()>0 && orderItemList.size()== boxItemList.size()){
            return true;
        }
        return false;
    }


}
