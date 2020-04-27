package project2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
	
	Cell[][] KB;
	ArrayList<Cell> pastMoves;
	ArrayList<Cell> pendingMoves;
	ArrayList<Cell> prospectCells; 
	ArrayList<Cell> minesFound;
	ArrayList<Cell> allHiddenCells;
	MineField environment;
	double numOfMines;
	double minesIdentified; 
	int cellsRevealed; 
	ArrayList<Cell> borderlineCells;
	Map<Cell, Double> probs;
	
	public Player(MineField minefield) {
		environment=minefield;
		numOfMines = 0;
		minesIdentified = 0;
		cellsRevealed = 0;
		pastMoves = new ArrayList<Cell>();
		pendingMoves = new ArrayList<Cell>();
		prospectCells = new ArrayList<Cell>();
		minesFound = new ArrayList<Cell>();
		allHiddenCells = new ArrayList<Cell>();
		borderlineCells = new ArrayList<Cell>();
		probs = new HashMap<Cell, Double>();
		
		//Initialize the Knowledge base with empty cells and updates the hidden cells around them
		KB = new Cell[minefield.size][minefield.size];
		for(int i = 0; i < minefield.size; i++) {
			for(int j = 0; j < minefield.size; j++) {
				KB[i][j] = new Cell(i,j);
				KB[i][j].setHidden(adjacentCells(KB, new Cell(i,j)));
				allHiddenCells.add(KB[i][j]);
			}
		}
	}
	
	/**
	 * Gets the number of hidden cells around a particular cell
	 * @param grid
	 * @param cell
	 * @return
	 */
	private int adjacentCells(Cell[][] grid, Cell cell) {
		int hidden=0;
		for(int i = -1; i<=1;i++) {
			for(int j = -1; j<=1;j++) {
				if(cell.getx()+i>=0 && cell.getx()+i<grid.length && cell.gety()+j>=0 && cell.gety()+j<grid.length)
					hidden++;
			}
		}
		return hidden;
	}
	
	public int solve() {
		//First move is random
		openCell(getRandomCell());
		
		//Play till all cell in the knowledge base are revealed
		while(cellsRevealed != KB.length*KB.length) {
		//for(int i = 0; i < 100; i++) {
			Cell curr = inferenceCell();
			if(curr.equals(new Cell(KB.length,KB.length))) {
				break;
		//		System.out.println("MINES INDENTIFIED: " + minesIdentified + 
		//			"\nNUMBER OF TOTAL MINES: " + numOfMines);
		//		System.out.println(minesFound);
		//		return 1;
			}
			openCell(curr);
			cellsRevealed++;
			display();
			updateBorderlineCells();
			//System.out.println(borderlineCells);
		    //System.out.println(prospectCells);
			System.out.println();
		}
		//return -1;
		System.out.println("MINES INDENTIFIED: " + minesIdentified + 
				"\nNUMBER OF TOTAL MINES: " + numOfMines);
		return (int)((minesIdentified/numOfMines)*100);
	}
	
	/**
	 * Using the current clues in the Knowledge base, infers a cell that will be safe to open
	 * @return
	 */
	public Cell inferenceCell() {
		
		Cell previous = pastMoves.get(pastMoves.size()-1);
		
		//if the last opened cell has a clue greater than 0. Than add it to a list to prospect later 
		if(KB[previous.getx()][previous.gety()].getClue() > 0) {
			prospectCells.add(previous);
		}
		
		//if the last opened cell has a clue of 0, add all the cells around that cell to pending moves
		if(KB[previous.getx()][previous.gety()].getClue() == 0) {
			for(int i = -1; i<=1;i++) {
				for(int j = -1; j<=1;j++) {
					if(previous.getx()+i>=0 && previous.getx()+i<KB.length && previous.gety()+j>=0 && previous.gety()+j<KB.length) {
						Cell nextmove = new Cell(previous.getx()+i,previous.gety()+j);
						if(!pastMoves.contains(nextmove))
							pendingMoves.add(nextmove);
					}
				}
			}
		}
		
		//if there are pending moves. return the next pending move
		//Some moves might have repeated so if we already made a pending move, then we 
		//remove it from the list and go to the next pending move.
		if(pendingMoves.size() > 0) {
			while(pendingMoves.size()>0) {
				if(!pastMoves.contains(pendingMoves.get(0)))
					return pendingMoves.remove(0);
				else 
					pendingMoves.remove(0);
			}		
		}
		
		
		while(prospectCells.size()>0) {
			Cell index = prospectCells.remove(0);
			int counter = 1;
			
			while(counter < 3) {
				if(KB[index.getx()][index.gety()].getClue()==counter) {
					
					//Look at the 1s that are in corners 
					if(KB[index.getx()][index.gety()].getHidden() == counter) {
						for(int row = -1; row<=1;row++) {
							for(int col = -1; col<=1;col++) {
								if(index.getx()+row>=0 && index.getx()+row<KB.length && index.gety()+col>=0 && index.gety()+col<KB.length) {
									if(!pastMoves.contains(KB[index.getx()+row][index.gety()+col]) && !minesFound.contains(KB[index.getx()+row][index.gety()+col])) {
										System.out.println("FOUND MINE IN: " + KB[index.getx()+row][index.gety()+col]);
										pastMoves.add(KB[index.getx()+row][index.gety()+col]);
										allHiddenCells.remove(KB[index.getx()+row][index.gety()+col]);
										minesFound.add(KB[index.getx()+row][index.gety()+col]);
										minesIdentified++;
										numOfMines++;
										KB[index.getx()+row][index.gety()+col].setMine(true);
										mineFound(new Cell(index.getx()+row,index.gety()+col));
										cellsRevealed++;
									}
								}
							}
						}
					}
				}
				counter++;
			}
		}
		
		
		//if there are pending moves. return the next pending move
		if(pendingMoves.size() > 0) {
			while(pendingMoves.size()>0) {
				if(!pastMoves.contains(pendingMoves.get(0)))
					return pendingMoves.remove(0);
				else 
					pendingMoves.remove(0);
			}		
		}

		
		//return new Cell(10,10);
		
		Cell rand = getRandomCell();
		//if(cellsRevealed != KB.length*KB.length) {
		//return minimizeRisk();
		//}
		//else {
		//	System.out.println(cellsRevealed);
		//	return new Cell(10,10);
		//}
		//while(pastMoves.contains(rand)) {
	//		rand = getRandomCell();
	//	}
		
		return rand;
		
	}
	
	public Cell basicAgent() {
		Cell previous = pastMoves.get(pastMoves.size()-1);
		
		//if the last opened cell has a clue greater than 0. Than add it to a list to prospect later 
		if(KB[previous.getx()][previous.gety()].getClue() > 0) {
			prospectCells.add(previous);
		}
		
		//if the last opened cell has a clue of 0, add all the cells around that cell to pending moves
		if(KB[previous.getx()][previous.gety()].getClue() == 0) {
			for(int i = -1; i<=1;i++) {
				for(int j = -1; j<=1;j++) {
					if(previous.getx()+i>=0 && previous.getx()+i<KB.length && previous.gety()+j>=0 && previous.gety()+j<KB.length) {
						Cell nextmove = new Cell(previous.getx()+i,previous.gety()+j);
						if(!pastMoves.contains(nextmove))
							pendingMoves.add(nextmove);
					}
				}
			}
		}
		
		//if there are pending moves. return the next pending move
		//Some moves might have repeated so if we already made a pending move, then we 
		//remove it from the list and go to the next pending move.
		if(pendingMoves.size() > 0) {
			while(pendingMoves.size()>0) {
				if(!pastMoves.contains(pendingMoves.get(0)))
					return pendingMoves.remove(0);
				else 
					pendingMoves.remove(0);
			}		
		}
		
		return getRandomCell();
	}
	
	public Cell minimizeCost() {
		
		
		
		return getRandomCell();
	}
	
	public void updateBorderlineCells() {
		
		boolean known = false; 
		boolean unknown = false; 
		borderlineCells.clear();
		probs.clear();
		
		for(int row=0; row<KB.length; row++) {
			for(int col=0; col<KB[0].length; col++) {
				if(!pastMoves.contains(KB[row][col]) && !minesFound.contains(KB[row][col])) {
					for(int i = -1; i<=1;i++) {
						for(int j = -1; j<=1;j++) {
							if(row+i>=0 && row+i<KB.length && col+j>=0 && col+j<KB.length) {
								if(pastMoves.contains(KB[row+i][col+j])) 
									known = true;	
								if(!pastMoves.contains(KB[row+i][col+j])) 
									unknown = true;
							}
						}
					}
				}
				if(known && unknown) { 
					borderlineCells.add(KB[row][col]);
					probs.put(KB[row][col],	null);
				}
				known = false;
				unknown = false;
			}
		}
	}
	
	/**
	 * opens a cell in the Knowledge base and updates its contents
	 * @param cell
	 */
	public void openCell(Cell cell) {
		System.out.println("OPENING CELL: " + cell);
		int x = environment.queryLoc(cell.getx(), cell.gety());
		if(x != -1) {
			KB[cell.getx()][cell.gety()].setClue(x - minesAround(cell));
		}
		else if(x == -1) {
			KB[cell.getx()][cell.gety()].setClue(x);
		}
		allHiddenCells.remove(cell);
		pastMoves.add(cell); 
		
		//Cell is a mine
		if(KB[cell.getx()][cell.gety()].getClue()==-1) {
			isMine(cell);
		}
		
		//Cell is not mine
		if(KB[cell.getx()][cell.gety()].getClue()>=0) {
			isSafe(cell);
		}
	}
	
	
	/**
	 * return a random cell for when we can't pick a cell from inference
	 * @return
	 */
	public Cell getRandomCell() {
		if(allHiddenCells.size() == 0) {
			return new Cell(KB.length,KB.length);
		}
		Cell rand = allHiddenCells.get((int)(Math.random()*allHiddenCells.size()));
		System.out.println("\nRANDOM CELL GENERATED: " + "(" + rand.getx() + "," + rand.gety() + ")");
		
		return rand;
	}
	
	/**
	 * Updates the Knowledge base of a cell that had a mine in it. 
	 * Increases the surrounding cell's mines counter by 1
	 * Decreases the surrounding cell's hidden counter by 1 
	 * @param cell
	 */
	public void isMine(Cell cell) {
		System.out.println("A MINE WAS CHOSEN");
		numOfMines++;
		KB[cell.getx()][cell.gety()].setMine(true);
		mineFound(new Cell(cell.getx(),cell.gety()));
		minesFound.add(KB[cell.getx()][cell.gety()]);
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
		
		//This new clue can be used in conjunction with other clues to figure out stuff. Thus we have to add
		//all current cells that have a clue greater than 1 to the prospect list
		if(prospectCells.size() == 0) {
			for(int i = 0; i < KB.length; i++) {
				for(int j = 0; j < KB[0].length; j++) {
					if(KB[i][j].getClue()>0) {
						prospectCells.add(KB[i][j]);
					}
				}
			}
		}
	}

	/**
	 * Decreases the clue of all known cells around it by 1
	 * Also adds the cells that will have a clue of 0 after this into the pendingMoves list
	 * @param cell
	 */
	public void mineFound(Cell cell) {
		
		for(int i = -1; i<=1;i++) {
			for(int j = -1; j<=1;j++) {
				if(cell.getx()+i>=0 && cell.getx()+i<KB.length && cell.gety()+j>=0 && cell.gety()+j<KB.length) {
					
					//if you already visited a cell and they have a clue bigger than 0
					if(pastMoves.contains(KB[cell.getx()+i][cell.gety()+j]) && KB[cell.getx()+i][cell.gety()+j].getClue()>0) {
						//Decrease each cell's clue by one
						int currentClue = KB[cell.getx()+i][cell.gety()+j].getClue();
						int currentHidden = KB[cell.getx()+i][cell.gety()+j].getHidden();
						int currentMinesFound = KB[cell.getx()+i][cell.gety()+j].getMinesFound();
						
						KB[cell.getx()+i][cell.gety()+j].setClue(currentClue-1);
						KB[cell.getx()+i][cell.gety()+j].setMinesFound(currentMinesFound+1);
						KB[cell.getx()+i][cell.gety()+j].setHidden(currentHidden-1);
						
						
						if(currentClue == 1) {
							prospectCells.remove(KB[cell.getx()+i][cell.gety()+j]);
							
							//if a cell has a clue of 1 and the mine that cell was referring to was found. 
							//All cells that haven't been opened around them are safe so add them to pending moves
							for(int row = -1; row<=1;row++) {
								for(int col = -1; col<=1;col++) {
									if(cell.getx()+i+row>=0 && cell.getx()+i+row<KB.length && cell.gety()+j+col>=0 && cell.gety()+j+col<KB.length) {
										if(!pastMoves.contains(KB[cell.getx()+i+row][cell.gety()+j+col])) {
											if(!pendingMoves.contains(KB[cell.getx()+i+row][cell.gety()+j+col])) {
												pendingMoves.add(KB[cell.getx()+i+row][cell.gety()+j+col]);
											}
										}
									}
								}
							}
						}
						if(currentClue == 2) {
							if(!prospectCells.contains(KB[cell.getx()+i][cell.gety()+j])) {
								prospectCells.add(KB[cell.getx()+i][cell.gety()+j]);
							}
						}
					}
					//If you haven't visited a cell then decrease their hidden cells and mines found variables
					else if(!pastMoves.contains(KB[cell.getx()+i][cell.gety()+j])){
						int currentHidden = KB[cell.getx()+i][cell.gety()+j].getHidden();
						int currentMinesFound = KB[cell.getx()+i][cell.gety()+j].getMinesFound();
						
						KB[cell.getx()+i][cell.gety()+j].setMinesFound(currentMinesFound+1);
						KB[cell.getx()+i][cell.gety()+j].setHidden(currentHidden-1);
					}
				}
			}
		}
	}
	
	/**
	 * Gets the number of known mines around a cell
	 * @param cell
	 */
	public int minesAround(Cell cell) {
		int mines = 0;
		
		for(int i = -1; i<=1;i++) {
			for(int j = -1; j<=1;j++) {
				if(cell.getx()+i>=0 && cell.getx()+i<KB.length && cell.gety()+j>=0 && cell.gety()+j<KB.length) {
					if(pastMoves.contains(KB[cell.getx()+i][cell.gety()+j]) && KB[cell.getx()+i][cell.gety()+j].isMine()) {
						mines++;
					}
				}
			}
		}
		
		return mines;
	}
	
	/**
	 * Display the board
	 */
	public void display() {
		for(int i=0; i<KB.length; i++) {
			for(int j=0; j<KB[0].length; j++) {
				if(KB[i][j].isMine()) 
					System.out.print("* ");
				else if(pastMoves.contains(KB[i][j]) && KB[i][j].getClue() == 0)
					System.out.print("O ");
				else 
					System.out.print(KB[i][j].getClue() + " ");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String []args) {
		
		/*
		int[][] performance = new int[13][30]; 
		
		for(int i = 3; i<16; i++) {
			System.out.println("\nBoard Size: "  + i + "x" + i + "\n");
			for(int j = 1; j<(2*i)-1; j++) {
				System.out.println("Mines: " + j);
				MineField answer = new MineField(i,j);
				Player one = new Player(answer);
				performance[i-3][j-1] = one.solve();
			}
			
		}
		
		for(int i = 0; i<performance.length; i++) {
			for(int j =0; j<performance[0].length; j++) {
				System.out.print(performance[i][j] + " ");
			}
			System.out.println();
		}
			System.out.println(performance);
		*/
		
		MineField answer = new MineField(8,10);
		Player one = new Player(answer);
		System.out.println("SCORE: " + one.solve());
		
		System.out.println("Knowledge Base:");
		one.display();
		System.out.println("\n\nEnvironment:");
		answer.display();
	}
}
