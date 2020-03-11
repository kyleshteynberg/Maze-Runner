package project2; 

public class Player {
	
	Cell[][] KB;
	MineField environment;
	int numOfMines;
	
	public Player(MineField minefield) {
		environment=minefield;
		numOfMines = 0;
		
		//Initialize the Knowledge base with empty cells
		KB = new Cell[minefield.size][minefield.size];
		for(int i = 0; i < minefield.size; i++) {
			for(int j = 0; j < minefield.size; j++) {
				KB[i][j] = new Cell(i,j);
			}
		}
	}
	
	public int solve() {
		//Total number of mines that were safely identified out of the total number of mines 
		int minesIdentified=0; 
	
		openCell(getRandomCell()); 
		
		//return minesIdentified/totalMines;
		return 1;
	}

	public void openCell(Cell cell) {
		int x = environment.queryLoc(cell.getx(), cell.gety());
		KB[cell.getx()][cell.gety()].setClue(x);
		
		//Cell is a mine
		if(KB[cell.getx()][cell.gety()].getClue()==-1) {
			numOfMines++;
			isMine(cell);
		}
		
		//Cell is not mine
		if(KB[cell.getx()][cell.gety()].getClue()>=0) {
			isSafe(cell);
		}
		
	}
	
	/**
	 * return a random cell for when we can't deduce a cell from inference
	 * @return
	 */
	public Cell getRandomCell() {
		int x = (int)(Math.random()*KB.length);
		int y = (int)(Math.random()*KB.length);
		System.out.println("\nRANDOM CELL GENERATED: " + "(" + x + "," + y + ")");
		
		return new Cell(x,y);
	}
	
	/**
	 * Updates the Knowledge base of a cell that had a mine in it. 
	 * Increases the surrounding cell's mines counter by 1
	 * Decreases the surrounding cell's hidden counter by 1 
	 * @param cell
	 */
	public void isMine(Cell cell) {
		KB[cell.getx()][cell.gety()].setMine(true);
		for(int i = -1; i<=1;i++) {
			for(int j = -1; j<=1;j++) {
				if(cell.getx()+i>=0 && cell.getx()+i<KB.length && cell.gety()+j>=0 && cell.gety()+j<KB.length) {
					int current = KB[cell.getx()+i][cell.gety()+j].getMinesFound();
					KB[cell.getx()+i][cell.gety()+j].setMinesFound(current+1);
					
					current = KB[cell.getx()+i][cell.gety()+j].getHidden();
					KB[cell.getx()+i][cell.gety()+j].setHidden(current-1);
				}
			}
		}
	}
	
	/**
	 * Updates the Knowledge base of a cell that is safe 
	 * Increase the safe counter of surrounding cells by 1
	 * Decreases the hidden counter of the surrounding cells by 1
	 * @param cell
	 */
	public void isSafe(Cell cell) {
		KB[cell.getx()][cell.gety()].setMine(false);
		for(int i = -1; i<=1;i++) {
			for(int j = -1; j<=1;j++) {
				if(cell.getx()+i>=0 && cell.getx()+i<KB.length && cell.gety()+j>=0 && cell.gety()+j<KB.length) {
					int current = KB[cell.getx()+i][cell.gety()+j].getSafeFound();
					KB[cell.getx()+i][cell.gety()+j].setSafeFound(current+1);
					
					current = KB[cell.getx()+i][cell.gety()+j].getHidden();
					KB[cell.getx()+i][cell.gety()+j].setHidden(current-1);
				}
			}
		}
	}

	/**
	 * Display the board
	 */
	public void display() {
		for(int i=0; i<KB.length; i++) {
			for(int j=0; j<KB[0].length; j++) {
				if(KB[i][j].isMine()) 
					System.out.print("* ");
				else
					System.out.print(KB[i][j].getClue() + " ");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String []args) {
		
		MineField answer = new MineField(10,5);
		Player one = new Player(answer);
		one.solve();
		
		System.out.println("Knowledge Base:");
		one.display();
		System.out.println("\n\nEnvironment:");
		answer.display();
	}
}
