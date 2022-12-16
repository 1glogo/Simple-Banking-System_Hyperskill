package banking;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class Card {
    String cardNumber;

    String pin;

    Integer balance;

    public long getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }


    public String newRandomCardNumber() {
        Random r = new Random();
        String randomNumber9 = String.format("%09d", r.nextInt(999999999));
        String cardNumberExclLuhn = "400000"+randomNumber9;

        List<Integer> cardNumberList = new ArrayList<>();

        for (int i = 0; i < cardNumberExclLuhn.length();  i++) {
            cardNumberList.add( parseInt(cardNumberExclLuhn.substring(i, i+1)));
        }
        for (int i = 0 ; i < cardNumberList.size() ; i++) {
            int value = cardNumberList.get(i);
            if (i%2 == 0) {
                value = 2*value;
            }
            if (value > 9) {
                value = value -9;
            }
            cardNumberList.set(i, value);
        }

        int luhnNumberResult;

        if ((cardNumberList.stream().flatMapToInt(IntStream::of).sum())%10 == 0){
            luhnNumberResult = 0;
        } else {
            luhnNumberResult = 10 - (cardNumberList.stream().flatMapToInt(IntStream::of).sum())%10;
        }
        String luhnNumber = Integer.toString(luhnNumberResult);

        return cardNumberExclLuhn+luhnNumber;
    }
    public Card() {
        Random r = new Random();
        this.cardNumber = newRandomCardNumber()/*"400000"+randomNumber9+"5"*/;

        String randomNumber4 = String.format("%04d", r.nextInt(9999));
        this.pin = randomNumber4;

        this.balance = 0;
    }

    public Card (String cardNumber, String pin, Integer balance){
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }
}
