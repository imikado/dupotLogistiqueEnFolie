package mika.dupot.logistiqueenfolie.Domain;

import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;


/*
Message from User

user: red,blue,green,yellow, none

action: askConnect

action: askMove
x: N
y: N

action: askAddBomb
x: N
y: N

Message from server

status: OK,KO

user: red,blue,green,yellow

[ que le user]
action: setUser
user: red,blue,green,yellow
[ que le user]

[ autre users]
action: userConnected
user:red,blue,green,yellow
[ autre users]

action: move
x: N
y: N

action: addBomb
x: N
y: N

action: exploseBomb
x: N
y: N
length: N

 */

public class GamePlay {


    protected int iScore=0;

    public static final int ITEM_CAMERA = 1;
    public static final int ITEM_TABLET = 2;
    public static final int ITEM_USBKEY = 3;

    public static final int ITEM_RACK = 1;

    private ArrayMap<Integer, Integer> stockList;



    public static final int CASE_BELT=4;

    public static int max=6;


    private int[][] map;


    public static GamePlay gamePlay;
    private float screenCaseWidth;

    private ArrayList<Box> boxList;
    private ArrayList<Order> orderList;
    private ArrayList<Rack> rackList;
    private int currentTemplateType=0;
    private int currentOrderId=1;

    private int nbLife;
    private int level=1;
    private int nbCycleOrder=0;

    private Player player;

    private Point arrowPlayerTakeObjectFromRack;
    private Point arrowPlayerPutObjectOnRack;
    private Point arrowPlayerPutObjectInBox;

    private Point arrowPlayerCannotTakeObjectFromRack;
    private Point arrowPlayerCannotPutObjectOnRack;
    private Point arrowPlayerCannotPutObjectInBox;

    private boolean isBeltRunning=false;

    private boolean isAnimationBoxSucessRunning=false;

    private int ticAnimation=0;
    private Draw pendingAnimation;


    //instance
    public GamePlay() {

    }
    public static GamePlay getInstance() {
        if (gamePlay == null) {
            gamePlay = new GamePlay();
            gamePlay.resetGame();
        }
        return gamePlay;
    }
    public static void resetInstance(){
        gamePlay =null;
    }


    //score
    public void addToScore(int add_){
        iScore+=add_;
    }
    public int getScore(){
        return iScore;
    }

    //box / order
    public Box getBoxAtX(int x_) {
        int maxX= map[0].length-1;

        int boxX=maxX-x_;

        if(boxList.size() <= boxX){
            Box nullBox=new Box(0,new ArrayList<Integer>(),0,0,0);
            nullBox.doesntExist();

            return nullBox;
        }else{
            return boxList.get(boxX);
        }


    }
    public ArrayList<Order> getListOrder(){
        return orderList;
    }


    //map
    public int getMapMaxX(){
        return this.map[0].length;
    }
    public int getMapMaxY(){
        return this.map.length;
    }
    public int getMapValue(int x_,int y_){
        if(x_> map[0].length || x_<0 || y_> map.length || y_<0 ){
            return -1;
        }
        return this.map[y_][x_];
    }
    public int[][] getMap(){

        return map;
    }
    public boolean isFree(int x_, int y_) {

        if (isNotInMap(x_, y_)) {
            return false;
        }

        return this.map[y_][x_] == 0;
    }
    private boolean isNotInMap(int x_, int y_) {
        if (y_ < 0 || y_ > map.length) {
            return true;
        } else return x_ < 0 || x_ > map[0].length;
    }

    public void subToStock(int item_){
        stockList.put(item_, (stockList.get(item_)-1));
    }
    public void addToStock(int item_){
        stockList.put(item_, (stockList.get(item_)+1));
    }
    public int getFromStock(int item_){
        return stockList.get(item_);
    }


