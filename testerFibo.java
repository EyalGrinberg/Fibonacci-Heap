import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class testerFibo {

	public static void main(String[] args) {
		//testInsertMeldDelete();
		testDecreaseCutDelete();
		//testKMin();
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
	
}
