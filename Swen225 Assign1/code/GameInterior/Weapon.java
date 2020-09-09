package GameInterior;
import TextDesign.Colors;

public class Weapon implements Card {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //models.Weapon Attributes
  private String name;
  private String weaponColor = Colors.randomColor();

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Weapon(String weapon)
  {
    name = weapon;
  }

  //------------------------
  // INTERFACE
  //------------------------

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String toString()
  {
    return weaponColor + "[" + getName()+ "]" + reset;
  }
}