    //game
    public void resetGame(){


        isAnimationBoxSucessRunning=false;

        nbLife=3;
        iScore=0;


        currentTemplateType=0;
        currentOrderId=1;


        this.map =null;

        this.map = new int[][]{
                {9, 9, 9, 9, 9, 9},
                {1, 0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0},
                {4, 4, 4, 4, 4, 4},

        };
        
        //orders
        orderList =new ArrayList<Order>();
        boxList =new ArrayList<Box>();

        player =new Player();
        player.setX(0);
        player.setY(6);


        PathFinder oPatFinder = new PathFinder();
        oPatFinder.setMap(getMap());
        oPatFinder.setTarget(new Point(3, 3));
        oPatFinder.setStart(new Point(player.getX(), player.getY()));


        ArrayList<Point> bestPathPointList=null;

        oPatFinder.calculatePointList(PathFinder.RIGHT);
        ArrayList<Point> pathPointListUp = oPatFinder.getPointList();
        if(oPatFinder.hasFound()) {
            bestPathPointList = pathPointListUp;
        }


        player.setTargetPath(bestPathPointList);

        //cycle to create rack

        rackList=new ArrayList<Rack>();

        stockList=new ArrayMap<Integer, Integer>();
        stockList.put(ITEM_CAMERA,0);
        stockList.put(ITEM_USBKEY,0);
        stockList.put(ITEM_TABLET,0);


        for(int y=0;y< map.length;y++){
            for(int x=0;x<map[0].length;x++){
                if(getMapValue(x,y)==ITEM_RACK) {

                    int item_rack=0;

                    if(x==0) {
                        item_rack=ITEM_CAMERA;

                        addToStock(item_rack);

                    }else if(x==2) {
                        item_rack=ITEM_USBKEY;

                        addToStock(item_rack);

                    }else if(x==4) {
                        item_rack=ITEM_TABLET;

                        addToStock(item_rack);
                    }


                    rackList.add( new Rack(item_rack,x,y));
                }



            }
        }

        processGameForPlayer();

    }

    public Rack getRackAtCoord(int x_,int y_){
        for(Rack rack : rackList){
            if(rack.getX()==x_ && rack.getY()==y_){
                return rack;
            }
        }
        return null;
    }

