package addition;

import java.util.*;

public class BotUser {
    private final Long userId;
    private String userName;
    private final String firstName;
    private String telephone;
    private String currantQuestion;
    private String currantFileName;
    private State currantState;
    private State lastState;
    private final Order order;
    private double priceTulip;
    private double pricePeony;
    private FlowersSet flowersSet;
    private final List<SendId> ids;

    public BotUser(Long userId) {
        this.userId = userId;
        this.userName = "";
        this.firstName = "";
        this.telephone = "";
        this.currantQuestion = "";
        this.currantState = State.START;
        this.priceTulip = AppProperties.get().goods.getPriceTulip();
        this.pricePeony = AppProperties.get().goods.getPriceTulip();
        this.order = new Order();
        this.flowersSet = new FlowersSet();
        this.ids = new ArrayList<>();
    }

    public BotUser(Long userId, String firstName) {
        this.userId = userId;
        this.userName = "";
        this.firstName = firstName;
        this.telephone = "";
        this.currantQuestion = "";
        this.currantState = State.START;
        this.priceTulip = AppProperties.get().goods.getPriceTulip();
        this.pricePeony = AppProperties.get().goods.getPriceTulip();
        this.order = new Order();
        this.flowersSet = new FlowersSet();
        this.ids = new ArrayList<>();
    }


    public List<SendId> getIds(){return ids;}
    public void setCurrantFileName(String currantFileName) {
        this.currantFileName = currantFileName;
    }

    public void setFlowersSet(FlowersSet setOfFlower) {
        if(setOfFlower!= null)
        this.flowersSet = setOfFlower;
    }

    public void setCurrantState(State currantState) {
       System.out.println(this.currantState +"->"+currantState);
       System.out.println(order.toString().replaceAll("\n"," "));
        System.out.println(flowersSet.toString().replaceAll("\n"," "));
        this.lastState = this.currantState;
        this.currantState = currantState;
    }

    public State getLastState(){
        return lastState;
    }

    public void setUserName(String userName) {
        if(userName!=null) {
            this.userName = userName;
        }
    }

    public void setTelephone(String telephone) {
        if(telephone!=null) {
            this.telephone = telephone;
        }
    }

    public void setPriceTulip(double price){
        this.priceTulip = price;
    }

    public void setPricePeony(double price){
        this.pricePeony = price;
    }

    public void setCurrantQuestion(String currantQuestion) {
        if(currantQuestion!=null) {
            this.currantQuestion = currantQuestion;
        }
    }

    public void setFirstName(String firstName) {
        this.userName = firstName;
    }

    public Long getUserId() {return userId;}

    public String getUserName() {return userName;}

    public String getFirstName() {return firstName;}

    public String getTelephone() {return telephone;}

    public String getCurrantQuestion(){ return  currantQuestion;}

    public State getCurrantState() {return currantState;}

    public Order getOrder() { return order;}

    public FlowersSet getFlowersSet() {return flowersSet;}

    public double getPriceTulip() {return priceTulip; }

    public double getPricePeony() {
        return pricePeony;
    }

    public double getCurrantFlowerPrice(){ return currantFileName.contains(AppProperties.PEONY)? pricePeony : priceTulip;}

    public void addFlower(int countOfFlower){
        Flower flower = new Flower(currantFileName, getCurrantFlowerPrice(), countOfFlower);
        if(flowersSet == null) {
            flowersSet = new FlowersSet();
        }
        flowersSet.addFlower(flower);
    }
    public void removeLastFlower(){
        if(flowersSet.getFlowers().size()> 0) {
            setCurrantFileName(getFlowersSet().getFlowers().get(flowersSet.getFlowers().size() - 1).fileName());
            flowersSet.getFlowers().remove(flowersSet.getFlowers().size() - 1);
        }
    }
    public void cleanOrder() { order.cleanOrder();}

    public void cleanFlowersSet() { flowersSet.clean();}

}
