package addition;

public record Flower(String fileName, double price, int count) {

    public String getName() {
        return FileLoader.getSingleName(fileName);
    }

    public int getCount() {
        return count;
    }

    public double getCost() {
        return price * count;
    }

}
