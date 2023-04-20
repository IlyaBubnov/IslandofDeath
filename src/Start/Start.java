package Start;

import Entities.Herbivores.Rabbit;
import Entities.Predators.Wolf;
import Services.IslandRules;
import Constants.IslandSettings;
import Location.Island;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Start  {

    //Поле класса
    public static final ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);


    public static void main(String[] args)  {

        System.out.println("Shang Tsung: It Has Began!");

        //Инициализирую остров. Размеры можно задать в классе IslandSettings.
        Island island = new Island(IslandSettings.ISLAND_LENGTH, IslandSettings.ISLAND_WIDTH);

        //Передаю остров "на управление" объекту класса IslandRules. См. описание конструктора класса IslandRules.
        IslandRules islandManagement = new IslandRules(island);

        //Создаю остров заданного размера и заполняю случайными юнитами, не превышая макс кол-ва юнитов в локации.
        islandManagement.getIsland().constructingAnIsland();
        islandManagement.getIsland().fillCells(IslandSettings.MAX_UNITS_IN_ONE_CELL);

        //Потокам передаю 2 задания на выполнение:
        //1-е и основное - запускаю процесс симуляции жизни,
        //2-е - запускаю процесс сбора статистики.
        ses.scheduleWithFixedDelay(islandManagement.createSimulation(), 50, 150, TimeUnit.MILLISECONDS);
        ses.scheduleWithFixedDelay(islandManagement.getStatistics().runStatistics(), 50, 150, TimeUnit.MILLISECONDS);
    }
}
