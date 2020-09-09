package GameInterior.Tiles;
import GameInterior.Room;

public class Corridor extends Tile {

    //------------------------
    // CONSTRUCTOR
    //------------------------
    public Corridor(int y, int x) {
        super(y, x);
    }

    //------------------------
    // GETTERS AND SETTERS
    //------------------------

    @Override
    public Room getRoom() {
        return null;
    }

    @Override
    public void setRoom(Room room) {}

    @Override
    public String toString() {
        if(player == null) return "-";
        return player.nameInitial();
    }
}
