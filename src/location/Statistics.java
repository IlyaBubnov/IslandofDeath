package location;

import entities.Entities;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Класс Statistics содержит методы для сбора статистики событий и вывода их в консоль.
public class Statistics {

    private final Island island;

    public Statistics (Island island) {
        this.island = island;
    }

    //Метод savingStatistics
    //Метод будет вести подсчет статистики по всем сущностям, находящимся на острове, и возвращать мапу, где ключом
    //будет символ юнита, а значением - кол-во таких объектов на острове.
    public Map <String, Integer> savingStatistics () {
    //Создаю новую мапу, в которой буду хранить данные о статистике для каждой сущности, обнаруженной на острове.
    //Буду использовать ConcurrentHashMap для эффективного применения в случае работы с большим кол-ом хранимых данных
    //и операций доступа к ним. Для себя:
        //- Операции чтения не требуют блокировок и выполняются параллельно.
        //- Операции записи зачастую также могут выполняться параллельно без блокировок.
        //- Элементы карты имеют значение value, объявленное как volatile. Использование volatile — это один из способов
        //обеспечения согласованного доступа к переменной разными потоками.
        Map <String, Integer> entitiesStatistics = new ConcurrentHashMap<>();
        //С помощью двух циклов fori, которые перебирают каждую локацию острова, получаю ее юниты и прохожусь по ним
        //циклом for-each, где, проходя по списку List, получаю символы всех юнитов.
        for (int i = 0; i < this.island.getLength(); i++) {
            for (int j = 0; j < this.island.getWidth(); j++) {
                Cell cell = this.island.getCells()[i][j];
                List <Entities> entities = cell.getEntitiesList();
                    for (Entities entity : entities) {
                        String entitySymbol = entity.getType();
                        //Если в мапе нет такого юнита (по символу), то добавляем его и фиксируем кол-во 1, иначе
                        //если юнит уже присутствует в мапе, то просто получаем значение по ключу и увеличиваем
                        //кол-во на 1.
                        if (!entitiesStatistics.containsKey(entitySymbol)) {
                            entitiesStatistics.put(entitySymbol, 1);
                        } else {
                        entitiesStatistics.put(entitySymbol, (entitiesStatistics.get(entitySymbol) +1));
                        }
                    }
            }
        }
        return entitiesStatistics;
    }

    //Метод printStatistics выводит статистику, собранную методом savingStatistics в мапу, в консоль.
    public void printStatistics (Map <String, Integer> entitiesStatistics) {
        //Использую stream для получения общего количества юнитов на острове (за текущий день)
        int totalEntities = entitiesStatistics
                            .values()
                            .stream()
                            .mapToInt(Integer::intValue)
                            .sum();
        System.out.println("Total entities = " + totalEntities);
        //В цикле foreach через лямбду выводим в консоль пары ключ-значение.
        entitiesStatistics
                .forEach((key, value) -> System.out.print(MessageFormat.format("{0} = {1}, ", key, value)));
                    System.out.println("\n");
    }
}