    public String cycle() {

        try {

            nbCycleOrder++;
            if(nbCycleOrder>6){
                addNewOrder();
                nbCycleOrder=0;
            }


            isBeltRunning=false;

            for(Box oBox: boxList){
                oBox.render();
                oBox.decreaseTimeLeft();

                if(oBox.getTimeLeft()==0){

                    Box boxToFail=oBox;

                    enableAnimation(new Draw(Draw.TYPE_ANIMATION_BOX_FAILED,boxToFail.getX(),7));

                    calculateDecreaseScoreFor(oBox);
                }

                if(oBox.isArrived()==false){
                    isBeltRunning=true;
                }
            }



            if(getNbLife()==0){
                return "GAME_OVER";
            }

            return "CONTINUE";

        }catch (Exception e){
            Log.e("GamePlay ERROR",e.toString());

            return "ERROR";
         }

    }
    public Message onTouchCoord(int x_,int y_){

        Point arrowPlayerToTakeFromRack=getArrowPlayerTakeObjectFromRack();
        Point arrowPlayerToPutOnRack=getArrowPlayerPutObjectOnRack();
        Point arrowPlayerToPutInBox=getArrowPlayerPutObjectInBox();

        Point arrowPlayerCannotTakeFromRack=getArrowPlayerCannotTakeObjectFromRack();
        Point arrowPlayerCannotPutOnRack=getArrowPlayerCannotPutObjectOnRack();
        Point arrowPlayerCannotPutInBox=getArrowPlayerCannotPutObjectInBox();


        if(arrowPlayerToTakeFromRack!=null && arrowPlayerToTakeFromRack.x==x_ && arrowPlayerToTakeFromRack.y==y_) {

            Rack rackToTakeFromObject = getRackAtCoord(x_, y_);
            player.setItem(rackToTakeFromObject.getItem());

            rackToTakeFromObject.empty();

            processGameForPlayer(player);
            return new Message();

        }else if(arrowPlayerCannotTakeFromRack!=null && arrowPlayerCannotTakeFromRack.x==x_ && arrowPlayerCannotTakeFromRack.y==y_){

            if(player.isFull()){
                Message oMessage=new Message();
                oMessage.setType(Message.TYPE_TOAST);
                oMessage.setArgument(" déjà plein ! ");

                return oMessage;
            }

        }else if(arrowPlayerToPutOnRack!=null && arrowPlayerToPutOnRack.x==x_ && arrowPlayerToPutOnRack.y==y_) {

            Rack rackToPutOnObject = getRackAtCoord(x_, y_);

            rackToPutOnObject.setItem(player.getItem());

            player.empty();

            processGameForPlayer(player);
            return new Message();

        }else if(arrowPlayerCannotPutOnRack!=null && arrowPlayerCannotPutOnRack.x==x_ && arrowPlayerCannotPutOnRack.y==y_) {

            Rack rackWhereCannotPutItem=getRackAtCoord(arrowPlayerCannotPutOnRack.x,arrowPlayerCannotPutOnRack.y);

            if(rackWhereCannotPutItem.isFull()){
                Message oMessage=new Message();
                oMessage.setType(Message.TYPE_TOAST_ERROR);
                oMessage.setArgument(" L'étagère est déjà pleine ! ");

                return oMessage;
            }

        }else if(arrowPlayerToPutInBox!=null && arrowPlayerToPutInBox.x==x_ && arrowPlayerToPutInBox.y==y_) {

            Box boxToPutObjectIn = getBoxAtX(x_);

            boxToPutObjectIn.addItem(player.getItem());

            if(getFromStock(player.getItem()) > 0) {
                subToStock(player.getItem());
            }

            if(getFromStock(player.getItem())==0) {
                addObjectOnRandomRack(player.getItem());
            }

            player.empty();

            if(boxToPutObjectIn.isFull()){
                int scoreBox=calculateScoreFor(boxToPutObjectIn);

                Message oMessage=new Message();
                oMessage.setType(Message.TYPE_ANIMATION);

                enableAnimation(new Draw(Draw.TYPE_ANIMATION_BOX_SUCCESS,boxToPutObjectIn.getX(),7));

                processGameForPlayer(player);
                return oMessage;
            }


            processGameForPlayer(player);
            return new Message();


        }else if(arrowPlayerCannotPutInBox!=null && arrowPlayerCannotPutInBox.x==x_ && arrowPlayerCannotPutInBox.y==y_) {

            if(player.isFull()){
                Message oMessage=new Message();
                oMessage.setType(Message.TYPE_TOAST_ERROR);
                oMessage.setArgument("Pas pour cette commande ! ");

                return oMessage;
            }else{
                Message oMessage=new Message();
                oMessage.setType(Message.TYPE_TOAST_ERROR);
                oMessage.setArgument("Vous n'avez pas de produit ! ");

                return oMessage;
            }

        }


        if(isFree(x_,y_)) {

            PathFinder oPatFinder = new PathFinder();
            oPatFinder.setMap(getMap());
            oPatFinder.setTarget(new Point(x_, y_));
            oPatFinder.setStart(new Point(player.getX(), player.getY()));


            ArrayList<Point> bestPathPointList=null;

            oPatFinder.calculatePointList(PathFinder.UP);
            ArrayList<Point> pathPointListUp = oPatFinder.getPointList();
            if(oPatFinder.hasFound()) {
                bestPathPointList = pathPointListUp;
            }

            oPatFinder.calculatePointList(PathFinder.DOWN);
            ArrayList<Point> pathPointListDown = oPatFinder.getPointList();
            if(oPatFinder.hasFound() && (bestPathPointList==null || pathPointListDown.size() < bestPathPointList.size())){
                bestPathPointList=pathPointListDown;
            }

            oPatFinder.calculatePointList(PathFinder.LEFT);
            ArrayList<Point> pathPointListLeft = oPatFinder.getPointList();
            if(oPatFinder.hasFound() && (bestPathPointList==null || pathPointListLeft.size() < bestPathPointList.size())){
                bestPathPointList=pathPointListLeft;
            }

            oPatFinder.calculatePointList(PathFinder.RIGHT);
            ArrayList<Point> pathPointListRight = oPatFinder.getPointList();
            if(oPatFinder.hasFound() && (bestPathPointList==null || pathPointListRight.size() < bestPathPointList.size())){
                bestPathPointList=pathPointListRight;
            }

            player.setTargetPath(bestPathPointList);

        }



        processGameForPlayer(player);
        return new Message();

    }

