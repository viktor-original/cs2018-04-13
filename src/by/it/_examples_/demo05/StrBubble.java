package by.it._examples_.demo05;

// Demonstrate the Bubble sort with strings.
class StrBubble {   
  public static void main(String args[]) {   
    String strs[] = { 
                     "this", "is", "a", "test", 
                     "of", "a", "string", "sort"  
                    }; 
    int a, b; 
    String t;   
    int size;   
   
    size = strs.length; // number of elements to sort   
   
    // display original array   
    System.out.print("Original array is:");  
    for(int i=0; i < size; i++)  
      System.out.print(" " + strs[i]);   
    System.out.println();   
   
    // This is the bubble sort for strings.   
    for(a=1; a < size; a++)   
      for(b=size-1; b >= a; b--) {   
        if(strs[b-1].compareTo(strs[b]) > 0) { // if out of order   
          // exchange elements    
          t = strs[b-1];   
          strs[b-1] = strs[b];   
          strs[b] = t;   
        }   
      }   
   
    // display sorted array   
    System.out.print("Sorted array is:");   
    for(int i=0; i < size; i++)  
      System.out.print(" " + strs[i]);   
    System.out.println();  
  }  
}