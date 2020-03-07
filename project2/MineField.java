import java.util.Random;


public class MineField {
	Cell[][] grid;
	
	public MineField(int size, int mines) {
		grid=MineGen(size, mines);
	}
	
	/**
	 * Generates a random minefield with dimensions size by size and specified number of mines
	 */
	private Cell[][] MineGen(int size, int mines) {
		Cell grid[][] = new Cell[size][size];
		for(int i = 0; i<size;i++) {
			for(int j = 0; j<size;j++) {
				grid[i][j] = new Cell(i,j);
				grid[i][j].setMine(false);
			}
		}

		Random rand = new Random();
		int x=rand.nextInt(size-1);
		int y=rand.nextInt(size-1);
		for(int i = 0; i<mines; i++) {
			while(grid[x][y].isMine()==true) {
				x=rand.nextInt(size-1);
				y=rand.nextInt(size-1);
			}
			grid[x][y].setMine(true);
		}
		
		return grid;
	}

	/**
	 * Determines number of mines adjacent to given cell
	 * Input: grid of cells and chosen cell
	 * Output: returns number of adjacent cells with mines
	 */
	private int adjacentMines(Cell cell) {
		int mines=0;
		for(int i = -1; i<=1;i++) {
			for(int j = -1; j<=1;j++) {
				if(cell.getx()+i>=0 && cell.getx()+i<grid.length && cell.gety()+j>=0 && cell.gety()+j<grid.length)
					if(grid[cell.getx()+i][cell.gety()+j].isMine())
						mines++;
			}
		}
		
		return mines;
	}

	/**
	 * Allows player to query cell at given location
	 * Input: x and y coord of cell
	 * Output: Returns -1 if chosen cell is mined, otherwise returns number of adjacent cells with mines
	 */
	public int queryLoc(int posx, int posy) {
		if(grid[posx][posy].isMine()==true)	
			return -1;
		else 
			return adjacentMines(grid[posx][posy]);
	}
}
