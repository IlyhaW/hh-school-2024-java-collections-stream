package tasks;

import common.Person;
import common.PersonService;
import common.PersonWithResumes;
import common.Resume;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/*
  Еще один вариант задачи обогащения
  На входе коллекция персон
  Сервис умеет по personId искать их резюме (у каждой персоны может быть несколько резюме)
  На выходе нужно получить объекты с персоной и ее списком резюме
 */
public class Task8 {
  private final PersonService personService;

  public Task8(PersonService personService) {
    this.personService = personService;
  }

  public Set<PersonWithResumes> enrichPersonsWithResumes(Collection<Person> persons) {
    // Собираем все personId из коллекции персон
    Set<Integer> personIds = persons.stream()
            .map(Person::id)
            .collect(Collectors.toSet());

    // Вызываем метод findResumes один раз для всех ID
    Set<Resume> allResumes = personService.findResumes(personIds);

    // Группируем резюме по personId
    var resumesByPersonId = allResumes.stream()
            .collect(Collectors.groupingBy(Resume::personId, Collectors.toSet()));

    // Преобразуем каждую персону в PersonWithResumes
    return persons.stream()
            .map(person -> new PersonWithResumes(
                    person,
                    resumesByPersonId.getOrDefault(person.id(), Set.of()) // Если резюме нет, возвращаем пустой Set
            ))
            .collect(Collectors.toSet());
  }

}