    public void enableAnimation(Draw animationDraw_){
        pendingAnimation=animationDraw_;
    }
    public void disableAnimation(){
        pendingAnimation=null;
    }
    public boolean animationIsRunning(){
        if(pendingAnimation==null){
            return false;
        }
        return true;
    }

    public void addObjectOnRandomRack(int item_) {
        int randomRack=(int)(Math.random() * rackList.size() );

        if(rackList.get(randomRack).isFull()) {
            addObjectOnRandomRack(item_);
        }else{
            rackList.get(randomRack).setItem(item_);
        }

    }

    public void updateBoxList(){
        int x2=5;
        for(Box oBox: boxList){
            oBox.setTargetX(x2);

            x2--;
        }
    }

    //score
    public void calculateDecreaseScoreFor(Box oBoxToFail_) {
        subLife();

        for(Order oOrderLoop: orderList){
            if(oOrderLoop.orderId==oBoxToFail_.getOrderId()){
                orderList.remove(oOrderLoop);
                break;
            }
        }

        boxList.remove(oBoxToFail_);

        updateBoxList();


    }
    public int calculateScoreFor(Box oBoxToWin_) {

        int scoreBox=5*(oBoxToWin_.getBoxItemList().size());

        addToScore(scoreBox);

        for(Order oOrderLoop: orderList){
            if(oOrderLoop.orderId==oBoxToWin_.getOrderId()){
                orderList.remove(oOrderLoop);
                break;
            }
        }

        boxList.remove(oBoxToWin_);

        updateBoxList();


        if(iScore <50){
            level=1;
        }else if(iScore <150){
            level=2;
        }else if(iScore <250){
            level=3;
        }


        return scoreBox;


    }


    //arrow get item

    public void resetAllArrow(){

        arrowPlayerPutObjectInBox=null;
        arrowPlayerTakeObjectFromRack=null;
        arrowPlayerPutObjectOnRack=null;

        arrowPlayerCannotPutObjectInBox=null;
        arrowPlayerCannotTakeObjectFromRack=null;
        arrowPlayerCannotPutObjectOnRack=null;


    }


    //life
    private void subLife(){
        nbLife-=1;

        if(nbLife<0){
            nbLife=0;
        }
    }
    public int getNbLife(){
        return nbLife;
    }


    public void addNewOrder() {


        ArrayList<Integer> tItemNewOrder = new ArrayList<Integer>();

        if(level == 1){

            if (currentTemplateType == 0){
                tItemNewOrder.add(3);

            }else if (currentTemplateType == 1){
                tItemNewOrder.add(2);

            }else if (currentTemplateType == 2){
                tItemNewOrder.add(1);
            }else if (currentTemplateType == 3){
                tItemNewOrder.add(2);
            }

        }else  if(level ==2 ){

            if (currentTemplateType == 0){
                tItemNewOrder.add(3);
                tItemNewOrder.add(1);

            }else if (currentTemplateType == 1){
                tItemNewOrder.add(1);
                tItemNewOrder.add(2);

            }else if (currentTemplateType == 2){
                tItemNewOrder.add(3);
            }else if (currentTemplateType == 3){
                tItemNewOrder.add(2);
                tItemNewOrder.add(1);
            }

        }else{ //level 3

            if (currentTemplateType == 0){
                tItemNewOrder.add(3);
                tItemNewOrder.add(1);
                tItemNewOrder.add(2);

            }else if (currentTemplateType == 1){
                tItemNewOrder.add(1);
                tItemNewOrder.add(3);

            }else if (currentTemplateType == 2){
                tItemNewOrder.add(3);
                tItemNewOrder.add(2);
                tItemNewOrder.add(1);
            }else if (currentTemplateType == 3){
                tItemNewOrder.add(2);
                tItemNewOrder.add(1);
            }

        }


        orderList.add(new Order(currentOrderId, tItemNewOrder));

        int x2=5- boxList.size();
        boxList.add(new Box(currentOrderId,tItemNewOrder,-1,7,x2));

        currentOrderId++;
        currentTemplateType++;
        if(currentTemplateType>3){
            currentTemplateType=0;
        }


    }

    public Player getPlayer(){
        return player;
    }

    public Draw calculRenderAnimation(){
        return pendingAnimation;
    }

