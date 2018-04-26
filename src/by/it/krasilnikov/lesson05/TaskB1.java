package by.it.krasilnikov.lesson05;
/*
Создайте 5 различных строк в списке ArrayList:

1. Создайте список строк.
2. Добавьте в него 5 различных строк.
3. Выведите его размер на экран.
4. Используя цикл выведите его содержимое на экран, каждое значение с новой строки.

*/


import java.util.ArrayList;
import java.util.List;

public class TaskB1 {
    public static void main(String[] args) {
        List<String> arr=new ArrayList<>();
        arr.add("ONE");
        arr.add("TWO");
        arr.add("THREE");
        arr.add("FOUR");
        arr.add("FIVE");
        System.out.println(arr.size());
        for (String str:arr)
            System.out.println(str);
    }
}
