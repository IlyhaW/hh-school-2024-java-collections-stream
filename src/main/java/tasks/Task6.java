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

  private static String formatDescription(Person person, Area area) {
    return person.firstName() + " - " + area.getName();
  }

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    // Создаем отображение ID региона в его имя
    Map<Integer, Area> areaIdToArea = areas.stream()
            .collect(Collectors.toMap(Area::getId, area -> area));

    // Генерируем строки "Имя - регион" через стримы
    return persons.stream()
            .flatMap(person -> personAreaIds.getOrDefault(person.id(), Collections.emptySet()).stream()
                    .map(areaId -> areaIdToArea.get(areaId))
                    .filter(Objects::nonNull)
                    .map(area -> formatDescription(person, area)))
            .collect(Collectors.toSet());
  }


}