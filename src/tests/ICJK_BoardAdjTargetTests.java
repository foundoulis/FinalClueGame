package tests;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class ICJK_BoardAdjTargetTests {
	// We make the Board static because we can load it one time and
		// then do all the tests.
		private static Board board;

		@BeforeClass
		public static void setUp() {
			// Board is singleton, get the only instance and initialize it
			board = Board.getInstance();
			// set the file names to use my config files
			board.setConfigFiles(
					"ICJK_ClueLayout.csv",
					"ICJK_Legend.txt",
					"TDNFTP_players.txt",
					"TDDF_weapons.txt",
					"TDDF_people.txt");
			board.initialize();
		}

		// Ensure that player does not move around within room
		// These cells are Purple on the planning spreadsheet
		@Test
		public void testAdjacenciesInsideRooms() {
			// Test a corner
			Set<BoardCell> testList = board.getAdjList(2, 1);
			assertEquals(0, testList.size());
			// Test one that has walkway underneath
			testList = board.getAdjList(10, 1);
			assertEquals(0, testList.size());
			// Test one that has walkway above
			testList = board.getAdjList(18, 19);
			assertEquals(0, testList.size());
			// Test one that is in middle of room
			testList = board.getAdjList(24, 12);
			assertEquals(0, testList.size());
			// Test one beside a door
			testList = board.getAdjList(22, 13);
			assertEquals(0, testList.size());
			// Test one in a corner of room
			testList = board.getAdjList(5, 19);
			assertEquals(0, testList.size());
		}

		// Ensure that the adjacency list from a doorway is only the
		// walkway. NOTE: This test could be merged with door
		// direction test.
		// These tests are RED on the planning spreadsheet
		@Test
		public void testAdjacencyRoomExit() {
			// TEST DOORWAY RIGHT
			Set<BoardCell> testList = board.getAdjList(6, 7);
			assertEquals(1, testList.size());
			assertTrue(testList.contains(board.getCellAt(6, 8)));
			// TEST DOORWAY LEFT
			testList = board.getAdjList(16, 22);
			assertEquals(1, testList.size());
			assertTrue(testList.contains(board.getCellAt(16, 21)));
			// TEST DOORWAY DOWN
			testList = board.getAdjList(13, 18);
			assertEquals(1, testList.size());
			assertTrue(testList.contains(board.getCellAt(14, 18)));
			// TEST DOORWAY UP
			testList = board.getAdjList(14, 4);
			assertEquals(1, testList.size());
			assertTrue(testList.contains(board.getCellAt(13, 4)));
			// TEST DOORWAY RIGHT, WHERE THERE'S A WALKWAY above
			testList = board.getAdjList(22, 5);
			assertEquals(1, testList.size());
			assertTrue(testList.contains(board.getCellAt(22, 6)));

		}

		// Test adjacency at entrance to rooms
		// These tests are Orange in planning spreadsheet
		@Test
		public void testAdjacencyDoorways() {
			// Test beside a door direction RIGHT
			Set<BoardCell> testList = board.getAdjList(7, 8);
			assertTrue(testList.contains(board.getCellAt(7, 7)));
			assertTrue(testList.contains(board.getCellAt(7, 9)));
			assertTrue(testList.contains(board.getCellAt(6, 8)));
			assertTrue(testList.contains(board.getCellAt(8, 8)));
			assertEquals(4, testList.size());
			// Test beside a door direction DOWN
			testList = board.getAdjList(6, 17);
			assertTrue(testList.contains(board.getCellAt(5, 17)));
			assertTrue(testList.contains(board.getCellAt(7, 17)));
			assertTrue(testList.contains(board.getCellAt(6, 16)));
			assertTrue(testList.contains(board.getCellAt(6, 18)));
			assertEquals(4, testList.size());
			// Test beside a door direction LEFT
			testList = board.getAdjList(16, 21);
			assertTrue(testList.contains(board.getCellAt(16, 22)));
			assertTrue(testList.contains(board.getCellAt(16, 20)));
			assertTrue(testList.contains(board.getCellAt(15, 21)));
			assertEquals(3, testList.size());
			// Test beside a door direction UP
			testList = board.getAdjList(21, 12);
			assertTrue(testList.contains(board.getCellAt(21, 13)));
			assertTrue(testList.contains(board.getCellAt(21,11)));
			assertTrue(testList.contains(board.getCellAt(22, 12)));
			assertTrue(testList.contains(board.getCellAt(20, 12)));
			assertEquals(4, testList.size());
		}

		// Test a variety of walkway scenarios
		// These tests are Highlight Yellow on the planning spreadsheet
		@Test
		public void testAdjacencyWalkways() {
			// Test on top edge of board, just one walkway piece
			Set<BoardCell> testList = board.getAdjList(0, 8);
			assertTrue(testList.contains(board.getCellAt(1, 8)));
			assertEquals(1, testList.size());

			// Test on left edge of board, three walkway pieces
			testList = board.getAdjList(19, 0);
			assertTrue(testList.contains(board.getCellAt(20, 0)));
			assertTrue(testList.contains(board.getCellAt(19, 1)));
			assertEquals(2, testList.size());

			// Test between two rooms, walkways up and down
			testList = board.getAdjList(3, 18);
			assertTrue(testList.contains(board.getCellAt(2, 18)));
			assertTrue(testList.contains(board.getCellAt(4, 18)));
			assertEquals(2, testList.size());

			// Test surrounded by 4 walkways
			testList = board.getAdjList(20, 8);
			assertTrue(testList.contains(board.getCellAt(20, 9)));
			assertTrue(testList.contains(board.getCellAt(20, 7)));
			assertTrue(testList.contains(board.getCellAt(19, 8)));
			assertTrue(testList.contains(board.getCellAt(21, 8)));
			assertEquals(4, testList.size());

			// Test on bottom edge of board, next to 1 room piece
			testList = board.getAdjList(25, 8);
			assertTrue(testList.contains(board.getCellAt(24, 8)));
			assertTrue(testList.contains(board.getCellAt(25, 9)));
			assertEquals(2, testList.size());

			// Test on right edge of board, next to 1 room piece
			testList = board.getAdjList(8, 23);
			assertTrue(testList.contains(board.getCellAt(7, 23)));
			assertTrue(testList.contains(board.getCellAt(8, 22)));
			assertEquals(2, testList.size());

			// Test on walkway next to door that is not in the needed
			// direction to enter
			testList = board.getAdjList(13, 17);
			assertTrue(testList.contains(board.getCellAt(14, 17)));
			assertTrue(testList.contains(board.getCellAt(13, 16)));
			assertTrue(testList.contains(board.getCellAt(12, 17)));
			assertEquals(3, testList.size());
		}

		// Tests of just walkways, 1 step, includes on edge of board
		// and beside room
		// Have already tested adjacency lists on all four edges, will
		// only test two edges here
		// These are Dark BLUE on the planning spreadsheet
		@Test
		public void testTargetsOneStep() {
			board.calcTargets(25, 15, 1);
			Set<BoardCell> targets = board.getTargets();
			assertEquals(1, targets.size());
			assertTrue(targets.contains(board.getCellAt(24, 15)));

			board.calcTargets(13, 0, 1);
			targets = board.getTargets();
			assertEquals(2, targets.size());
			assertTrue(targets.contains(board.getCellAt(13, 1)));
			assertTrue(targets.contains(board.getCellAt(12, 0)));
		}

		// Tests of just walkways, 2 steps
		// These are Dark BLUE on the planning spreadsheet
		@Test
		public void testTargetsTwoSteps() {
			board.calcTargets(25, 15, 2);
			Set<BoardCell> targets = board.getTargets();
			assertEquals(1, targets.size());
			assertTrue(targets.contains(board.getCellAt(23, 15)));
		}

		// Tests of just walkways, 4 steps
		// These are LIGHT BLUE on the planning spreadsheet
		@Test
		public void testTargetsFourSteps() {
			board.calcTargets(25, 15, 4);
			Set<BoardCell> targets = board.getTargets();
			assertEquals(2, targets.size());
			assertTrue(targets.contains(board.getCellAt(21, 15)));
			assertTrue(targets.contains(board.getCellAt(22, 16)));

		}

		// Tests of just walkways plus one door, 6 steps
		// These are LIGHT BLUE on the planning spreadsheet
		// Test getting into room, doesn't require all steps
		@Test
		public void testTargetsSixSteps() {
			board.calcTargets(25, 15, 6);
			Set<BoardCell> targets = board.getTargets();
			assertEquals(8, targets.size());
			assertTrue(targets.contains(board.getCellAt(21, 13)));
			assertTrue(targets.contains(board.getCellAt(20, 14)));
			assertTrue(targets.contains(board.getCellAt(19, 15)));
			assertTrue(targets.contains(board.getCellAt(20, 16)));
			assertTrue(targets.contains(board.getCellAt(21, 17)));
			assertTrue(targets.contains(board.getCellAt(23, 16)));
			assertTrue(targets.contains(board.getCellAt(22, 16)));
		}

		// Test getting into a room
		// These are Dark BLUE on the planning spreadsheet

		@Test
		public void testTargetsIntoRoom() {
			// One room is exactly 2 away
			board.calcTargets(21, 16, 2);
			Set<BoardCell> targets = board.getTargets();
			assertEquals(6, targets.size());
			assertTrue(targets.contains(board.getCellAt(23, 16)));
			assertTrue(targets.contains(board.getCellAt(22, 15)));
			assertTrue(targets.contains(board.getCellAt(21, 14)));

			assertTrue(targets.contains(board.getCellAt(20, 15)));
			assertTrue(targets.contains(board.getCellAt(19, 16)));
			assertTrue(targets.contains(board.getCellAt(20, 17)));
		}

		// Test getting into room, doesn't require all steps
		// These are dark BLUE on the planning spreadsheet
		@Test
		public void testTargetsIntoRoomShortcut() {
			board.calcTargets(25, 15, 6);
			Set<BoardCell> targets = board.getTargets();
			assertEquals(8, targets.size());
			assertTrue(targets.contains(board.getCellAt(21, 13)));
			assertTrue(targets.contains(board.getCellAt(20, 14)));
			assertTrue(targets.contains(board.getCellAt(19, 15)));
			assertTrue(targets.contains(board.getCellAt(20, 16)));
			assertTrue(targets.contains(board.getCellAt(21, 17)));
			assertTrue(targets.contains(board.getCellAt(23, 16)));
			assertTrue(targets.contains(board.getCellAt(22, 16)));

		}

		// Test getting out of a room
		// These are LIGHT BLUE on the planning spreadsheet
		@Test
		public void testRoomExit() {
			// Take one step, essentially just the adj list
			board.calcTargets(18, 4, 1);
			Set<BoardCell> targets = board.getTargets();
			// Ensure doesn't exit through the wall
			assertEquals(1, targets.size());
			assertTrue(targets.contains(board.getCellAt(19, 4)));
			// Take two steps
			board.calcTargets(18, 4, 2);
			targets = board.getTargets();
			assertEquals(3, targets.size());
			assertTrue(targets.contains(board.getCellAt(19, 3)));
			assertTrue(targets.contains(board.getCellAt(19, 5)));
			assertTrue(targets.contains(board.getCellAt(20, 4)));
		}

	}