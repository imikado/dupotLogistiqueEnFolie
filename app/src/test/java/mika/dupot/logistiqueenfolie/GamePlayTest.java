package mika.dupot.logistiqueenfolie;


import org.junit.Test;

import java.util.ArrayList;

import mika.dupot.logistiqueenfolie.Domain.GamePlay;
import mika.dupot.logistiqueenfolie.Domain.Order;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

        //on instancie le jeu
        GamePlay oGamePlay=GamePlay.getInstance();
        //on desactive le tic d'animation
        oGamePlay.getPlayer().setTicMovingNb(0);
        //on clic à la position de départ du joueur pour desactiver l'animation de départ
        oGamePlay.onTouchCoord(0,6);

        //on cycle 7 fois pour avoir une premiere commande
        for(int i=0;i<7;i++) {
            oGamePlay.cycle();
        }

        //on calcul le rendu du jeu
        oGamePlay.calculRender();
        // pour verifier que le personnage est bien la
        assertEquals(0,oGamePlay.getPlayer().getX());
        assertEquals(6,oGamePlay.getPlayer().getY());

        //on verifie la presence d'une commande
        ArrayList<Order> orderList = oGamePlay.getListOrder();
        //que cette commande contient bien une clef usb
        Order firstOrder = orderList.get(0);
        assertEquals( 1,firstOrder.getOrderId());
        assertEquals( GamePlay.ITEM_USBKEY,firstOrder.getListItem().get(0).intValue());

        //on clic pour aller a coté d'une etagere de clef usb
        oGamePlay.onTouchCoord(3,5);

        //on boucle 5 fois pour y arriver
        for(int i=0;i<4;i++) {
            oGamePlay.calculRender();
            oGamePlay.calculRender();
        }
        //on verifie qu'on est bien arrivé
        assertEquals(3,oGamePlay.getPlayer().getX());
        assertEquals(5,oGamePlay.getPlayer().getY());
        //on verifie que l'etagere contient bien une clef usb
        assertEquals(GamePlay.ITEM_USBKEY,oGamePlay.getRackAtCoord(2,5).getItem());
        //logiquement, une fleche devrait apparaitre
        assertNotNull( oGamePlay.getArrowPlayerTakeObjectFromRack());

        //on clique sur la fleche
        oGamePlay.onTouchCoord(2,5);
        //le robot doit etre plein, et l'etagere vide
        assertTrue( oGamePlay.getPlayer().isFull());
        assertFalse( oGamePlay.getRackAtCoord(2,5).isFull());
        //on se deplace jusqu'au carton de la commande
        oGamePlay.onTouchCoord(5,6);
        //on laisse les 5 etapes pour y arriver
        for(int i=0;i<4;i++) {
            oGamePlay.calculRender();
            oGamePlay.calculRender();
        }
        //on verifie qu'on est arrivé
        assertEquals(5,oGamePlay.getPlayer().getX());
        assertEquals(6,oGamePlay.getPlayer().getY());
        //on laisse le temps à l'animation de faire arriver notre carton
        for(int i=0;i<8;i++) {
            oGamePlay.cycle();
        }
        //on verifie qu'il est bien arrive en coordonnee 5
        assertEquals(5,oGamePlay.getBoxAtX(5).getX());
        //et qu'il a le status arrivé
        assertTrue( oGamePlay.getBoxAtX(5).isArrived());
        //on doit avoir une fleche d'affiché
        assertNotNull( oGamePlay.getArrowPlayerPutObjectInBox());
        //on clique dessus
        oGamePlay.onTouchCoord(5,7);
        //le robot devient vide
        assertFalse( oGamePlay.getPlayer().isFull());
        //le score augmente à 5
        assertEquals(5,oGamePlay.getScore());


    }
}