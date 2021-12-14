import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class testerFibo {

	public static void main(String[] args) {
		Random HeapGen = new Random();
		int n = 20;
		List<Integer> orderOfInsertions = new ArrayList<Integer>();
		int [] bank= new int[n];
		FibonacciHeap myHeap = new FibonacciHeap();
		for (int i=0; i<n; i++) { //create heap
			//int key = getRandom(HeapGen, bank);//tester.getRandomInt2(treeSize, bank3); //get random number above 500
			//String info = "String of" + key;
			int key = i;
			myHeap.insert(key);
			orderOfInsertions.add(key);
		}
		myHeap.printToTestInitialInsertions(orderOfInsertions);
		myHeap.deleteMin();
		myHeap.auxFuncNew();
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
