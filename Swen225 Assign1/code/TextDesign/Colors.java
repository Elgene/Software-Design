package TextDesign;
import javax.swing.text.BadLocationException;
import java.util.Random;

public class Colors{

    // this code is retrieved from StackOverflow
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println#5762502

    // normal colours
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Bold High Intensity
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN

    public static final String[] colors = new String[]{
            ANSI_RESET, ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE
    };

    public static final String[] boldColors = new String[]{
            GREEN_BOLD_BRIGHT, YELLOW_BOLD_BRIGHT, BLUE_BOLD_BRIGHT, PURPLE_BOLD_BRIGHT, CYAN_BOLD_BRIGHT
    };

    public static String randomColor(){
        return colors[new Random().nextInt(colors.length)];
    }

    public static String randomBoldColor() {
        return boldColors[new Random().nextInt(boldColors.length)];
    }
  }