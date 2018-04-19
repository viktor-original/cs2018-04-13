package by.it._tasks_.lesson03;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("all") //море warnings. всех прячем.

//поставьте курсор на следующую строку и нажмите Ctrl+Shift+F10
public class Testing03 {


    @Test(timeout = 2500)
    public void testTaskA1() throws Exception {
        run("7 2").include("9 5 14 3 1\n9.0 5.0 14.0 3.5 1.0");
    }

    @Test(timeout = 2500)
    public void testTaskA2() throws Exception {
        Testing03 testing = run("");
        String[] lines = testing.stringWriter.toString().trim().split("\\n");
        if (lines.length < 5)
            fail("Недостаточно строк");
        if (!lines[0].equals("Мое любимое стихотворение:") &&
            !lines[0].equals("Моё любимое стихотворение:"))
            fail("Нет заголовка: Мое любимое стихотворение:");
        String old = "old";
        for (String s : lines) {
            if (s.length() < 5)
                fail("Слишком короткие строки");
            if (old.equals(s))
                fail("Есть одинаковые строки");
            old = s;
        }
    }

    @Test(timeout = 2500)
    public void testTaskB1() throws Exception {
        run("").include("575.222")
                .include("111.111 ")
                .include("7 73 273 ")
                .include("111.111");
    }

    @Test(timeout = 2500)
    public void testTaskB2() throws Exception {
        run("2 5 3").include("-1.0").include("-1.5");
        run("2 4 2").include("-1.0\n");
        run("2 2 2").include("Отрицательный дискриминант");
    }

    @Test(timeout = 2500)
    public void testTaskC1() throws Exception {
        try {
            Method m = run("").aClass.getDeclaredMethod("convertCelsiumToFahrenheit", int.class);
            assertEquals(104.0, (double) m.invoke(null, 40), 1e-22);
            assertEquals(68.0, (double) m.invoke(null, 20), 1e-22);
            assertEquals(32.0, (double) m.invoke(null, 0), 1e-22);
        } catch (NoSuchMethodException e) {
            org.junit.Assert.fail("Метод convertCelsiumToFahrenheit не найден или не работает");
        }
    }

    @Test(timeout = 2500)
    public void testTaskC2() throws Exception {
        try {
        Method m = run("").aClass.getDeclaredMethod("sumDigitsInNumber", int.class);
        assertEquals((int) m.invoke(null, 5467), 22);
        assertEquals((int) m.invoke(null, 5555), 20);
        assertEquals((int) m.invoke(null, 1111), 4);
        assertEquals((int) m.invoke(null, 9993), 30);
        } catch (NoSuchMethodException e) {
            org.junit.Assert.fail("Метод sumDigitsInNumber не найден");
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
    private static Testing03 run(String in) {
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
        return new Testing03(clName, in);
    }

    public Testing03() {
        //Конструктор тестов
    }

    //Конструктор тестов
    //    private Testing(String className) {
    //        this(className, "");
    //    }
    private String className;
    Class<?> aClass;
    //Основной конструктор тестов
    private Testing03(String className, String in) {
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
    private Testing03 is(String str) {
        assertTrue("Ожидается такой вывод:\n<---начало---->\n" + str + "<---конец--->",
                stringWriter.toString().equals(str));
        return this;
    }

    private Testing03 include(String str) {
        assertTrue("Строка не найдена: " + str + "\n", stringWriter.toString().contains(str));
        return this;
    }

    private Testing03 exclude(String str) {
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
