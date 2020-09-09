package GameInterior;
import TextDesign.Colors;

public class GameCharacter implements Card {

  private String name;
  private String characterColor = Colors.randomColor();
  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameCharacter(String name)
  {
    this.name = name;
  }

  //------------------------
  // GETTERS, SETTERS AND TO STRINGS
  //------------------------

  @Override
  public String getName() {
    return name;
  }
  @Override
  public String toString()
  {
    return characterColor + "[" + name + "]" + reset;
  }  

}