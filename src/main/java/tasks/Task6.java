package tasks;

import common.Area;
import common.Person;
import java.util.*;
import java.util.stream.Collectors;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 {

  private static String formatDescription(Person person, String areaName) {
    return person.firstName() + " - " + areaName;
  }

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    // Создаем отображение ID региона в его имя
    Map<Integer, String> areaIdToName = areas.stream()
            .collect(Collectors.toMap(Area::getId, Area::getName));

    // Генерируем строки "Имя - регион" через стримы
    return persons.stream()
            .flatMap(person -> personAreaIds.getOrDefault(person.id(), Collections.emptySet()).stream()
                    .map(areaId -> areaIdToName.get(areaId))
                    .filter(Objects::nonNull)
                    .map(areaName -> formatDescription(person, areaName))
            )
            .collect(Collectors.toSet());
  }


}