package GameInterior;
import TextDesign.Colors;

public interface Card {
    String getName();
    String toString();
    String reset = Colors.ANSI_RESET;
}