package GameInterior;

import java.util.Set;
import java.util.HashSet;
import TextDesign.Colors;
import GameInterior.Tiles.RoomTile;


public class Room implements Card {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  private Weapon weapon;
  private String name;
  private Set tiles = new HashSet();
  private String roomColor = Colors.randomColor();

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(String aName)
  {
    name = aName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void setWeapon(Weapon weapon)
  {
    this.weapon = weapon;
  }

  /**
   * add the input room square to this room.
   */
  public void addRoomTile(RoomTile t)
  {
    tiles.add(t);
  }

  @Override
  public String getName()
  {
    return name;
  }

  public Set getTiles()
  {
    return tiles;
  }

  @Override
  public String toString() {
    return roomColor + name + reset;
  }
}