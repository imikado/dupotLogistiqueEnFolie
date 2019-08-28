package mika.dupot.logistiqueenfolie;


import org.junit.Test;

import java.util.ArrayList;

import mika.dupot.logistiqueenfolie.Domain.PathFinder;

import static org.junit.Assert.*;


import mika.dupot.logistiqueenfolie.Domain.Point;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PathFinderTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void objectCanBeCreated(){
        PathFinder oPathF = new PathFinder();

        assertTrue(true );

    }

    @Test
    public void startFrom_simple1(){

        int[][] tMap=new int[][]{
                {1,1,1,1},
                {0,0,0,0},
                {0,1,1,1},
                {0,0,0,0}
        };

        /*
        *       {1,1,1,1},
                {S,.,.,T},
                {0,1,1,1},
                {0,0,0,0}
        * */

        Point start=new Point(0,1);
        Point target=new Point(3,1);

        PathFinder oPathFinder = new PathFinder();
        oPathFinder.setMap(tMap);
        oPathFinder.setTarget(target);
        oPathFinder.setStart(start);

        //left false
        oPathFinder.calculatePointList(PathFinder.LEFT);
        assertEquals(false,oPathFinder.hasFound());

        //right true
        oPathFinder.calculatePointList(PathFinder.RIGHT);
        assertEquals(true,oPathFinder.hasFound());

        ArrayList<Point> pointList = oPathFinder.getPointList();

        ArrayList<Point> ExpectedPointList=new ArrayList<Point>();
        ExpectedPointList.add(new Point(0,1));
        ExpectedPointList.add(new Point(1,1));
        ExpectedPointList.add(new Point(2,1));
        ExpectedPointList.add(new Point(3,1));


        assertEquals(ExpectedPointList.toString(), pointList.toString());

        //up false
        oPathFinder.calculatePointList(PathFinder.UP);
        assertEquals(false,oPathFinder.hasFound());

        //down false
        oPathFinder.calculatePointList(PathFinder.DOWN);
        assertEquals(false,oPathFinder.hasFound());

    }

    @Test
    public void startFrom_simple(){

        int[][] tMap=new int[][]{
                {1,1,1,1},
                {0,0,0,0},
                {0,1,1,1},
                {0,0,0,0}
        };

        /*
        *
        *
        *       {1,1,1,1},
                {.,.,.,T},
                {.,1,1,1},
                {S,0,0,0}
        *
        * */

        Point start=new Point(0,3);
        Point target=new Point(3,1);

        PathFinder oPathFinder = new PathFinder();
        oPathFinder.setMap(tMap);
        oPathFinder.setTarget(target);
        oPathFinder.setStart(start);

        //left false
        oPathFinder.calculatePointList(PathFinder.LEFT);
        assertEquals(false,oPathFinder.hasFound());

        //right false
        oPathFinder.calculatePointList(PathFinder.RIGHT);
        assertEquals(false,oPathFinder.hasFound());


        //up true
        oPathFinder.calculatePointList(PathFinder.UP);
        assertEquals(true,oPathFinder.hasFound());

        ArrayList<Point> pointList = oPathFinder.getPointList();

        ArrayList<Point> ExpectedPointList=new ArrayList<Point>();
        ExpectedPointList.add(new Point(0,3));
        ExpectedPointList.add(new Point(0,2));
        ExpectedPointList.add(new Point(0,1));
        ExpectedPointList.add(new Point(1,1));
        ExpectedPointList.add(new Point(2,1));
        ExpectedPointList.add(new Point(3,1));


        assertEquals(ExpectedPointList.toString(), pointList.toString());

        //down false
        oPathFinder.calculatePointList(PathFinder.DOWN);
        assertEquals(false,oPathFinder.hasFound());

    }

    @Test
    public void startFrom_complex1(){

        int[][] tMap=new int[][]{
                {1,1,1,1},
                {0,0,0,0},
                {1,1,1,0},
                {0,0,0,0},
                {0,1,1,1},
                {0,0,0,0}
        };

        /*
        *
        *       {1,1,1,1},
                {0,0,0,T},
                {1,1,1,.},
                {.,.,.,.},
                {.,1,1,1},
                {S,0,0,0}
        *
        * */

        Point start=new Point(0,5);
        Point target=new Point(3,1);

        PathFinder oPathFinder = new PathFinder();
        oPathFinder.setMap(tMap);
        oPathFinder.setTarget(target);
        oPathFinder.setStart(start);

        //left false
        oPathFinder.calculatePointList(PathFinder.LEFT);
        assertEquals(false,oPathFinder.hasFound());

        //right false
        oPathFinder.calculatePointList(PathFinder.RIGHT);
        assertEquals(false,oPathFinder.hasFound());

        //up true
        oPathFinder.calculatePointList(PathFinder.UP);
        assertEquals(true,oPathFinder.hasFound());

        ArrayList<Point> pointList = oPathFinder.getPointList();

        ArrayList<Point> ExpectedPointList=new ArrayList<Point>();
        ExpectedPointList.add(new Point(0,5));
        ExpectedPointList.add(new Point(0,4));
        ExpectedPointList.add(new Point(0,3));
        ExpectedPointList.add(new Point(1,3));
        ExpectedPointList.add(new Point(2,3));
        ExpectedPointList.add(new Point(3,3));
        ExpectedPointList.add(new Point(3,2));
        ExpectedPointList.add(new Point(3,1));


        assertEquals(ExpectedPointList.toString(), pointList.toString());

        //down false
        oPathFinder.calculatePointList(PathFinder.DOWN);
        assertEquals(false,oPathFinder.hasFound());

    }

    //2019-05-08 09:30:31.589 15189-15189/mika.dupot.logistiqueenfolie I/ontouch: start x:1 y:0
    //2019-05-08 09:30:31.589 15189-15189/mika.dupot.logistiqueenfolie I/ontouch: target x:5 y:6

    @Test
    public void startFrom_complex2(){

        int[][] tMap=new int[][]{
                {0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 3, 3, 3, 3, 3},
                {0, 0, 0, 0, 0, 0},

        };

        System.out.println("lengthX "+Integer.toString( tMap[0].length));
        System.out.println("lengthY "+Integer.toString( tMap.length));


        Point start=new Point(5,6);
        Point target=new Point(1,0);

        PathFinder oPathFinder = new PathFinder();
        oPathFinder.setMap(tMap);
        oPathFinder.setTarget(target);
        oPathFinder.setStart(start);
        oPathFinder.calculatePointList(PathFinder.LEFT);

        ArrayList<Point> pointList = oPathFinder.getPointList();

        ArrayList<Point> ExpectedPointList=new ArrayList<Point>();
        ExpectedPointList.add(new Point(5,6));
        ExpectedPointList.add(new Point(4,6));
        ExpectedPointList.add(new Point(3,6));
        ExpectedPointList.add(new Point(2,6));
        ExpectedPointList.add(new Point(1,6));
        ExpectedPointList.add(new Point(0,6));
        ExpectedPointList.add(new Point(0,5));
        ExpectedPointList.add(new Point(0,4));
        ExpectedPointList.add(new Point(1,4));
        ExpectedPointList.add(new Point(2,4));
        ExpectedPointList.add(new Point(3,4));
        ExpectedPointList.add(new Point(4,4));
        ExpectedPointList.add(new Point(5,4));
        ExpectedPointList.add(new Point(5,3));
        ExpectedPointList.add(new Point(5,2));
        ExpectedPointList.add(new Point(4,2));
        ExpectedPointList.add(new Point(3,2));
        ExpectedPointList.add(new Point(2,2));
        ExpectedPointList.add(new Point(1,2));
        ExpectedPointList.add(new Point(0,2));
        ExpectedPointList.add(new Point(0,1));
        ExpectedPointList.add(new Point(0,0));
        ExpectedPointList.add(new Point(1,0));



        assertEquals(ExpectedPointList.toString(), pointList.toString());

    }


    @Test
    public void startFrom_complex3(){

        int[][] tMap=new int[][]{
                {9, 9, 9, 9, 9, 9},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 0, 0, 0, 0},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 2, 0, 3, 0},
                {0, 0, 0, 0, 0, 0},
                {4, 4, 4, 4, 4, 4},

        };

        /**
         *
         *
         *                 {9, 9, 9, 9, 9, 9},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {1, ., ., ., ., T},
         *                 {1, ., 2, 0, 3, 0},
         *                 {1, ., 2, 0, 3, 0},
         *                 {0, S, 0, 0, 0, 0},
         *                 {4, 4, 4, 4, 4, 4},
         *
         *
         */


        System.out.println("lengthX "+Integer.toString( tMap[0].length));
        System.out.println("lengthY "+Integer.toString( tMap.length));


        Point start=new Point(1,6);
        Point target=new Point(5,3);

        PathFinder oPathFinder = new PathFinder();
        oPathFinder.setMap(tMap);
        oPathFinder.setTarget(target);
        oPathFinder.setStart(start);


        oPathFinder.calculatePointList(PathFinder.LEFT);
        assertEquals(false,oPathFinder.hasFound());


        oPathFinder.calculatePointList(PathFinder.UP);
        //assertEquals(true,oPathFinder.hasFound());

        ArrayList<Point> pointList = oPathFinder.getPointList();

        ArrayList<Point> ExpectedPointList=new ArrayList<Point>();
        ExpectedPointList.add(new Point(1,6));
        ExpectedPointList.add(new Point(1,5));
        ExpectedPointList.add(new Point(1,4));
        ExpectedPointList.add(new Point(1,3));
        ExpectedPointList.add(new Point(2,3));
        ExpectedPointList.add(new Point(3,3));
        ExpectedPointList.add(new Point(4,3));
        ExpectedPointList.add(new Point(5,3));

        assertEquals(ExpectedPointList.toString(), pointList.toString());


        oPathFinder.calculatePointList(PathFinder.DOWN);
        assertEquals(false,oPathFinder.hasFound());

    }

    @Test
    public void startFrom_complex4(){

        int[][] tMap=new int[][]{
                {9, 9, 9, 9, 9, 9},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 0, 0, 0, 0},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 2, 0, 3, 0},
                {0, 0, 0, 0, 0, 0},
                {4, 4, 4, 4, 4, 4},

        };

        /**
         *
         *
         *                 {9, 9, 9, 9, 9, 9},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {1, ., ., ., 0, 0},
         *                 {1, S, 2, T, 3, 0},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {0, 0, 0, 0, 0, 0},
         *                 {4, 4, 4, 4, 4, 4},
         *
         *
         */


        System.out.println("lengthX "+Integer.toString( tMap[0].length));
        System.out.println("lengthY "+Integer.toString( tMap.length));


        Point start=new Point(1,4);
        Point target=new Point(3,4);

        PathFinder oPathFinder = new PathFinder();
        oPathFinder.setMap(tMap);
        oPathFinder.setTarget(target);
        oPathFinder.setStart(start);

        //UP
        oPathFinder.calculatePointList(PathFinder.UP);
        assertEquals(true,oPathFinder.hasFound());

        ArrayList<Point> pointListUp = oPathFinder.getPointList();

        int calculFor3_3=oPathFinder.calculateScoreFor(new Point(3,3));
        assertEquals(1,calculFor3_3);
        int calculFor4_3=oPathFinder.calculateScoreFor(new Point(4,3));
        assertEquals(2,calculFor4_3);


        assertEquals(5,pointListUp.size());


        //DOWN
        oPathFinder.calculatePointList(PathFinder.DOWN);
        assertEquals(true,oPathFinder.hasFound());


        ArrayList<Point> pointListDown = oPathFinder.getPointList();
        assertEquals(7,pointListDown.size());





    }

    @Test
    public void startFrom_complexWithPriorSameFlow(){

        int[][] tMap=new int[][]{
                {9, 9, 9, 9, 9, 9},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 0, 0, 0, 0},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 2, 0, 3, 0},
                {0, 0, 0, 0, 0, 0},
                {4, 4, 4, 4, 4, 4},

        };

        /**
         *
         *
         *                 {9, 9, 9, 9, 9, 9},
         *                 {1, S, 2, 0, 3, T},
         *                 {1, ., 2, 0, 3, .},
         *                 {1, ., ., ., ., .},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {0, 0, 0, 0, 0, 0},
         *                 {4, 4, 4, 4, 4, 4},
         *
         *
         */


        System.out.println("lengthX "+Integer.toString( tMap[0].length));
        System.out.println("lengthY "+Integer.toString( tMap.length));


        Point start=new Point(1,1);
        Point target=new Point(5,1);

        PathFinder oPathFinder = new PathFinder();
        oPathFinder.setMap(tMap);
        oPathFinder.setTarget(target);
        oPathFinder.setStart(start);


        oPathFinder.calculatePointList(PathFinder.DOWN);
        assertEquals(true,oPathFinder.hasFound());


        ArrayList<Point> pointList = oPathFinder.getPointList();

        ArrayList<Point> ExpectedPointList=new ArrayList<Point>();
        ExpectedPointList.add(new Point(1,1));
        ExpectedPointList.add(new Point(1,2));
        ExpectedPointList.add(new Point(1,3));
        ExpectedPointList.add(new Point(2,3));
        ExpectedPointList.add(new Point(3,3));
        ExpectedPointList.add(new Point(4,3));
        ExpectedPointList.add(new Point(5,3));
        ExpectedPointList.add(new Point(5,2));
        ExpectedPointList.add(new Point(5,1));

        assertEquals(ExpectedPointList.toString(), pointList.toString());



    }

    @Test
    public void calculateScoreFor1(){
        int[][] tMap=new int[][]{
                {9, 9, 9, 9, 9, 9},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 0, 0, 0, 0},
                {1, 0, 2, 0, 3, 0},
                {1, 0, 2, 0, 3, 0},
                {0, 0, 0, 0, 0, 0},
                {4, 4, 4, 4, 4, 4},

        };

        /**
         *
         *
         *
         *                 {9, 9, 9, 9, 9, 9},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {1, 0, 0, X, 0, 0},
         *                 {1, 0, 2, 0, 3, 0},
         *                 {1, X, 2, 0, 3, 0},
         *                 {0, S, X, T, 0, 0},
         *                 {4, 4, 4, 4, 4, 4},
         *
         *
         *
         */


        System.out.println("lengthX "+Integer.toString( tMap[0].length));
        System.out.println("lengthY "+Integer.toString( tMap.length));


        Point start=new Point(1,6);
        Point target=new Point(3,6);

        PathFinder oPathFinder = new PathFinder();
        oPathFinder.setMap(tMap);
        oPathFinder.setTarget(target);
        oPathFinder.setStart(start);

        int scoreByRight=oPathFinder.calculateScoreFor(new Point(2,6) );
        int scoreByUp=oPathFinder.calculateScoreFor(new Point(1,5) );


        assertEquals(1,scoreByRight);
        assertEquals(3,scoreByUp);


    }

}