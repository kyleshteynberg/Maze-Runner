
public class Player {
	
	static Cell[][] KB;
	static MineField environment;
	static int boom=0;
	
	private static void importMap(MineField minefield) {
		environment=minefield;
	}

	private static void openCell(Cell cell) {
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
	
	private static int solve(MineField map) {
		int score=0;
		return score;
	}
	
	public static void main(String []args) {
		importMap(new MineField(10, 5));
		solve(environment);
	}
}
