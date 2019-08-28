package mika.dupot.logistiqueenfolie.Domain;

import java.util.ArrayList;

public class Order {
    protected int orderId;

    protected ArrayList<Integer> tItem;


    public Order(int orderId_,  ArrayList<Integer> tItem_   ){
        orderId=orderId_;

        tItem=tItem_;
    }

    public ArrayList<Integer> getListItem(){
        return tItem;
    }


    public int getOrderId() {
        return orderId;
    }
}
