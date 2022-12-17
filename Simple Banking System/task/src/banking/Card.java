package banking;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class Card {
    String cardNumber;

    String pin;

    Integer balance;

    //Function producing random card number
    //standard format: 400000 + 9 random digits + Luhn number
    //400000 493832089 5
    public String newRandomCardNumber() {
        //generating all digits except of Luhn number
        Random r = new Random();
        String randomNumber9 = String.format("%09d", r.nextInt(999999999));
        String cardNumberExclLuhn = "400000"+randomNumber9;

        //Luhn number calculation
        //1. All digits are added to an integer collection in order to calculate Luhn NUmber
        List<Integer> cardNumberList = new ArrayList<>();
        for (int i = 0; i < cardNumberExclLuhn.length();  i++) {
            cardNumberList.add(parseInt(cardNumberExclLuhn.substring(i, i+1)));
        }
        //1.a. Numbers located in an even index (starting from 0) get to be double
        //Any number greater than 9, get to be subtracted by 9
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

        //2. Sum of all the amended numbers i carried out
        // multiples of 10 have Luhn number of 0
        // in all other cases Luhn_Number = 10-SUM % 10
        int luhnNumberResult;

        if ((cardNumberList.stream().flatMapToInt(IntStream::of).sum())%10 == 0){
            luhnNumberResult = 0;
        } else {
            luhnNumberResult = 10 - (cardNumberList.stream().flatMapToInt(IntStream::of).sum())%10;
        }
        String luhnNumber = Integer.toString(luhnNumberResult);

        //3. Luhn number is added to the randomly generated number
        return cardNumberExclLuhn+luhnNumber;
    }

    //Constructors
    //Default No Args Constructor requesting random number
    public Card() {
        Random r = new Random();
        this.cardNumber = newRandomCardNumber();
        this.pin = String.format("%04d", r.nextInt(9999));
        this.balance = 0;
    }

    public Card (String cardNumber, String pin, Integer balance){
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    //Getters and Setters
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
}
