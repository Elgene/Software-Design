package GameInterior.Tiles;
import GameInterior.Player;
import GameInterior.Room;
import TextDesign.Colors;

public abstract class Tile {

  protected String name;
  protected final int x ; // x cordinate of the top left corner of the square
  protected final int y ; // y cordinate of the top left corner of the square

  // Squares Associations
  protected Player player;
  protected String white = Colors.ANSI_RESET;

  public boolean north, east, south, west;
  public abstract void setRoom(Room room);
  public abstract Room getRoom();

  public Tile(int y, int x) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }


  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public abstract String toString();
}