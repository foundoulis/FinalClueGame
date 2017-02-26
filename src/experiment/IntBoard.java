package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IntBoard {	
	private Map<BoardCell, Set<BoardCell>> adjMtx;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	private BoardCell[][] grid;
	
	
	
	public IntBoard(int boardSizeX, int boardSizeY) {
		super();
		adjMtx = new HashMap<BoardCell, Set<BoardCell>>();
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		grid = new BoardCell[boardSizeX][boardSizeY];
		for(int x = 0;x<boardSizeX; x++){
			for(int y = 0; y<boardSizeY;y++){
				grid[x][y]=new BoardCell(x,y);
			}
		}
		calcAdjacencies(); //setup adjMtx
		
	}
	
	private void calcAdjacencies(){
		BoardCell boardKey;
		BoardCell boardValue;
		for(int x = 0; x<grid.length;x++){
			for(int y = 0; y<grid[0].length;y++){
				boardKey = grid[x][y];
				if(x-1>=0){
					boardValue = grid[x-1][y];
					addToAdjMap(boardKey, boardValue);
				}
				if(y-1>=0){
					boardValue = grid[x][y-1];
					addToAdjMap(boardKey, boardValue);
				}
				if(x+1<grid.length){
					boardValue = grid[x+1][y];
					addToAdjMap(boardKey, boardValue);
				}
				if(y+1<grid[0].length){
					boardValue = grid[x][y+1];
					addToAdjMap(boardKey, boardValue);
				}
			}
		}
		
		
	}
	
	private void addToAdjMap(BoardCell key, BoardCell value){
		if(adjMtx.containsKey(key))
			adjMtx.get(key).add(value);
		else{
			Set<BoardCell> temp = new HashSet<BoardCell>(); 
			temp.add(value);
			adjMtx.put(key, temp);
		}
		
	}
	
	public void calcTargets(BoardCell startCell, int pathLength){
	
	}
	
	public Set<BoardCell> getTargets(){
		return null;
	}
	public Set<BoardCell> getAdjList(BoardCell cell){
		return adjMtx.get(cell);
	}
	
	public BoardCell getCell(int x, int y){
		return grid[x][y];
	}
}
