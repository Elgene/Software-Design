package GameInterior;
import GameInterior.Tiles.*;
import java.util.*;


/**A class used to create Cluedo game board.
 * this class is responsible for constructing, nine rooms,blank tiles, corridors, walls and Inaccessible tiles.
 * The Room is named after its initial name. */


public class Board {


  private final int COLS = 24; // board's rows total
  private final int ROWS = 25; // board's columns total

  private List<Room> rooms = new ArrayList<>();
  private Tile[][] tile = new Tile[ROWS][COLS];
  private Player[] players;

  private GameCharacter[] charactersList = new GameCharacter[] {
          new GameCharacter("Miss Scarlett"),
          new GameCharacter("Colonel Mustard"),
          new GameCharacter("Mrs. White"),
          new GameCharacter("Mr. Green"),
          new GameCharacter("Mrs. Peacock"),
          new GameCharacter("Professor Plum")
  };

  private Weapon[] weapons = new Weapon[] {
          new Weapon("Candlestick"),
          new Weapon("Lead Pipe"),
          new Weapon("Revolver"),
          new Weapon("Spanner"),
          new Weapon("Dagger"),
          new Weapon("Rope")
  };

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Board(Player[] players) {
    this.players = players;
    setUpBoard();
  }

  /** Construct each essential board element mentioned above(such as walls, rooms, corridors and blank tiles) */
  public void setUpBoard() {
    setUpNineRooms();
    setUpCorridors();
    setUpInaccessibleTiles();
    setUpBlankTiles();
    setUptWalls();
  }


  /** Construct walls around the board and doors for each rooms.  */
  private void setUptWalls() {
    for (int row = 0; row < ROWS; ++row) {
      for (int col = 0; col < COLS; ++col) {

        // construct kitchen door
        if ((row == 6 && col == 4) || (row == 7 && col == 4))
          continue;

        // construct Ball Room door (facing west and east)
        if ((row == 5 && col == 7) || (row == 5 && col == 8) || (row == 5 && col == 15)
                || (row == 5 && col == 16))
          continue;

        // construct another Ball Room door (facing south)
        if ((row == 7 && col == 9) || (row == 8 && col == 9) || (row == 7 && col == 14)
                || (row == 8 && col == 14))
          continue;

        // construct Conservatory door
        if ((row == 4 && col == 19) || (row == 5 && col == 19))
          continue;

        // construct Dining Room doors
        if ((row == 12 && col == 7) || (row == 12 && col == 8) || (row == 15 && col == 6)
                || (row == 16 && col == 6))
          continue;

        // construct Billiard Room doors
        if ((row == 9 && col == 17) || (row == 9 && col == 18) || (row == 12 && col == 22)
                || (row == 13 && col == 22))
          continue;

        // construct library doors
        if ((row == 13 && col == 20) || (row == 14 && col == 20) || (row == 16 && col == 16)
                || (row == 16 && col == 17))
          continue;

        // construct Lounge door.
        if ((row == 18 && col == 5) || (row == 19 && col == 5))
          continue;

        // construct Hall doors
        if ((row == 17 && col == 11) || (row == 18 && col == 11) || (row == 17 && col == 12)
                || (row == 18 && col == 12) || (row == 20 && col == 14) || (row == 20 && col == 15))
          continue;

        // construct study door
        if ((row == 20 && col == 18) || (row == 21 && col == 18))
          continue;

        // construct roomsquare and corridor walls.
        if (!(tile[row][col] instanceof RoomTile) && !(tile[row][col] instanceof Corridor))
          continue;

        /* set up north, south, east and west wall for the tile and corridors*/
        // top
        if (row - 1 >= 0) {
          if ((tile[row - 1][col] instanceof RoomTile) || (tile[row - 1][col] instanceof Corridor)) {
            if (tile[row - 1][col].getClass() != tile[row][col].getClass()) {
              tile[row][col].north = true;
              tile[row - 1][col].south = true;
            }
          }
        }

        // bottom
        if (row + 1 <= 24) {
          if ((tile[row + 1][col] instanceof RoomTile) || (tile[row + 1][col] instanceof Corridor))
            if (tile[row + 1][col].getClass() != tile[row][col].getClass()) {
              tile[row][col].south = true;
              tile[row + 1][col].north = true;
            }
        }

        // left
        if (col - 1 >= 0) {
          if ((tile[row][col - 1] instanceof RoomTile) || (tile[row][col - 1] instanceof Corridor))
            if (tile[row][col - 1].getClass() != tile[row][col].getClass()) {
              tile[row][col].west = true;
              tile[row][col - 1].east = true;
            }
        }

        // right
        if (col + 1 <= 23) {
          if ((tile[row][col + 1] instanceof RoomTile) || (tile[row][col + 1] instanceof Corridor))
            if (tile[row][col + 1].getClass() != tile[row][col].getClass()) {
              tile[row][col].east = true;
              tile[row][col + 1].west = true;
            }
        }
      }
    }
  }

