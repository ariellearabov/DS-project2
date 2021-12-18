/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode min;
	private HeapNode first;
	private int size;
	
	public FibonacciHeap() {
		this.min = null;
		this.first = null;
		this.size = 0;
	}
	
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty() // Returns true if the size of heap equals 0 --> O(1)
    {
    	if (this.size == 0) {
    		return true;
    		}
    	return false; 
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)  // Returns the node we inserted, inserts node with given key as first node, updates all relevant fields --> O(1)
    {   
    	HeapNode insertedNode = new HeapNode(key);
    	if (this.isEmpty()) { // tree is empty, intialize min and first
    		this.min = insertedNode;
    		this.first = insertedNode;
    	}
    	else {
    		insertedNode.setNext(this.first);
    		insertedNode.setPrev(this.first.prev);
    		this.first.setPrev(insertedNode);
    		insertedNode.prev.setNext(insertedNode);
    		this.first = insertedNode;
    		if (insertedNode.key < this.min.key) { // updates minimal node if true
    			this.min = insertedNode;
    		}
    	}
    	updateSize(1);
    	return insertedNode; 
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	// dont forget to update size
    	// dont forget to include edge case where tree is empty and when tree has only one node
     	return; // should be replaced by student code
     	
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin() // Returns heap's minimal node --> O(1)
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	// dont forget to update size
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return this.size; // should be replaced by student code
    }
    	
    private void updateSize(int i) { // Updates size of heap according to change in nodes --> O(1)
    	this.size += i;
    }
    
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) // Deletes the given node --> O(logn)
    {    
    	if (x == this.min) { // x is already the minimal node
    		deleteMin();
    	}
    	if (this.size == 1) { // only one node in heap
    		this.min = null;
    		this.first = null; 
    	}
    	else { 
    		decreaseKey(x,- x.key - Integer.MIN_VALUE); // waiting to get fixed when we know if min can be inf
        	deleteMin();
    	}
    	updateSize(-1);
    }
    
   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
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
    public int potential() 
    {    
    	
    	return 3;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return -345; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return -456; // should be replaced by student code
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
    	// dont forget to update size if needed
        int[] arr = new int[100];
        return arr; // should be replaced by student code
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
    		this.next = this; //make sure thats correct
    		this.prev = this;
    		this.parent = null; 
    	}

    	public int getKey() { // Returns node's key --> O(1)
    		return this.key;
    	}
    	
    	public int getRank() { // Returns node's rank --> O(1)
    		return this.rank;
    	}
    	
    	public void setRank(int k) { // Sets node's rank --> O(1)
    		this.rank = k;
    	}
    	
    	public int getMark() { // Return node's mark --> O(1)
    		return this.mark;
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
