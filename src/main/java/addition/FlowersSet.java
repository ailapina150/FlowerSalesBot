package addition;

import java.util.ArrayList;
import java.util.List;

public class FlowersSet {

    private final List<Flower> flowers;
    public int countSet;
    public boolean inputSetNumber;

    public List<Flower> getFlowers() {
        return flowers;
    }

    public FlowersSet(List<Flower> flowers, int count, boolean inputSetNumber) {

        this.flowers = new ArrayList<>();
        this.flowers.addAll(flowers);
        this.countSet = Math.max(count, 1);
        this.inputSetNumber = inputSetNumber;
    }

    public FlowersSet() {
        this.flowers = new ArrayList<>();
        countSet = 1;
        inputSetNumber = false;
    }

    public int getCountSet() {
        return countSet;
    }

    public void setInputSetNumber(boolean inputSetNumber) {
        this.inputSetNumber = inputSetNumber;
    }

    public boolean isInputSetNumber() {
        return inputSetNumber;
    }

    public int getCountFlowers() {
        if (flowers.size() == 0) return 0;
        return flowers.stream().map(Flower::getCount).reduce(Integer::sum).get() * getCountSet();
    }

    public void addFlower(Flower flower){
        flowers.add(flower);
    }

    public void clean(){
        countSet = 1;
        flowers.clear();
    }
    public Double getCost(){
        if (flowers.size() == 0) return 0.;
        return flowers.stream().map(Flower::getCost).reduce(Double::sum).get();
    }

    public void setCountSet(int countSet) {
        this.countSet = countSet;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for (Flower flower : flowers) {
            s.append(" * ")
                    .append(flower.getName())
                    .append(" - ")
                    .append(flower.getCount())
                    .append("шт - ")
                    .append(flower.getCost())
                    .append(AppProperties.MONEY)
                    .append("\n");
        }
        s.append("Количество букетов - " )
                .append(getCountSet())
                .append("шт \n Стоимость  -  ")
                .append(getCost())
                .append("x")
                .append(getCountSet())
                .append("=")
                .append(getCost()* getCountSet())
                .append(AppProperties.MONEY)
                .append("\n");
        return s.toString();
    }
}
