package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class ICJK_BoardTests {

	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 26;
	public static final int NUM_COLUMNS = 24;

	private static Board board;

	@BeforeClass
	public static void setUp() {

		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles(
				"ICJK_ClueLayout.csv",
				"ICJK_Legend.txt",
				"TDNFTP_players.txt",
				"TDDF_weapons.txt",
				"TDDF_people.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testRooms() {
		// Get the map of initial => room
		Map<Character, String> legend = board.getLegend();
		// Ensure we read the correct number of rooms
		assertEquals(LEGEND_SIZE, legend.size());
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few
		// others
		assertEquals("Master bedroom", legend.get('M'));
		assertEquals("Guest bedroom", legend.get('B'));
		assertEquals("Laundry room", legend.get('L'));
		assertEquals("Dining room", legend.get('D'));
		assertEquals("Walkway", legend.get('W'));
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns

		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void FourDoorDirections() {
		BoardCell room = board.getCellAt(7, 7);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(9, 3);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(16, 22);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(23, 16);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(14, 14);
		assertFalse(room.isDoorway());
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(6, 0);
		assertFalse(cell.isDoorway());

	}

	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(14, numDoors);
	}

	@Test
	public void testRoomInitials() {
		assertEquals('M', board.getCellAt(0, 0).getInitial());
		assertEquals('O', board.getCellAt(4, 12).getInitial());
		assertEquals('M', board.getCellAt(9, 0).getInitial());
		assertEquals('K', board.getCellAt(21, 22).getInitial());
		assertEquals('B', board.getCellAt(21, 0).getInitial());
		assertEquals('G', board.getCellAt(0, 22).getInitial());
		assertEquals('X', board.getCellAt(12, 13).getInitial());
	}
}
