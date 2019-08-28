package mika.dupot.logistiqueenfolie;


import org.junit.Test;

import mika.dupot.logistiqueenfolie.Domain.GamePlay;

import static org.junit.Assert.assertEquals;

public class GamePlayTest {

    @Test
    public void calculateWidthRatio() {


        GamePlay oGamePlay=new GamePlay();

        int ratio=oGamePlay.calculateWidthRatio(400, 50 , 100);

        assertEquals(200,ratio);
    }
}