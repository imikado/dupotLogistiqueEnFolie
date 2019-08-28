package mika.dupot.logistiqueenfolie.Domain;

public class Message {

    public static final int TYPE_EMPTY=0;
    public static final int TYPE_TOAST = 1;
    public static final int TYPE_TOAST_ERROR = 2;

    public static final int TYPE_ANIMATION =3;


    protected int type=0;
    protected String argument;

    public void setType(int type_){
        type=type_;
    }
    public int getType(){
        return type;
    }

    public void setArgument(String argument_){
        argument=argument_;
    }


    public String getArgument(){
        return argument;
    }
}
