package mika.dupot.logistiqueenfolie.Domain;

public class Rack {

    protected int item=0;

    protected int x;
    protected int y;

    public Rack(int item_,int x_,int y_){
        item=item_;
        x=x_;
        y=y_;

    }

    public boolean isFull(){
        if(item>0){
            return true;
        }
        return false;
    }
    public void empty(){
        item=0;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }


    public int getItem(){
        return item;
    }
    public void setItem(int item_){
        item=item_;
    }
}
