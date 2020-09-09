package GameInterior.Tiles;
import GameInterior.Room;
import TextDesign.Colors;

public class RoomTile extends Tile {
    private Room room;
    private String red = Colors.ANSI_RED;

    public RoomTile(int y, int x, String name){
        super(y, x);
        this.name = name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room r) {
        this.room = r;
    }

    @Override
    public String toString() {
        if (player == null) {
            if (north || south || east || west) {
                return red + name.substring(0, 1) + white;
            }
            else return name.substring(0, 1);
        }
        return player.nameInitial();
    }
}