    public ArrayList<Draw> calculRender(){

        ArrayList<Draw> drawList=new ArrayList<Draw>();

        drawList=getRenderMap(drawList);

        drawList=getRenderPath(drawList);

        drawList=getRenderOrders(drawList);

        drawList=getRenderPlayer(drawList);

        drawList=getRenderArrow(drawList);

        return drawList;

    }

    private ArrayList<Draw> getRenderPath(ArrayList<Draw> drawList) {
        Player oPlayer=getPlayer();

        if(oPlayer.shouldProcessPathList()) {


            for (int i = 0; i < oPlayer.getPathPointList().size(); i++) {

                int pathX=oPlayer.getPathPointList().get(i).x;
                int pathY=oPlayer.getPathPointList().get(i).y;


                drawList.add(new Draw(Draw.TYPE_PATH,pathX,pathY));


            }
        }

        return drawList;

    }

    public Draw getRackToDraw(int x_,int y_){
        Rack rackToDraw=getRackAtCoord(x_,y_);

        int typeDraw=0;

        if(rackToDraw==null || rackToDraw.isFull()==false){
            return new Draw(Draw.TYPE_EMPTY_RACK,x_,y_);

        }else if(rackToDraw.getItem()==ITEM_CAMERA) {
            typeDraw=Draw.TYPE_CAMERA_RACK;
        }else if(rackToDraw.getItem()==ITEM_TABLET) {
            typeDraw=Draw.TYPE_TABLET_RACK;
        }else if(rackToDraw.getItem()==ITEM_USBKEY) {
            typeDraw=Draw.TYPE_USBKEY_RACK;
        }

        return new Draw(typeDraw,x_,y_);


    }

    public ArrayList<Draw> getRenderMap( ArrayList<Draw> drawList_) {


        for(int y=0;y< getMapMaxY();y++) {


            for (int x = 0; x < getMapMaxX(); x++) {

                if(y>0) {

                    drawList_.add(new Draw(Draw.TYPE_CASE,x,y));

                }

                if (getMapValue(x, y) == GamePlay.ITEM_RACK ) {

                    drawList_.add(getRackToDraw( x, y));


                } else if (getMapValue(x, y) == CASE_BELT) {


                    if(isBeltRunning){
                        Hashtable beltParamList=new Hashtable();
                        beltParamList.put(Draw.PARAM_ISRUNNING,true);

                        drawList_.add(new Draw(Draw.TYPE_BELT,x,y,beltParamList));
                    }else{
                        Hashtable beltParamList=new Hashtable();
                        beltParamList.put(Draw.PARAM_ISRUNNING,false);

                        drawList_.add(new Draw(Draw.TYPE_BELT,x,y,beltParamList));

                    }

                    Box oBox=getBoxAtX(x);
                    if(oBox.exist()) {

                        oBox.render();

                        Hashtable paramList=new Hashtable();
                        paramList.put(Draw.PARAM_ITEMLIST,oBox.getBoxItemList());
                        paramList.put(Draw.PARAM_ORDERID,oBox.getOrderId());
                        paramList.put(Draw.PARAM_TIMELEFT,oBox.getTimeLeft());
                        paramList.put(Draw.PARAM_TIMETOTAL,oBox.getTimeTotal());
                        paramList.put(Draw.PARAM_TICMOVINGTO,oBox.getTicLeft());

                        paramList.put(Draw.PARAM_ISARRIVED,oBox.isArrived());


                        drawList_.add(new Draw(Draw.TYPE_BOX,oBox.getX(),y,paramList ));

                    }


                }
            }
        }


        return drawList_;


    }

    public ArrayList<Draw> getRenderOrders(ArrayList<Draw> drawList_){
        ArrayList<Order> tOrder=getListOrder();

        //int caseWidth=(getCaseWidth() /4);

        int x2=0;
        for(Order oOrder : tOrder){

            int x3=x2;

            Hashtable paramList=new Hashtable();
            paramList.put(Draw.PARAM_ORDERID,oOrder.getOrderId());
            paramList.put(Draw.PARAM_ITEMLIST,oOrder.getListItem());

            drawList_.add(new Draw(Draw.TYPE_ORDER,x3,0,paramList));


            int ecart=oOrder.getListItem().size();

            if(ecart <2){
                ecart=2;
            }

            x2+=ecart+2;
        }

        return drawList_;
    }

