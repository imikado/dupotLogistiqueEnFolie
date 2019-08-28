package mika.dupot.logistiqueenfolie.Domain;

public class Point {
    public int x;
    public int y;

    public Point(int x_,int y_){
        x=x_;
        y=y_;
    }

    public String toString(){
        return Integer.toString(x)+"_"+Integer.toString(y);
    }
}