  /** Construct nine rooms and add them to the lists of the rooms*/
  private void setUpNineRooms() {
    setUpConservatory();
    setUpBilliardRoom();
    setUpDiningRoom();
    setUpStudyRoom();
    setUpBallRoom();
    setUpLibrary();
    setUpKitchen();
    setUpLounge();
    setUpHall();
  }

  /** construct Kitchen and add it to the lists of rooms*/
  private void setUpKitchen() {
    Room Kitchen = new Room("Kitchen");
    rooms.add(Kitchen);
    for (int row = 1; row <= 6; ++row)
      for (int col = 0; col <= 5; ++col) {
        tile[row][col] = new RoomTile(row, col, Kitchen.getName());
        tile[row][col].setRoom(Kitchen);
        Kitchen.addRoomTile((RoomTile) tile[row][col]);
      }
  }

  /** Construct Ball Room and add it to the lists of rooms */
  private void setUpBallRoom() {
    Room BallRm = new Room("Ball Room");
    rooms.add(BallRm);
    for (int row = 1; row <= 7; ++row)
      for (int col = 8; col <= 15; ++col) {
        if (((col <= 9 && col >= 8) || (col <= 15 && col >= 14)) && row == 1) continue;
        tile[row][col] = new RoomTile(row, col, BallRm.getName());
        tile[row][col].setRoom(BallRm);
        BallRm.addRoomTile((RoomTile) tile[row][col]);
      }
  }

  /**Construct Conservatory room and add it to the lists of rooms*/
  private void setUpConservatory() {
    Room ConservtRm = new Room("Conservatory");
    rooms.add(ConservtRm);
    for (int row = 1; row <= 5; ++row)
      for (int col = 18; col <= 23; ++col) {
        if (row == 5 && col == 19 || row == 5 && col == 18) continue;
        tile[row][col] = new RoomTile(row, col, ConservtRm.getName());
        tile[row][col].setRoom(ConservtRm);
        ConservtRm.addRoomTile((RoomTile) tile[row][col]);
      }
  }

  /**construct Dining Room add it to the lists of rooms*/
  private void setUpDiningRoom() {
    Room DiningRm = new Room("Dining Room");
    rooms.add(DiningRm);
    for (int row = 9; row <= 15; ++row)
      for (int col = 0; col <= 7; ++col) {
        if (row == 9 && (col <= 7 && col >= 5))continue;
        tile[row][col] = new RoomTile(row, col, DiningRm.getName());
        tile[row][col].setRoom(DiningRm);
        DiningRm.addRoomTile((RoomTile) tile[row][col]);
      }
  }


  /** Construct Billiard Room and add it to the lists of rooms */
  private void setUpBilliardRoom() {
    Room BilliardRm = new Room("Billiard Room");
    rooms.add(BilliardRm);
    for (int row = 8; row <= 12; ++row)
      for (int col = 18; col <= 23; ++col) {
        tile[row][col] = new RoomTile(row, col, BilliardRm.getName());
        tile[row][col].setRoom(BilliardRm);
        BilliardRm.addRoomTile((RoomTile) tile[row][col]);
      }
  }

