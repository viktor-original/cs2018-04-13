package by.it.krasilnikov.lesson05;
/*
Три массива
1.  Введите с клавиатуры 20 чисел, сохраните их в список и рассортируйте по трём другим спискам:
    Число делится на 3 (x%3==0), делится на 2 (x%2==0) и все остальные.
    Числа, которые делятся на 3 и на 2 одновременно, например 6, попадают в оба списка.
2.  Статический метод void printList(List<Integer> list) должен выводить на экран
    все элементы переданного ему списка list, каждый элемент - с новой строки.
3.  Используя метод printList выведите ваши три списка на экран.
    Сначала тот, который для x%3, потом тот, который для x%2, потом последний.
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskC1 {
    public static void main(String[] args) throws Exception
    {   Scanner scanner = new Scanner(System.in);
        int scan=scanner.nextInt();
        List<Integer> list1=new ArrayList<>();
        List<Integer> list2=new ArrayList<>();
        List<Integer> list3=new ArrayList<>();
        for (String a : args) {
            if(Integer.parseInt(a)%3==0){
                list1.add(Integer.parseInt(a));
            }
            if(Integer.parseInt(a)%2==0){
                list1.add(Integer.parseInt(a));
            }
if(Integer.parseInt(a)%3!=0  && Integer.parseInt(a)%2!=0  ){
    list3.add(Integer.parseInt(a));
}

        }
        printList(list1);
        printList(list2);
        printList(list3);


    }

    private static void printList(List<Integer> list) {
        for (Integer aList : list) System.out.println(aList);
    }


}
