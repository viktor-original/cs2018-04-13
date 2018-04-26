package by.it.krasilnikov.lesson06;

import javax.sound.midi.Soundbank;

public class DogHelper {
    //должен печатать в консоль через пробел имена всех собак
    static void printAllNames(Dog[] dogs){
        for (Dog dog : dogs) {
            System.out.print(dog.getName()+" ");
        }
        System.out.println();
    }
    //должен вычислять средний возраст переданных ему собак
    static double averageAge(Dog[] dogs){
        double sum=0;
        for (Dog dog : dogs) {
            sum=sum+dog.getAge();
        }
        return sum/dogs.length;
    }
}
