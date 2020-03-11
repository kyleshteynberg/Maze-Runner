package project2; 

public class Player {
	
	Cell[][] KB;
	MineField environment;
	int boom=0;
	
	public Player(MineField minefield) {
		environment=minefield;
		KB = new Cell[minefield.size][minefield.size];
	}

	public void openCell(Cell cell) {
		int x = environment.queryLoc(cell.getx(), cell.gety());
		KB[cell.getx()][cell.gety()].setClue(x);
		
		//Cell is a mine
		if(KB[cell.getx()][cell.gety()].getClue()==-1) {
			boom++;
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
			return;
		}
		
		//Cell is not mine
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
	
	public int solve() {
		//Total number of mines that were safely identified out of the total number of mines 
		int minesIdentified=0;
		int totalMines=0; 
	
		
		openCell(new Cell((int)Math.random()*KB.length, (int)Math.random()*KB[0].length)); 
		
		
		
		return minesIdentified/totalMines;
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
		
		Player one = new Player(new MineField(10,5));
		one.solve();
		one.display();
	}
}
