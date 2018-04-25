package by.it._tasks_.lesson06;

import org.junit.Test;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("all") //море warnings. всех прячем.

//поставьте курсор на следующую строку и нажмите Ctrl+Shift+F10
public class Testing06 {

    @Test(timeout = 2500)
    public void testTaskA1() throws Exception {
        run("").include("Шарик 5").include("Тузик 3");

        System.out.println("Проверка класса Dog");
        Class cl = findClass("Dog");
        assert cl != null;

        Object dog = cl.newInstance();
        assert dog != null;

        findMethod(cl, "setName", String.class).invoke(dog, "Шарик");
        assertEquals("\nИмя не прочиталось ", "Шарик", (String) findMethod(cl, "getName").invoke(dog));

        findMethod(cl, "setAge", int.class).invoke(dog, 5);
        assertEquals("\nВозраст не прочитался ", 5, (int) findMethod(cl, "getAge").invoke(dog));
        System.out.println("Проверка класса Dog - OK");
    }


    @Test(timeout = 2500)
    public void testTaskA2() throws Exception {
        run("").include("Кличка: Шарик. Возраст: 5").include("Кличка: Тузик. Возраст: 3");

        System.out.println("Проверка класса Dog");
        Class cl = findClass("Dog");
        assert cl != null;

        Object dog = cl.newInstance();
        assert dog != null;

        findMethod(cl, "setName", String.class).invoke(dog, "Шарик");
        assertEquals("\nИмя не прочиталось ", "Шарик", (String) findMethod(cl, "getName").invoke(dog));

        findMethod(cl, "setAge", int.class).invoke(dog, 5);
        assertEquals("\nВозраст не прочитался ", 5, (int) findMethod(cl, "getAge").invoke(dog));

        assertEquals("\ntoString() не работает как ожидается ", "Кличка: Шарик. Возраст: 5", (String) findMethod(cl, "toString").invoke(dog));
        System.out.println("Проверка класса Dog - OK");
    }

    @Test(timeout = 2500)
    public void testTaskB1() throws Exception {
        System.out.println("Проверка класса Dog");
        Class cl = findClass("Dog");
        assert cl != null;

        //create dogs
        int count = 10;
        Object[] dogs = (Object[]) Array.newInstance(cl, count);
        double controlAge = 0;
        for (int i = 0; i < dogs.length; i++) {
            Object dog = cl.newInstance();
            assert dog != null;
            findMethod(cl, "setName", String.class).invoke(dog, "dog" + i);
            int age = (i + 1);
            findMethod(cl, "setAge", int.class).invoke(dog, age);
            controlAge = controlAge + age;
            dogs[i] = dog;
        }
        controlAge = controlAge / dogs.length;

        Class hlp = findClass("DogHelper");
        assert hlp != null;

        //ищем метод вывода имен
        findMethod(hlp, "printAllNames", dogs.getClass());

        //ищем метод расчета среднего
        Method averageAge = findMethod(hlp, "averageAge", dogs.getClass());
        double avg = (double) averageAge.invoke(null, new Object[]{dogs});
        assertEquals("Ожидается другой возраст для 10 тестовых собак", controlAge, avg, 0.001);

        System.out.println("Проверка класса Dog - OK");
        //проверка запуска на 5 ожидаемых собаках
        run("").include("Шарик Жучка Бобик Барбос Полкан").include("3.0");

    }


    @Test(timeout = 2500)
    public void testTaskC1() throws Exception {
        Class cl = findClass("Dog");
        assert cl != null;

        for (int i = 1; i < 25; i++) {
            //Шансы на победу = 0.2 * возраст + 0.3 * вес + 0.5 * силу укуса.
            String n1 = "dog" + i;
            String n2 = "pup" + i;
            int age1 = 1+(int) (Math.random() * 10);
            int age2 = 1+(int) (Math.random() * 10);
            int w1 = 1+(int) (Math.random() * 10);
            int w2 = 1+(int) (Math.random() * 10);
            double p1 = 1.1+(int)(Math.random() * 15);
            double p2 = 1.1+(int)(Math.random() * 15);
            double win1 = 0.2 * age1 + 0.3 * w1 + 0.5 * p1;
            double win2 = 0.2 * age2 + 0.3 * w2 + 0.5 * p2;
            boolean expected = win1 > win2;

            //делаем собак
            Object dog1 = cl.newInstance();
            Object dog2 = cl.newInstance();
            assert dog1 != null;
            assert dog2 != null;

            findMethod(cl, "setName", String.class).invoke(dog1, n1);
            findMethod(cl, "setAge", int.class).invoke(dog1, age1);
            findMethod(cl, "setWeight", int.class).invoke(dog1, w1);
            findMethod(cl, "setPower", double.class).invoke(dog1, p1);

            findMethod(cl, "setName", String.class).invoke(dog2, n2);
            findMethod(cl, "setAge", int.class).invoke(dog2, age2);
            findMethod(cl, "setWeight", int.class).invoke(dog2, w2);
            findMethod(cl, "setPower", double.class).invoke(dog2, p2);

            System.out.println("--------- Начало эксперимента ----------------");
            //определим победителя
            boolean actual = (boolean) findMethod(cl, "win", cl).invoke(dog1, dog2);
            System.out.println("1) "+n1+ " Шанс:"+win1+"\n2) "+n2+" Шанс:"+win2);
            if (expected)
                System.out.println("По тесту должен победить "+n1);
            else
                System.out.println("По тесту должен победить "+n2);
            assertEquals("Победитель определен неверно", expected, actual);

            //проверим их же через run
            String in = String.format(
                    "%s\n%d\n%d\n%f\n" + "%s\n%d\n%d\n%f\n",
                    n1, age1, w1, p1,
                    n2, age2, w2, p2
            );
            System.out.println("------- Проверка поединка через ввод с клавиатуры ------");
            run(in).include((expected)?n1:n2);
            System.out.println("--------- Конец эксперимента ----------------\n\n");

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
    private static Testing06 run(String in) {
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
        return new Testing06(clName, in);
    }

    public Testing06() {
        //Конструктор тестов
    }

    //Конструктор тестов
    //    private Testing(String className) {
    //        this(className, "");
    //    }

    //Основной конструктор тестов
    private Testing06(String className, String in) {
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
    private Testing06 is(String str) {
        assertTrue("Ожидается такой вывод:\n<---начало---->\n" + str + "<---конец--->",
                stringWriter.toString().equals(str));
        return this;
    }

    private Testing06 include(String str) {
        assertTrue("Строка не найдена: " + str + "\n", stringWriter.toString().contains(str));
        return this;
    }

    private Testing06 exclude(String str) {
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
