
public class Player {
	
	Cell[][] KB;
	static MineField environment;
	
	private static void importMap(MineField minefield) {
		environment=minefield;
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
