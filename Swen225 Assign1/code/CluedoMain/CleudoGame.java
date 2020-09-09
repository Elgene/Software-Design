package CluedoMain;

import Exceptions.*;
import GameInterior.Tiles.Tile;
import GameInterior.*;
import TextDesign.Colors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class CleudoGame
{
  private boolean gameEnded;
  private List<Card> deck = new ArrayList<>(); // easier to shuffle deck if using list :)
  private Board board;
  private CardTuple solution;
  private CardTuple suggestion;
  private CardTuple accusation;
  private List<CardTuple> suspects = new ArrayList<>();

  private GameCharacter[] charList = new GameCharacter[]{
          new GameCharacter("Miss Scarlett"),
          new GameCharacter("Colonel Mustard"),
          new GameCharacter("Mrs. White"),
          new GameCharacter("Mr. Green"),
          new GameCharacter("Mrs. Peacock"),
          new GameCharacter("Professor Plum")
  };

  private Player[] players;

  public CleudoGame(){
    newGame();
    redraw();
  }

  /**
   * Sets up the game by setting up the players, the deck and distributing the cards
   *
   */
  private void newGame(){
    chooseCharacters(); //sets up player from user input and display them on board.
    board = new Board(players);
    solution = new CardTuple(random(board.getRooms().toArray(new Room[9])), random(board.getWeapons()), random(board.getCharacters()));
    setUpDeck(solution.getRoom(), solution.getWeapon(), solution.getCharacter());
    dealCards();
    setUpPlayers();
  }

  /**
   * Arrange the players' pieces on the appropriate positions on the board (depending on where the character originally starts)
   */
  private void setUpPlayers() {

    for (Player p : players) {
      Tile tile;

      if(p.getCharacter().getName().equalsIgnoreCase("Miss Scarlett"))
        tile = board.getTiles()[24][7];

      else if(p.getCharacter().getName().equalsIgnoreCase("Mrs. White"))
        tile = board.getTiles()[0][9];

      else if(p.getCharacter().getName().equalsIgnoreCase("Professor Plum"))
        tile = board.getTiles()[19][23];

      else if(p.getCharacter().getName().equalsIgnoreCase("Mr. Green"))
        tile = board.getTiles()[0][14];

      else if(p.getCharacter().getName().equalsIgnoreCase("Mrs. Peacock"))
        tile = board.getTiles()[6][23];

      else
        tile = board.getTiles()[17][0];

      p.setTile(tile);
    }
  }

  /**
   * Arrange the players' pieces on the appropriate positions on the board (depending on where the character originally starts)
   */
  private void chooseCharacters(){
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    // asking for number of players.
    while(true) {
      System.out.println("Please enter the number of players (3 - 6 players): ");
      int totalPlayers;

      try {
        try {
          totalPlayers = Integer.parseInt(input.readLine());
        } catch(RuntimeException e){throw new InvalidPlayerNumberException("Not a valid number of players");}

        // If player number invalid, ask again
        if(totalPlayers > 6 || totalPlayers < 3) throw new InvalidPlayerNumberException("Wrong number of player");

      } catch (IOException | GameException e) {
        System.out.println(e.getMessage());
        continue;
      }
      players = new Player[totalPlayers];
      break;
    }

    // Assign player to character
    List<GameCharacter> characters = new ArrayList<>(Arrays.asList(charList));
    for(int i = 1; i <= players.length; ++i) {
      label: while (true) {
        System.out.println(Colors.ANSI_RESET + "Player " + i + ", please choose a single digit number: ");
        Player player;

        try {
          String playerName = input.readLine();
          if(playerName.equals("")) {
            System.out.println("Player name not given. Name automatically set to \"Unknown\".");
            playerName = "Unknown";
          }

          // Avoid duplicate for player name
          for (Player pl : players)
            if(pl != null && pl.getName().equals(playerName))
              throw new InvalidNameException("Player name already taken");

          //set player name
          player = new Player(playerName);
          System.out.println("Player " + playerName + ", please choose your character from below");
          String character = "[";


          for (GameCharacter gc : characters)
            if (gc != null) character += (gc.getName() + ", ");
          System.out.println(character.substring(0, character.length()-2) + " ]");

          // Set characters
          String characterName = input.readLine();
          boolean duplicateName = false;
          for (int j = 0; j < charList.length; j++)
            if (charList[j] != null && !characters.isEmpty() && characters.contains(charList[j])
                    && charList[j].getName().contains(characterName)) {
              duplicateName = true;
              characters.remove(charList[j]);
              break;
            }

          if(!duplicateName)
            throw new InvalidNameException("Character name does not exist or is already taken.");

          player.setCharacter(new GameCharacter(characterName));

        } catch (IOException | InvalidNameException e) {
          System.out.println(e.getMessage());
          continue label;// If user input is not a proper character, ask again
        }
        players[i-1] = player;
        break label;
      }
    }

  }


  private <T> T random(T[] values) {
    return values[new Random().nextInt(values.length)];
  }

  /**
   * Player's turn is processed here. If player is out of the game, they cannot take a turn.
   */
  public void playerTurn () {
    BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));

    for(Player player : players) {

      if(gameEnded) return;
      while(true) {
        if (!player.canWin())
        {
          System.out.println("Player" + player.getName() + " is out of the game, sorry!");
          break;
        }
        System.out.println(player.getName() + " Enter \"rd\" (roll dice) to begin your turn.");

        try {
          String action = buff.readLine();
          if(!(action.equals("rd")))
            throw new GameException("Please roll dice :(");
          movePlayer(player, buff);
          break;

        } catch (IOException | GameException e) {
          System.out.println(e.getMessage());
        }
      }

      try {
        if (player.canWin()) {
          suggestAccuseEndTurn(player, buff);
        }
      } catch (GameException | IOException e) {
        System.out.println(e.getMessage());
      }
      resetSandA();
    }
  }

  /**
   * Player's input after the turn is processed here. The player is allowed to either suggest (if they are in a room), accuse or end their turn.
   */
  private void suggestAccuseEndTurn(Player player, BufferedReader buff) throws GameException, IOException {
    System.out.println("Player " + player.getName() + ", you are currently in "+  player.getTile().getRoom() +", what's your next move? \n Please enter: \n  > \t\"suggest\" to make your suggestion \n  > \t\"accuse\" to make an accusation \n  > \t\"end turn\" to end your turn");
    String afterMove;
    while(true) {
      try {
        afterMove = buff.readLine();
        if (!(afterMove.equals("suggest") || afterMove.equals("accuse") || afterMove.equalsIgnoreCase("end turn"))) throw new GameException("Please re-enter your input");
        break;
      } catch (IOException | GameException e) {
        System.out.println(e.getMessage());
      }
    }


    switch (afterMove) {
      case "suggest":
        if (!player.canWin())
          throw new GameException("Player's previous accusation was refuted, so they cannot make further suggestions or accusations");
        if(player.getTile().getRoom() == null) throw new GameException("Player is not in room");
        suggest(player, buff);
        break;

      case "accuse":
        if (!player.canWin()) {
          // Accuser gets to see the solution, but not the other players
          throw new GameException("Player's previous accusation was refuted, so they cannot make further suggestions or accusations.");
        }
        accuse(player, buff);

      case "end turn":
        System.out.println("Player " + player.getName() + " has ended their turn.\n");
        return;

      // Player is out of the game
      default:
        break;
    }
  }

  /**
   * Resets suggestion or accusation made in the previous turn
   */
  private void resetSandA(){
    suggestion = null;
    accusation = null;
    for (Player player : players) {
      player.setAccusation(null);
      player.setSuggestion(null);
    }
  }

  /**
   * This method carries out the suggestion.
   * @param player
   * @param buff
   */
  private void suggest(Player player, BufferedReader buff) throws GameException, IOException {
    suggestion = craftSandA(buff, player);

    // store suggestion into player class
    player.makeSuggestion(suggestion);

    // teleport weapon into suggested room
    player.getTile().getRoom().setWeapon(suggestion.getWeapon());

    // ask next player to refute a card if the previous one does not have a card to refute
    int count = 0;

    tag : for(Player p : players) {
      if (!p.getName().equalsIgnoreCase(player.getName()))
      {
        while (true) {
          try {
            Card c;
            System.out.println("Player " + p.getName() + ", do you have a card to refute player " + player.getName() + "?  enter \"y\" to refute, and \"n\" if you do not have a card.");

            String ans = buff.readLine().toLowerCase();

            if (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n"))
              throw new GameException("wrong input");

            if (ans.equals("y")) {
              System.out.println("enter the name of a suggestion card: ");
              String name = buff.readLine();
              if (!(name.equalsIgnoreCase(suggestion.getCharacter().getName())
                      || name.equalsIgnoreCase(suggestion.getRoom().getName())
                      || name.equalsIgnoreCase(suggestion.getWeapon().getName())))
                throw new GameException("name entered is not one of suggestion cards");
              c = drawCard(p, name);
            } else break;

            if (c == null) throw new GameException("player does not have the relating card");
            ++count; // increment card when player choose to present one of the suggestion cards.
            break tag;
          } catch (IOException | GameException e) {
            System.out.println(e.getMessage());
          }
        }
      }
    }

    // if all players presented relevent card, then count == 3
    if(count == 1) {
      System.out.println("Suggestion is refuted because someone is holding one of the cards");
    }
    else {
      if(!suspects.contains(suggestion))// if the suggestion is already in suspects, do not add it.
        suspects.add(suggestion);
      System.out.println("Suggestion is not refuted... Could this be the possible solution?? ");
    }

  }

  /**
   * This method crafts the suggestion and accusations based on user inputs.
   * @param buff
   * @return
   * @throws InvalidNameException
   * @throws IOException
   */
  private CardTuple craftSandA(BufferedReader buff, Player player) throws InvalidNameException, IOException {
    if(player != null) { // If the player argument is not null, the player is trying to make a suggestion

      Room room;
      Weapon weapon;
      GameCharacter p;

      while(true) {
        try {
          room = player.getTile().getRoom();

          System.out.println("Enter a weapon: ");
          String w = buff.readLine();
          Weapon result = null;
          for (Weapon x1 : board.getWeapons()) {
            if (x1.getName().equalsIgnoreCase(w)) {
              result = x1;
              break;
            }
          }
          weapon = result;
          if (weapon == null) throw new InvalidNameException("No such weapon");

          System.out.println("Enter a character: ");
          String c = buff.readLine();
          GameCharacter found = null;
          for (GameCharacter x : board.getCharacters()) {
            if (x.getName().equalsIgnoreCase(c)) {
              found = x;
              break;
            }
          }
          p = found;
          if (p == null) throw new InvalidNameException("Character not found");
          break;
        } catch(GameException e) {
          System.out.println(e.getMessage());
        }
      }
      return new CardTuple(room, weapon, p);
    }

    // The code will not reach here unless the player argument is not null, which means that the player is trying to accuse
    System.out.println("Enter a room: ");
    String r = buff.readLine();
    Room room = null;
    for (Room room1 : board.getRooms()) {
      if (room1.getName().equalsIgnoreCase(r)) {
        room = room1;
        break;
      }
    }

    if(room == null) throw new InvalidNameException("No such room");
    System.out.println("Enter a weapon: ");
    String w = buff.readLine();
    Weapon weapon = null;
    for (Weapon x1 : board.getWeapons()) {
      if (x1.getName().equalsIgnoreCase(w)) {
        weapon = x1;
        break;
      }
    }

    if(weapon == null) throw new InvalidNameException("No such weapon");
    System.out.println("Enter a character: ");
    String c = buff.readLine();
    GameCharacter p = null;
    for (GameCharacter x : board.getCharacters()) {
      if (x.getName().equalsIgnoreCase(c)) {
        p = x;
        break;
      }
    }

    if(p == null) throw new InvalidNameException("Character not found");
    return new CardTuple(room, weapon, p);
  }

  /**
   * Returns the card that the player is trying to get. Null if not in hand.
   * @param player
   * @param name
   * @return
   */
  public Card drawCard(Player player, String name){
    for (Card x : player.getCards()) {
      if (x.getName().equals(name)) {
        return x;
      }
    }
    return null;
  }

  /**
   * This method carries out the accusation.
   * @param player,
   * @param buff
   */
  private void accuse(Player player, BufferedReader buff){
    while(true) {
      try {
        accusation = craftSandA(buff, null);
        player.makeAccusation(accusation); // store accusation into player
        System.out.println("Murder is: " + solution + '\n');
        break;
      } catch (InvalidNameException | IOException e) {
        System.out.println(e.getMessage());
      }
    }
    if(!accusation.equals(solution)) {
      System.out.println("Accusation is refuted as the cards in the accusation are not all presented in murder cards.");
      player.setCanWin(false);
      System.out.println("Player " + player.getName() + " is out of the game");
      int count = 0;
      for (Player p : players)
      {
        if(!p.canWin())
        {
          count++;
        }
      }
      if (count == players.length)
      {
        gameEnded = true;
        System.out.println("Unfortunately all the players are out of the game... Nobody wins :(");
      }
    }
    else {
      System.out.println("Accusation is correct as all the cards are the murder cards\n");
      System.out.println("Game Won!\n" + "The winner is player" + player.getName() + '\n');
      // gameWon
      gameEnded = true;
    }

  }

  private void redraw(){
    System.out.println(toString());
  }


  //=========================
  //  ACTIONS
  //=========================
  /**
   * This method moves the player around the board.
   * A player can accuse at any time during their turn.
   * @param player
   * @param input
   */
  private void movePlayer(Player player, BufferedReader input) {

    Random random = new Random();

    // Setting range from minimum 2 (since there are 2 die) to maximum 12
    int dice = random.nextInt(12-2) + 2;

    for(int i = 0; i < dice; ++i){
      System.out.println(player.getName() + " has " + (dice-i) + " steps left.");
      while(true) {
        try {
          System.out.println("Enter W for North, A for West, S for South and D for East");
          String direction = input.readLine().toLowerCase();
          if (!direction.equalsIgnoreCase("w") && !direction.equalsIgnoreCase("s") && !direction.equalsIgnoreCase("a") && !direction.equalsIgnoreCase("d") && !direction.equalsIgnoreCase("accuse"))
            throw new InvalidMovementException("Invalid Move Input");

          String move = "";
          switch (direction) {
            case "w":
              move = "north";
              break;
            case "s":
              move = "south";
              break;
            case "a":
              move = "west";
              break;
            case "d":
              move = "east";
              break;

            case "accuse":
              if (!player.canWin()) {

                throw new GameException("Player's previous accusation was refuted, so they cannot make further suggestions or accusations");
              }
              accuse(player, input);
              return;
          }

          player.setSquare(board, move);
          if(player.getTile().getRoom() != null) {
            System.out.println("\nplayer: " + player.getName() + " entered the " + player.getTile().getRoom());
            redraw();
            return;
          }
          break;
        } catch (IOException | GameException e) {
          System.out.println(e.getMessage());
        }
      }
      redraw();
    }
  }

  /**
   * This method takes all non-murder solution cards, shuffles it and sets up the deck.
   * @param r, w, c
   */
  private void setUpDeck(Room r, Weapon w, GameCharacter c){
    for (Room room : board.getRooms()) {
      if(room == r) continue;
      deck.add(room);
    }

    for (Weapon weapon : board.getWeapons()) {
      if(weapon == w) continue;
      deck.add(weapon);
    }

    for (GameCharacter character : board.getCharacters()) {
      if(character.getName().equals(c.getName()))
        continue; // add all non-murder character to the deck.
      deck.add(character);
    }
  }

  /**
   * Deal the cards to each player until deck is empty.
   */
  private void dealCards(){
    while(true)
      for (Player player : players) {
        Collections.shuffle(deck); // shuffle the cards
        player.addCard(deck.remove(deck.size() - 1)); // player is dealt one shuffled card at each loop until deck is empty
        if(deck.isEmpty()) return;
      }
  }

  public String toString(){
    String s = "";
    s += board.toString() + '\n';
    for (Player player : players) {
      s += player.toString() + '\n';
    }
    s += "Current suggestion: " + (suggestion != null ? suggestion.toString() : "null") + '\n';
    s += "Current accusation: " + (accusation != null ? accusation.toString() : "null") + '\n';
    s += "Possible Solution: " + suspects + '\n';
    return s;
  }


  /**
   * Starts the game, and is terminated when the game ends.
   */
  public void play(){
    while(true) {
      if(gameEnded) return;
      playerTurn();
    }
  }

  public static void main(String[] args){
    new CleudoGame().play();
  }

}