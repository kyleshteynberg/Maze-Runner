package project2;

public class Cell {
	private int x;
	private int y;
	private boolean mine;
	private int clue;
	private int safeFound=0;
	private int minesFound=0;
	private int hidden=8;
	
  
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	    
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public int getx() {
		return x;
	}
	
	public int gety() {
		return y;
	}
	
	public boolean isMine() {
		return mine;
	}
	
	public void setMine(boolean input) {
		mine=input;
	}
	
	public int getClue() {
		return clue;
	}
	
	public void setClue(int input) {
		clue=input;
	}
	
	public int getSafeFound() {
		return safeFound;
	}
	
	public void setSafeFound(int input) {
		safeFound=input;
	}
	
	public int getMinesFound() {
		return minesFound;
	}
	
	public void setMinesFound(int input) {
		minesFound=input;
	}
	
	public int getHidden() {
		return hidden;
	}
	
	public void setHidden(int input) {
		hidden=input;
	}
	
	@Override
	public boolean equals(Object o) {    	
		if(o != null && o instanceof Cell) {
			if(this.x == ((Cell)o).x){
				if(this.y == ((Cell)o).y) 
					return true;
				}
			}
		return false; 
	}
}
