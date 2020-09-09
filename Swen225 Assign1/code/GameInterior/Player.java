package GameInterior;

import TextDesign.Colors;
import Exceptions.InvalidMovementException;
import GameInterior.Tiles.Corridor;
import GameInterior.Tiles.RoomTile;
import GameInterior.Tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class Player
{

  private List<Card> playerCardLists = new ArrayList<>();
  private String playerName;

  private GameCharacter characterOfGame;
  private boolean ableToWin = true;
  private Tile tile;

  private CardTuple suggestPlayer;
  private CardTuple accusationPlayer;

  private String color = Colors.randomBoldColor();
  private String whiteColor = Colors.ANSI_WHITE;

  /**
   *State the player's name
   * @param playerName the name of the player for this game
   */
  public Player(String playerName) {
    this.playerName = playerName;
  }

  /**
   * Takes the first word/digit
   * @return the first character of the word
   */
  public String nameInitial() {
    return color + playerName.substring(0, 1) + whiteColor;
  }

  /**
   * check for whether the player can win the game
   * @return true for winning the game
   */
  public boolean canWin() {
    return ableToWin;
  }

  /**
   *To update the latest player that can possibly win the game
   * @param ableToWin update the player that wins the game
   */
  public void setCanWin(boolean ableToWin) {
    this.ableToWin = ableToWin;
  }

  /**
   *Gets the player's name
   * @return the player's name as a string
   */
  public String getName() {
    return playerName;
  }

  /**
   *Player sets/updates the suggestion
   * @param suggestPlayer updates the suggestion for next player
   */
  public void setSuggestion(CardTuple suggestPlayer) {
    this.suggestPlayer = suggestPlayer;
  }

  /**
   *Player sets/updates the accusation
   * @param accusationPlayer Re set the accusation for the next player
   */
  public void setAccusation(CardTuple accusationPlayer) {
    this.accusationPlayer = accusationPlayer;
  }

  /**
   *Player makes a suggestion
   * @param suggestPlayer suggest the possible suspect
   */
  public void makeSuggestion(CardTuple suggestPlayer){
    this.suggestPlayer = suggestPlayer;
  }

  /**
   *Player makes an accusation
   * @param accusationPlayer player make an accusation of the possible suspect for this game
   */
  public void makeAccusation(CardTuple accusationPlayer){
    this.accusationPlayer = accusationPlayer;
  }


  /**
   *Player gets the character
   * @return gets the character of the player
   */
  public GameCharacter getCharacter() {
    return characterOfGame;
  }

  /**
   *Player is able to set a character
   * @param characterOfGame updates the character for this game
   */
  public void setCharacter(GameCharacter characterOfGame) {
    this.characterOfGame = characterOfGame;
  }

  /**
   * Use white as a default color
   * @return the white for this game
   */
  public String getWhite() {
    return whiteColor;
  }

  /**
   *Sets/ updates to white
   * @param whiteColor update the color to white
   */
  public void setWhite(String whiteColor) {
    this.whiteColor = whiteColor;
  }

  /**
   * Get the square
   * @return the tile
   */
  public Tile getTile() {
    return tile;
  }

  /**
   *updates the tile
   * @param tile re-set the square for the player
   */
  public void setTile(Tile tile){
    this.tile = tile;
    getTile().setPlayer(this);
  }

  /**
   *Player gets the card
   * @return player that has the card
   */
  public List<Card> getCards() {
    return playerCardLists;
  }

  /**
   * Player add the cards
   * @param card increase the number of cards
   */
  public void addCard(Card card){
    playerCardLists.add(card);
  }

  /**
   * reset the directionPlayer that updates the neighbor of squares.
   * @param boardCoordinate the position of the board
   * @param directionPlayer  the direction of the player
   * @throws InvalidMovementException for invalid movement
   */
  public void setSquare(Board boardCoordinate, String directionPlayer) throws InvalidMovementException {

    int xPos = 0, yPos = 0;

    if(directionPlayer.equals("north"))
      yPos = -1;
    else if(directionPlayer.equals("south"))
      yPos = 1;
    else if(directionPlayer.equals("east"))
      xPos = 1;
    else xPos = -1;

    if(tile.getX() + xPos < 0 || tile.getX() + xPos > 23 ||
            tile.getY() + yPos < 0 || tile.getY() + yPos > 24) {
      throw new InvalidMovementException("That action will move the character out of bounds. Invalid Move.");
    }
    if(hasWall(getTile(), xPos, yPos)) throw new InvalidMovementException("walled");
    Tile tile = boardCoordinate.getTiles()[getTile().getY() + yPos][getTile().getX() + xPos];
    if(!(tile instanceof Corridor || tile instanceof RoomTile)) throw new InvalidMovementException("This move is invalid as this tile is inaccessible.");
    if(tile.getPlayer() != null) throw new InvalidMovementException("There's already a player on this tile.");

    // tile has no one standing on it currently
    this.tile.setPlayer(null);

    // create double link between tile and player
    this.tile = tile;

    // create doubly link between tile and player
    getTile().setPlayer(this);
  }

  /**
   * Checks for whether walls are a squared based on the direction of xPos and yPos.
   * @param tile the tile for this game
   * @param xPos the x position of this game
   * @param yPos the y position of this game
   * @return when there is a wall
   */
  private boolean hasWall(Tile tile, int xPos, int yPos){
    if(yPos == -1)
      if(tile.north)
        return true;
    if(yPos == 1)
      if(tile.south)
        return true;
    if(xPos == 1)
      if(tile.east)
        return true;
    if(xPos == -1)
      return tile.west;
    return false;
  }

  /**
   * Print statement of what player hold which cards
   * @return the to string of the player
   */
  public String toString() {

    String possibleCards = "";
    for (Card card : playerCardLists) {
      possibleCards += card.toString() + ", ";
    }

    return  "Player " + playerName + ", " + characterOfGame + " is holding: " + possibleCards + whiteColor;
  }

}