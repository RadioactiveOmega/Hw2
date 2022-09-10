package homework;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexExamples {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }

        @Override
        public String toString() {
            return name + " (" + id + ")";
        }
    }

    private static Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia"),
    };
        /*  Raw data:

        0 - Harry
        0 - Harry
        1 - Harry
        2 - Harry
        3 - Emily
        4 - Jack
        4 - Jack
        5 - Amelia
        5 - Amelia
        6 - Amelia
        7 - Amelia
        8 - Amelia

        **************************************************

        Duplicate filtered, grouped by name, sorted by name and id:

        Amelia:
        1 - Amelia (5)
        2 - Amelia (6)
        3 - Amelia (7)
        4 - Amelia (8)
        Emily:
        1 - Emily (3)
        Harry:
        1 - Harry (0)
        2 - Harry (1)
        3 - Harry (2)
        Jack:
        1 - Jack (4)
     */

    public static void main(String[] args) {
        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Duplicate filtered, grouped by name, sorted by name and id:");
        System.out.println();

        task1(RAW_DATA);

        System.out.println();
        System.out.println("**************************************************");

        System.out.println("Task2\n");
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 6);
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 7);
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 8);
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 9);
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 11);
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 12);
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 13);
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 14);
        task2(new int[]{3, 4, 2, 7, 7, 9,0}, 15);

        System.out.println();
        System.out.println("**************************************************");

        System.out.println("Task3\n");
        System.out.println(
                fuzzySearch("car", "ca6$$#_rtwheel") + "\n"  +  // true
                fuzzySearch("cwhl", "cartwheel") + "\n"  +       // true
                fuzzySearch("cwhee", "cartwheel") + "\n"  +      // true
                fuzzySearch("cartwheel", "cartwheel") + "\n"  +  // true
                fuzzySearch("cwheeel", "cartwheel") + "\n"  +    // false
                fuzzySearch("lw", "cartwheel") + "\n"            // false
                         );


        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться
                Key:Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */



        /*
        Task2

            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару менно в скобках, которые дают сумму - 10
         */



        /*
        Task3
            Реализовать функцию нечеткого поиска
                    fuzzySearch("car", "ca6$$#_rtwheel"); // true
                    fuzzySearch("cwhl", "cartwheel"); // true
                    fuzzySearch("cwhee", "cartwheel"); // true
                    fuzzySearch("cartwheel", "cartwheel"); // true
                    fuzzySearch("cwheeel", "cartwheel"); // false
                    fuzzySearch("lw", "cartwheel"); // false
         */
    }


    public static void task1(Person[] data){
        List<Person> people = Arrays.stream(data)           // Deleted duplicates, sorted by name and id
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(Person::getName)
                        .thenComparing(Person::getId))
                .toList();

        for(int i = 0; i < people.size(); i++){

            String key = people.get(i).getName();           // Taking unique key (name)
            System.out.println("Key:" + key);

            long value = people.stream()                    // Filtering by name == key and counting
                    .filter(e -> e.getName().equals(key))
                    .count();

            System.out.println("Value:" + value);

            if(i + value < people.size())                   // Skipping the same names
                i+=value - 1;
            else
                return;
        }
    }

    public static void task2(int[] arr, int sum){

        if(arr == null){
            System.out.println("Array is null");
            return;
        }

        List<Integer> data = Arrays.stream(arr).boxed().toList(); // Or use Arrays.stream(ints).boxed().collect(Collectors.toList());
                                                                  // Arrays.stream(arr).boxed().toList(); for Java 16+

        for(int i: data){
            if(data.contains(sum - i) && data.indexOf(sum - i) != data.lastIndexOf(i)){

                System.out.println("[" + i + ", " + (sum - i) + "]");
                return;

            }
        }
        System.out.println("Pair not found");
    }

    public static boolean fuzzySearch(String patternStr, String searchStr){

        if(patternStr == null || searchStr == null) return false;

        if(searchStr.contains(patternStr)) return true;

        StringBuilder patternSB = new StringBuilder();             // Making a pattern like [something]letter[something]:
                                                                   // car -> .*c.*a.*r.*
        patternSB.append(".*");                                    // .* in regex is any character in any quantity

        for(char c: patternStr.toCharArray()){
            patternSB.append(c).append(".*");
        }

        Pattern pattern = Pattern.compile(patternSB.toString());

        Matcher matcher = pattern.matcher(searchStr);

        return matcher.matches();
    }
}
