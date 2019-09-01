package mika.dupot.logistiqueenfolie;


import org.junit.Test;

import java.util.ArrayList;

import mika.dupot.logistiqueenfolie.Domain.GamePlay;
import mika.dupot.logistiqueenfolie.Domain.Message;
import mika.dupot.logistiqueenfolie.Domain.Order;


import static org.junit.Assert.assertEquals;

public class GamePlayTest {

    @Test
    public void testMove() {

        /*
        {9, 9, 9, 9, 9, 9},
        {1, 0, 1, 0, 1, 0},
        {1, 0, 1, 0, 1, 0},
        {0, 0, 0, 0, 0, 0},
        {1, 0, 1, 0, 1, 0},
        {1, 0, 1, 0, 1, 0},
        {J, 0, 0, 0, 0, 0},
        {4, 4, 4, 4, 4, 4},
        */

        //s 0,6
        //t 3,3

        GamePlay oGamePlay=GamePlay.getInstance();
        oGamePlay.resetGame();

        oGamePlay.getPlayer().setTicMovingNb(0);

        oGamePlay.onTouchCoord(0,6);
        oGamePlay.cycle();
        oGamePlay.cycle();
        oGamePlay.cycle();
        oGamePlay.cycle();
        oGamePlay.cycle();
        oGamePlay.cycle();
        oGamePlay.cycle();

        oGamePlay.calculRender();

        assertEquals(0,oGamePlay.getPlayer().getX());
        assertEquals(6,oGamePlay.getPlayer().getY());

        ArrayList<Order> orderList = oGamePlay.getListOrder();

        Order firstOrder = orderList.get(0);
        assertEquals( 1,firstOrder.getOrderId());
        assertEquals( GamePlay.ITEM_USBKEY,firstOrder.getListItem().get(0).intValue());

        oGamePlay.onTouchCoord(3,5);

        for(int i=0;i<4;i++) { //needs 5 step to go to the target
            oGamePlay.calculRender();
            oGamePlay.calculRender();
        }

        assertEquals(3,oGamePlay.getPlayer().getX());
        assertEquals(5,oGamePlay.getPlayer().getY());

        assertEquals(GamePlay.ITEM_USBKEY,oGamePlay.getRackAtCoord(2,5).getItem());


        assertEquals( "2_5", oGamePlay.getArrowPlayerTakeObjectFromRack().toString());

        oGamePlay.onTouchCoord(2,5);
        oGamePlay.cycle();

        assertEquals( true, oGamePlay.getPlayer().isFull());

        assertEquals(false,oGamePlay.getRackAtCoord(2,5).isFull());

        oGamePlay.onTouchCoord(5,6);

        for(int i=0;i<4;i++) { //needs 5 step to go to the target
            oGamePlay.calculRender();
            oGamePlay.calculRender();
        }

        assertEquals(5,oGamePlay.getPlayer().getX());
        assertEquals(6,oGamePlay.getPlayer().getY());

        for(int i=0;i<7;i++) {
            oGamePlay.cycle();
        }

        assertEquals(5,oGamePlay.getBoxAtX(5).getX());
        assertEquals(true,oGamePlay.getBoxAtX(5).isArrived());



        assertEquals( "5_7", oGamePlay.getArrowPlayerPutObjectInBox().toString());

        oGamePlay.onTouchCoord(5,7);

        assertEquals( false, oGamePlay.getPlayer().isFull());

        assertEquals(5,oGamePlay.getScore());




    }
}