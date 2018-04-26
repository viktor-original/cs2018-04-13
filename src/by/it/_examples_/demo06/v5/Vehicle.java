package by.it._examples_.demo06.v5;// Use a return value.
 
class Vehicle {  
  int passengers; // number of passengers  
  int fuelcap;    // fuel capacity in gallons 
  int mpg;        // fuel consumption in miles per gallon 
 
  // Return the range. 
  int range() { 
    return mpg * fuelcap; 
  } 
}

