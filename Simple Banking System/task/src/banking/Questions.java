package banking;

import java.util.*;
import java.util.stream.IntStream;
import static java.lang.Integer.parseInt;

public class Questions {

    //Function checks that the Card Number created is unique
    //If not, a new cardNUmber is created and checked again
    static void cardNumberCheck(Card newCard, String url) {

        //Creating List with any card Number Matches from the database
        SqlAddQueryMethods addQuery = new SqlAddQueryMethods();
        List<String> cardList = addQuery.getAllCardNumbers(newCard.getCardNumber(), url);

        if(cardList.size()>0 &&
                cardList.stream().anyMatch(e -> Objects.equals(e, newCard.getCardNumber()))) {
            newCard.setCardNumber(newCard.newRandomCardNumber());
            cardNumberCheck(newCard, url);
        }
    }

    //Questions initialising the application interface with the client
    public static void startQuestions(String url) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("1. Create an account \n2. Log into account \n0. Exit");
        
        int reply = -1;
        try {
            reply = Integer.parseInt(userInput.next("[012]"));
        }
        //incorrect input try again
        catch (Exception e){
            System.out.println("Provide correct number.");
            startQuestions(url);
        }
        //Option 1 creating account
        if (reply == 1) {
            //New card is created and a unique number provided using function cardNumberCheck
            Card newCard = new Card();
            cardNumberCheck (newCard, url);
            System.out.println("Your card has been created");


            //Insert Card to database
            SqlAddQueryMethods addQuery = new SqlAddQueryMethods();
            addQuery.insert(newCard.getCardNumber(), newCard.getPin(), url);

            //Printing to user their card information
            System.out.println("Your card number:");
            System.out.println(newCard.getCardNumber());
            System.out.println("Your card PIN:");
            System.out.println(newCard.getPin());

            //starting questions again
            startQuestions(url);
        }
        //Option 2 log in to account
        else if (reply == 2) {
            logInQuestions(url);
        }
        //Option 2 Exit application
        else if (reply == 0) {
            userInput.close();
            System.exit(0);
        }
        

    }
    //Log in questions, requesting Pin and Card Number
    private static void logInQuestions(String url) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String replyCardNumber = userInput.nextLine();
        System.out.println("Enter your PIN:");
        String replyCardPin = userInput.nextLine();

        //Creating List with any card Number Matches
        SqlAddQueryMethods addQuery = new SqlAddQueryMethods();
        List<Card> cardList = addQuery.getMatchingCard(replyCardNumber,url);
        //Ensuring CardNumber Exists and CardNumber & Pin are matching
        if (cardList.size()>0
                && cardList.get(0).getCardNumber().equals(replyCardNumber)
                && cardList.get(0).getPin().equals(replyCardPin)){
            System.out.println("You have successfully logged in!");
            Card card = cardList.get(0);
            // Forwarded to the follow up questions within the account
            afterLogInQuestions (card, url);
        } else {
            System.out.println("Wrong card number or PIN!");
            startQuestions(url);
        }
    }

    //Questions and actions once user has successfully logged in
    static void afterLogInQuestions (Card card, String url) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("1. Balance \n2. Add income \n3. Do transfer \n" +
                "4. Close account \n5. Log out \n0. Exit");

        int reply = -1;

        //Ensuring that the input provided is digit 0 to 5
        try {
            reply = Integer.parseInt(userInput.next("[0-5]"));
        }catch (Exception e){
            System.out.println("Provide correct number.");
            afterLogInQuestions(card, url);
        }

        SqlAddQueryMethods addQuery = new SqlAddQueryMethods();

        //Check balance
        if (reply == 1) {
            System.out.println("Balance: "+card.getBalance());
            afterLogInQuestions(card, url);
        }
        //Add income
        else if (reply == 2) {
            System.out.println("Enter income for "+ card.getCardNumber() +": ");

            //Ensuring that value to be added is a positive number
            int addValue = 0;
            try {
                addValue = Integer.parseInt(userInput.next("-?\\d+"));
                if (addValue<=0){
                    System.out.println("Ensure that income is a positive value.");
                    afterLogInQuestions(card, url);
                }
            } catch (Exception e) {
                System.out.println("Ensure that income is a positive value consisting only of digits.");
                afterLogInQuestions(card, url);
            }

            //income value added to database
            addQuery.addIncome(card.getCardNumber(), addValue, url);

            //Start log in questions with the updated card information
            Card updatedCard = addQuery.getMatchingCard(card.getCardNumber(), url).get(0);
            afterLogInQuestions (updatedCard,url);

        }
        //Transferring between accounts
        else if (reply == 3) {
            //Start transfer
            System.out.println("Transfer");
            System.out.println("Enter card number:");
            String destinationCardNumber = String.valueOf(userInput.next());


            //Check that the cardNumber is not exactly the same with the account
            if (destinationCardNumber.equals(card.getCardNumber())){
                System.out.println("You can't transfer money to the same account!");
                afterLogInQuestions (card,url);
            }

            //Luhn Algorithm check
            else if (!luhnNumberComparison(destinationCardNumber))  {
                System.out.println("Probably you made a mistake in the card number. Please try again!");
                afterLogInQuestions (card,url);
            }

            //Receiver card number is not in the database
            else if (addQuery.getMatchingCard(destinationCardNumber, url).size() == 0) {
                System.out.println("Such a card does not exist.");
                afterLogInQuestions (card,url);
            }

            else if (addQuery.getMatchingCard(destinationCardNumber, url).size() == 1) {
                System.out.println("Enter how much money you want to transfer: ");
                int transferAmount = userInput.nextInt();
                if (transferAmount > card.getBalance()) {
                    System.out.println("Not enough money!");
                    afterLogInQuestions (card,url);
                } else if (transferAmount <= 0) {
                    System.out.println("Transfer amount should be greater than 0");
                    afterLogInQuestions (card,url);
                } else {
                    addQuery.reduceBalance(card.getCardNumber(),transferAmount, url);
                    addQuery.increaseBalance(destinationCardNumber,transferAmount,url);
                    System.out.println("Success!");
                    Card updatedCard = addQuery.getMatchingCard(card.getCardNumber(), url).get(0);
                    afterLogInQuestions (updatedCard,url);
                }

            }
             else {
                System.out.println("you passed everything you lucky devil");
                afterLogInQuestions (card,url);
            }
        }else if (reply == 4) {
            //Close account
            addQuery.deleteCard(card.getCardNumber(),url);
            System.out.println("The account has been closed!");
            startQuestions(url);
        }else if (reply == 5) {
            startQuestions(url);
        }else if (reply == 0) {
            userInput.close();
            System.exit(0);
        }
    }
    //Luhn number Review
    public static Boolean luhnNumberComparison(String cardNumber) {

        int cardNumberLength = cardNumber.length();

        String cardNumberExclLuhn = cardNumber.substring(0, cardNumberLength-1);

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
        String luhnNumberCalculated = Integer.toString(luhnNumberResult);

        String luhnNumberImported = cardNumber.substring(cardNumberLength-1,cardNumberLength);

        return luhnNumberCalculated.equals(luhnNumberImported);
    }
}


