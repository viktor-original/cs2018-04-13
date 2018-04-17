package by.it._tasks_.lesson02;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

@SuppressWarnings("all")

//поставьте курсор на следующую строку и нажмите Ctrl+Shift+F10
public class Testing02 {


    @Test(timeout = 1500)
    public void testTaskA1() throws Exception {
        run("").include("Hello world!");
    }

    @Test(timeout = 1500)
    public void testTaskA2() throws Exception {
        run("").include(
                "Я начинаю изучать Java!\n" +
                        "Я начинаю изучать Java!\n" +
                        "Я начинаю изучать Java!\n" +
                        "Я начинаю изучать Java!\n" +
                        "Я начинаю изучать Java!\n"
        );
    }

    @Test(timeout = 1500)
    public void testTaskA3() throws Exception {
        run("").include("3*3+4*4=25");
    }

    @Test(timeout = 1500)
    public void testTaskB1() throws Exception {
        run("7").include("49");
    }

    @Test(timeout = 1500)
    public void testTaskB2() throws Exception {
        run("").include("20");
    }

    @Test(timeout = 1500)
    public void testTaskB3() throws Exception {
        run("").include("C Новым Годом");
    }

    @Test(timeout = 1500)
    public void testTaskC1() throws Exception {
        run("7\n3\n").include("Sum = 10\n");
    }

    @Test(timeout = 1500)
    public void testTaskC2() throws Exception {
        run("34\n26\n").include(
                "DEC:34+26=60\n" +
                        "BIN:100010+11010=111100\n" +
                        "HEX:22+1a=3c\n" +
                        "OCT:42+32=74\n");
    }

    @Test(timeout = 1500)

    public void testTaskC3() throws Exception {
        run("75\n").include("29.51\n");
        Testing02 t = run("100\n").include("39.35\n");
        try {
            Method m = t.aClass.getDeclaredMethod("getWeight", int.class);
            assertEquals((Double) m.invoke(null, 100), 39.35, 1e-100);
            assertEquals((Double) m.invoke(null, 75), 29.51, 1e-100);
        } catch (NoSuchMethodException e) {
            org.junit.Assert.fail("Метод getWeight не найден");
        }
    }

    /*
    ===========================================================================================================
    НИЖЕ ВСПОМОГАТЕЛЬНЫЙ КОД ТЕСТОВ. НЕ МЕНЯЙТЕ В ЭТОМ ФАЙЛЕ НИЧЕГО.
    Но изучить как он работает - можно, это всегда будет полезно.
    ===========================================================================================================
     */

    private Class findClass(String SimpleName) {
        String full = this.getClass().getName();
        String dogPath = full.replace(this.getClass().getSimpleName(), SimpleName);
        try {
            return Class.forName(dogPath);
        } catch (ClassNotFoundException e) {
            fail("\nТест не пройден. Класс " + SimpleName + " не найден.");
        }
        return null;
    }

    private Method findMethod(Class<?> cl, String name, Class... param) {
        try {
            return cl.getDeclaredMethod(name, param);
        } catch (NoSuchMethodException e) {
            fail("\nТест не пройден. Метод " + cl.getName() + "." + name + " не найден");
        }
        return null;
    }

    private Object invoke(Method method, Object o, Object... value) {
        try {
            return method.invoke(o, value);
        } catch (Exception e) {
            fail("\nНе удалось вызвать метод " + method.getName());
        }
        return null;
    }


    //метод находит и создает класс для тестирования
    //по имени вызывающего его метода, testTaskA1 будет работать с TaskA1
    private static Testing02 run(String in) {
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
        return new Testing02(clName, in);
    }

    public Testing02() {
        //Конструктор тестов
    }

    //Конструктор тестов
    //    private Testing(String className) {
    //        this(className, "");
    //    }
    private String className;
    Class<?> aClass;
    //Основной конструктор тестов
    private Testing02(String className, String in) {
        //this.className = className;
        aClass = null;
        try {
            aClass = Class.forName(className);
            this.className = className;

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
            Method main = aClass.getDeclaredMethod("main", argTypes);
            main.invoke(null, (Object) new String[]{});

        } catch (Exception x) {
            x.printStackTrace();
        }
        System.setOut(oldOut); //возврат вывода
    }

    //проверка вывода
    private Testing02 is(String str) {
        assertTrue("Ожидается такой вывод:\n<---начало---->\n" + str + "<---конец--->",
                stringWriter.toString().equals(str));
        return this;
    }

    private Testing02 include(String str) {
        assertTrue("Строка не найдена: " + str + "\n", stringWriter.toString().contains(str));
        return this;
    }

    private Testing02 exclude(String str) {
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
