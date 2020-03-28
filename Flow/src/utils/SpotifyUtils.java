package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SpotifyUtils {
    /*private String getCurrentTrack()
    {
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","curl -X \"GET\" \"https://api.spotify.com/v1/me/player\" -H \"Accept: application/json\" -H \"Content-Type: application/json\" -H \"Authorization: Bearer BQD__piGKeISRqq13e3-xrdQ4CMTXEo2FLjNHB-Ur7dMGo09iIm5GPMxI5DUxMLkqIinXlY5ZkZ6s6zUerTmkMR4JhOx7kGRzLEQOxHTX-fO2JupttUe94GVBxq55P7MbuUTjeFFx-JfQo9nMBaB1IE56eMyLUkwyfAae0aMIkoZty1FbLjl-ocnuRzoSMNMf5tS_IDCK5uyVbv5hTLa5dxVaaMS_kCaX1x987I0jg1hdRppQst4ev-6uLp-HeGJhkJbntMr-i5F81dEhX8ba_pKKmdg-p-gHw\""});
            return procToString(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }*/
    public static BufferedImage getArtworkImage()
    {
        try {
            return ImageIO.read(getArtworkURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean single()
    {
        String str = getTrackInfo();
        assert str != null;
        if(str.contains("\"album_type\":\"single\""))
            return true;
        return false;
    }
    public static void increaseVolume(int am)
    {
        try {
            MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tset currentvol to get sound volume\n" +
                    "\tif currentvol > "+ (100-am)+" then\n" +
                    "\t\tset sound volume to 100\n" +
                    "\telse\n" +
                    "\t\tset sound volume to currentvol + "+am+"\n" +
                    "\tend if\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void decreaseVolume(int am)
    {
        try {
            MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "    set currentvol to get sound volume\n" +
                    "    -- volume wraps at 100 to 0\n" +
                    "    if currentvol < " + am +" then\n" +
                    "        set sound volume to 0\n" +
                    "    else\n" +
                    "        set sound volume to currentvol - "+am+"\n" +
                    "    end if\n" +
                    "end tell\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static URL getArtworkURL()
    {
        String sUrl = null;
        try {
            sUrl = MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tset long_id to id of current track\n" +
                    "\tset AppleScript's text item delimiters to \":\"\n" +
                    "\tset short_id to long_id's third text item\n" +
                    "\tset _art to do shell script \"curl -s -X GET 'https://open.spotify.com/track/\" & short_id & \"' | grep -o 'https:\\\\/\\\\/i.scdn.co\\\\/image\\\\/.\\\\{40\\\\}' | head -1\"\n" +
                    "end tell\n" +
                    "\n" +
                    "return _art");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new URL(sUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return getArtworkURL();
        }
    }
    public static void togglePlay()
    {
        try {
            MacOSUtils.runAppleScript("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tplaypause\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void play()
    {
        try {
            MacOSUtils.runAppleScript("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tplay\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void pause()
    {
        try {
            MacOSUtils.runAppleScript("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tpause\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static double playerPosition()
    {
        try {
           return Double.parseDouble(MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn player position as text\n" +
                    "end tell"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    public static void setPlayBackPosition(double pos)
    {
        try {
            MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tset player position to "+ pos +"\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int duration()
    {
        try {
            return Integer.parseInt(MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tduration of the current track\n" +
                    "end tell").trim().replace("\"", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static boolean playing()
    {
        try {
           String str =  MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn player state\n" +
                    "end tell").trim();
           if(str.equals("playing"))
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void nextTrack()
    {
        try {
            MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tnext track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void previousTrack()
    {
        try {
            MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tprevious track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getTrackArtist()
    {
       return getTrackArtists().get(0);
    }
    public static java.util.List<String> getTrackArtists()
    {
        java.util.List<String> names = new ArrayList<>();
        String str = getTrackInfo();
        int pos1 = str.indexOf("a song by") + 10;
        String ret = str.substring(pos1, str.indexOf(" on Spotify", pos1));
        for(String s: ret.split(", "))
            names.add(s);
        return names;
    }
    public static String getTrackAlbum()
    {
       String s = getTrackInfo();
       int pos1 = s.indexOf("alt=") + 5;
       return s.substring(pos1, s.indexOf('\"', pos1)).trim().replace("&#039;", "'");
    }
    public static String getTrackId()
    {
        try {
            return MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn spotify url of the current track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getTrackName()
    {
       String str = getTrackInfo();
       int pos = str.indexOf("name\":") + 7;
       return str.substring(pos, str.indexOf("\"",pos)).replace("\\u2019","'").replace("\\", "");
    }
    public static String getTrackInfo()
    {
        try {
            return MacOSUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tset long_id to id of current track\n" +
                    "\tset AppleScript's text item delimiters to \":\"\n" +
                    "\tset short_id to long_id's third text item\n" +
                    "\tset _art to do shell script \"curl -s -X GET 'https://open.spotify.com/track/\" & short_id & \"' \"\n" +
                    "end tell\n" +
                    "\n" +
                    "\n" +
                    "return _art");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
