import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class project1 {
	
	static class Cell {
	    public int x;
	    public int y;
	    public boolean wall;
	    
	    public Cell(int x, int y) {
	        this.x = x;
	        this.y = y;
	        wall=false;
	    }
	}
	
	/**
	 * Generates a random maze of dimensions size by size and probability of any one space 
	 * being a wall equal to prob
	 * 1 represents a wall and 0 represents an open space.
	 */
	public static Cell[][] generateMaze(int size, double prob) {
		Cell grid[][] = new Cell[size][size];
		for(int i = 0; i<size;i++) {
			for(int j = 0; j<size;j++) {
				grid[i][j] = new Cell(i,j);
			}
		}

		
		Random rand = new Random();
		
		for(int i = 0; i<size;i++) {
			for(int j = 0; j<size;j++) {
				if(rand.nextDouble()<prob)
					grid[i][j].wall=true;

			}
		}
		grid[0][0].wall=false;
		grid[size-1][size-1].wall=false;
		return grid;
	}
	
	public static boolean searchDFS(Cell[][] maze){
		//List<String> path = new ArrayList<String>();
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		@SuppressWarnings("unused")
		int loopIter=0;
		
		Stack<Cell> fringe = new Stack<Cell>();
		fringe.add(maze[0][0]);
		Cell state=null;
		while (fringe.size()>0) {
			loopIter++;
			state=fringe.pop();
			if(state.x==maze.length-1 && state.y==maze.length-1)
				break;
			else if(!closed_set.contains(state)){
				if(state.x != maze.length-1)
					if(maze[state.x+1][state.y].wall == false)
						fringe.add(maze[state.x+1][state.y]);
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false)
						fringe.add(maze[state.x-1][state.y]);
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false)
						fringe.add(maze[state.x][state.y+1]);
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false)
						fringe.add(maze[state.x][state.y-1]);
				
				closed_set.add(state);
			}
			if(fringe.size() > maxFringe)
				maxFringe = fringe.size();
		}
		if(state.x==maze.length-1 && state.y==maze.length-1)
			return true;
		return false;
	} 
	
	public static boolean searchBFS(Cell[][] maze){
		//List<String> path = new ArrayList<String>();
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		@SuppressWarnings("unused")
		int loopIter=0;
		
		Queue<Cell> fringe = new LinkedList<Cell>();
		fringe.add(maze[0][0]);
		Cell state=null;
		while (fringe.size()>0) {
			loopIter++;
			state=fringe.poll();
			if(state.x==maze.length-1 && state.y==maze.length-1)
				break;
			else if(!closed_set.contains(state)){
				if(state.x != maze.length-1)
					if(maze[state.x+1][state.y].wall == false)
						fringe.add(maze[state.x+1][state.y]);
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false)
						fringe.add(maze[state.x-1][state.y]);
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false)
						fringe.add(maze[state.x][state.y+1]);
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false)
						fringe.add(maze[state.x][state.y-1]);
				
				closed_set.add(state);
			}
			if(fringe.size() > maxFringe)
				maxFringe = fringe.size();
		}
		if(state.x==maze.length-1 && state.y==maze.length-1)
			return true;
		return false;
	} 
	
	public static void main(String []args) {
		int size = 5;
		Cell[][] maze = generateMaze(size, .4);
		
		for(int i = 0; i<size; i++){
		    for(int j = 0; j<size; j++){
		    	if(maze[i][j].wall==true)
		    		System.out.print("1 ");
		    	else
		    		System.out.print("0 ");
		    }
		    System.out.println();
		}
		
		System.out.println(searchDFS(maze));
		System.out.println(searchBFS(maze));
		
	}
}
