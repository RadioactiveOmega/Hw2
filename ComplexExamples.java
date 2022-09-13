package homework;


import java.util.*;
import java.util.stream.Collectors;

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
                fuzzySearch("car", "ca6$$#_rtwheel") + "\n"  +   // true
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

        Arrays.stream(data)
                .filter(Objects::nonNull)
                .filter(person -> person.getName() != null)                                             // Map will not work if there are NULL in the names
                .distinct()                                                                             // Delete duplicates
                .sorted(Comparator.comparing(Person::getId))
                .collect(Collectors.groupingBy(Person::getName,                                         // Get a map with <String, Long>
                        Collectors.counting()))
                .forEach((key, value) -> System.out.println("Key:" + key +"\n" + "Value:" + value));    // Print map
    }

    public static void task2(int[] inputArr, int sum){

        if(inputArr == null){
            System.out.println("Array is null");
            return;
        }

        int[] arr = Arrays.stream(inputArr).filter(el -> el <= sum).toArray();

        for (int i = 0; i < arr.length; i++) {
            int j = i + 1;

            while(j < arr.length) {
                if (arr[i] + arr[j] == sum) {
                    System.out.println("[" + arr[i] + ", " + arr[j] + "]");
                    return;
                }
                j++;
            }

        }
        System.out.println("Pair not found");


        // Or use tht realization for print all pairs
/*        IntStream.range(0, arr.length)
                .forEach(i -> IntStream.range(0, arr.length)
                        .filter(j -> i < j && arr[i] + arr[j] == sum)
                        .forEach(j -> System.out.println(Arrays.toString(new int[]{arr[i], arr[j]})))); */


    }

    public static boolean fuzzySearch(String patternStr, String searchStr){

        if(patternStr == null || searchStr == null) {return false;}

        if(searchStr.contains(patternStr)) {return true;}

        List<Character> pattern = new ArrayList<>(patternStr.chars().mapToObj((i) -> (char) i).toList());   // Convert String -> List<Character>
        List<Character> search = new ArrayList<>(searchStr.chars().mapToObj((i) -> (char) i).toList());

        search.removeIf(character -> !pattern.contains(character));                                         // Removing all char from searchStr, that are not in patternStr
        return  search.equals(pattern);



        // Or you can use that realization
        /*int patternIndex = 0;

        for (int i = 0; i < searchStr.length(); i++) {
            if (patternStr.charAt(patternIndex) == searchStr.charAt(i)) {   // We are looking for the same letter and if found,
                patternIndex++;                                             // then we are looking for the next
                if (patternIndex == patternStr.length()) {
                    return true;
                }
            }
        }
        return false;*/



    }
}
