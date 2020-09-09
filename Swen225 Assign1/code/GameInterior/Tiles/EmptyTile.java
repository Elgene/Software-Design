package GameInterior.Tiles;
import GameInterior.Room;

public class EmptyTile extends Tile {

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public EmptyTile(int y, int x){
        super(y, x);
    }

    //------------------------
    // GETTERS AND SETTERS
    //------------------------

    @Override
    public Room getRoom(){
        return null;
    }

    @Override
    public void setRoom(Room room) {
    }

    @Override
    public String toString() {
        return " ";
    }
}
