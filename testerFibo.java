import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import FibonacciHeap.HeapNode;

//import FibonacciHeap.HeapNode;

public class testerFibo {

	public static void main(String[] args) {
		//testInsertMeldDelete();
		//testDecreaseCutDelete();
		//testKMin();
		//theoryTester();
		theoryTester2();
		}
	public static void theoryTester2() {
		int k = 14;
		int[] expoList = {6,8,10,12,14};
		int m = 48;//(int) Math.pow(3, k) -1;
		FibonacciHeap myHeap = new FibonacciHeap();
		long startTime = System.nanoTime();
		for (int i=0; i<=m; i++) {
			myHeap.insert(i);
		}
		//myHeap.auxFuncNew();
		int cnt = 0;
		for (int i=1; i<=(3*m/4); i++) {
			cnt+=1;
			System.out.println("******After delete num " + cnt);
			myHeap.deleteMin();
			myHeap.auxFuncNew();
		}
		System.out.println("******After deltion loop******");
		myHeap.auxFuncNew();
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		totalTime=totalTime/1000000;
		System.out.println("Total run time is: " + totalTime);
		System.out.println("Total links is: " + FibonacciHeap.totalLinks());
		System.out.println("Total cuts is: " + FibonacciHeap.totalCuts());
		System.out.println("Potential is: " + myHeap.potential());
		System.out.println("Number of trees: " + myHeap.trees);
		System.out.println("Marked nodes: " + myHeap.marked);
	}
	
