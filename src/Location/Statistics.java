package Location;

import Entities.Entities;
import Services.IslandRules;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

    private final Island island;

    public Statistics (Island island) {
        this.island = island;
    }

    //Метод savingStatistics
    //Метод будет вести подсчет статистики по всем сущностям, находящимся на острове, и возвращать мапу, где ключом
    //будет символ юнита, а значением - кол-во таких объектов на острове.
    private Map <String, Integer> savingStatistics () {
    //Создаю новую мапу, в кот. буду хранить данные о статистике для каждой сущности, обнаруженной на острове.
        Map <String, Integer> entitiesStatistics = new HashMap<>();
    //С помощью двух циклов fori, кот. перебирают каждую локацию острова, получаю ее юниты и прохожусь по ним
    //циклом for-each, где, проходя по списку List, получаю символы всех юнитов
        for (int i = 0; i < island.getLength(); i++) {
            for (int j = 0; j < island.getWidth(); j++) {
                Cell cell = island.getCells()[i][j];
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
    private void printStatistics (Map <String, Integer> entitiesStatistics) {
        //Использую форматирование строк с помощью класса `MessageFormat`для "читабельности".
        System.out.println(MessageFormat.format("DAY ###{0}###", IslandRules.DAY.get()));
        //В цикле foreach через лямбду выводим в консоль пары ключ-значение.
            entitiesStatistics.forEach((key, value) ->
                System.out.print(MessageFormat.format("{0} - {1}, ", key, value)));
        System.out.println();
    }

    //Метод runStatistics
    //Буду использовать объект анонимного класса `Runnable` для описания задачи, которую необходимо выполнить
    //в отдельном потоке. Этот объект передается в конструктор класса `Thread`, который создает новый поток выполнения
    //и связывает его с заданным объектом `Runnable`. Т.о., на практике использование объекта типа `Runnable` в данном
    //методе позволяет создавать отдельные потоки выполнения и гарантирует асинхронное выполнение задачи без
    //блокирования основного потока.
    public Runnable runStatistics () {
        return () -> printStatistics(savingStatistics());
    }
}
