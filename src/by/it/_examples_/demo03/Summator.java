package by.it._examples_.demo03;

public class Summator {
    private static int sum(int a, int b, int c){
        int sum=a+b;
        return sum+c;
    }

    public static void main(String[] args) {
        int k=5; int m=6; int n=7;
        int res=sum(k,m,n);
        System.out.println("res="+res);
        System.out.println("res2="+sum(2,2,2));
        System.out.println("res3="+sum(3,3,3));
    }
}
