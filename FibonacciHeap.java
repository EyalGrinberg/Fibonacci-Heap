import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode min;
	public HeapNode exLeft; ///change to private before admission!!!!!!!!!!!!!!!!!
	public List<HeapNode> root_lst;
	public int marked;
	public int trees;
	private static int totalLinks;
	private static int totalCuts;
	private int size;
	
	public FibonacciHeap(HeapNode rootMin) { //constructor for new one tree heap
		this.min = rootMin;
		this.exLeft = rootMin;
		this.min.setNext(rootMin);
		this.min.setPrev(rootMin);
		this.marked = 0;
		this.trees = 1;
		this.size = (int) Math.pow(2,rootMin.getRank());
		totalLinks = 0;
		totalCuts = 0;
		this.root_lst = new ArrayList<HeapNode>();
		for (int i=0; i<3*rootMin.getRank(); i++) {
			this.root_lst.add(new HeapNode(-1));
		}
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
    	if (this.isEmpty()) { //empty tree
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
    	this.trees += 1;
    	return toAdd;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin() // O(logn) amortized. O(n) worst case
    {
    	if (this.isEmpty()) { //empty heap - return
    		return;
    	}
    	
    	if ((this.min.getPrev()==this.min)&&(this.min.getChild()==null)) { //one node heap ---> return empty heap
    		this.min = null;
    		this.exLeft = null;
    		return;
    	}
    	if ((this.min.getPrev()==this.min)) {//single tree heap
    		this.exLeft = this.min.getChild();
    		this.min.getChild().setParent(null);
    		this.min.setChild(null);
    		this.consolidate();
    		this.size-=1;
    		return;
    	}
    	if (this.min.getChild()!=null) { //has children 
    		HeapNode traveler = this.min.getChild(); //unmark the children
    		int end = traveler.getKey();
    		if (traveler.mark) { //unmark and update field
				traveler.mark = false;
				this.marked -=1;
			}
    		traveler = traveler.getNext();
    		while (traveler.getKey()!= end) { //unmark all and update field
    			if (traveler.mark) {
    				traveler.mark = false;
    				this.marked -=1;
    			}
    			traveler = traveler.getNext();
    		}// unmarking done
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
    			while ((tmp!=null)&&(tmp.getNext()!=this.min.getChild())) { //go to last "son" *****added last tmp not null****
    				tmp.setParent(null);
    				tmp = tmp.getNext();
    			}
    			this.min.getNext().setPrev(tmp); //connect brother and last "son"
    			tmp.setNext(this.min.getNext());
    			tmp.setParent(null);
    			this.min.setNext(null);//disconnect from everyone ---> delete
    			this.min.setPrev(null);
    			this.min.setChild(null);
    		}
    	}else { //no children. just connect brothers
    		this.min.getNext().setPrev(this.min.getPrev());//connect brothers to each other
    		this.min.getPrev().setNext(this.min.getNext());
    		if (this.min.getKey()==this.exLeft.getKey()){
    			this.exLeft = this.min.getNext();
    		}
    		this.min.setNext(null);// disconnect from heap
    		this.min.setPrev(null);
    	}
    	
    	for (int i=0; i<=Math.log(root_lst.size())+3; i++) {//update exLeft incase exLeft was deleted
    		if ((root_lst.get(i).getKey()!=-1)&&(root_lst.get(i).getKey()!=this.min.getKey())) { //connect exLeft with other roots and end
    			this.exLeft = root_lst.get(i);
    			break;
    		}
    		
    	}
     	this.consolidate();
     	this.size -=1;
    }
     	
    
    
    private void consolidate() { //O(logn) amortized. O(n) worst case.
    	for (int i=0; i<=Math.log(root_lst.size())+3; i++) {//insert roots into auxiliary list
    		if (root_lst.get(i).getKey()!=-1) {
    			HeapNode dumby = new HeapNode(-1);
    			root_lst.set(i, dumby);
    		}
    	}
    	HeapNode tmp = this.exLeft;
    	int rank = this.min.getRank();
    	if ((tmp.getNext()==tmp) || (tmp.getPrev()==tmp)){ //one tree heap
     		this.min = tmp; //update min and finish
     		this.root_lst.set(rank, tmp);
     		return;
     	}
    	HeapNode stopper = this.exLeft;
    	HeapNode stopNow = null;
    	while ((tmp != stopNow)||((tmp.getParent()==null)&&(rank!=0)&&(this.root_lst.get(rank)!=tmp)&&(this.root_lst.get(rank-1)!=tmp))) { //stop at last root
    		stopNow = stopper;
    		rank = tmp.getRank();
    		if (this.root_lst.get(rank).getKey()==-1) { //only tree from this size
    			this.root_lst.set(rank, tmp);
    			tmp = tmp.getNext();
    		}else{ // there is another tree of this size
    			tmp.join(this.root_lst.get(rank));
    			totalLinks+=1;
    			this.root_lst.set(rank, new HeapNode(-1));
    			if (tmp.getParent()!=null) { //update "root" for new tree
    				tmp = tmp.parent; //check if next spot in tree list is available in next iteration
    				stopNow = tmp.getChild();
    			}
    		}
    		if (tmp != null) {
    			rank = tmp.getRank();
    		}
    	}
    	for (int i=0; i<2*Math.ceil(Math.log(this.size)); i++){
    		if ((this.root_lst.get(i).getKey()<this.min.getKey())&&((this.root_lst.get(i).getKey()!=-1))) {
    			this.min = this.root_lst.get(i); //update min pointer
    		}
    	}
    	int j=0;
    	while ((j<this.root_lst.size())&&(this.root_lst.get(j).getKey() == -1)) { //go over roots until first tree
    		j++;
    	}
    	this.exLeft = this.root_lst.get(j); //lowest ranked tree is exLeft
    	tmp = this.exLeft;
    	for(int k = j; k<=Math.ceil(2*Math.log(this.size())+1); k++) { //connecting roots
    		if (this.root_lst.get(k).getKey() != -1) {
    			tmp.setNext(this.root_lst.get(k));
    			this.root_lst.get(k).setPrev(tmp);
    			tmp = this.root_lst.get(k);
    		}
    	}
    	tmp.setNext(this.exLeft);//connect both ends of root list
    	this.exLeft.setPrev(tmp);
    	HeapNode stopLoop = this.exLeft;//finding the new minimum
    	this.min = stopLoop;
    	tmp = exLeft.getNext();
    	while (tmp != stopLoop) {
    		if (tmp.getKey()<this.min.getKey()) {
    			this.min = tmp;
    		}
    		tmp = tmp.getNext();
    	}
    	this.countersRep();//updates number of trees in O(logn)
    }
    
    

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin() //O(1)
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
    public void meld (FibonacciHeap heap2) //O(1)
    {
    	 this.exLeft.getPrev().setNext(heap2.exLeft); // connect midleft to midright
    	 heap2.exLeft.getPrev().setNext(this.exLeft); // connect right end to left end
    	 HeapNode toSave = this.exLeft.getPrev(); //temporary
    	 this.exLeft.setPrev(heap2.exLeft.getPrev()); //connect left end to right end
    	 heap2.exLeft.setPrev(toSave); //connect midright to midleft
    	 this.size += heap2.size(); //update size, marks and trees
    	 this.marked += heap2.marked;
    	 this.trees += heap2.trees;
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
    public int size() //O(1)
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
    public int[] countersRep() //O(logn)
    {
    	int[] arr = new int[(int) (3*Math.ceil(Math.log(this.size())))];
    	int stopper = this.exLeft.getKey();
    	HeapNode tmp = this.exLeft;
    	int cntTrees = 0;
    	for (int i=0; i<arr.length; i++) { //create draft array
    		arr[tmp.getRank()]+=1;
    		cntTrees+=1;
    		tmp = tmp.getNext();
    		if (tmp.getKey()==stopper) {
    			break;
    		}
    	}
    	int highestIndex = 0;
    	for (int j=0; j<arr.length; j++) { // get final size
    		if (arr[j]!=0) {
    			highestIndex = j;
    		}
    	}
    	int[] result = new int[highestIndex+1];
    	for (int k=0; k<result.length; k++) { // create final array
    		result[k] = arr[k];
    	}
    	this.trees = cntTrees;//update number of trees
        return result; 
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
    	int delta = Integer.MIN_VALUE;
    	this.decreaseKey(x, delta);
    	this.min = x;
    	if (x.getParent()!=null) {
    		this.cascadeCut(x);
    		totalCuts += 1;
    	}
    	this.deleteMin();
    	return; 
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.key -= delta;
    	if (x.getKey()<this.min.getKey()) { //update min
    		this.min = x;
    	}
    	if (x.getParent()==null) { //no parent no problem
    		return;
    	}
    	if (x.getKey()<x.getParent().getKey()) { //cut necessary
    		this.cascadeCut(x);
    		totalCuts+=1;
    	}
    	return; 
    }
    
    public void cascadeCut(HeapNode son) {
    	if (son.getKey()==son.getParent().getChild().getKey()) { //son is parent's actual child -----> switch the child
    		if (son.getNext().getKey() == son.getKey()) { //only child	
    			son.getParent().setChild(null);
    			son.getParent().setRank(0);
    		}else { // has brothers
    			son.getParent().setChild(son.getNext());
    			son.getNext().setPrev(son.getPrev());
    			son.getPrev().setNext(son.getNext());// connect brothers
    			son.getParent().setRank(son.getParent().getChild().getRank()+1);
    		}
    	}else {
    		son.getNext().setPrev(son.getPrev());
			son.getPrev().setNext(son.getNext());// connect brothers
    	}
    	HeapNode toSave = son.getParent(); // save parent for checking up the heap
    	son.setParent(null);
    	if (son.mark) {
    		son.mark = false;
    		this.marked -=1;
    	}
    	son.setNext(this.exLeft);
    	son.setPrev(this.exLeft.getPrev());
    	this.exLeft.getPrev().setNext(son);
    	this.exLeft.setPrev(son);
    	this.exLeft = son; //insert son as exLeft and adjust connections
    	if (toSave.mark) { //check father
    		this.cascadeCut(toSave);//cut father
    		totalCuts+=1;
    	}else if ((!toSave.mark)&&(toSave.getParent()==null)) { // root should stay unmarked
    		return;
    	}else { // not marked and not a root ---->mark
    		toSave.mark(); // mark father
    		this.marked+=1;
    	}
    	this.trees +=1; // one tree added to heap
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
    	int result = 2*this.marked + this.trees;
    	return result; // should be replaced by student code
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
    	return totalLinks; // should be replaced by student code
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
    	return totalCuts; // should be replaced by student code
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
    	int[] arr = new int[k];
    	if (k==1) { //one node tree. protect from zero in index stopping condition
    		arr[0] = H.findMin().getKey();
    		return arr;
    	}
    	HashMap<Integer, HeapNode> originals = new HashMap<>(); //pointer dictionary to access new candidates in O(1)
    	arr[0] = H.findMin().getKey();
    	HeapNode tmp = H.findMin().getChild(); //pointer in original heap
    	FibonacciHeap auxHeap = new FibonacciHeap();
    	for (int j=1; j<k; j++) { //still need more keys
            int stopper = tmp.getKey();
            auxHeap.insert(stopper); //insert to auxHeap
            originals.put(tmp.getKey(), tmp); //add to pointer dictionary
            while (tmp.getNext().getKey()!=stopper) { // add sons to auxHeap and pointer dictionary
            	tmp = tmp.getNext();
            	auxHeap.insert(tmp.getKey());
            	originals.put(tmp.getKey(), tmp);
            }
            arr[j] = auxHeap.findMin().getKey();
            stopper = auxHeap.findMin().getKey();
            tmp = originals.get(stopper);
            auxHeap.deleteMin();
            while (tmp.getChild()==null) { //added node has children in original heap ----> next round will add his children to the aux heap
            	j+=1;
            	if (j>=k) {
            		break;
            	}
            	arr[j] = auxHeap.findMin().getKey();
            	tmp = originals.get(auxHeap.findMin().getKey());
            	auxHeap.deleteMin();
            }
            tmp = tmp.getChild();	
            } 	
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
    
    public void auxFuncNew() {
    	boolean[] checkList = new boolean[5*this.size()];
    	this.exLeft.heapTravel(exLeft.getKey(), checkList, 0);
    	int sum = 0;
    	for (int i=0; i<=2*Math.log(checkList.length); i++) {
    		if (this.root_lst.get(i).getKey()!=-1) {
    			sum += Math.pow(2, i);
    		}
    	}
    	System.out.println("Total keys printed ---> " + sum );
    }
    
    /*public void display() {
        display(this.min);
        System.out.println();
    }

    private void display(HeapNode c) {
        System.out.print("(");
        if (c == null) {
            System.out.print(")");
            return;
        } else {
            HeapNode temp = c;
            do {
                if (c.parent == null){
                    System.out.print("|root|");
                }
                System.out.print(temp.getKey());
                HeapNode k = temp.getChild();
                display(k);
                System.out.print("->");
                temp = temp.getNext();
            } while (temp != c);
            System.out.print(")");
        }
    }*/
    
 
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
    		this.mark = true;
    	}
    	
    	public void setRank(int k) {
    		this.rank = k;
    	}
    	
    	public int getRank() {
    		return this.rank;
    	}
    	
    	public void join(HeapNode toJoin) { //O(1) connect roots of trees and "meld" the min's sons with the max root
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
    	public void heapTravel(int finStopper, boolean[] checkList, int cnt) { ///**** add before printing - connections,ranks, minHeap
    		if ((this.getChild()==null)&&(this.getNext().getKey()==finStopper)) {
    			if (checkList[this.getKey()]) {
    				return;
    			}
    			System.out.println(" key is " + this.getKey() + " rank is " + this.getRank());
    			cnt+=1;
    			if ((this.getChild()!=null)&&(this.getChild().getParent()!=this)) {
    				System.out.println("child " + this.getChild().getKey() + "not connected to parent " + this.getKey());
    			}
    			if ((this.getNext().getPrev()!=this)) {
    				System.out.println("brother " + this.getNext().getKey() + "not connected to brother " + this.getKey());
    			}
    			if ((this.getChild()!=null)&&(this.getChild().getKey()<this.getKey())) {
    				System.out.println("child " + this.getChild().getKey() + "is smaller than " + this.getKey());
    			}
    			checkList[this.getKey()]=true;
    			return;
    		}
    		if (this.getChild()!=null) {
    			this.getChild().heapTravel(this.getChild().getKey(), checkList, cnt);
    		}
    		if (checkList[this.getKey()]) {
				return;
			}
    		System.out.println(" key is " + this.getKey() + " rank is " + this.getRank());
    		cnt+=1;
    		if ((this.getChild()!=null)&&(this.getChild().getParent()!=this)) {
				System.out.println("child " + this.getChild().getKey() + "not connected to parent " + this.getKey());
			}
			if ((this.getNext().getPrev()!=this)) {
				System.out.println("brother " + this.getNext().getKey() + "not connected to brother " + this.getKey());
			}
			if ((this.getChild()!=null)&&(this.getChild().getKey()<this.getKey())) {
				System.out.println("child " + this.getChild().getKey() + "is smaller than " + this.getKey());
			}
    		checkList[this.getKey()]=true;
    		if (this.getParent()==null) {
    			System.out.println("**specific tree is done, tree number is " + cnt + " **\n**tree size is " + Math.pow(2, this.getRank()) + "**\n");
    			//cnt = 0;
    		}
    		HeapNode tmp = this;
    		while (tmp.getNext().getKey()!=finStopper) {//I have brothers
    			tmp.getNext().heapTravel(finStopper, checkList, cnt);
    			tmp = tmp.getNext();
    		}
    		if ((this.getParent()==null)&&(this.getNext().getKey()!=finStopper)) {
    			this.getNext().heapTravel(finStopper, checkList, cnt);
    		}
    		
    		//System.out.println("");
    		return;
    	}
    	
    }
}
