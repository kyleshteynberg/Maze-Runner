import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class project1 {
	
	static class Cell {
	    public int x;
	    public int y;
	    public boolean wall;
	    public double h_euclidean;
	    public int h_manhatten;
	    public String path;
	    
	    public Cell(int x, int y) {
	        this.x = x;
	        this.y = y;
	        wall=false;
	    }
	}
	
	static class compareEuclidian implements Comparator<Cell> 
	{ 
	    public int compare(Cell a, Cell b) 
	    { 
	        if (a.h_euclidean-b.h_euclidean>0)
	        	return 1;
	        if (a.h_euclidean-b.h_euclidean<0)
	        	return -1;
	        return 0;
	    }

	} 
	static class compareManhatten implements Comparator<Cell> 
	{ 
	    public int compare(Cell a, Cell b) 
	    { 
	        return a.h_manhatten-b.h_manhatten;
	    }

	} 
	
	/**
	* Provides heuristic based on Euclidean distance between the current state and goal state
	**/
	public static double heuristicEuclidian(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2)^2 + (y1 - y2)^2);
	}
	
	/**
	* Provides heuristic based on Manhatten distance between the current state and goal state
	**/
	public static int heuristicManhatten(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2) + Math.abs(y1-y2);
	}
	
	/**
	 * Generates a random maze of dimensions size by size and probability of any one space being a wall equal to prob
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
	
	/**
	 * Determines a feasible path through a maze using DFS search algorithm
	 * Also tracks the fringe size at it's largest point
	 * and how many nodes are checked before finding the goal
	 */
	public static boolean searchDFS(Cell[][] maze){
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		int loopIter=0;
		
		Stack<Cell> fringe = new Stack<Cell>();
		maze[0][0].path = "(0,0)";
		fringe.add(maze[0][0]);
		Cell state=null;
		while (fringe.size()>0) {
			state=fringe.pop();
			if(state.x==maze.length-1 && state.y==maze.length-1)
				break;
			else if(!closed_set.contains(state)){
				loopIter++;
				if(state.x != maze.length-1)
					if(maze[state.x+1][state.y].wall == false) {
						maze[state.x+1][state.y].path = maze[state.x][state.y].path + ", (" + maze[state.x+1][state.y].x + ", " + maze[state.x+1][state.y].y + ")";
						fringe.add(maze[state.x+1][state.y]);
						}
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false) {
						maze[state.x-1][state.y].path = maze[state.x][state.y].path + ", (" + maze[state.x-1][state.y].x + ", " + maze[state.x-1][state.y].y + ")";
						fringe.add(maze[state.x-1][state.y]);
						}
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false) {
						maze[state.x][state.y+1].path = maze[state.x][state.y].path + ", (" + maze[state.x][state.y+1].x + ", " + maze[state.x][state.y+1].y + ")";
						fringe.add(maze[state.x][state.y+1]);
						}
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false) {
						maze[state.x][state.y-1].path = maze[state.x][state.y].path + ", (" + maze[state.x][state.y-1].x + ", " + maze[state.x][state.y-1].y + ")";
						fringe.add(maze[state.x][state.y-1]);
						}
				
				closed_set.add(state);
			}
			if(fringe.size() > maxFringe)
				maxFringe = fringe.size();
		}
		System.out.println("DFS Nodes Checked: " + loopIter);
		if(state.x==maze.length-1 && state.y==maze.length-1) {
			System.out.println("DFS Path: " + state.path);
			return true;
		}
		return false;
	} 
	
	/**
	 * Determines a feasible path through a maze using BFS search algorithm
	 * Also tracks the fringe size at it's largest point
	 * and how many nodes are checked before finding the goal
	 */
	public static boolean searchBFS(Cell[][] maze){
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		int loopIter=0;
		
		Queue<Cell> fringe = new LinkedList<Cell>();
		fringe.add(maze[0][0]);
		Cell state=null;
		while (fringe.size()>0) {
			state=fringe.poll();
			if(state.x==maze.length-1 && state.y==maze.length-1)
				break;
			else if(!closed_set.contains(state)){
				loopIter++;
				if(state.x != maze.length-1)
					if(maze[state.x+1][state.y].wall == false) {
						maze[state.x+1][state.y].path = maze[state.x][state.y].path + ", (" + maze[state.x+1][state.y].x + ", " + maze[state.x+1][state.y].y + ")";
						fringe.add(maze[state.x+1][state.y]);
					}
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false) {
						maze[state.x-1][state.y].path = maze[state.x][state.y].path + ", (" + maze[state.x-1][state.y].x + ", " + maze[state.x-1][state.y].y + ")";
						fringe.add(maze[state.x-1][state.y]);
					}
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false) {
						maze[state.x][state.y+1].path = maze[state.x][state.y].path + ", (" + maze[state.x][state.y+1].x + ", " + maze[state.x][state.y+1].y + ")";
						fringe.add(maze[state.x][state.y+1]);
					}
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false) {
						maze[state.x][state.y-1].path = maze[state.x][state.y].path + ", (" + maze[state.x][state.y-1].x + ", " + maze[state.x][state.y-1].y + ")";
						fringe.add(maze[state.x][state.y-1]);
					}
				
				closed_set.add(state);
			}
			if(fringe.size() > maxFringe)
				maxFringe = fringe.size();
		}
		System.out.println("BFS Nodes Checked: " + loopIter);
		if(state.x==maze.length-1 && state.y==maze.length-1) {
			System.out.println("BFS Path: " + state.path);
			return true;
		}
		return false;
	} 
	
	/**
	 * Determines a feasible path through a maze using A* search algorithm and Euclidian Distance as a heuristic
	 * Also tracks the fringe size at it's largest point
	 * and how many nodes are checked before finding the goal
	 */
	public static boolean searchAstarEuclidian(Cell[][] maze){
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		int loopIter=0;
		
		PriorityQueue<Cell> fringe = new PriorityQueue<Cell>(new compareEuclidian());
		fringe.add(maze[0][0]);
		Cell state=null;
		while (fringe.size()>0) {
			state=fringe.poll();
			if(state.x==maze.length-1 && state.y==maze.length-1)
				break;
			else if(!closed_set.contains(state)){
				loopIter++;
				if(state.x != maze.length-1)
					if(maze[state.x+1][state.y].wall == false) {
						maze[state.x+1][state.y].h_euclidean=heuristicEuclidian(maze[state.x+1][state.y].x,maze[state.x+1][state.y].y,maze.length-1,maze.length-1);
						maze[state.x+1][state.y].path = maze[state.x][state.y].path + ", (" + maze[state.x+1][state.y].x + ", " + maze[state.x+1][state.y].y + ")";
						fringe.add(maze[state.x+1][state.y]);
						}
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false) {
						maze[state.x-1][state.y].h_euclidean=heuristicEuclidian(maze[state.x-1][state.y].x,maze[state.x-1][state.y].y,maze.length-1,maze.length-1);
						maze[state.x-1][state.y].path = maze[state.x][state.y].path + ", (" + maze[state.x-1][state.y].x + ", " + maze[state.x-1][state.y].y + ")";
						fringe.add(maze[state.x-1][state.y]);
						}
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false) {
						maze[state.x][state.y+1].h_euclidean=heuristicEuclidian(maze[state.x][state.y+1].x,maze[state.x][state.y+1].y,maze.length-1,maze.length-1);
						maze[state.x][state.y+1].path = maze[state.x][state.y].path + ", (" + maze[state.x][state.y+1].x + ", " + maze[state.x][state.y+1].y + ")";
						fringe.add(maze[state.x][state.y+1]);
						}
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false) {
						maze[state.x][state.y-1].h_euclidean=heuristicEuclidian(maze[state.x][state.y-1].x,maze[state.x][state.y-1].y,maze.length-1,maze.length-1);
						maze[state.x][state.y-1].path = maze[state.x][state.y].path + ", (" + maze[state.x][state.y-1].x + ", " + maze[state.x][state.y-1].y + ")";
						fringe.add(maze[state.x][state.y-1]);
						}
				
				closed_set.add(state);
			}
			if(fringe.size() > maxFringe)
				maxFringe = fringe.size();
		}
		System.out.println("Euclidian A* Search Nodes Checked: " + loopIter);
		if(state.x==maze.length-1 && state.y==maze.length-1) {
			System.out.println("Euclidian A* Search Path:" + state.path);
			return true;
			}
		return false;
	} 
	
	/**
	 * Determines a feasible path through a maze using A* search algorithm and Manhatten Distance as a heuristic
	 * Also tracks the fringe size at it's largest point
	 * and how many nodes are checked before finding the goal
	 */
	public static boolean searchAstarManhatten(Cell[][] maze){
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		int loopIter=0;
		
		PriorityQueue<Cell> fringe = new PriorityQueue<Cell>(new compareManhatten());
		fringe.add(maze[0][0]);
		Cell state=null;
		while (fringe.size()>0) {
			state=fringe.poll();
			if(state.x==maze.length-1 && state.y==maze.length-1)
				break;
			else if(!closed_set.contains(state)){
				loopIter++;
				if(state.x != maze.length-1)
					if(maze[state.x+1][state.y].wall == false) {
						maze[state.x+1][state.y].h_manhatten=heuristicManhatten(maze[state.x+1][state.y].x,maze[state.x+1][state.y].y,maze.length-1,maze.length-1);
						maze[state.x+1][state.y].path = maze[state.x][state.y].path + ", (" + maze[state.x+1][state.y].x + ", " + maze[state.x+1][state.y].y + ")";
						fringe.add(maze[state.x+1][state.y]);
						}
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false) {
						maze[state.x-1][state.y].h_manhatten=heuristicManhatten(maze[state.x-1][state.y].x,maze[state.x-1][state.y].y,maze.length-1,maze.length-1);
						maze[state.x-1][state.y].path = maze[state.x][state.y].path + ", (" + maze[state.x-1][state.y].x + ", " + maze[state.x-1][state.y].y + ")";
						fringe.add(maze[state.x-1][state.y]);
						}
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false) {
						maze[state.x][state.y+1].h_manhatten=heuristicManhatten(maze[state.x][state.y+1].x,maze[state.x][state.y+1].y,maze.length-1,maze.length-1);
						maze[state.x][state.y+1].path = maze[state.x][state.y].path + ", (" + maze[state.x][state.y+1].x + ", " + maze[state.x][state.y+1].y + ")";
						fringe.add(maze[state.x][state.y+1]);
						}
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false) {
						maze[state.x][state.y-1].h_manhatten=heuristicManhatten(maze[state.x][state.y-1].x,maze[state.x][state.y-1].y,maze.length-1,maze.length-1);
						maze[state.x][state.y-1].path = maze[state.x][state.y].path + ", (" + maze[state.x][state.y-1].x + ", " + maze[state.x][state.y-1].y + ")";
						fringe.add(maze[state.x][state.y-1]);
						}
				
				closed_set.add(state);
			}
			if(fringe.size() > maxFringe)
				maxFringe = fringe.size();
		}
		System.out.println("Manhatten A* Search Nodes Checked: " + loopIter);
		if(state.x==maze.length-1 && state.y==maze.length-1) {
			System.out.println("Manhatten A* Search Path:" + state.path);
			return true;
			}
		return false;
	} 
	
	public static void main(String []args) {
		int size = 5;
		Cell[][] maze = generateMaze(size, .3);
		
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
		System.out.println(searchAstarEuclidian(maze));
		System.out.println(searchAstarManhatten(maze));
		
	}
}