	public static void theoryTester() {
		long startTime = System.nanoTime();
		int k = 25;
		int m = (int) Math.pow(2, k);
		FibonacciHeap myHeap = new FibonacciHeap();
		int sum = 1;
		List<FibonacciHeap.HeapNode> pointerList = new ArrayList<FibonacciHeap.HeapNode>();
		List<Integer> impKeys = new ArrayList();
		List<FibonacciHeap.HeapNode> oneKey = new ArrayList();
		impKeys.add(sum);
		for (int i=1; i<k; i++) {
			sum += Math.pow(2, k-i);
			impKeys.add(sum);	
		}
		for (int i=m-1; i>-2; i--) {
			myHeap.insert(i);
			if (impKeys.contains(i)) {
				pointerList.add(myHeap.exLeft);
			}
			if (i==m-2) {
				oneKey.add(myHeap.exLeft);
			}
		}
		myHeap.deleteMin();
		System.out.println("****After first deleteMin*****");
		//myHeap.auxFuncNew();
		System.out.println("****After decreaseKey loop*****");
		for (int j=k; j>0; j--) {
			myHeap.decreaseKey(pointerList.get(j-1), m+1);
		}
		//myHeap.auxFuncNew();
		System.out.println("****After Special decrease m-2*****");
		//myHeap.decreaseKey(oneKey.get(0), m+1); //*****סעיף ו'
		//myHeap.auxFuncNew();
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		totalTime=totalTime/1000000;
		System.out.println("Total run time is: " + totalTime);
		System.out.println("Total links is: " + FibonacciHeap.totalLinks());
		System.out.println("Total cuts is: " + FibonacciHeap.totalCuts());
		System.out.println("Potential is: " + myHeap.potential());
		System.out.println("Number of trees: " + myHeap.trees);
		System.out.println("Marked nodes: " + myHeap.marked);
	}
	public static void testKMin() {  
		Random HeapGen = new Random();
		int n = 129;
		int k = 70;
		List<Integer> orderOfInsertions = new ArrayList<Integer>();
		int [] bank= new int[3*n+1];
		FibonacciHeap myHeap = new FibonacciHeap();
		int[] putIn = {4, 2, 9, 15, 22, 6, 38, 14, 36, 20, 24, 21, 11, 10, 5, 18, 37, 16, 13, 8, 0, 33, 17, 12, 23, 7, 28, 19, 32, 1, 27, 30, 39, 31, 25, 34, 29, 35, 3, 40};
		for (int i=0; i<n; i++) { //create heap
			int key = getRandom(HeapGen, bank);
			String info = "String of" + key;
			myHeap.insert(key);
			orderOfInsertions.add(key);
		}
		myHeap.deleteMin();
		myHeap.auxFuncNew();
		System.out.println("Actual result is " + Arrays.toString(FibonacciHeap.kMin(myHeap, k)));
		System.out.println("Actual result is " + Arrays.toString(FibonacciHeap.kMin(myHeap, 1)));
		System.out.println("Actual result is " + Arrays.toString(FibonacciHeap.kMin(myHeap, 2)));
		System.out.println("Actual result is " + Arrays.toString(FibonacciHeap.kMin(myHeap, 34)));
		System.out.println("All keys in order are " + Arrays.toString(FibonacciHeap.kMin(myHeap, n-1)));
		myHeap.auxFuncNew();
	}
	public static void testDecreaseCutDelete() {
		Random HeapGen = new Random();
		int n = 75;
		List<Integer> orderOfInsertions = new ArrayList<Integer>();
		int [] bank= new int[3*n+1];
		FibonacciHeap myHeap = new FibonacciHeap();
		int[] putIn = {4, 2, 9, 15, 22, 6, 38, 14, 36, 20, 24, 21, 11, 10, 5, 18, 37, 16, 13, 8, 0, 33, 17, 12, 23, 7, 28, 19, 32, 1, 27, 30, 39, 31, 25, 34, 29, 35, 3, 40};
		for (int i=0; i<n; i++) { //create heap
			int key = getRandom(HeapGen, bank);
			String info = "String of" + key;
			myHeap.insert(key);
			orderOfInsertions.add(key);
		}
		System.out.println("counterRep array ---->" + Arrays.toString(myHeap.countersRep()));
		System.out.println("Tree min is " + myHeap.findMin().getKey());
		System.out.println("Number of marked " + myHeap.marked + " Number of trees " + myHeap.trees + " Potential is " + myHeap.potential());
		System.out.println("Number of Links is " + FibonacciHeap.totalLinks() + " Number of cuts is " + FibonacciHeap.totalCuts());
		myHeap.decreaseKey(myHeap.findMin().getNext(), 3*n);
		System.out.println("*****Decrease after insertions*****");
		System.out.println("Tree min is " + myHeap.findMin().getKey());
		System.out.println("*****After deleteMin*****");
		myHeap.deleteMin();
		System.out.println("counterRep array ---->" + Arrays.toString(myHeap.countersRep()));
		myHeap.auxFuncNew();
		System.out.println("Number of Links is " + FibonacciHeap.totalLinks() + " Number of cuts is " + FibonacciHeap.totalCuts());
		System.out.println("Number of marked " + myHeap.marked + " Number of trees " + myHeap.trees + " Potential is " + myHeap.potential());
		System.out.println("Tree min is " + myHeap.findMin().getKey());
		int delta = myHeap.exLeft.getPrev().getChild().getKey() - myHeap.findMin().getKey() + 1; //make sure cascade happens and min switched
		System.out.println("*****First decrease*****");
		delta = myHeap.exLeft.getPrev().getChild().getChild().getChild().getNext().getKey() - myHeap.findMin().getKey() + 1;
		System.out.println("Decraesing Node " + myHeap.exLeft.getPrev().getChild().getChild().getChild().getNext().getKey() + " By " + delta);
		myHeap.decreaseKey(myHeap.exLeft.getPrev().getChild().getChild().getChild().getNext(), delta);
		myHeap.auxFuncNew();
		System.out.println("counterRep array ---->" + Arrays.toString(myHeap.countersRep()));
		System.out.println("Number of Links is " + FibonacciHeap.totalLinks() + " Number of cuts is " + FibonacciHeap.totalCuts());
		System.out.println("Number of marked " + myHeap.marked + " Number of trees " + myHeap.trees + " Potential is " + myHeap.potential());
		System.out.println("*****Second decrease(of brother----> father needs to be cut)*****");
		delta = myHeap.exLeft.getPrev().getChild().getChild().getChild().getKey() - myHeap.exLeft.getPrev().getChild().getChild().getKey() + 2;
		System.out.println("Decraesing Node " + myHeap.exLeft.getPrev().getChild().getChild().getChild().getKey() + " By " + delta);
		System.out.println("Father is " + myHeap.exLeft.getPrev().getChild().getChild().getKey());
		myHeap.decreaseKey(myHeap.exLeft.getPrev().getChild().getChild().getChild(), delta);
		myHeap.auxFuncNew();
		System.out.println("counterRep array ---->" + Arrays.toString(myHeap.countersRep()));
		System.out.println("Number of Links is " + FibonacciHeap.totalLinks() + " Number of cuts is " + FibonacciHeap.totalCuts());
		System.out.println("Number of marked " + myHeap.marked + " Number of trees " + myHeap.trees + " Potential is " + myHeap.potential());
		System.out.println("*****Deleting minimum's child*****");
		System.out.println("Tree min is " + myHeap.findMin().getKey());
		System.out.println("Deleting  " + myHeap.findMin().getChild().getKey());
		myHeap.delete(myHeap.findMin().getChild());
		myHeap.auxFuncNew();
		System.out.println("counterRep array ---->" + Arrays.toString(myHeap.countersRep()));
		System.out.println("Number of Links is " + FibonacciHeap.totalLinks() + " Number of cuts is " + FibonacciHeap.totalCuts());
		System.out.println("Number of marked " + myHeap.marked + " Number of trees " + myHeap.trees + " Potential is " + myHeap.potential());
		System.out.println("Tree min is " + myHeap.findMin().getKey());
	}
	public static void testInsertMeldDelete() {
		Random HeapGen = new Random();
		int n =25;
		List<Integer> orderOfInsertions = new ArrayList<Integer>();
		List<Integer> orderOfInsertions2 = new ArrayList<Integer>();
		int [] bank= new int[3*n+1];
		int[] putIn = {4, 2, 9, 15, 22, 6, 38, 14, 36, 20, 24, 21, 11, 10, 5, 18, 37, 16, 13, 8};
		int[] putIn2 = {0, 33, 17, 12, 23, 7, 28, 19, 32, 1, 27, 30, 39, 31, 25, 34, 29, 35, 3, 40};
		FibonacciHeap myHeap = new FibonacciHeap();
		FibonacciHeap heap2 = new FibonacciHeap();
		for (int i=0; i<n; i++) { //create heap
			int key = getRandom(HeapGen, bank);
			String info = "String of" + key;
			//int key = putIn[i];
			myHeap.insert(key);
			orderOfInsertions.add(key);
		}
		for (int i=0; i<n; i++) { //create heap
			int key = getRandom(HeapGen, bank);
			String info = "String of" + key;
			heap2.insert(key);
			orderOfInsertions2.add(key);
		}
		myHeap.printToTestInitialInsertions(orderOfInsertions);
		heap2.printToTestInitialInsertions(orderOfInsertions2);
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		System.out.println("Tree array is " + Arrays.toString(myHeap.countersRep()));
		System.out.println("*******************After deletion in first heap***************");
		myHeap.deleteMin();
		myHeap.auxFuncNew();
		System.out.println("Tree array is " + Arrays.toString(myHeap.countersRep()));
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		System.out.println("Tree size is " + myHeap.size());
		System.out.println("*************After deletion in second heap******************");
		heap2.deleteMin();
		//heap2.auxFuncNew();
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		System.out.println("Tree size is " + heap2.size());
		System.out.println("***************After meld*******************");
		myHeap.meld(heap2);
		myHeap.auxFuncNew();
		System.out.println("Tree array is " + Arrays.toString(myHeap.countersRep()));
		System.out.println("Tree size is " + myHeap.size());
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		System.out.println("******************Melded after deletion*****************");
		myHeap.deleteMin();
		//myHeap.auxFuncNew();
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		System.out.println("Tree size is " + myHeap.size());
		System.out.println("*************Melded after 3 insertions**************");
		int key = getRandom(HeapGen, bank);
		myHeap.insert(key);
		System.out.println("Inserted " + key);
		key = getRandom(HeapGen, bank);
		myHeap.insert(key);
		System.out.println("Inserted " + key);
		key = getRandom(HeapGen, bank);
		myHeap.insert(key);
		System.out.println("Inserted " + key);
		//myHeap.auxFuncNew();
		System.out.println("Tree size is " + myHeap.size());
		System.out.println("*************Melded after 7 deletions**************");
		myHeap.deleteMin();
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		myHeap.deleteMin();
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		myHeap.deleteMin();
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		myHeap.deleteMin();
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		myHeap.deleteMin();
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		myHeap.deleteMin();
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		myHeap.deleteMin();
		myHeap.auxFuncNew();
		System.out.println("Tree array is " + Arrays.toString(myHeap.countersRep()));
		System.out.println("Tree size is " + myHeap.size());
		System.out.println("Tree minimum is " + myHeap.findMin().getKey());
		}
	
	
	
