package addition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Goods implements Serializable {
   private double priceTulip;
   private double pricePeony;
   private int minFlowerCount;
   private double discount;
   private int discountSize;
   private final List<String> finished = new ArrayList<>();

   public Goods() {
      this.priceTulip = 3.;
      this.pricePeony = 3.5;
      this.minFlowerCount = 30;
      this.discount = 3.;
      this.discountSize = 100;
   }


   public List<String> getFinished() {
      return finished;
   }

   public int getMinFlowerCount() {
      return minFlowerCount;
   }

   public void setMinFlowerCount(int minFlowerCount) {
      this.minFlowerCount = minFlowerCount;
   }

   public double getDiscount() {
      return discount;
   }

   public void setDiscount(double discount) {
      this.discount = discount;
   }

   public int getDiscountSize() {
      return discountSize;
   }

   public void setDiscountSize(int discountSize) {
      this.discountSize = discountSize;
   }

   public double getPricePeony() {return pricePeony;}

   public void setPricePeony(double peony) {
      this.pricePeony = peony;
   }

   public double getPriceTulip() {
      return priceTulip;
   }

   public void setPriceTulip(double tulip) {
      this.priceTulip = tulip;
   }


   @Override
   public String toString() {
      return "Goods{" +
              "priceTulip=" + priceTulip +
              ", pricePeony=" + pricePeony +
              ", minFlowerCount=" + minFlowerCount +
              ", discount=" + discount +
              ", discountSize=" + discountSize +
              ", finished=" + finished +
              '}';
   }
}
