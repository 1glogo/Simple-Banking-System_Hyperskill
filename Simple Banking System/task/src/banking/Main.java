package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:.\\"+args[1];
        /*String url = "jdbc:sqlite:.\\tests.db";*/
        SqlCreateMethods.createNewDatabase(url);
        SqlCreateMethods.createNewTable(url);

        Questions.startQuestions(url);

    }


}