package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  /*private long count; - имеется встроенный счетчик*/

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    //persons.remove(0); - используем skip, дабы не изменять поток
    return persons.stream()
            .skip(1)
            .map(Person::firstName)
            .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons)); //distinct не нужен, Set - всегда уникален
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    Map<Integer, String> map = new HashMap<>(1);
    for (Person person : persons) {
      if (!map.containsKey(person.id())) {
        map.put(person.id(), convertPersonToString(person));
      }
    }
    return map;
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Set<Person> personSet = new HashSet<>(persons1); //преобразуем одну коллекцию в set для оптимиации поиска (О(1) вместо O(n))
    return persons2.stream()
            .anyMatch(personSet::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count(); //убрали глобальную переменную count
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке

  // Спустя пару бессонных часов я нашел ответ!
  // Фокус завязан на подкапотном устройстве HashSet
  // Хеш-функция от целого числа (int) - это само это число, поскольку это простой тип,
  // Хеш-таблица хранит данные по бакетам, номер которого определяется через хеш функцию,
  // Исходя из этих двух условий, элементы будут располагаться по возрастанию, несмотря на порядок добавления,
  // И при проходе toString выдаст нам "сортированный"  список, т.к. HashSet будет проходить по бакетам в порядке возрастания

  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers); //перемешали
    Set<Integer> set = new HashSet<>(integers);//HashSet не сохранил порядок добавления
    assert snapshot.toString().equals(set.toString()); //toString возращает отсортированную строку
  }
}
