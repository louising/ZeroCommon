import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// partition.java
// demonstrates partitioning an array
// to run this program: C>java PartitionApp
////////////////////////////////////////////////////////////////
class ArrayPar {
    private long[] theArray;          // ref to array theArray
    private int nElems;               // number of data items
    //--------------------------------------------------------------

    public ArrayPar(int max)          // constructor
    {
        theArray = new long[max];      // create the array
        nElems = 0;                    // no items yet
    }

    //--------------------------------------------------------------
    public void insert(long value)    // put element into array
    {
        theArray[nElems] = value;      // insert it
        nElems++;                      // increment size
    }

    //--------------------------------------------------------------
    public int size()                 // return number of items
    {
        return nElems;
    }

    //--------------------------------------------------------------
    public void display()             // displays array contents
    {
        System.out.print("A=");
        for (int j = 0; j < nElems; j++)    // for each element,
            System.out.print(theArray[j] + " ");  // display it
        System.out.println("");
    }

    //--------------------------------------------------------------
    public int partitionIt(int left, int right, long pivot) {
        int leftPtr = left - 1;
        int rightPtr = right + 1;
        while (true) {
            while (leftPtr < right && theArray[++leftPtr] < pivot)
                ;
            while (rightPtr > left && theArray[--rightPtr] > pivot)
                ;
            if (leftPtr >= rightPtr) // ==: if the pivot is the largest or smallest
                break;
            else
                swap(leftPtr, rightPtr);
        }
        return leftPtr;
    }

    //--------------------------------------------------------------
    public void swap(int dex1, int dex2)  // swap two elements
    {
        long temp;
        temp = theArray[dex1];             // A into temp
        theArray[dex1] = theArray[dex2];   // B into A
        theArray[dex2] = temp;             // temp into B
    }  // end swap()
    //--------------------------------------------------------------
}  // end class ArrayPar
////////////////////////////////////////////////////////////////

public class PartitionApp {
    
    static void test() {
        
        try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
                System.out.println(br.readLine());
                br.lines();
        } catch(RuntimeException | IOException e) {
            e.printStackTrace();
        }
        
        int binary = 0b110; //Binary 2^2 + 2^1 = 6
        System.out.println(binary);
        int eight = 0110; //Octal 8^2 + 8^1 = 72
        System.out.println(eight);
        int hex = 0x110; //hexadecimal 16^2 + 16^1 = 272
        System.out.println(hex);
    }
    
    public static void main(String[] args) throws Exception {
        //List<String> list = ["item"];
        //Set<String> set = {"item"};
        
        //FileReader a = new FileReader("a.txt");
        
        test();
        //testPartition();
    }

    protected static void testPartition() {
        int maxSize = 10;             
        ArrayPar arr = new ArrayPar(maxSize);               

        for (int j = 0; j < maxSize; j++) {
            long n = (int) (java.lang.Math.random() * 100);
            arr.insert(n);
        }

        arr.display();

        long pivot = 99;
        System.out.print("Pivot is " + pivot);
        int size = arr.size();
        int partDex = arr.partitionIt(0, size - 1, pivot);

        System.out.println(", Partition is at index " + partDex);
        arr.display();
    }
}