    private ArrayList<Draw> getRenderPlayer(ArrayList<Draw> drawList_) {


        Hashtable paramList=new Hashtable();
        paramList.put(Draw.PARAM_TARGETX, player.getTargetX());
        paramList.put(Draw.PARAM_TARGETY, player.getTargetY());
        paramList.put(Draw.PARAM_TICMOVINGTO, player.getTicMovingTo());
        paramList.put(Draw.PARAM_ROWUSING, player.getRowUsing());
        paramList.put(Draw.PARAM_ITEM, player.getItem());


        player.processBeforeDraw();

        drawList_.add(new Draw(Draw.TYPE_PLAYER, player.getX(), player.getY(),paramList));

        player.processAfterDraw();



        return drawList_;
    }

    private ArrayList<Draw> getRenderArrow(ArrayList<Draw> drawList_) {


        if(getArrowPlayerPutObjectInBox()!=null){
            drawList_.add(new Draw(Draw.TYPE_ARROWPUTINBOX,getArrowPlayerPutObjectInBox().x,getArrowPlayerPutObjectInBox().y));
        }
        if(getArrowPlayerTakeObjectFromRack()!=null){
            drawList_.add(new Draw(Draw.TYPE_ARROWTAKEFROMRACK,getArrowPlayerTakeObjectFromRack().x,getArrowPlayerTakeObjectFromRack().y));
        }
        if(getArrowPlayerPutObjectOnRack()!=null){
            drawList_.add(new Draw(Draw.TYPE_ARROWPUTONRACK,getArrowPlayerPutObjectOnRack().x,getArrowPlayerPutObjectOnRack().y));
        }

        if(getArrowPlayerCannotPutObjectInBox()!=null){

            drawList_.add(new Draw(Draw.TYPE_ARROWPUTINBOX,getArrowPlayerCannotPutObjectInBox().x,getArrowPlayerCannotPutObjectInBox().y));
            drawList_.add(new Draw(Draw.TYPE_DONT,getArrowPlayerCannotPutObjectInBox().x,getArrowPlayerCannotPutObjectInBox().y));
        }
        if(getArrowPlayerCannotTakeObjectFromRack()!=null){
            drawList_.add(new Draw(Draw.TYPE_ARROWTAKEFROMRACK,getArrowPlayerCannotTakeObjectFromRack().x,getArrowPlayerCannotTakeObjectFromRack().y));
            drawList_.add(new Draw(Draw.TYPE_DONT,getArrowPlayerCannotTakeObjectFromRack().x,getArrowPlayerCannotTakeObjectFromRack().y));

        }
        if(getArrowPlayerCannotPutObjectOnRack()!=null){
            drawList_.add(new Draw(Draw.TYPE_ARROWPUTONRACK,getArrowPlayerCannotPutObjectOnRack().x,getArrowPlayerCannotPutObjectOnRack().y));
            drawList_.add(new Draw(Draw.TYPE_DONT,getArrowPlayerCannotPutObjectOnRack().x,getArrowPlayerCannotPutObjectOnRack().y));

        }


        int nbLife = getNbLife();

        for (int i = 3; i >= 0; i--) {
            if (nbLife > i) {

                drawList_.add(new Draw(Draw.TYPE_LIFEUP,9 + i, 16));

            } else {

                drawList_.add(new Draw(Draw.TYPE_LIFEDOWN,9 + i, 16));

            }
        }

        return drawList_;
    }

    public void processGameForPlayer(){
        processGameForPlayer(player);
    }

    public void processGameForPlayer(Player player_) {

        resetAllArrow();

        if(isPlayerCloseToARack(player_)){

            if(player_.isFull()){

                if(couldPlayerPutObjectOnRack(player_)) {
                    setArrowPlayerPutObjectOnRack(player_);
                }else{
                    setArrowPlayerCannotPutObjectOnRack(player_);
                }

            }else{
                if(couldPlayerTakeObjectFromRack(player_) ){
                    setArrowPlayerTakeObjectFromRack(player_);
                }else{
                    setArrowPlayerCannotTakeObjectFromRack(player_);
                }

            }

        }else if(isPlayerCloseToABox(player_)){
            if(couldPlayerPutObjectInBox(player_)){
                setArrowPlayerPutObjectInBox(player_);
            }else{
                setArrowPlayerCannotPutObjectInBox(player_);
            }
        }



    }

