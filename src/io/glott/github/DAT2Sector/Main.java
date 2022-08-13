package io.glott.github.DAT2Sector;

import java.io.File;

public class Main
{

    public static String WORKING_DIRECTORY = null;

    public static void main(String[] args)
    {
        try
        {
            WORKING_DIRECTORY = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            WORKING_DIRECTORY = WORKING_DIRECTORY.substring(0, WORKING_DIRECTORY.lastIndexOf(File.separator));
        } catch (Exception ignored)
        {
            WORKING_DIRECTORY = System.getProperty("user.home") + File.separator + "Downloads";
        }

        DataHandler handler = new DataHandler();
        Display display = new Display(handler);
        display.run();
    }
}
