package by.it._tasks_.lesson05;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("all") //море warnings. всех прячем.

//поставьте курсор на следующую строку и нажмите Ctrl+Shift+F10
public class Testing05 {

    @Test(timeout = 2500)
    public void testTaskA1() throws Exception {
        run("1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n").
                include("0\n9\n8\n7\n6\n5\n4\n3\n2\n1\n");
        run("1\n-2\n3\n4\n5\n666\n7\n8\n9\n0\n").
                include("0\n9\n8\n7\n666\n5\n4\n3\n-2\n1\n");
    }


    @Test(timeout = 2500)
    public void testTaskA2() throws Exception {
        run("ONE\nTWO\nTHREE\nFOUR\nFIVE\nEND\n").include("[ONE, TWO, THREE, FOUR, FIVE]");
        run("ONE\nEND\n").include("[ONE]");
    }

    @Test(timeout = 2500)
    public void testTaskB1() throws Exception {
        String[] lines=run("").stringWriter.toString().split("\n");
        assertTrue("Неверный размер",lines.length==6 && lines[0].trim().equals("5"));
    }


    @Test(timeout = 2500)
    public void testTaskB2() throws Exception {
        run("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n22\n33\n44\n55\n66\n77\n88\n99\n0")
                .include("a=[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n" +
                        "b=[11, 22, 33, 44, 55, 66, 77, 88, 99, 0]\n");
    }


    @Test(timeout = 2500)
    public void testTaskC1() throws Exception {
        run("1\n2\n3\n4\n5\n6\n7\n8\n9\n44\n55\n11\n11\n12\n14\n12\n45\n38\n88\n77").
                include("3\n6\n9\n12\n12\n45\n2\n4\n6\n8\n44\n12\n14\n12\n38\n88\n1\n5\n7\n55\n11\n11\n77");
    }

    @Test(timeout = 2500)
    public void testTaskC2() throws Exception {
        run("1\n-2\n3\n4\n5\n666\n7\n8\n9\n0\n1\n-1\n3\n4\n5\n665\n7\n8\n9\n0\n").
                include("666\n665\n9\n9\n8\n8\n7\n7\n5\n5\n4\n4\n3\n3\n1\n1\n0\n0\n-1\n-2");
    }


    /*
    ===========================================================================================================
    НИЖЕ ВСПОМОГАТЕЛЬНЫЙ КОД ТЕСТОВ. НЕ МЕНЯЙТЕ В ЭТОМ ФАЙЛЕ НИЧЕГО.
    Но изучить как он работает - можно, это всегда будет полезно.
    ===========================================================================================================
     */
    //метод находит и создает класс для тестирования
    //по имени вызывающего его метода, testTaskA1 будет работать с TaskA1
    private static Testing05 run(String in) {
        Throwable t = new Throwable();
        StackTraceElement trace[] = t.getStackTrace();
        StackTraceElement element;
        int i = 0;
        do {
            element = trace[i++];
        }
        while (!element.getMethodName().contains("test"));

        String[] path = element.getClassName().split("\\.");
        String nameTestMethod = element.getMethodName();
        String clName = nameTestMethod.replace("test", "");
        clName = element.getClassName().replace(path[path.length - 1], clName);
        System.out.println("\n---------------------------------------------");
        System.out.println("Старт теста для " + clName + "\ninput:" + in);
        System.out.println("---------------------------------------------");
        return new Testing05(clName, in);
    }

    public Testing05() {
        //Конструктор тестов
    }

    //Конструктор тестов
    //    private Testing(String className) {
    //        this(className, "");
    //    }

    //Основной конструктор тестов
    private Testing05(String className, String in) {
        //this.className = className;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail("Не найден класс " + className);
        }
        reader = new StringReader(in); //заполнение ввода
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return reader.read();
            }
        };
        System.setIn(inputStream);   //перехват ввода

        System.setOut(newOut); //перехват стандартного вывода
        try {
            Class[] argTypes = new Class[]{String[].class};
            Method main = c.getDeclaredMethod("main", argTypes);
            main.invoke(null, (Object) new String[]{});

        } catch (Exception x) {
            x.printStackTrace();
        }
        System.setOut(oldOut); //возврат вывода
    }

    //проверка вывода
    private Testing05 is(String str) {
        assertTrue("Ожидается такой вывод:\n<---начало---->\n" + str + "<---конец--->",
                stringWriter.toString().equals(str));
        return this;
    }

    private Testing05 include(String str) {
        assertTrue("Строка не найдена: " + str + "\n", stringWriter.toString().contains(str));
        return this;
    }

    private Testing05 exclude(String str) {
        assertTrue("Лишние данные в выводе: " + str + "\n", !stringWriter.toString().contains(str));
        return this;
    }


    //переменные теста
    private StringWriter stringWriter = new StringWriter();
    private PrintStream oldOut = System.out;
    private StringReader reader;


    //поле для перехвата потока вывода
    private PrintStream newOut;

    {
        newOut = new PrintStream(new OutputStream() {
            private byte bytes[] = new byte[2];

            @Override
            public void write(int b) throws IOException {
                if (b < 0) { //ловим и собираем двухбайтовый UTF (первый код > 127, или в байте <0)
                    if (bytes[0] == 0) { //если это первый байт
                        bytes[0] = (byte) b; //то запомним его
                    } else {
                        bytes[1] = (byte) b; //иначе это второй байт
                        String s = new String(bytes); //соберем весь символ
                        stringWriter.append(s); //запомним вывод для теста
                        oldOut.append(s); //копию в обычный вывод
                        bytes[0] = 0; //забудем все.
                    }
                } else {
                    char c = (char) b; //ловим и собираем однобайтовый UTF
                    bytes[0] = 0;
                    if (c != '\r') {
                        stringWriter.write(c); //запомним вывод для теста
                    }
                    oldOut.write(c); //копию в обычный вывод
                }
            }
        });
    }

}