	public static int getRandom(Random randomGen, int[] bank) { 
		int key = randomGen.nextInt(bank.length);
		while ((bank[key]==1)&&(key!=0)) {
			key = randomGen.nextInt(bank.length);	
	}
		bank[key]=1;
		return key;
	}
	
    //********************************tester functions for FibbonacciHeap Class********************************
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
    	boolean[] negCheckList = new boolean[5*this.size()];
    	this.exLeft.heapTravel(exLeft.getKey(), checkList, negCheckList, 0);
    	int sum = 0;
    	for (int i=0; i<=2*Math.log(checkList.length); i++) {
    		if (this.root_lst.get(i).getKey()!=Integer.MIN_VALUE) {
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
  //*******************for testing in HeapNode*****************************
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
	public void heapTravel(int finStopper, boolean[] checkList, boolean[] negCheckList, int cnt) { ///**** add before printing - connections,ranks, minHeap
		if ((this.getChild()==null)&&(this.getNext().getKey()==finStopper)) {
			if ((this.getKey()>=0)&&(checkList[this.getKey()])) { // first condition added last for theory
				return;
			}
			if ((this.getKey()<0)&&(negCheckList[-this.getKey()])) { // first condition added last for theory
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
			if (this.getKey()>=0) { // added for theory
				checkList[this.getKey()]=true;
			}
			if (this.getKey()<0) { // added for theory
				negCheckList[-this.getKey()]=true;
			}
			return;
		}
		if (this.getChild()!=null) {
			this.getChild().heapTravel(this.getChild().getKey(), checkList, negCheckList, cnt);
		}
		if ((this.getKey()>=0)&&(checkList[this.getKey()])) {
			return;
		}
		if ((this.getKey()<0)&&(negCheckList[-this.getKey()])) {
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
		if (this.getKey()>=0) {
			checkList[this.getKey()]=true;
		}
		if (this.getKey()<0) {
			negCheckList[-this.getKey()]=true;
		}
		if (this.getParent()==null) {
			System.out.println("**specific tree is done, tree number is " + cnt + " **\n**tree size is " + Math.pow(2, this.getRank()) + "Root is: " + this.getKey() + "**\n");
			cnt = 0;
		}
		HeapNode tmp = this;
		while (tmp.getNext().getKey()!=finStopper) {//I have brothers
			tmp.getNext().heapTravel(finStopper, checkList, negCheckList, cnt);
			tmp = tmp.getNext();
		}
		if ((this.getParent()==null)&&(this.getNext().getKey()!=finStopper)) {
			this.getNext().heapTravel(finStopper, checkList, negCheckList, cnt);
		}
		
		System.out.println("");
		return;
	}
	//*************************HeapNodeTestingFunctions*********************************
}
