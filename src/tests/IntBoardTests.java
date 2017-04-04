//
//package tests;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import experiment.BoardCell;
//import experiment.IntBoard;
//
//public class IntBoardTests {
//	private IntBoard board;
//
//	@Before
//	public void setUp() {
//		board = new IntBoard(4, 4);
//	}
//
//	/*
//	 * Test adjacencies for top left corner
//	 */
//	@Test
//	public void testAdjacency00() {
//		BoardCell cell = board.getCell(0, 0);
//		Set<BoardCell> testList = board.getAdjList(cell);
//		assertTrue(testList != null);
//		assertTrue(testList.contains(board.getCell(1, 0)));
//		assertTrue(testList.contains(board.getCell(0, 1)));
//		assertEquals(2, testList.size());
//
//	}
//
//	/*
//	 * Test adjacencies for bottom right corner
//	 */
//	@Test
//	public void testAdjacency33() {
//		BoardCell cell = board.getCell(3, 3);
//		Set<BoardCell> testList = board.getAdjList(cell);
//		assertTrue(testList != null);
//		assertTrue(testList.contains(board.getCell(2, 3)));
//		assertTrue(testList.contains(board.getCell(3, 2)));
//		assertEquals(2, testList.size());
//	}
//
//	/*
//	 * Test adjacencies for a right edge 1,3
//	 */
//	@Test
//	public void testAdjacency13() {
//		BoardCell cell = board.getCell(1, 3);
//		Set<BoardCell> testList = board.getAdjList(cell);
//		assertTrue(testList != null);
//		assertTrue(testList.contains(board.getCell(0, 3)));
//		assertTrue(testList.contains(board.getCell(2, 3)));
//		assertTrue(testList.contains(board.getCell(1, 2)));
//		assertEquals(3, testList.size());
//	}
//
//	/*
//	 * Test adjacencies for a left edge 3,0
//	 */
//	@Test
//	public void testAdjacency30() {
//		BoardCell cell = board.getCell(3, 0);
//		Set<BoardCell> testList = board.getAdjList(cell);
//		assertTrue(testList != null);
//		assertTrue(testList.contains(board.getCell(2, 0)));
//		assertTrue(testList.contains(board.getCell(3, 1)));
//		assertEquals(2, testList.size());
//	}
//
//	/*
//	 * Test adjacencies for second column middle 1,1
//	 */
//	@Test
//	public void testAdjacency11() {
//		BoardCell cell = board.getCell(1, 1);
//		Set<BoardCell> testList = board.getAdjList(cell);
//		assertTrue(testList != null);
//		assertTrue(testList.contains(board.getCell(0, 1)));
//		assertTrue(testList.contains(board.getCell(1, 0)));
//		assertTrue(testList.contains(board.getCell(1, 2)));
//		assertTrue(testList.contains(board.getCell(2, 1)));
//		assertEquals(4, testList.size());
//	}
//
//	/*
//	 * Test adjacencies for second column middle 2,2
//	 */
//	@Test
//	public void testAdjacency22() {
//		BoardCell cell = board.getCell(2, 2);
//		Set<BoardCell> testList = board.getAdjList(cell);
//		assertTrue(testList != null);
//		assertTrue(testList.contains(board.getCell(2, 1)));
//		assertTrue(testList.contains(board.getCell(1, 2)));
//		assertTrue(testList.contains(board.getCell(3, 2)));
//		assertTrue(testList.contains(board.getCell(2, 3)));
//		assertEquals(4, testList.size());
//	}
//
//	/*
//	 * Test targets at 0,0 and roll of 1
//	 */
//	@Test
//	public void testTargets00_1() {
//		BoardCell cell = board.getCell(0, 0);
//		board.calcTargets(cell, 1);
//		Set<BoardCell> targets = board.getTargets();
//		assertTrue(targets != null);
//		assertEquals(2, targets.size());
//		assertTrue(targets.contains(board.getCell(0, 1)));
//		assertTrue(targets.contains(board.getCell(1, 0)));
//
//	}
//
//	/*
//	 * Test targets at 3,3 and roll of 1
//	 */
//	@Test
//	public void testTargets33_1() {
//		BoardCell cell = board.getCell(3, 3);
//		board.calcTargets(cell, 1);
//		Set<BoardCell> targets = board.getTargets();
//		assertTrue(targets != null);
//		assertEquals(2, targets.size());
//		assertTrue(targets.contains(board.getCell(2, 3)));
//		assertTrue(targets.contains(board.getCell(3, 2)));
//
//	}
//
//	/*
//	 * Test targets at 0,0 and roll of 2
//	 */
//	@Test
//	public void testTargets00_2() {
//		BoardCell cell = board.getCell(0, 0);
//		board.calcTargets(cell, 2);
//		Set<BoardCell> targets = board.getTargets();
//		assertTrue(targets != null);
//		assertEquals(3, targets.size());
//		assertTrue(targets.contains(board.getCell(0, 2)));
//		assertTrue(targets.contains(board.getCell(2, 0)));
//		assertTrue(targets.contains(board.getCell(1, 1)));
//
//	}
//
//	/*
//	 * Test targets at 2,2 and roll of 2
//	 */
//	@Test
//	public void testTargets22_2() {
//		BoardCell cell = board.getCell(2, 2);
//		board.calcTargets(cell, 2);
//		Set<BoardCell> targets = board.getTargets();
//		assertTrue(targets != null);
//		assertEquals(6, targets.size());
//		assertTrue(targets.contains(board.getCell(0, 2)));
//		assertTrue(targets.contains(board.getCell(2, 0)));
//		assertTrue(targets.contains(board.getCell(1, 1)));
//		assertTrue(targets.contains(board.getCell(3, 1)));
//		assertTrue(targets.contains(board.getCell(1, 3)));
//		assertTrue(targets.contains(board.getCell(3, 3)));
//
//	}
//
//	/*
//	 * Test targets at 1,1 and roll of 2
//	 */
//	@Test
//	public void testTargets11_2() {
//		BoardCell cell = board.getCell(1, 1);
//		board.calcTargets(cell, 2);
//		Set<BoardCell> targets = board.getTargets();
//		assertTrue(targets != null);
//		assertEquals(6, targets.size());
//		assertTrue(targets.contains(board.getCell(3, 1)));
//		assertTrue(targets.contains(board.getCell(1, 3)));
//		assertTrue(targets.contains(board.getCell(2, 2)));
//		assertTrue(targets.contains(board.getCell(2, 0)));
//		assertTrue(targets.contains(board.getCell(0, 2)));
//		assertTrue(targets.contains(board.getCell(0, 0)));
//
//	}
//
//	/*
//	 * Test targets at 0,0 and roll of 3
//	 */
//	@Test
//	public void testTargets00_3() {
//		BoardCell cell = board.getCell(0, 0);
//		board.calcTargets(cell, 3);
//		Set<BoardCell> targets = board.getTargets();
//		assertTrue(targets != null);
//		assertEquals(6, targets.size());
//		assertTrue(targets.contains(board.getCell(0, 3)));
//		assertTrue(targets.contains(board.getCell(1, 2)));
//		assertTrue(targets.contains(board.getCell(2, 1)));
//		assertTrue(targets.contains(board.getCell(3, 0)));
//		assertTrue(targets.contains(board.getCell(1, 0)));
//		assertTrue(targets.contains(board.getCell(0, 1)));
//	}
//
//}
