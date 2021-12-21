import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class testerFibo {

	public static void main(String[] args) {
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
		System.out.println("*******************After deletion in first heap***************");
		myHeap.deleteMin();
		myHeap.auxFuncNew();
		System.out.println("Tree size is " + myHeap.size());
		System.out.println("*************After deletion in second heap******************");
		heap2.deleteMin();
		heap2.auxFuncNew();
		System.out.println("Tree size is " + heap2.size());
		System.out.println("***************After meld*******************");
		myHeap.meld(heap2);
		myHeap.auxFuncNew();
		System.out.println("Tree size is " + myHeap.size());
		System.out.println("******************Melded after deletion*****************");
		myHeap.deleteMin();
		myHeap.auxFuncNew();
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
		myHeap.auxFuncNew();
		System.out.println("Tree size is " + myHeap.size());
		System.out.println("*************Melded after 7 deletions**************");
		myHeap.deleteMin();
		myHeap.deleteMin();
		myHeap.deleteMin();
		myHeap.deleteMin();
		myHeap.deleteMin();
		myHeap.deleteMin();
		myHeap.deleteMin();
		myHeap.auxFuncNew();
		System.out.println("Tree size is " + myHeap.size());
		}
	
	
	
	
	
	public static int getRandom(Random randomGen, int[] bank) { 
		int key = randomGen.nextInt(bank.length);
		while ((bank[key]==1)) {
			key = randomGen.nextInt(bank.length);	
	}
		bank[key]=1;
		return key;
	}
	
}
