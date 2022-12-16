package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //specify filename when you run filename "-fileName db.s3db."
        //String url = "jdbc:sqlite:.\\"+args[1];

        //below you can manually add the filename (hardcoded)
        String url = "jdbc:sqlite:.\\tests.db";

        //Creation of database and table
        SqlCreateMethods.createNewDatabase(url);
        SqlCreateMethods.createNewTable(url);

        //starting app
        Questions.startQuestions(url);

    }


}