import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode min;
	private HeapNode exLeft;
	public List<HeapNode> root_lst;
	private int size;
	
	public FibonacciHeap(HeapNode rootMin) {
		this.min = rootMin;
		this.exLeft = rootMin;
		this.min.setNext(rootMin);
		this.min.setPrev(rootMin);
		this.root_lst = new ArrayList<HeapNode>();
	}
	
	public FibonacciHeap() {
		this.min = null;
		this.root_lst = new ArrayList<HeapNode>();
	}

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty() // O(1) - no root ---> heap is empty
    {
    	return this.min==null;  
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key) //O(1)
    {    
    	HeapNode toAdd = new HeapNode(key); 
    	if (this.isEmpty()) {
    		this.min = toAdd;
    		this.exLeft = toAdd; //pointer to connect end of root list
    		this.min.setPrev(toAdd);
    		this.min.setNext(toAdd);
    		this.min.setChild(null);
    	}else {
    		toAdd.setNext(this.exLeft); // connect to previous node inserted
    		toAdd.setPrev(this.exLeft.getPrev()); // connect to other end
    		toAdd.getNext().setPrev(toAdd); // connect brothers to new node
    		toAdd.getPrev().setNext(toAdd);
    		this.exLeft = toAdd;
    		if (toAdd.getKey()<this.min.getKey()) { //update min if necessary
    			this.min = toAdd;
    		}
    	}
    	HeapNode dumby = new HeapNode(-1);
    	this.root_lst.add(dumby); //create a template list for future consolidation
    	this.size+=1;
    	return toAdd;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	if (this.isEmpty()) { //empty heap - return
    		return;
    	}
    	if ((this.min.getPrev()==this.min)||(this.min.getNext()==this.min)) { //one node heap ---> return empty heap
    		this.min = null;
    		this.exLeft = null;
    		return;
    	}
    	if (this.min.getChild()!=null) { //has children
    		this.min.getChild().setPrev(this.min.getPrev());
    		HeapNode tmp = this.min.getChild();
    		if (this.min == this.exLeft) { // update extremes 
    			this.exLeft = this.min.getNext();
    		}
    		if (tmp==null) { //no child
    			this.min.getPrev().setNext(this.min.getNext());//connect brothers to each other
    			this.min.getNext().setPrev(this.min.getPrev());
    			this.min.setPrev(null); //disconnect from brothers ---> deleting
    			this.min.setNext(null);
    		}else {
    			tmp.setParent(null); //disconnect from deleted node
    			this.min.getPrev().setNext(tmp); //connect brother and first son
    			tmp.setPrev(this.min.getPrev());
    			while (tmp.getNext()!=this.min.getChild()) { //go to last "son"
    				tmp.setParent(null);
    				tmp = tmp.getNext();
    			}
    			this.min.getNext().setPrev(tmp); //connect brother and last "son"
    			tmp.setNext(this.min.getNext());
    			this.min.setNext(null);//disconnect from everyone ---> delete
    			this.min.setPrev(null);
    			this.min.setChild(null);
    		}
    	}else { //no children. just connect brothers
    		this.min.getNext().setPrev(this.min.getPrev());//connect brothers to each other
    		this.min.getPrev().setNext(this.min.getNext());
    		this.min.setNext(null);// disconnect from heap
    		this.min.setPrev(null);
    	}
     	this.consolidate();
     	this.size -=1;
    }
     	
    
    
    private void consolidate() {
    	HeapNode tmp = this.exLeft;
    	int rank = this.min.getRank();
    	if ((tmp.getNext()==tmp) || (tmp.getPrev()==tmp)){ //one tree heap
     		this.min = tmp; //update min and finish
     		this.root_lst.set(rank, tmp);
     		return;
     	}
    	HeapNode stopNow = this.exLeft;
    	while (tmp.next != stopNow) { //stop at last root
    		if (this.root_lst.get(rank).getKey()==-1) { //only tree from this size
    			this.root_lst.set(rank, tmp);
    			tmp = tmp.getNext();
    		}else{
    			tmp.join(this.root_lst.get(rank));
    			this.root_lst.set(rank, new HeapNode(-1));
    			if (tmp.getParent()!=null) { //update "root" for new tree
    				tmp = tmp.parent; //check if next spot in tree list is available in next iteration
    			}
    		}
    		rank = tmp.getRank();
    	}
    	if (this.root_lst.get(rank).getKey()==-1) { //check for last root
			this.root_lst.set(rank, tmp);
		}else{
			tmp.join(this.root_lst.get(rank));
		}
    	for (int i=0; i<Math.ceil(Math.log(this.size)); i++){
    		if ((this.root_lst.get(i).getKey()<this.min.getKey())&&((this.root_lst.get(i).getKey()!=-1))) {
    			this.min = this.root_lst.get(i); //update min pointer
    		}
    	}
    	int j=0;
    	while (this.root_lst.get(j).getKey() == -1) { //go over roots until first tree
    		j++;
    	}
    	this.exLeft = this.root_lst.get(j); //lowest ranked tree is exLeft
    	tmp = this.exLeft;
    	for(int k = j; k<=Math.ceil(Math.log(this.size())+1); k++) { //connecting roots
    		if (this.root_lst.get(k).getKey() != -1) {
    			tmp.setNext(this.root_lst.get(k));
    			this.root_lst.get(k).setPrev(tmp);
    			tmp = this.root_lst.get(k);
    		}
    	}
    	tmp.setNext(this.exLeft);//connect both ends of root list
    	this.exLeft.setPrev(tmp);
    }
    
    

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
    	if (this.isEmpty()) {
    		return null;
    	}
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
    	 this.exLeft.getPrev().setNext(heap2.exLeft); // connect midleft to midright
    	 heap2.exLeft.getPrev().setNext(this.exLeft); // connect right end to left end
    	 this.exLeft.setPrev(heap2.exLeft.getPrev()); //connect left end to right end
    	 heap2.exLeft.setPrev(this.exLeft.getPrev()); //connect midright to midleft
    	 this.size += heap2.size(); //update size
    	 if (this.min.getKey()>heap2.min.getKey()) {
    		 this.min = heap2.min; //update min
    	 }
    	 
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	if (this.isEmpty()) {
    		return 0;
    	}
    	return this.size; 
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
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
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
    	return -234; // should be replaced by student code
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
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }
    
    //********************************tester functions********************************
    public void printToTestInitialInsertions(List<Integer> orderList) {
    	System.out.println("Checking everyone was inserted and connected");
    	HeapNode tmp = this.exLeft;
    	HeapNode stopper = this.exLeft;
    	List<Integer> output = new ArrayList<Integer>();
    	while (tmp.getNext()!=stopper) {
    		output.add(tmp.getKey());
    		tmp = tmp.getNext();
    	}
    	output.add(tmp.getKey());
    	System.out.println(output);
    	Collections.reverse(output);
    	System.out.println("Insertion Order");
    	System.out.println(output);
    	if (output.equals(orderList)) {
    		System.out.println("Correct! output is equal to reversed insertion order and connected properly");
    	}else {
    		System.out.println("problem in initial insertions");
    	}
    }
    
    public void auxFunc() {
    	this.exLeft.printToTestAfterManipulations(exLeft.getKey());
    }
    public void auxFuncNew() {
    	boolean[] checkList = new boolean[this.size()+1];
    	this.exLeft.heapTravel(exLeft.getKey(), checkList);
    }
    
 
    //***********************************************End of testing functions*************************
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
    	public String info;
    	private boolean mark;
    	private int rank;
    	private HeapNode child;
    	private HeapNode next;
    	private HeapNode prev;
    	private HeapNode parent;
    	
    	public HeapNode(int key) {
    		this.key = key;
    	}

    	public int getKey() {
    		return this.key;
    	}
    	
    	public void setChild(HeapNode child) {
    		this.child = child;
    	}
    	
    	public void setNext(HeapNode next) {
    		this.next = next;
    	}
    	
    	public void setPrev(HeapNode prev) {
    		this.prev = prev;
    	}
    	
    	public void setParent(HeapNode parent) {
    		this.parent = parent;
    	}
    	
    	public HeapNode getChild() {
    		return this.child;
    	}
    	
    	public HeapNode getNext() {
    		return this.next;
    	}
    	
    	public HeapNode getPrev() {
    		return this.prev;
    	}
    	
    	public HeapNode getParent() {
    		return this.parent;
    	}
    	
    	public void mark() {
    		if (!this.mark) {
    			this.mark = true;
    		}else {
    			//cut command to add and make false
    		}
    	}
    	
    	public void setRank(int k) {
    		this.rank = k;
    	}
    	
    	public int getRank() {
    		return this.rank;
    	}
    	
    	public void join(HeapNode toJoin) { //connect roots of trees and "meld" the min's sons with the max root
    		if (toJoin.getKey()>this.getKey()) { //case #1 - this remains root
    			HeapNode saveNextRoot = this.getNext();
    			this.setRank(this.getRank()+1); //rank one up
    			if (this.getChild()!=null) {
    				toJoin.setPrev(this.getChild().getPrev()); //connect new son to last "son"
    				this.getChild().getPrev().setNext(toJoin); //connect last "son" to new son
    				this.getChild().setPrev(toJoin); //connect first son to new son
    				toJoin.setNext(this.getChild()); //connect new son to old son
    				this.setChild(toJoin); //update son
    				toJoin.setParent(this);
    			}else { //both don't have children
    				this.setPrev(toJoin.getPrev()); //"steal" brothers
    				this.setNext(toJoin.getNext());
    				toJoin.setNext(toJoin); //disconnect brothers
    				toJoin.setPrev(toJoin);
    				toJoin.setParent(this);//connect new father and son
    				this.setChild(toJoin);
    			}
    			this.setNext(saveNextRoot);
    			
    		}else { //case #2 - next node becomes root ----->connect symmetrically
    			HeapNode saveNextRoot = this.getNext();
    			toJoin.setRank(toJoin.getRank()+1); //rank one up**********added last
    			if (toJoin.getChild()!=null) {
    				this.setPrev(toJoin.getChild().getPrev()); 
    				toJoin.getChild().getPrev().setNext(this);
    				toJoin.getChild().setPrev(this);
    				this.setNext(toJoin.getChild());
    				this.setParent(toJoin);//*******added last
    				toJoin.setChild(this);
    			}else { //both don't have children
    				toJoin.setPrev(this.getPrev());//"steal" brothers
    				toJoin.setNext(this.getNext());
    				this.setNext(this);//disconnect brothers
    				this.setPrev(this);
    				this.setParent(toJoin); //connect new father and son
    				toJoin.setChild(this);
    			}
    			toJoin.setNext(saveNextRoot);
    		}
    	}
    	//*******************for testing*****************************
    	public void printToTestAfterManipulations(int finStopper) {
    	    System.out.print("key is " + this.getKey() + " rank is " + this.getRank());
    	    if (this.parent==null) {
    	    	if (this.getChild()!=null) {
    	    		System.out.println("");
    	    		this.getChild().printToTestAfterManipulations(finStopper);
    	    	}else if (this.getNext()!=this) {
    	    		System.out.println(" ----> specefic tree done!");
    	    		this.getNext().printToTestAfterManipulations(finStopper);
    	    	}
    	    }//else {
    	    	HeapNode stopper = this;
    	    	HeapNode tmp = this;
    	    	tmp = tmp.getNext();
    	    	while (tmp!=stopper) {
    	    		 System.out.print(" -----> key is " + tmp.getKey() + " rank is " + tmp.getRank());
    	    		 tmp = tmp.getNext();
    	    	}
    	    	System.out.println("");
    	    	if ((this.getChild()==null)&&(this.getNext()==this)) {
    	    		tmp = this.getParent();
    	    		while (tmp.getParent()!=null) {
    	    			tmp = tmp.getParent();
    	    		}
    	    		System.out.println("specefic tree done!");
    	    		if (tmp.getNext().getKey()==finStopper) {
    	    			System.out.println(" ----->specefic tree done!");
    	    			return;
    	    		}else {
    	    			tmp = tmp.getNext();
    	    			tmp.printToTestAfterManipulations(finStopper);
    	    		}
    	    	}else {
    	    		tmp = this.getChild();
    	    		tmp.printToTestAfterManipulations(finStopper);
    	    	}
    	    	
    	    //}
    	    	
    	 }
    	public void heapTravel(int finStopper, boolean[] checkList) { ///**** add before printing - connections,ranks, minHeap
    		if ((this.getChild()==null)&&(this.getNext().getKey()==finStopper)) {
    			if (checkList[this.getKey()]) {
    				return;
    			}
    			System.out.print(" key is " + this.getKey() + " rank is " + this.getRank());
    			checkList[this.getKey()]=true;
    			return;
    		}
    		if (this.getChild()!=null) {
    			this.getChild().heapTravel(this.getChild().getKey(), checkList);
    		}
    		if (checkList[this.getKey()]) {
				return;
			}
    		System.out.print(" ----> key is " + this.getKey() + " rank is " + this.getRank());
    		checkList[this.getKey()]=true;
    		if (this.getParent()==null) {
    			System.out.println("\n" + " specific tree is done");
    		}
    		HeapNode tmp = this;
    		while (tmp.getNext().getKey()!=finStopper) {//I have brothers
    			tmp.getNext().heapTravel(finStopper, checkList);
    			tmp = tmp.getNext();
    		}
    		if ((this.getParent()==null)&&(this.getNext().getKey()!=finStopper)) {
    			this.getNext().heapTravel(finStopper, checkList);
    		}
    		
    		System.out.println("");
    		return;
    	}
    }
}
