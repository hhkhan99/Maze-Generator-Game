package scool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Proj4 {
	// main method
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		 
	    System.out.println("Enter total rows for the maze you need: ");
	    int rows = input.nextInt();
	     
	    System.out.println("Enter total columns for the maze you need: ");
	    int cols = input.nextInt();
		 
	    System.out.println("Maze: ");
	    Maze maze = new Maze();// create a new Maze instance m
	    maze.createMaze(rows, cols);
	     
	    input.close();
	}
	
// createMaze method creates the maze using "DisjSets" class
public void createMaze(int rows, int cols) {
		
		int total = rows*cols;
		DisjSets dSet = new DisjSets(total); //Create an object of the disjoint set class
		 
		List<List<Node>> m = new ArrayList<List<Node>>();
		 
		m = initMaze(rows, cols, m); //Initialize the maze
		 
		Random rand = new Random(); // rand object to randomly select the cell
		 
		while(dSet.find(0) != dSet.find(total - 1)) { //while the start is not equal to end keep looping
			 
			int  currentRow  = rand.nextInt(rows); //Random row number between 0 - rows
			int  currentCol  = rand.nextInt(cols); //Random column number between 0 - cols
			Node currentCell = m.get(currentRow).get(currentCol); //Current cell by using the Node class
			int currentValue = currentCell.val;
			 
			int currRoot = dSet.find(currentValue); //call find on current cell
			int nextRoot;
			boolean removeHorizontal = false;//boolean value to remove the horizontal walls
			 
			if(currentValue == total - 1) {
				continue; //if the current cell is the right most cell continue without any operation
			}
			 
			if(currentRow == rows - 1) {//if its last row only break the right wall 
				nextRoot = dSet.find(currentValue + 1);
			} 
			else if(currentCol == cols - 1) {//if its last column only can break the bottom wall
				nextRoot = dSet.find(currentValue + cols);
				removeHorizontal = true;
			} else { //Randomly select either right wall or bottom wall to break
				boolean rightWall;
				rightWall = rand.nextBoolean();
				 
				if(rightWall) {
					nextRoot = dSet.find(currentValue + 1);
				} else {
					nextRoot = dSet.find(currentValue + cols);
					removeHorizontal = true;
				}
			}

			if(currRoot != nextRoot) {
				 
				dSet.union(currRoot, nextRoot); //union the two sets
				 
				if(removeHorizontal) {
					currentCell.horizontal = false; //Break the horizontal wall
				} else {
					currentCell.vertical = false; //Break the vertical wall
				}
			}
		}
		
		//make the last bottom right cell open 
		Node destination = m.get(rows - 1).get(cols - 1);
		destination.horizontal = false;
		destination.vertical = false;
		 
		generateMaze(rows, cols, m); //Print maze to the console
	}

	
// To initialize the maze 
	private List<List<Node>> initMaze(int rows, int cols, List<List<Node>> m) {
		 
		int count = 0;
		for(int i=0; i<rows; i++) {
			List<Node> row = new ArrayList<>();
			for(int j=0; j<cols; j++) {
				row.add(new Node(count++, true, true)); //Stores current index as value of the node
			}
			m.add(row);
		}
		 
		return m;
	}
	
//generateMaze method prints the maze to the console
	private void generateMaze(int rows, int cols, List<List<Node>> m) {
		
		System.out.print("   "); //Print a space on top left corner
		for(int j=1; j<cols; j++) {
			System.out.print(" __"); //Print the top first line
		}
		 
		System.out.println();
		 
		for(int i=0; i<rows; i++) {
			 
			if(i == 0) {
				System.out.print(" "); //top left corner open
			} else {
				System.out.print("|"); //Print the first line from the left
			}
			 
			for(int j=0; j<cols; j++) {
				 
				if(m.get(i).get(j).horizontal) {// if horizontal == true then dont brake horizontal wall
					System.out.print("__"); //Horizontal maze wall not broken
				} else {
					System.out.print("  "); //Horizontal maze wall broken
				}
				 
				if(m.get(i).get(j).vertical) {// if vertical == true then dont brake vertical wall
					System.out.print("|"); //Vertical maze wall not broken
				} else {
					System.out.print(" "); //Vertical maze wall broken
				}
			}
			 
			System.out.println();
		}
		 
	}
	
// class to store a particular cell
	private class Node {
		int val;
		boolean horizontal;
		boolean vertical;
		 
		Node(int value, boolean horizontal, boolean vertical) {
			this.val = value;
			this.horizontal = horizontal;
			this.vertical = vertical;
		}
	}
	



}
//unmodified DisjSets class from the Textbook
class DisjSets
{
    /**
     * Construct the disjoint sets object.
     * @param numElements the initial number of disjoint sets.
     */
    public DisjSets( int numElements )
    {
        s = new int [ numElements ];
        for( int i = 0; i < s.length; i++ )
            s[ i ] = -1;
    }

    /**
     * Union two disjoint sets using the height heuristic.
     * For simplicity, we assume root1 and root2 are distinct
     * and represent set names.
     * @param root1 the root of set 1.
     * @param root2 the root of set 2.
     */
    public void union( int root1, int root2 )
    {
        if( s[ root2 ] < s[ root1 ] )  // root2 is deeper
            s[ root1 ] = root2;        // Make root2 new root
        else
        {
            if( s[ root1 ] == s[ root2 ] )
                s[ root1 ]--;          // Update height if same
            s[ root2 ] = root1;        // Make root1 new root
        }
    }

    /**
     * Perform a find with path compression.
     * Error checks omitted again for simplicity.
     * @param x the element being searched for.
     * @return the set containing x.
     */
    public int find( int x )
    {
        if( s[ x ] < 0 )
            return x;
        else
            return s[ x ] = find( s[ x ] );
    }

    private int [ ] s;


   
}
