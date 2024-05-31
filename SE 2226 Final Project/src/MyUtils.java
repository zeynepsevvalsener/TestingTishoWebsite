import java.util.ArrayList;

public class MyUtils {

    public static String makeLinkFriendly(String input){
        String output = "";
        for (int i = 0; i < input.length() ; i++){
            if (input.charAt(i) == ' '){
                output += "+";
            }
            else
                output += input.charAt(i);
        }
        return output;
    }
    public static void sleepFor (int seconds){
        try {
            Thread.sleep(seconds* 1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