    public boolean isPlayerCloseToARack(Player player_){
        Rack rackOnTheLeft=getRackAtCoord(player_.getX()-1,player_.getY());
        if(rackOnTheLeft==null){
            return false;
        }

        return true;
    }

    public boolean isPlayerCloseToABox(Player player_){
        if(getMapValue(player_.getX(),player_.getY()+1) != CASE_BELT ){
            return false;
        }

        Box boxOnTheBottom=getBoxAtX(player_.getX());
        if(boxOnTheBottom.isArrived()==false || boxOnTheBottom.exist() ==false){
            return false;
        }

        return true;
    }




    //take object from rack
    public boolean couldPlayerTakeObjectFromRack(Player player_) {
        if(player_.isFull()==true){
            return false;
        }

        Rack rackOnTheLeft=getRackAtCoord(player_.getX()-1,player_.getY());
        if(rackOnTheLeft==null){
            return false;
        }

        if(rackOnTheLeft.isFull()){
            return true;
        }
        return false;

    }
    public void setArrowPlayerTakeObjectFromRack(Player player_) {
        arrowPlayerTakeObjectFromRack=new Point(player_.getX()-1,player_.getY());
    }
    public Point getArrowPlayerTakeObjectFromRack(){
        return arrowPlayerTakeObjectFromRack;
    }
    public void setArrowPlayerCannotTakeObjectFromRack(Player player_) {
        arrowPlayerCannotTakeObjectFromRack=new Point(player_.getX()-1,player_.getY());
    }
    public Point getArrowPlayerCannotTakeObjectFromRack(){
        return arrowPlayerCannotTakeObjectFromRack;
    }



    //put object to rack
    public boolean couldPlayerPutObjectOnRack(Player player_) {
        if(player_.isFull()==false){
            return false;
        }

        Rack rackOnTheLeft=getRackAtCoord(player_.getX()-1,player_.getY());
        if(rackOnTheLeft==null){
            return false;
        }

        if(false==rackOnTheLeft.isFull()){
            return true;
        }
        return false;
    }
    public void setArrowPlayerPutObjectOnRack(Player player_) {
        arrowPlayerPutObjectOnRack=new Point(player_.getX()-1,player_.getY());
    }
    public Point getArrowPlayerPutObjectOnRack(){
        return arrowPlayerPutObjectOnRack;
    }
    public void setArrowPlayerCannotPutObjectOnRack(Player player_) {
        arrowPlayerCannotPutObjectOnRack=new Point(player_.getX()-1,player_.getY());
    }
    public Point getArrowPlayerCannotPutObjectOnRack(){
        return arrowPlayerCannotPutObjectOnRack;
    }


    //put object in box
    public boolean couldPlayerPutObjectInBox(Player player_) {
        if(player_.isFull()==false){
            return false;
        }

        if(getMapValue(player_.getX(),player_.getY()+1) != CASE_BELT ){
            return false;
        }

        Box boxOnTheBottom=getBoxAtX(player_.getX());
        if(boxOnTheBottom.isArrived()==false || boxOnTheBottom.exist() ==false){
            return false;
        }

        if(boxOnTheBottom.shouldAccept(player_.getItem())){
            return true;
        }
        return false;

    }
    public void setArrowPlayerPutObjectInBox(Player player_){
        arrowPlayerPutObjectInBox=new Point(player_.getX(),player_.getY()+1);
    }
    public Point getArrowPlayerPutObjectInBox(){
        return arrowPlayerPutObjectInBox;
    }
    public void setArrowPlayerCannotPutObjectInBox(Player player_){
        arrowPlayerCannotPutObjectInBox=new Point(player_.getX(),player_.getY()+1);
    }
    public Point getArrowPlayerCannotPutObjectInBox(){
        return arrowPlayerCannotPutObjectInBox;
    }
}
