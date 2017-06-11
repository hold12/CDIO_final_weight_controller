package lang;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Lang {
    private static String lang;
    private static String country;

    private static ResourceBundle resourceBundle;

    public static String msg(String message) { return resourceBundle.getString(message); }

    public static boolean setLanguage(String[] args) {
        if (args.length == 2 && (args[0] != null && args[1] != null)) {
            lang = args[0];
            country = args[1];
        }
        else return false;

        try
        {
            Locale locale = new Locale(lang, country);
            resourceBundle = ResourceBundle.getBundle("lang", locale);
        }
        catch (NullPointerException | MissingResourceException ex)
        {
            System.out.println("You either specified some wrong arguments or you specified a language that doesn't exist in this game.");
            System.out.println("Usage: java -jar <APPLICATION_JAR>.jar <language> <country> (ex. en US)");
            return false;
        }
        return true;
    }
}