/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap {

	private HeapNode min;
	private HeapNode first;
	private int size;
	private static int numOfCuts = 0;
	private static int numOfLinks = 0;
	private int numOfTrees;
	private int numOfMarks;


	public FibonacciHeap() {
		this.min = null;
		this.first = null;
		this.size = 0;
	}

	public HeapNode getFirst() { //need to remove before submission only relevant for tests
		return this.first;
	}

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty() // Returns true if the size of heap equals 0 --> O(1)
    {
		return this.size == 0;
	}
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)  // Returns the node we inserted, creates a new node with given key, calls insertNode(HeapNode x) --> O(1)
    {   
    	HeapNode insertedNode = new HeapNode(key);
		insertNode(insertedNode);
    	return insertedNode; 
    }

	private void insertNode(HeapNode x) { //inserts a node as the first node, updates all relevant fields --> O(1)
		if (this.isEmpty()) { // tree is empty, initialize min and first
			this.min = x;
			this.first = x;
		}
		else {
			x.setNext(this.first);
			x.setPrev(this.first.prev);
			this.first.setPrev(x);
			x.getPrev().setNext(x);
			this.first = x; //sets the node as the first node of the heap
			if (x.getKey() < this.findMin().getKey()) { // updates minimal node if true
				this.min = x;
			}
		}
		numOfTrees++;
		updateSize(1);
	}

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin() //Deletes the minimal node of the heap, calls consolidate(HeapNode x)--> O(log(n))
    {
		if (this.isEmpty()) { //if tree is empty there is nothing to delete.
			return;
		}
		else if (this.size == 1) { // there is only one node in the heap.
			this.min = null;
			this.first = null;
		}
		else {
			HeapNode x = this.findMin().getChild();
			if (x == null){ //minimal node has no children
				this.findMin().getNext().setPrev(this.findMin().getPrev());
				this.findMin().getPrev().setNext(this.findMin().getNext());
				if (this.findMin() == this.first) {
					this.first = this.first.getNext();
				}
			}
			else { //need to change
				if (this.findMin() == this.first) {
					this.first = this.first.getChild();
				}
				else {
					this.findMin().getPrev().setNext(x); //connects the children of minimal node we deleted to --
					this.findMin().getNext().setPrev(x.getPrev()); //-- other roots without changing the order
					x.getPrev().setNext(this.findMin().getNext());
					x.setPrev(this.findMin().getPrev());
				}
				while (x.getParent() != null) { //sets parent of all the children of min to null (makes them roots)
					if (x.getMark() == 1) {
						x.setMark(0);
						numOfMarks--;
					}
					x.setParent(null);
					x = x.getNext();
				}
			}
			consolidate(this.first); //corrects the heap by preforming consolidation
		}
    	updateSize(-1);
    }

	private void consolidate(HeapNode x) { //Preforms consolidation on the heap calls
		// toBuckets(HeapNode x), romBuckets(HeapNode[] B) --> O(log(n))
		HeapNode[] B = toBuckets(x);
		fromBuckets(B);
	}

	private HeapNode[] toBuckets(HeapNode x) { //Distributes all trees to their appropriate "bucket", preforms linking as required --> O(log(n))
		int n =  (int) (2 * (Math.log10(this.size())/Math.log10(2)) + 1);
		HeapNode[] B = new HeapNode[n]; //creates the "buckets"
		x.getPrev().setNext(null);
		while (x != null) { //for each tree puts it in the correct bucket
			HeapNode currNode = x;
			x = x.getNext();
			while (B[currNode.getRank()] != null) { //if there is another tree in the bucket, preform link
				currNode = link(currNode, B[currNode.getRank()]);
				numOfLinks++;
				numOfTrees--;
				B[currNode.getRank() - 1] = null;
			}
			B[currNode.getRank()] = currNode;
		}
		return B;
	}

	private void fromBuckets(HeapNode[] B) { //collects all the trees from the buckets,
		//arranges them from the smallest to the largest and updates minimal node --> O(log(n))
		this.first = null;
		int counter = 0;
		for (int i = B.length - 1; i >= 0; i--){ //performed in reverse order so the heap is arranged in degrees in ascending order
			if (B[i] != null) { //if the bucket isn't empty, meaning there is a tree
				counter++;
				if (this.first == null) { //initializing the heap happens only when this.first == null
					this.first = B[i];
					this.min = this.first;
					this.first.setNext(this.first);
					this.first.setPrev(this.first);
				}
				else {
					insertNode(B[i]); //inserts the node
				}
			}
		}
		numOfTrees = counter; //updates number of trees in heap
	}

	private HeapNode link(HeapNode node1, HeapNode node2){ //Links two nodes from the same rank
		// calls linking(HeapNode x, HeapNode y) --> O(1)
		if (node1.getKey() < node2.getKey()) {
			return linking(node2 , node1);
		}
		return linking(node1 , node2);
	}

	private HeapNode linking(HeapNode x, HeapNode y) { //Preforms the link between two roots that have the same rank --> O(1)
		//Returns the node that remains the root after the linking process
		x.setParent(y);
		if (y.getChild() == null) {
			x.setPrev(x);
			x.setNext(x);
		}
		else { //y has children
			y.getChild().getPrev().setNext(x);
			x.setNext(y.getChild());
			x.setPrev(y.getChild().getPrev());
			y.getChild().setPrev(x);
		}
		y.setChild(x);
		y.UpdateRank(1);
		return y;
	}


   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin() { // Returns heap's minimal node --> O(1)
    	return this.min;
    }
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2) //Melds two heaps together --> O(1)
    {
		heap2.first.getPrev().setNext(this.first);
		this.first.getPrev().setNext(heap2.first);
		HeapNode connectingNode = this.first.getPrev();
		this.first.setPrev(heap2.first.getPrev());
		heap2.first.setPrev(connectingNode);
		if (this.findMin().getKey() > heap2.findMin().getKey()) { //updates minimal node
			this.min = heap2.min;
		}
		this.numOfTrees += heap2.numOfTrees; //updates the fields of melded heap
		this.numOfMarks += heap2.numOfMarks;
		this.size += heap2.size();
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size() { // Returns heap's size --> O(1)
    	return this.size;
    } //Returns the number of nodes in the heap --> O(1)
    	
    private void updateSize(int i) { //Updates the number of nodes in the heap --> O(1)
		this.size += i;
    }
    
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep() { //Returns an array with the number of trees of a certain rank in the heap.
		if (this.isEmpty()) { //if heap is empty, return an empty array.
			return new int[0];
		}
		int maxRank = 0; //finding the biggest rank of a root in heap
		HeapNode x = this.first;
		if (maxRank < x.getRank()) {
			maxRank = x.getRank();
		}
		x = x.getNext();
		while (x != this.first){
			int currRank = x.getRank();
			if (maxRank < currRank) {
				maxRank = currRank;
			}
			x = x.getNext();
		}
		int[] counter = new int[maxRank];
		counter[x.getRank()] += 1; // x = this.first
		x = x.getNext();
		while (x != this.first){ //updating number of trees of rank i in counter.
			counter[x.getRank()] += 1;
			x = x.getNext();
		}
        return counter;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x)  // Deletes the given node --> O(log(n))
    {
		cascadingCut(x); //cutting the node we want to delete from its parent
		this.min = x; //setting it momentarily as the minimal node (delete min corrects it)
		deleteMin();
    	updateSize(-1);
    }

	private void cascadingCut(HeapNode x){ //preforms a cascading cut process staring at x --> O(log(n)).
		HeapNode y = x.getParent();
		if (y == null){ //x is a root
			return;
		}
		cut(x,y);
		numOfTrees ++;
		numOfCuts ++;
		if (y.getParent() != null) { //parent isn't a root.
			if (y.getMark() == 0) {
				y.setMark(1);
				numOfMarks++;
			}
			cascadingCut(y);
		}
	}

	private void cut(HeapNode x, HeapNode y){ //cuts node x from its parent y --> O(1).
		x.setParent(null);
		x.setMark(0);
		y.UpdateRank(-1);
		if (x.getNext() == x) { //x is an only child
			y.setChild(null);
		}
		else {
			if (x.getParent() == x.getParent().getChild()){ //cutting "first" child of y
				y.setChild(x.getNext());
			}
			x.getPrev().setNext(x.getNext());
			x.getNext().setPrev(x.getPrev());
		}
		insertNode(x); //Inserts the nodes that's been cut into the heap.
	}

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta) //Decreases the key of the node x by a non-negative value delta --> O(log(n))
    {    
    	x.key = x.getKey() - delta;
		if (x.getKey() < x.getParent().getKey()) { //heap order violation, CUT THE NODE.
			cascadingCut(x);
		}
		if (this.findMin().getKey() > x.getKey()) { //update minimal node
			this.min = x;
		}
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() { //Returns the current potential of the heap --> O(1)
		return numOfTrees + (2 * numOfMarks);
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks() //Returns the number of links preformed during the run time of the program --> O(1)
    {    
    	return numOfLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts() //Returns the number of cuts preformed on the heap --> O(1).
    {    
    	return numOfCuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {
		int [] minKElms = new int[k];
		FibonacciHeap tempHeap = new FibonacciHeap(); // what is default package?
		tempHeap.insert(H.first.getKey()); //root of the forest
		for (int i = 0; i < k; i++){
			HeapNode currMin = tempHeap.findMin();
			minKElms[i] = currMin.getKey(); // adds the minimal node in temp heap to the array
			tempHeap.deleteMin();
			HeapNode child = currMin.getChild(); //taken from H?
			tempHeap.insert(child.getKey());
			child = child.getNext();
			while (child.getParent().getChild() != child) { //adds all relevant children to temp
				tempHeap.insert(child.getKey());
			}
		}
        return minKElms;
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
    	private int rank;
    	private int mark;  // 0 = unmarked , 1 = marked
    	private HeapNode child;
    	private HeapNode next;
    	private HeapNode prev;
    	private HeapNode parent; 
    	

    	public HeapNode(int key) {
    		this.key = key;
    		this.rank = 0;
    		this.mark = 0;
    		this.child = null;
    		this.next = this;
    		this.prev = this;
    		this.parent = null; 
    	}

    	public int getKey() { // Returns node's key --> O(1)
			return this.key;
    	}
    	
    	public int getRank() { // Returns node's rank --> O(1)
			return this.rank;
    	}
    	
    	public void UpdateRank(int k) { // Sets node's rank --> O(1)
    		this.rank += k;
    	}
    	
    	public int getMark() { // Return node's mark --> O(1)
    		return this.mark;
    	}

	   public boolean getMarked() { //need to remove before submission only relevant for tests
			if (getMark() == 0) {
				return false;
			}
			return true;
	   }
    	
    	public void setMark(int k) { // Sets node's mark, gets only 0 or 1 depending on whether child was cut or not --> O(1)
    		if ((k==0) || (k==1)) {
    			this.mark = k;
    		}
    	}
    	
    	public HeapNode getChild() { // Returns node's child --> O(1)
    		return this.child;
    	}
    	
    	public void setChild(HeapNode child) { // Sets node's child --> O(1)
    		this.child = child;
    	}
    	
    	public HeapNode getNext() { // Returns node's right neighbor --> O(1)
    		return this.next;
    	}
    	
    	public void setNext(HeapNode next) { // Sets node's right neighbor --> O(1)
    		this.next = next;
    	}
    	
    	public HeapNode getPrev() { // Returns node's left neighbor --> O(1)
    		return this.prev;
    	}
    	
    	public void setPrev(HeapNode prev) { // Sets node's left neighbor --> O(1)
    		this.prev = prev;
    	}
    	
    	public HeapNode getParent() { // Returns node's parent --> O(1)
    		return this.parent;
    	}
    	
    	public void setParent(HeapNode parent) { // Sets node's parent --> O(1)
    		this.parent = parent;
    	}
    }
}
