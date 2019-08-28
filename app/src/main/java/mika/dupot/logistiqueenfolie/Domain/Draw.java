package mika.dupot.logistiqueenfolie.Domain;

import java.util.Hashtable;

public class Draw {

    //type
    public static final int TYPE_CASE=0;

    public static final int TYPE_CAMERA_RACK=30;
    public static final int TYPE_TABLET_RACK=31;
    public static final int TYPE_USBKEY_RACK=32;
    public static final int TYPE_EMPTY_RACK=33;


    public static final int TYPE_BELT=4;

    public static final int TYPE_BOX=5;

    public static final int TYPE_PATH=6;
    public static final int TYPE_ORDER=7;
    public static final int TYPE_PLAYER=8;

    public static final int TYPE_ANIMATION_BOX_SUCCESS=9;
    public static final int TYPE_ANIMATION_BOX_FAILED = 10;


    //param
    public static final int PARAM_ITEMLIST=1;
    public static final int PARAM_ORDERID=2;
    public static final int PARAM_TIMELEFT=3;
    public static final int PARAM_TIMETOTAL=4;
    public static final int PARAM_TARGETX=5;
    public static final int PARAM_TARGETY=6;
    public static final int PARAM_TICMOVINGTO=7;
    public static final int PARAM_ROWUSING=8;
    public static final int PARAM_ITEM=9;

    public static final int TYPE_ARROWPUTONRACK = 10;
    public static final int TYPE_ARROWPUTINBOX = 11;
    public static final int TYPE_ARROWTAKEFROMRACK = 12;

    public static final int TYPE_LIFEUP = 13;
    public static final int TYPE_LIFEDOWN = 14;
    public static final int TYPE_DONT = 15;

    public static final int PARAM_ISARRIVED = 16;

    public static final int PARAM_ISRUNNING = 17;


    private final Hashtable paramList;
    private int type;
    private int x;
    private int y;


    public Draw(int type_,int x_,int y_){
        type=type_;
        x=x_;
        y=y_;
        paramList=new Hashtable();
    }

    public Draw(int type_,int x_,int y_, Hashtable paramList_){
        type=type_;
        x=x_;
        y=y_;
        paramList=paramList_;
    }

    public int getType(){
        return type;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Hashtable getParamList(){
        return paramList;
    }

    public Object getParam(int paramField_){
        return paramList.get(paramField_);
    }



}
