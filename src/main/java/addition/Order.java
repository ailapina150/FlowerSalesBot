package addition;

import java.util.*;

public class Order {

    private final List<FlowersSet> flowersSetList;
    private String date;
    private String time;
    private String address;
    private String wrapper;
    private String telephone;

    public Order() {
        this.flowersSetList = new ArrayList<>();
        date = "";
        time = "";
        address = "";
        wrapper = "";
        telephone = "";
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<FlowersSet> getFlowerSetList() {
        return flowersSetList;
    }

    public void addFlowerSet(FlowersSet setOfFlower) {
        flowersSetList.add(setOfFlower);
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public String getWrapper() {
        return wrapper;
    }

    public String getTelephone() {
        return telephone;
    }

    public void cleanOrder() {
        flowersSetList.clear();
        date = "";
        time = "";
        address = "";
        wrapper = "";
        telephone = "";
    }

    public void delLastSet() {
        if (flowersSetList.size() > 0) flowersSetList.remove(flowersSetList.size() - 1);
    }

    public FlowersSet getLastSet() {
        if (flowersSetList.size() > 0) {
            return flowersSetList.get(flowersSetList.size() - 1);
        }
        return null;
    }

    public String toSend() {
        return this +
                "\n\nДата: " + date +
                "\nВремя: " + time +
                "\nАдресс: " + address +
                "\nТелефон: " + telephone +
                "\nУпаковка: " + wrapper;
    }

    public String toString() {
        int i = 1;
        StringBuilder s = new StringBuilder("ЗАКАЗ: \n");
        for (FlowersSet flowers : flowersSetList) {
            s.append("\nБукет N").append(i).append("\n").append(flowers.toString());
            i++;
        }
        double discount = getDiscount();
        if (discount > 0.0001) {
            s.append("\nСкидка: ").append(discount).append(AppProperties.MONEY)
                    .append("\nК оплате: ").append(getCost() - discount);
        } else {
            s.append("\nК ОПЛАТЕ: ").append(getCost());
        }
        s.append(AppProperties.MONEY);
        return s.toString();
    }

    public double getCost() {
        if (flowersSetList.size() == 0) {
            return 0;
        }
        return flowersSetList.stream().map(s -> s.getCost() * s.getCountSet()).reduce(Double::sum).get();
    }

    public double getDiscount() {
        if (getCountFlowers() < AppProperties.get().goods.getDiscountSize()) {
            return 0.;
        }
        return getCost() * AppProperties.get().goods.getDiscount() / 100.;
    }

    public int getCountFlowers() {
        if (flowersSetList.size() == 0) {
            return 0;
        }
        return flowersSetList.stream().map(FlowersSet::getCountFlowers).reduce(Integer::sum).get();
    }

}
