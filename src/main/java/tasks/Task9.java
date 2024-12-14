package tasks;

import common.Person;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    if (persons == null || persons.isEmpty()) { //заменили условие в if, убрали фальш - персону
      return Collections.emptyList();
    }
    //persons.remove(0); - используем skip
    return persons.stream()
            .skip(1)
            .map(Person::firstName)
            .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    return getNames(persons).stream()
            .collect(Collectors.toSet()); //distinct не нужен, Set - всегда уникален
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    String result = "";
    if (person.secondName() != null) {
      result += person.secondName();
    }

    if (person.firstName() != null) {
      result += " " + person.firstName();
    }

    if (person.secondName() != null) {
      result += " " + person.secondName();
    }
    return result;
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
    if (persons1 == null || persons2 == null) {
      return false;
    }
    Set<Person> personSet = new HashSet<>(persons1); //преобразуем одну коллекцию в set для оптимиации поиска (О(1) вместо O(n))
    return persons2.stream()
            .anyMatch(personSet::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    if (numbers == null){
      return  0;
    }
    return numbers.filter(num -> num % 2 == 0).count(); //убрали глобальную переменную count
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  //toString для List и Set сохраняет порядок отображение элементов,
  // т.к. HashSet при создании на базе List охраняет порядок добавления
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers); //перемешали
    Set<Integer> set = new HashSet<>(integers);//LinkedHashSet сохранил порядок добавления
    assert snapshot.toString().equals(set.toString());
  }
}
