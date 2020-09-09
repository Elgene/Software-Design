package GameInterior;
public class CardTuple {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //models.Selection Attributes
  private final Room room;
  private final Weapon weapon;
  private final GameCharacter character;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CardTuple(Room aRoom, Weapon aWeapon, GameCharacter aCharacter)
  {
    room = aRoom;
    weapon = aWeapon;
    character = aCharacter;
  }

  public Room getRoom() {
    return room;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public GameCharacter getCharacter() {
    return character;
  }

  public String toString()
  {
    return " Room: " + getRoom() + " Character: " + getCharacter() + " Weapon: " + getWeapon();
  }

  /**
   * if the 3 fields room, character and weapon are the same object, return true, otherwise false. used for accusation and suggestion.
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o){
    // If the object is compared with itself then return true
    if (o == this) return true;

    if (!(o instanceof CardTuple))
      return false;

    return (getWeapon() == ((CardTuple) o).getWeapon())
            && (getCharacter() == ((CardTuple) o).getCharacter())
            && (getRoom() == ((CardTuple) o).getRoom());
  }
}