package Lang;

import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {
    private static String lang;
    private static String country;

    private static ResourceBundle resourceBundle;

    public static String msg(String message) { return resourceBundle.getString(message); }

    public static void setLanguage(String[] args) {
        if (args.length != 2) {
            lang = "en";
            country = "UK";
        } else {
            lang = args[0];
            country = args[1];
        }

        try
        {
            Locale locale = new Locale(lang, country);
            resourceBundle = ResourceBundle.getBundle("lang", locale);
        }
        catch (Exception ex)
        {
            System.out.println("You either specified some wrong arguments or you specified a language that doesn't exist in this game.");
            System.out.println("Usage: java -jar <APPLICATION_JAR>.jar <language> <country> (ex. da DK)");
        }

    }
}