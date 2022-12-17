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

        //Generating Luhn number
        String luhnNumber = Questions.luhnNumberGenerator(cardNumberExclLuhn);

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
