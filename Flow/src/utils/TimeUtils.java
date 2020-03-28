package utils;

public class TimeUtils {
    public static String secondsToString(int secconds)
    {
       int minutes = secconds/60;
        int seconds = secconds%60;
        if(seconds < 10)
            return minutes + ":0" + seconds;
        return minutes + ":" + seconds;
    }
}
