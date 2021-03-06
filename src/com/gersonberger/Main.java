package com.gersonberger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class Main extends Application {

    public static final long startuptime = System.currentTimeMillis();

    public static final String PROGRAMNAME = "IIDX-FX";
    public static final String PROGRAMVERSION = "1.3";
    public static final String PROGRAMDATE = "2016-03-07";

    public static String LOCALDIR;
    public static String SEPARATOR;

    public static final String SCOREFILENAME = "scores.txt";
    private static final String PROPERTYFILENAME = "set.properties";

    private static final String PROPERTYNAMETHEME = "theme";
    private static final String PROPERTYNAMECLEARCOLORS = "show_clearcolors";
    private static final String PROPERTYNAMEPLAYERSIDE = "playerside";
    private static final String PROPERTYNAMEDJNAME = "djname";
    private static final String PROPERTYNAMEPLAYERID = "playerid";
    private static final String PROPERTYNAMESONGLIST = "songlist";

    public static final String PROPERTYNAMESTYLECOL = "stylecolumn_visible";
    public static final String PROPERTYNAMETITLECOL = "titlecolumn_visible";
    public static final String PROPERTYNAMEARTISTCOL = "artistcolumn_visible";
    public static final String PROPERTYNAMEGENRECOL = "genrecolumn_visible";
    public static final String PROPERTYNAMEDIFFICULTYCOL = "difficultycolumn_visible";
    public static final String PROPERTYNAMELEVELCOL = "levelcolumn_visible";
    public static final String PROPERTYNAMERATINGNCOL = "rncolumn_visible";
    public static final String PROPERTYNAMERATINGHCOL = "rhcolumn_visible";
    public static final String PROPERTYNAMEBPMCOL = "bpmcolumn_visible";
    public static final String PROPERTYNAMELENGTHCOL = "lengthcolumn_visible";
    public static final String PROPERTYNAMENOTESCOL = "notescolumn_visible";
    public static final String PROPERTYNAMECLEARCOL = "clearcolumn_visible";
    public static final String PROPERTYNAMEGRADECOL = "gradecolumn_visible";
    public static final String PROPERTYNAMEEXCOL = "excolumn_visible";
    public static final String PROPERTYNAMEMISSCOL = "misscolumn_visible";

    private static final String PROPERTYNAMETITLESUGGESTIONS = "show_titlesuggestions";
    private static final String PROPERTYNAMEARTISTSUGGESTIONS = "show_artistsuggestions";

    public static boolean showTitleSuggestions;
    public static boolean showArtistSuggestions;

    public static String programTheme;
    public static final String THEMELIGHT = "light";
    public static final String THEMEDARK = "dark";
    public static final String THEMENANAHIRA = "nanahira";

    public static final String FILENAMETHEMELIGHT = "modena-adjust.css";
    public static final String FILENAMETHEMEDARK = "dark.css";
    public static final String FILENAMETHEMENANAHIRA = "nanahira.css";
    public static final String FILENAMECLEARCOLORS = "clear.css";

    public static boolean programClearColor;
    public static String programPlayerside;
    public static String djname;
    public static String playerid;
    public static String songlist;

    public static int os;
    public static final int WINDOWS = 1;
    public static final int LINUX = 2;
    public static final int MAC = 3;
    public static final int UNKNOWN = 0;

    private static File scoreFile;
    private static File propFile = null;

    @Override
    public void start(Stage primaryStage) throws IOException {
        os = determineOS(System.getProperty("os.name"));
        initDir();
        initProperties();
        findScoreFile();

        Locale.setDefault(Locale.ENGLISH);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResource("/img/icon32.png").toString()));
        primaryStage.getIcons().add(new Image(getClass().getResource("/img/icon256.png").toString()));
        primaryStage.setTitle(PROGRAMNAME);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1400);
        primaryStage.setHeight(846);
        primaryStage.show();
        System.out.println("\n" + getTime() + " Application loaded (took " + (System.currentTimeMillis() - Main.startuptime) + "ms)\n");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String getTime() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        return formatter.format(date);
    }

    private int determineOS(String osName) {
        if (osName.toLowerCase().contains("win")) return WINDOWS;
        if (osName.toLowerCase().contains("nux") || osName.toLowerCase().contains("nix")) return LINUX;
        if (osName.toLowerCase().contains("mac")) return MAC;
        return UNKNOWN;
    }

    private void initDir(){
        switch (os) {
            case WINDOWS:
                LOCALDIR = System.getProperty("user.home") + "\\AppData\\Roaming\\" + PROGRAMNAME;
                SEPARATOR = "\\";
                break;
            case LINUX:
                LOCALDIR = System.getProperty("user.home") + "/." + PROGRAMNAME;
                SEPARATOR = "/";
                break;
            case MAC:
                LOCALDIR = System.getProperty("user.home") + "/Library/" + PROGRAMNAME;
                SEPARATOR = "/";
                break;
            default:
                System.err.println("UNKNOWN OPERATING SYSTEM");
                System.exit(-1);
        }

        File folder = new File(LOCALDIR);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                System.err.println(getTime() + " Could not create folder " + folder.getPath());
                System.exit(-1);
            }
        }
    }

    public static void findScoreFile() {
        File ScoreFile = new File(LOCALDIR + SEPARATOR + SCOREFILENAME);
        if (!ScoreFile.exists()) scoreFile = null;
        else scoreFile = ScoreFile;
    }

    private void initProperties() throws IOException {
        propFile = new File(LOCALDIR + SEPARATOR + PROPERTYFILENAME);
        if (!propFile.exists()) {
            FileOutputStream fileOutputStream = new FileOutputStream(propFile.getPath());
            Properties properties = new Properties();
            properties.setProperty(PROPERTYNAMETHEME, THEMELIGHT);
            properties.setProperty(PROPERTYNAMECLEARCOLORS, String.valueOf(false));
            properties.setProperty(PROPERTYNAMEPLAYERSIDE, "1");
            properties.store(fileOutputStream, null);
            fileOutputStream.close();

            //default values
            programTheme = THEMELIGHT;
            showTitleSuggestions = false;
            showArtistSuggestions = true;
            programClearColor = false;
            programPlayerside = "1";
            djname = "";
            playerid = "";
            songlist = Style.OMNIMIX;
        } else {
            FileInputStream fileInputStream = new FileInputStream(propFile.getPath());
            Properties properties = new Properties();
            properties.load(fileInputStream);
            programTheme = properties.getProperty(PROPERTYNAMETHEME, THEMELIGHT);
            programClearColor = Boolean.valueOf(properties.getProperty(PROPERTYNAMECLEARCOLORS));
            programPlayerside = properties.getProperty(PROPERTYNAMEPLAYERSIDE, "1");
            showTitleSuggestions = Boolean.valueOf(properties.getProperty(PROPERTYNAMETITLESUGGESTIONS, "false"));
            showArtistSuggestions = Boolean.valueOf(properties.getProperty(PROPERTYNAMEARTISTSUGGESTIONS, "true"));
            djname = properties.getProperty(PROPERTYNAMEDJNAME, "");
            playerid = properties.getProperty(PROPERTYNAMEPLAYERID, "");
            songlist = properties.getProperty(PROPERTYNAMESONGLIST, Style.OMNIMIX);

            fileInputStream.close();
        }

    }

    public static void setProperties(boolean[] columnVisibility){
        if (propFile != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(propFile.getPath());
                Properties properties = new Properties();

                properties.setProperty(PROPERTYNAMETHEME, programTheme);
                properties.setProperty(PROPERTYNAMECLEARCOLORS, String.valueOf(programClearColor));
                properties.setProperty(PROPERTYNAMEPLAYERSIDE, String.valueOf(programPlayerside));

                properties.setProperty(PROPERTYNAMESTYLECOL, String.valueOf(columnVisibility[0]));
                properties.setProperty(PROPERTYNAMETITLECOL, String.valueOf(columnVisibility[1]));
                properties.setProperty(PROPERTYNAMEARTISTCOL, String.valueOf(columnVisibility[2]));
                properties.setProperty(PROPERTYNAMEGENRECOL, String.valueOf(columnVisibility[3]));
                properties.setProperty(PROPERTYNAMEDIFFICULTYCOL, String.valueOf(columnVisibility[4]));
                properties.setProperty(PROPERTYNAMELEVELCOL, String.valueOf(columnVisibility[5]));
                properties.setProperty(PROPERTYNAMERATINGNCOL, String.valueOf(columnVisibility[6]));
                properties.setProperty(PROPERTYNAMERATINGHCOL, String.valueOf(columnVisibility[7]));
                properties.setProperty(PROPERTYNAMEBPMCOL, String.valueOf(columnVisibility[8]));
                properties.setProperty(PROPERTYNAMELENGTHCOL, String.valueOf(columnVisibility[9]));
                properties.setProperty(PROPERTYNAMENOTESCOL, String.valueOf(columnVisibility[10]));
                properties.setProperty(PROPERTYNAMECLEARCOL, String.valueOf(columnVisibility[11]));
                properties.setProperty(PROPERTYNAMEGRADECOL, String.valueOf(columnVisibility[12]));
                properties.setProperty(PROPERTYNAMEEXCOL, String.valueOf(columnVisibility[13]));
                properties.setProperty(PROPERTYNAMEMISSCOL, String.valueOf(columnVisibility[14]));

                properties.setProperty(PROPERTYNAMETITLESUGGESTIONS, String.valueOf(showTitleSuggestions));
                properties.setProperty(PROPERTYNAMEARTISTSUGGESTIONS, String.valueOf(showArtistSuggestions));
                properties.setProperty(PROPERTYNAMEDJNAME, djname);
                properties.setProperty(PROPERTYNAMEPLAYERID, playerid);
                properties.setProperty(PROPERTYNAMESONGLIST, songlist);

                properties.store(fileOutputStream, null);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File getScoreFile() {
        return scoreFile;
    }

    public static File getPropFile() {
        return propFile;
    }

}
