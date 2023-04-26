package runner;

import services.IslandRules;
import constants.IslandSettings;
import location.Island;
import java.text.MessageFormat;

public class Start  {

    //Поле класса
    private final IslandRules islandManagement;

    //Конструктор класса
    public Start () {
        //Инициализирую остров. Размеры можно задать в классе IslandSettings.
        Island island = new Island(IslandSettings.ISLAND_LENGTH, IslandSettings.ISLAND_WIDTH);
        //Передаю остров "на управление" объекту класса IslandRules. См. описание конструктора класса IslandRules.
        this.islandManagement = new IslandRules(island);
    }
    //В методе start создаю остров заданного размера и заполняю случайными юнитами, не превышая макс кол-ва юнитов в локации.
    public void start () {
        this.islandManagement.getIsland().constructingAnIsland();
        this.islandManagement.getIsland().fillCells(IslandSettings.MAX_UNITS_IN_ONE_CELL);

        for (int i = 1; i <= IslandSettings.MAX_NUMBER_OF_DAYS; i++) {
            islandManagement.createSimulation();
            System.out.println(MessageFormat.format("DAY {0} of {1}", i, IslandSettings.MAX_NUMBER_OF_DAYS));
            islandManagement.getStatistics().printStatistics(islandManagement.getStatistics().savingStatistics());
        }
    }
}