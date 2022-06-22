package io.glott.github.DAT2Sector;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class DataHandler
{

    private static final String SCT_STA = "[INFO]\n" +
            "ZXX\n" +
            "ZXX_CTR\n" +
            "ZXX\n" +
            "N025.47.55.401\n" +
            "W080.17.36.692\n" +
            "60.000\n" +
            "54.000\n" +
            "5.480\n" +
            "1.000\n" +
            "\n" +
            "[VOR]\n" +
            "\n" +
            "[NDB]\n" +
            "\n" +
            "[AIRPORT]\n" +
            "KMIA 000.000 N025.47.43.300 W080.17.24.400\n" +
            "\n" +
            "[RUNWAY]\n" +
            "\n" +
            "[FIXES]\n" +
            "\n" +
            "[ARTCC]\n" +
            "\n" +
            "[ARTCC HIGH]\n" +
            "\n" +
            "[ARTCC LOW]\n" +
            "\n" +
            "[SID]\n" +
            "\n" +
            "[STAR]";

    private static final String SCT_END = "\n" +
            "[LOW AIRWAY]\n" +
            "\n" +
            "[HIGH AIRWAY]\n" +
            "\n" +
            "[GEO]\n" +
            "\n" +
            "[REGIONS]\n" +
            "\n" +
            "[LABELS]\n";

    private JProgressBar progressBar;

    public static String readFile(File f)
    {
        try
        {
            byte[] encoded = Files.readAllBytes(Paths.get(f.getPath()));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (Exception ignored)
        {
        }
        return "";

    }

    public File dat_sct(File[] files)
    {
        File e = null;
        try
        {
            e = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + files[0].getName().substring(0, 3).toUpperCase() + ".sct2");
            e.createNewFile();
            PrintWriter pw = new PrintWriter(e);
            pw.println(SCT_STA);

            int i = 0;
            for (File f : files)
            {
                String s = readFile(f);
                String line = "";
                pw.print(String.format("%1$-" + 24 + "s", f.getName().replace(".dat", "")).substring(0, 24) + "  N099.00.00.000 E099.00.00.000 N099.00.00.000 E099.00.00.000\n");
                boolean start = false;
                for (String z : s.split("\n"))
                {
                    if (z.contains("LINE !"))
                    {
                        start = true;
                        pw.print(convertLine(line));
                        line = "";
                    } else if (start)
                        line += "\n" + z;
                }

                i++;
                int k = (int) (100F * i / files.length);
                progressBar.setStringPainted(true);
                progressBar.setValue(k);
                progressBar.setString(i + " / " + files.length);
                progressBar.update(progressBar.getGraphics());
            }
            pw.println(SCT_END);
            pw.close();
            progressBar.setString("Converted " + files.length + " maps to sct2!");
        } catch (Exception ignored)
        {
        }

        return e;
    }

    public void sct_xml(HashMap<String, String[]> key, File sct)
    {
        try
        {
            File dir = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + sct.getName().substring(0, sct.getName().length() - 5) + " Maps");
            dir.mkdir();

            String mapID = "";
            boolean run = false;
            File current = null;
            PrintWriter pw = null;

            int i = 0;
            int size = readFile(sct).split("\n").length;
            int prev = 0;
            int num = 0;

            Scanner sc = new Scanner(sct);
            String s = sc.nextLine();
            while (sc.hasNextLine())
            {
                if (s.contains("[STAR]"))
                {
                    run = true;
                    s = sc.nextLine();
                    continue;
                } else if (run && s.contains("["))
                {
                    if (pw != null)
                    {
                        pw.println("  </Elements>\n" + "</VideoMap>");
                        pw.close();
                    }
                    progressBar.setString("Converted " + num + " maps to xml!");
                    break;
                }
                if (run && s.length() > 1)
                {
                    if (current != null && s.charAt(0) == ' ')
                    {
                        float[] z = convertDMS(s.replaceAll("\\s*(\\w.*)$", "$1"));
                        pw.println("    <Element xsi:type=\"Line\" Color=\"R80G80B40\" StartLon=\"" + z[1] + "\" StartLat=\"" + z[0] + "\" EndLon=\"" + z[3] + "\" EndLat=\"" + z[2] + "\" Style=\"Solid\" Thickness=\"0\" />");
                    } else
                    {
                        if (pw != null)
                        {
                            pw.println("  </Elements>\n" + "</VideoMap>");
                            pw.close();
                        }
                        mapID = s.replaceAll("  .*", "");
                        if (key.containsKey(mapID))
                        {
                            current = new File(dir, key.get(mapID)[1].replaceAll("[^A-Z0-9\\- ]", "") + ".xml");
                            current.createNewFile();
                            pw = new PrintWriter(current);
                            pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<VideoMap xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ShortName=\""
                                    + key.get(mapID)[0] + "\" LongName=\"" + key.get(mapID)[1] + "\" STARSGroup=\"\" STARSTDMOnly=\"false\" VisibleInList=\"true\">\n" + "  <Colors>\n"
                                    + "    <NamedColor Red=\"80\" Green=\"80\" Blue=\"40\" Name=\"R80G80B40\" />\n" + "  </Colors>\n" + "  <Elements>");
                            num++;
                        } else
                        {
                            current = null;
                            pw = null;
                        }
                    }
                }
                s = sc.nextLine();

                int k = (int) (100F * i / size);
                if (prev != k)
                {
                    progressBar.setStringPainted(true);
                    progressBar.setValue(k + 1);
                    progressBar.setString((k + 1) + "%");
                    progressBar.update(progressBar.getGraphics());
                    prev = k;
                }
                i++;
            }

        } catch (Exception ignored)
        {
        }
    }

    private String convertLine(String line)
    {
        String[] split = line.split("\n");
        if (split.length <= 1) return "";
        String out = "                          " + convertIndividualLine(split[1]);
        for (int i = 1; i < split.length - 1; i++)
            out += " " + convertIndividualLine(split[i]) + "\n                          " + convertIndividualLine(split[i]);
        out += " " + convertIndividualLine(split[split.length - 1]);
        return out + "\n";
    }

    private String convertIndividualLine(String s)
    {
        String[] z = s.split(" ");
        return "N0" + z[1] + "." + z[2] + "." + z[3].substring(0, 6) + " W" + z[5] + "." + z[6] + "." + z[7].substring(0, 6);
    }

    private float[] convertDMS(String s)
    {
        float[] z = {0, 0, 0, 0};
        String[] q = s.split(" ");
        for (int i = 0; i < 4; i++)
        {
            int neg = q[i].contains("S") || q[i].contains("W") ? -1 : 1;
            q[i] = q[i].replaceAll("[NSEW]", "");
            String[] l = q[i].split("\\.");
            z[i] = neg * (Integer.parseInt(l[0]) + Integer.parseInt(l[1]) / 60F + Float.parseFloat(l[2] + "." + l[3]) / 3600F);
        }
        return z;
    }

    public void retitleSCT(HashMap<String, String[]> key, File sct)
    {
        String s = readFile(sct);
        for (String name : key.keySet())
        {
            String rep = key.get(name)[1];
            rep = rep.replace("FLCHK - ", "FC - ");
            if (rep.length() > 34)
                rep = rep.substring(9, 34);
            else if (rep.length() > 25)
                rep = rep.substring(9);
            else if (rep.length() > 9)
                rep = rep.substring(9);
            String pad = "                         ".substring(rep.length());
            s = s.replaceAll(name + "\\s+", rep + " " + pad);
        }

        File e = null;
        try
        {
            e = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + sct.getName().substring(0, sct.getName().length() - 5) + "_ret.sct2");
            e.createNewFile();
            PrintWriter pw = new PrintWriter(e);
            pw.println(s);
            pw.close();
        } catch (Exception ignored)
        {
        }
    }

    public void mergeXML(File keyfile, File sct)
    {
        if (keyfile == null || sct == null) return;
        try
        {
            File out = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + sct.getName().substring(0, sct.getName().length() - 5) + " Video Maps.xml");
            out.createNewFile();

            PrintWriter pw = new PrintWriter(out);
            pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<VideoMaps xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");

            String loc = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + sct.getName().substring(0, sct.getName().length() - 5) + " Maps" + File.separator;
            String keys = readFile(keyfile);
            for (String key : keys.split("\\r?\\n"))
            {
                pw.print("  ");
                String[] map = key.split("\\|");
                if (map.length < 3) return;
                String map_name = map[2].replaceAll("[\\(\\)/]", "") + ".xml";
                File f = new File(loc + map_name);
                if (!f.exists()) continue;
                String map_xml = readFile(f).replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "").replaceAll("\r?\n", "\n  ")
                        .replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ", "");

                if (map.length == 4)
                    map_xml = map_xml.replace("STARSGroup=\"", "STARSGroup=\"" + map[3].charAt(0));

                pw.print(map_xml.substring(0, map_xml.length() - 2));
            }

            pw.println("</VideoMaps>");
            pw.close();
        } catch (Exception ignored)
        {
        }
    }

    public void setProgressBar(JProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }
}