  /** Construct Library and add it to the lists of rooms */
  private void setUpLibrary() {
    Room Library = new Room("Library");
    rooms.add(Library);
    for (int row = 14; row <= 18; ++row)
      for (int col = 17; col <= 23; ++col) {
        if ((row == 14 && col == 17) || (row == 14 && col == 23) || (row == 18 && col == 17) || (row == 18 && col == 23)) continue;
        tile[row][col] = new RoomTile(row, col, Library.getName());
        tile[row][col].setRoom(Library);
        Library.addRoomTile((RoomTile) tile[row][col]);
      }
  }


  /** Construct Lounge and add it to the lists of rooms */
  private void setUpLounge() {
    Room Lounge = new Room("Lounge");
    rooms.add(Lounge);
    for (int row = 19; row <= 24; ++row)
      for (int col = 0; col <= 6; ++col) {
        if (row == 24 && col == 6) continue;
        tile[row][col] = new RoomTile(row, col, Lounge.getName());
        tile[row][col].setRoom(Lounge);
        Lounge.addRoomTile((RoomTile) tile[row][col]);
      }
  }

  /** Construct hall and add it to the lists of rooms */
  private void setUpHall() {
    Room Hall = new Room("Hall");
    rooms.add(Hall);
    for (int row = 18; row <= 24; ++row)
      for (int col = 9; col <= 14; ++col) {
        tile[row][col] = new RoomTile(row, col, Hall.getName());
        tile[row][col].setRoom(Hall);
        Hall.addRoomTile((RoomTile) tile[row][col]);
      }
  }

  /** Construct Study and add it to the lists of rooms */
  private void setUpStudyRoom() {
    Room StudyRm = new Room("Study");
    rooms.add(StudyRm);
    for (int row = 21; row <= 24; ++row)
      for (int col = 17; col <= 23; ++col) {
        if (row == 24 && col == 17) continue;
        tile[row][col] = new RoomTile(row, col, StudyRm.getName());
        tile[row][col].setRoom(StudyRm);
        StudyRm.addRoomTile((RoomTile) tile[row][col]);
      }
  }


  /** Construct corridors */
  private void setUpCorridors() {
    for (int row = 0; row <= 24; ++row)
      for (int col = 0; col <= 23; ++col)
        if (!(tile[row][col] instanceof RoomTile))
          tile[row][col] = new Corridor(row, col);
  }

  /** Construct inaccessible tiles */
  private void setUpInaccessibleTiles() {
    for (int row = 10; row <= 16; ++row)
      for (int col = 10; col <= 14; ++col)
        tile[row][col] = new Inaccessible(row, col);
  }

  /** Construct blank space on the board */
  private void setUpBlankTiles() {
    //clear the first column to display players
    for (int j = 0; j <= 23; ++j) {
      if (j == 9 || j == 14) continue;
      tile[0][j] = new EmptyTile(0, j);
    }

    tile[1][6] = new EmptyTile(1, 6); 	//Between Kitchen and Ball Room
    tile[1][18] = new EmptyTile(1, 17);	//between Ball Room and Conservatory

    // make appropriate blank space on the right side of the board
    for (int i = 0; i <= 23; ++i) {
      if (!(i == 13 || i == 14 || i == 18 || i == 5 || i == 7 || i == 20)) continue;
      tile[i][23] = new EmptyTile(i, 23);
    }

    // make appropriate blank space on the left side of the board
    for (int i = 0; i <= 23; ++i) {
      if (!(i == 6 || i == 8 || i == 16 || i == 18))
        continue;
      tile[i][0] = new EmptyTile(i, 0);
    }

    // make appropriate blank space at bottom side of the board
    for (int j = 0; j <= 23; ++j) {
      if (!(j == 6 || j == 8 || j == 15 || j == 17)) continue;
      tile[24][j] = new EmptyTile(24, j);
    }
  }

  //-------------
  // GETTERS
  //------------
  public List<Room> getRooms() {
    return rooms;
  }

  public Tile[][] getTiles() {
    return tile;
  }

  public Weapon[] getWeapons() {
    return weapons;
  }

  public GameCharacter[] getCharacters() {
    return charactersList;
  }

  /** Print the board to the screen as a text*/
  public String toString() {
    String board = "";
    for (int row = 0; row <= 24; ++row) {
      for (int col = 0; col <= 23; ++col)
        board += tile[row][col].toString() + " ";
      board += '\n';
    }
    return board;
  }

}