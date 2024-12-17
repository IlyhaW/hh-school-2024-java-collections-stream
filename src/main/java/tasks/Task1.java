package tasks;

import common.Person;
import common.PersonService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы - ассимптотика работы О(m+n), где мы проходим по Set <Person> длинной n, и выполняем поиск в Map из m элементов списка List personIds
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    Map <Integer, Person> idPersonMap = persons.stream()
            .collect(Collectors.toMap(Person::id, person -> person));
    return personIds.stream()
            .map(idPersonMap::get)
            .collect(Collectors.toList());
  }
}

