package by.it._examples_.demo04;

// Demonstrate the do-while loop.
class DWDemo {   
  public static void main(String args[])   
    throws java.io.IOException { 
 
    char ch; 
 
    do { 
      System.out.print("Press a key following by ENTER: "); 
      ch = (char) System.in.read(); // get a char 
    } while(ch != 'q'); 
  }   
}