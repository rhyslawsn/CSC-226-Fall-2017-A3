/* ShortestPaths.java
   CSC 226 - Fall 2017
      
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java ShortestPaths
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java ShortestPaths file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.


   B. Bird - 08/02/2014
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;

class NodeHeapElement {
	public int n, weight;

	public NodeHeapElement(int n){
		this.n = n;
		this.weight = 999999;
	}
}

class NodeHeap {
	//Array based nodeHeap
	public NodeHeapElement[] nodeHeap;

	//nodeHeap[some i in indexes].n == n
	public int[] indexes;

	//Index of last valid element in nodeHeap
	private int heapEnd;

	//Initialize nodeHeap
	public NodeHeap(int size){
		nodeHeap = new NodeHeapElement[size + 1];
		heapEnd = size;
		indexes = new int[size + 1];

		for (int i = 0; i < size; i++){
			nodeHeap[i + 1] = new NodeHeapElement(i);
			indexes[i] = i + 1;
		}
	}

	//Size of nodeHeap
	public int size(){
		return heapEnd;
	}

	private void heapSwap(int i, int j) {
		NodeHeapElement elemi = nodeHeap[i];
		NodeHeapElement elemj = nodeHeap[j];

		nodeHeap[j] = elemi;
		nodeHeap[i] = elemj;

		indexes[nodeHeap[j].n] = j;
		indexes[nodeHeap[i].n] = i;
	}

	private void downHeap(int i) {
		if (2*i > heapEnd) {
			return;
		}

		//If two children, choose smallest
		int leftIndex = 2*i;
		int leftWeight = nodeHeap[leftIndex].weight;

		int rightIndex = 2*i+1;
		int rightWeight = 999999;

		if (rightIndex <= heapEnd) {
			rightWeight = nodeHeap[rightIndex].weight;
		}

		int minChildIndex = leftIndex;

		if (leftWeight > rightWeight) {
			minChildIndex = rightIndex;
		}if (nodeHeap[i].weight < nodeHeap[minChildIndex].weight) {
			return;
		}

		heapSwap(i, minChildIndex);
		downHeap(minChildIndex);
	}

	private void upHeap(int i) {
		//Parent is floor i/2
		if (Math.floor(i/2) < 1) {
			return;
		}

		//If two children, choose smallest
		int parentIndex = (int) Math.floor(i/2);

		if (nodeHeap[i].weight > nodeHeap[parentIndex].weight) {
			return;
		}

		heapSwap(i, parentIndex);
		upHeap(parentIndex);
	}

	//Remove and return the minimum element
	public NodeHeapElement removeMin(){
		NodeHeapElement removeElem = nodeHeap[1];
		heapSwap(1, heapEnd);
		heapEnd--;
		downHeap(1);
		return removeElem;
	}

	//Return the current weight of a node
	public int getWeight(int vertex){
		int heapIndex = indexes[vertex];
		NodeHeapElement e = nodeHeap[heapIndex];
		return e.weight;
	}

	//Set the weight of node oldNodeWeight to newNodeWeight
	public void adjust(int oldNodeWeight, int newNodeWeight){

		int oldNodeIndex = indexes[oldNodeWeight];
		NodeHeapElement adjustElement = nodeHeap[oldNodeIndex];
		if (newNodeWeight > adjustElement.weight)
			throw new Error();
		adjustElement.weight = newNodeWeight;
		
		upHeap(oldNodeIndex);
	}
}

class Data {
	int index;
	boolean scanned;
	ArrayList<Integer> nodeNeighbors;

	Data(int index, ArrayList<Integer> nodeNeighbors) {
		this.index = index;
		this.nodeNeighbors = nodeNeighbors;
		this.scanned = false;
	}
}

//Do not change the name of the ShortestPaths class
public class ShortestPaths{

    //TODO: Your code here   
    public static int numNodes;
	/* ShortestPaths(G) 
	   Given an adjacency matrix for graph G, calculates and stores the
	   shortest paths to all the vertices from the source vertex.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static void ShortestPaths(int[][] G, int source){
		numNodes = G.length;
		int min;
		int totalWeight = 0;

		// Initialize
		Data[] data = new Data[numNodes];
		NodeHeap queue = new NodeHeap(numNodes);
	}
        
    static void PrintPaths(int source){
       //TODO: Your code here   
    }
        
		
	/* main()
	   Contains code to test the ShortestPaths function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args) throws FileNotFoundException{
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			ShortestPaths(G, 0);
                        PrintPaths(0);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			//System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}