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
	    //pathLength only used in BFS because BFS is ensured to get the shortest route
	    public int pathLength;
	    
	    public Cell(int x, int y) {
	        this.x = x;
	        this.y = y;
	        wall=false;
	    }
	    
	    public String toString() {
	    	return "(" + x + "," + y + ")";
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
	 * Decides whether two graphs have an intersecting vertex 
	 */
	public static boolean intersectingGraphs(Cell[] a, Cell[] b) {
		
		if(a.length == 0 || b.length == 0) {
			return false;
		}
		
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < b.length; j++) {
				if((a[i].x == b[j].x) && (a[i].y == b[j].y)) {
					return true;
				}
			}
		}
		
		return false;
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
	 * Returns: True is maze is solvable and false if it is not
	 */
	public static boolean searchDFS(Cell[][] maze){
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		int loopIter=0;
		
		Stack<Cell> fringe = new Stack<Cell>();
		fringe.add(maze[0][0]);
		maze[0][0].path=maze[0][0].toString();
		Cell state=null;
		while (fringe.size()>0) {
			state=fringe.pop();
			if(state.x==maze.length-1 && state.y==maze.length-1)
				break;
			else if(!closed_set.contains(state)){
				loopIter++;
				if(state.x != maze.length-1)
					if(maze[state.x+1][state.y].wall == false) {
						maze[state.x+1][state.y].path = maze[state.x][state.y].path + ", " + maze[state.x+1][state.y].toString();
						fringe.add(maze[state.x+1][state.y]);
						}
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false) {
						maze[state.x-1][state.y].path = maze[state.x][state.y].path + ", " + maze[state.x-1][state.y].toString();
						fringe.add(maze[state.x-1][state.y]);
						}
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false) {
						maze[state.x][state.y+1].path = maze[state.x][state.y].path + ", " + maze[state.x][state.y+1].toString();
						fringe.add(maze[state.x][state.y+1]);
						}
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false) {
						maze[state.x][state.y-1].path = maze[state.x][state.y].path + ", " + maze[state.x][state.y-1].toString();
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
	 * Returns: Shortest Path Length through maze. Returns 0 if maze is unsolvable.
	 */
	public static int searchBFS(Cell[][] maze){
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		int loopIter=0;
		
		Queue<Cell> fringe = new LinkedList<Cell>();
		fringe.add(maze[0][0]);
		maze[0][0].path=maze[0][0].toString();
		maze[0][0].pathLength=0;
		Cell state=null;
		while (fringe.size()>0) {
			state=fringe.poll();
			if(state.x==maze.length-1 && state.y==maze.length-1)
				break;
			else if(!closed_set.contains(state)){
				loopIter++;
				if(state.x != maze.length-1)
					if(maze[state.x+1][state.y].wall == false) {
						maze[state.x+1][state.y].path = maze[state.x][state.y].path + ", " + maze[state.x+1][state.y].toString();
						maze[state.x+1][state.y].pathLength = maze[state.x][state.y].pathLength+1;
						fringe.add(maze[state.x+1][state.y]);
					}
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false) {
						maze[state.x-1][state.y].path = maze[state.x][state.y].path + ", " + maze[state.x-1][state.y].toString();
						maze[state.x-1][state.y].pathLength = maze[state.x][state.y].pathLength+1;
						fringe.add(maze[state.x-1][state.y]);
					}
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false) {
						maze[state.x][state.y+1].path = maze[state.x][state.y].path + ", " + maze[state.x][state.y+1].toString();
						maze[state.x][state.y+1].pathLength = maze[state.x][state.y].pathLength+1;
						fringe.add(maze[state.x][state.y+1]);
					}
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false) {
						maze[state.x][state.y-1].path = maze[state.x][state.y].path + ", " + maze[state.x][state.y-1].toString();
						maze[state.x][state.y-1].pathLength = maze[state.x][state.y].pathLength+1;
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
			return maze[state.x][state.y].pathLength;
		}
		return 0;
	} 
	
	/**
	 * Determines a feasible path through a maze using A* search algorithm and Euclidian Distance as a heuristic
	 * Also tracks the fringe size at it's largest point
	 * and how many nodes are checked before finding the goal
	 * Returns: Number of Nodes searched expanded upon
	 */
	public static int searchAstarEuclidian(Cell[][] maze){
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		int loopIter=0;
		
		PriorityQueue<Cell> fringe = new PriorityQueue<Cell>(new compareEuclidian());
		fringe.add(maze[0][0]);
		maze[0][0].path=maze[0][0].toString();
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
						maze[state.x+1][state.y].path = maze[state.x][state.y].path + ", " + maze[state.x+1][state.y].toString();
						fringe.add(maze[state.x+1][state.y]);
						}
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false) {
						maze[state.x-1][state.y].h_euclidean=heuristicEuclidian(maze[state.x-1][state.y].x,maze[state.x-1][state.y].y,maze.length-1,maze.length-1);
						maze[state.x-1][state.y].path = maze[state.x][state.y].path + ", " + maze[state.x-1][state.y].toString();
						fringe.add(maze[state.x-1][state.y]);
						}
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false) {
						maze[state.x][state.y+1].h_euclidean=heuristicEuclidian(maze[state.x][state.y+1].x,maze[state.x][state.y+1].y,maze.length-1,maze.length-1);
						maze[state.x][state.y+1].path = maze[state.x][state.y].path + ", " + maze[state.x][state.y+1].toString();
						fringe.add(maze[state.x][state.y+1]);
						}
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false) {
						maze[state.x][state.y-1].h_euclidean=heuristicEuclidian(maze[state.x][state.y-1].x,maze[state.x][state.y-1].y,maze.length-1,maze.length-1);
						maze[state.x][state.y-1].path = maze[state.x][state.y].path + ", " + maze[state.x][state.y-1].toString();
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
			return loopIter;
			}
		return loopIter;
	} 
	
	/**
	 * Determines a feasible path through a maze using A* search algorithm and Manhatten Distance as a heuristic
	 * Also tracks the fringe size at it's largest point
	 * and how many nodes are checked before finding the goal
	 * Returns: Number of Nodes searched expanded upon
	 */
	public static int searchAstarManhatten(Cell[][] maze){
		List<Cell> closed_set = new ArrayList<Cell>();
		int maxFringe = 0;
		int loopIter=0;
		
		PriorityQueue<Cell> fringe = new PriorityQueue<Cell>(new compareManhatten());
		fringe.add(maze[0][0]);
		maze[0][0].path=maze[0][0].toString();
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
						maze[state.x+1][state.y].path = maze[state.x][state.y].path + ", " + maze[state.x+1][state.y].toString();
						fringe.add(maze[state.x+1][state.y]);
						}
				if(state.x != 0)
					if(maze[state.x-1][state.y].wall == false) {
						maze[state.x-1][state.y].h_manhatten=heuristicManhatten(maze[state.x-1][state.y].x,maze[state.x-1][state.y].y,maze.length-1,maze.length-1);
						maze[state.x-1][state.y].path = maze[state.x][state.y].path + ", " + maze[state.x-1][state.y].toString();
						fringe.add(maze[state.x-1][state.y]);
						}
				if(state.y != maze.length-1)
					if(maze[state.x][state.y+1].wall == false) {
						maze[state.x][state.y+1].h_manhatten=heuristicManhatten(maze[state.x][state.y+1].x,maze[state.x][state.y+1].y,maze.length-1,maze.length-1);
						maze[state.x][state.y+1].path = maze[state.x][state.y].path + ", " + maze[state.x][state.y+1].toString();
						fringe.add(maze[state.x][state.y+1]);
						}
				if(state.y != 0)
					if(maze[state.x][state.y-1].wall == false) {
						maze[state.x][state.y-1].h_manhatten=heuristicManhatten(maze[state.x][state.y-1].x,maze[state.x][state.y-1].y,maze.length-1,maze.length-1);
						maze[state.x][state.y-1].path = maze[state.x][state.y].path + ", " + maze[state.x][state.y-1].toString();
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
			return loopIter;
			}
		return loopIter;
	} 
	
	public static boolean biDirectionalBFS(Cell[][] maze) {
		Cell[][] maze_a = new Cell[maze.length][maze.length];
		Cell[][] maze_b = new Cell[maze.length][maze.length];
		
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze.length; j++) {
				maze_a[i][j] = new Cell(maze[i][j].x, maze[i][j].y);
				maze_a[i][j].wall = maze[i][j].wall; 
				maze_b[i][j] = new Cell(maze[i][j].x, maze[i][j].y);
				maze_b[i][j].wall = maze[i][j].wall; 
			}
		}
		
			
		List<Cell> visited_a = new ArrayList<Cell>();
		List<Cell> visited_b = new ArrayList<Cell>();
		
		int maxFringe = 0;
		int loopIter=0;
		int mazeLength = maze.length-1; 
		
		Queue<Cell> fringe_a = new LinkedList<Cell>();
		Queue<Cell> fringe_b = new LinkedList<Cell>();
		
		fringe_a.add(maze_a[0][0]);
		fringe_b.add(maze_b[mazeLength][mazeLength]);
	
		maze_a[0][0].path=maze_a[0][0].toString();
		maze_b[mazeLength][mazeLength].path = maze_b[mazeLength][mazeLength].toString();
		
		
		Cell state_a = null;
		Cell state_b = null; 
		
		while (fringe_a.size()>0 && fringe_b.size()>0) {
			state_a = fringe_a.poll();
			state_b = fringe_b.poll();
	
			
			//Both current nodes are the same, meaning the bidirectional path met. 
			if(state_a.x==state_b.x && state_a.y==state_b.y) {
				visited_a.add(state_a);
				visited_b.add(state_b);
				break;
			}
			
			if(visited_a.contains(state_a) && visited_b.contains(state_a)) 
				break;
			if(visited_a.contains(state_b) && visited_b.contains(state_b)) 
				break;
			
			
			
			if(!visited_a.contains(state_a)){
				loopIter++;
				
				//Starting from source and working forward
				if(state_a.x != mazeLength)
					if(maze_a[state_a.x+1][state_a.y].wall == false && !visited_a.contains(maze_a[state_a.x+1][state_a.y])) {
						maze_a[state_a.x+1][state_a.y].path = maze_a[state_a.x][state_a.y].path + ", " + maze_a[state_a.x+1][state_a.y].toString();
						if(!fringe_a.contains(maze_a[state_a.x+1][state_a.y]))
							fringe_a.add(maze_a[state_a.x+1][state_a.y]);
					}
				if(state_a.x != 0)
					if(maze_a[state_a.x-1][state_a.y].wall == false && !visited_a.contains(maze_a[state_a.x-1][state_a.y])) {
						maze_a[state_a.x-1][state_a.y].path = maze_a[state_a.x][state_a.y].path + ", " + maze_a[state_a.x-1][state_a.y].toString();
						if(!fringe_a.contains(maze_a[state_a.x-1][state_a.y]))
							fringe_a.add(maze_a[state_a.x-1][state_a.y]);
					}
				if(state_a.y != mazeLength)
					if(maze_a[state_a.x][state_a.y+1].wall == false && !visited_a.contains(maze_a[state_a.x][state_a.y+1])) {
						maze_a[state_a.x][state_a.y+1].path = maze_a[state_a.x][state_a.y].path + ", " + maze_a[state_a.x][state_a.y+1].toString();
						if(!fringe_a.contains(maze_a[state_a.x][state_a.y+1]))
							fringe_a.add(maze_a[state_a.x][state_a.y+1]);
					}
				if(state_a.y != 0)
					if(maze_a[state_a.x][state_a.y-1].wall == false && !visited_a.contains(maze_a[state_a.x][state_a.y-1])) {
						maze_a[state_a.x][state_a.y-1].path = maze_a[state_a.x][state_a.y].path + ", " + maze_a[state_a.x][state_a.y-1].toString();
						if(!fringe_a.contains(maze_a[state_a.x][state_a.y-1]))
							fringe_a.add(maze_a[state_a.x][state_a.y-1]);
					}
				
				visited_a.add(state_a);      
			}
				
			
			if(visited_a.contains(state_b)) {
				visited_b.add(state_b);
				break; 
			}
			
			
			if(!visited_b.contains(state_b)) {
				loopIter++;
				//Starting from the goal and working backwards
				if(state_b.x != mazeLength)
					if(maze_b[state_b.x+1][state_b.y].wall == false && !visited_b.contains(maze_b[state_b.x+1][state_b.y])) {
						maze_b[state_b.x+1][state_b.y].path = maze_b[state_b.x][state_b.y].path + ", " + maze_b[state_b.x+1][state_b.y].toString();
						if(!fringe_b.contains(maze_b[state_b.x+1][state_b.y]))
							fringe_b.add(maze_b[state_b.x+1][state_b.y]);
					}
				if(state_b.x != 0)
					if(maze_b[state_b.x-1][state_b.y].wall == false && !visited_b.contains(maze_b[state_b.x-1][state_b.y])) {
						maze_b[state_b.x-1][state_b.y].path = maze_b[state_b.x][state_b.y].path + ", " + maze_b[state_b.x-1][state_b.y].toString();
						if(!fringe_b.contains(maze_b[state_b.x-1][state_b.y]))
							fringe_b.add(maze_b[state_b.x-1][state_b.y]);
					}
				if(state_b.y != mazeLength)
					if(maze_b[state_b.x][state_b.y+1].wall == false && !visited_b.contains(maze_b[state_b.x][state_b.y+1])) {
						maze_b[state_b.x][state_b.y+1].path = maze_b[state_b.x][state_b.y].path + ", " + maze_b[state_b.x][state_b.y+1].toString();
						if(!fringe_b.contains(maze_b[state_b.x][state_b.y+1]))
							fringe_b.add(maze_b[state_b.x][state_b.y+1]);
					}
				if(state_b.y != 0)
					if(maze_b[state_b.x][state_b.y-1].wall == false && !visited_b.contains(maze_b[state_b.x][state_b.y-1])) {
						maze_b[state_b.x][state_b.y-1].path = maze_b[state_b.x][state_b.y].path + ", " + maze_b[state_b.x][state_b.y-1].toString();
						if(!fringe_b.contains(maze_b[state_b.x][state_b.y-1]))
							fringe_b.add(maze_b[state_b.x][state_b.y-1]);
					}
				
				visited_b.add(state_b);
				
			}
			if(fringe_a.size() > maxFringe || fringe_b.size() > maxFringe)
				maxFringe = Math.max(fringe_a.size(), fringe_b.size());
		}
		System.out.println("BFS Nodes Checked: " + loopIter);
		if(visited_a.contains(state_a) && visited_b.contains(state_a)) {
			System.out.println("BFS Path: \nForward:" + maze_a[state_a.x][state_a.y].path + "\nBackward: " + maze_b[state_a.x][state_a.y].path);
			return true;
		}
		if(visited_a.contains(state_b) && visited_b.contains(state_b)){
			System.out.println("BFS Path: \nForward:" + maze_a[state_b.x][state_b.y].path + "\nBackward:" + maze_b[state_b.x][state_b.y].path);
			return true;
		}
		
		return false;
	}
	
	public static void main(String []args) {
		int size = 5;
		double prob = 0.3;
		//For testing
		Cell[][] maze = generateMaze(size, prob);
		
		for(int i = 0; i<size; i++){
		    for(int j = 0; j<size; j++){
		    	if(maze[i][j].wall==true) 
		    		System.out.print("1 ");
		    	else
		    		System.out.print("0 ");
		    }
		    System.out.println();
		}
		//System.out.println(searchDFS(maze));
		//System.out.println(searchBFS(maze));
		//System.out.println(searchAstarEuclidian(maze));
		//System.out.println(searchAstarManhatten(maze));
		//System.out.println(biDirectionalBFS(maze));
		
		
		
		//For testing different sizes and p-values over large amounts of mazes
	/*	int averageAstarManhatten = 0;
		int testLoops = 1000000;
		for(int i = 0; i<testLoops;i++) {
			maze=generateMaze(size, prob);
			if(searchAstarManhatten(maze))
				averageAstarManhatten++;
		}
		//System.out.println("DFS success: " + averageDFS +  "/" + testLoops);
		//System.out.println("DFS success: " + averageBFS +  "/" + testLoops);
		//System.out.println("DFS success: " + averageAstarEiclidian + "/" + testLoops);
		System.out.println("DFS success: " + averageAstarManhatten + "/" + testLoops);
		*/
		
		//For testing shortest path with different p-values over large amounts of mazes
		/*prob=0.7;
		int sumPathLength=0;
		double averagePathLength;
		int testLoops = 100000;
		int totalLoops=0;
		
		for(int j = 0; j<testLoops;j++) {
			maze=generateMaze(size, prob);
			int temp=searchBFS(maze);
			if(temp!=0) {
				sumPathLength+=searchBFS(maze);
				totalLoops++;
			}
		}*/
		/*prob=0;
		while(prob<=0.5) {
			for(int j = 0; j<testLoops;j++) {
				maze=generateMaze(size, prob);
				int temp=searchBFS(maze);
				if(temp!=0) {
					sumPathLength+=searchBFS(maze);
					totalLoops++;
				}
			}
			prob+=0.01;
		}
		averagePathLength = (double)sumPathLength/totalLoops;
		System.out.println("Average Path Length: " + averagePathLength);*/
		
		
		//For testing Euclidian Heuristics vs Manhattan Heuristics
		/*	double averageAstarManhatten = 0;
			int sumAstarManhatten=0;
			double averageAstarEuclidian = 0;
			int sumAstarEuclidian=0;
			int testLoops = 1000000;
			prob=0.7;
			for(int i = 0; i<testLoops;i++) {
				maze=generateMaze(size, prob);
				sumAstarEuclidian+=searchAstarEuclidian(maze);
				sumAstarManhatten+=searchAstarManhatten(maze);
			}
			averageAstarManhatten=(double)sumAstarManhatten/testLoops;
			averageAstarEuclidian=(double)sumAstarEuclidian/testLoops;
			System.out.println("Euclidian average Nodes expanded: " + averageAstarEuclidian);
			System.out.println("Manhatten average Nodes expanded: " + averageAstarManhatten);
			*/
	}
	
}
