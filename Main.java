import java.util.Optional;
import java.util.Scanner;
import java.util.Collection;

class Box<T> {
    private T item;

    public Box() {
        this.item = null;
    }

    public boolean isEmpty() {
        return item == null;
    }

    public void put(T item) {
        if (!isEmpty()) {
            throw new IllegalStateException("Коробка уже заполнена");
        }
        this.item = item;
    }

    public T get() {
        if (isEmpty()) {
            throw new IllegalStateException("Коробка пуста");
        }
        T temp = item;
        item = null;
        return temp;
    }

    public double getValueAsDouble() {
        if (isEmpty()) {
            throw new IllegalStateException("Коробка пуста");
        }
        if (item instanceof Number) {
            return ((Number) item).doubleValue();
        } else {
            throw new IllegalStateException("Значение в коробке не является числом");
        }
    }
}

class Storage<T> {
    private final Optional<T> value;

    public Storage(T value) {
        this.value = Optional.ofNullable(value);
    }

    public T getValueOrDefault(T defaultValue) {
        return value.orElse(defaultValue);
    }
}

public class Main {

    public static <T> T readValueFromConsole(Scanner scanner, Class<T> type) {
        try {
            if (type == Integer.class) {
                String input = scanner.nextLine().trim();
                return input.isEmpty() ? null : type.cast(Integer.parseInt(input));
            } else if (type == Double.class) {
                String input = scanner.nextLine().trim();
                return input.isEmpty() ? null : type.cast(Double.parseDouble(input));
            } else if (type == Float.class) {
                String input = scanner.nextLine().trim();
                return input.isEmpty() ? null : type.cast(Float.parseFloat(input));
            } else if (type == String.class) {
                String input = scanner.nextLine().trim();
                return input.isEmpty() ? null : type.cast(input);
            } else {
                throw new IllegalArgumentException("Неподдерживаемый тип: " + type);
            }
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат числа. Пожалуйста, введите корректное число.");
            return null;
        } catch (Exception e) {
            System.err.println("Ошибка чтения ввода: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("          Задание 1.1");
        Box<Integer> intBox = new Box<>();
        System.out.print("Введите целое число для коробки: ");
        
        int number = Integer.parseInt(scanner.nextLine());
        intBox.put(number);
        System.out.println("Число " + number + " помещено в коробку.");
        System.out.println("Значение из коробки: " + intBox.get());
        

        System.out.println("          Задание 1.2");
        System.out.print("Создаем Storage для чисел. Введите число (или оставьте пустым): ");
        String str3 = readValueFromConsole(scanner, String.class);
        Storage<String> strStorage3 = new Storage<>(str3);
        processStorage(strStorage3, "0");

        System.out.print("Создаем Storage для чисел. Введите число (или оставьте пустым): ");
        Integer num2 = readValueFromConsole(scanner, Integer.class);
        Storage<Integer> numStorage2 = new Storage<>(num2);
        processStorage(numStorage2, -1);

        System.out.print("Создаем Storage для строк. Введите строку (или оставьте пустым): ");
        String str1 = readValueFromConsole(scanner, String.class);
        Storage<String> strStorage = new Storage<>(str1);
        processStorage(strStorage, "default");

        System.out.print("Создаем Storage для строк. Введите строку (или оставьте пустым): ");
        String str2 = readValueFromConsole(scanner, String.class);
        Storage<String> strStorage2 = new Storage<>(str2);
        processStorage(strStorage2, "hello world");

        System.out.println("          Задание 2.2");
        Collection<Box<? extends Number>> boxes = new java.util.ArrayList<>();

        System.out.print("Введите целое число для первой коробки: ");
        Integer intValue = readValueFromConsole(scanner, Integer.class);
        boxes.add(new Box<Integer>() {{ put(intValue); }});

        System.out.print("Введите число с плавающей точкой для второй коробки: ");
        Double doubleValue = readValueFromConsole(scanner, Double.class);
        boxes.add(new Box<Double>() {{ put(doubleValue); }});

        System.out.print("Введите число с плавающей точкой для третьей коробки: ");
        Float floatValue = readValueFromConsole(scanner, Float.class);
        boxes.add(new Box<Float>() {{ put(floatValue); }});

        boxes.add(new Box<Integer>()); // Пустая коробка

        System.out.println("Содержимое коробок:");
        for (int i = 0; i < boxes.size(); i++) {
            Box<? extends Number> box = boxes.toArray(new Box[0])[i]; //извлекаем по индексу
            System.out.print("Коробка " + (i + 1) + ": ");
            if (box.isEmpty()) {
                System.out.println("пустая");
            } else {
                System.out.println(box.getValueAsDouble());
            }
        }

        double maxValue = findMax(boxes);
        System.out.println("Максимальное значение: " + maxValue);

        scanner.close();
    }

    public static <T> void processStorage(Storage<T> Storage, T defaultValue) {
        System.out.println("Значение из хранилища: " + Storage.getValueOrDefault(defaultValue));
    }

    public static double findMax(Collection<Box<? extends Number>> boxes) {
        double max = Double.NEGATIVE_INFINITY;

        for (Box<? extends Number> box : boxes) {
            try {
                Number value = box.get();
                if (value != null) {
                    max = Math.max(max, value.doubleValue());
                }
            } catch (IllegalStateException e) {
                // Игнорируем пустые коробки
            }
        }

        return max;
    }
}
