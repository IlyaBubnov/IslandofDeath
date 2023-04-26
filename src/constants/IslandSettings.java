package constants;

public class IslandSettings {

    //В полях класса IslandSettings задаю исходные данные:
    public static final int ISLAND_LENGTH = 5;                //Длина (высота) острова.
    public static final int ISLAND_WIDTH = 5;                 //Ширина острова.
    public static final int MAX_UNITS_IN_ONE_CELL = 30;       //Максимум юнитов в одной клетке.
    public static final int LOOSE_HEALTH_EVERY_DAY = 50;      //Кол-во % от здоровья, которое юнит теряет каждый день.
    public static final int RESTORE_HEALTH_IF_DO_NOTHING = 5; //Кол-во % от здоровья,кот. юнит получает, ничего не делая.
    public static final int MAX_NUMBER_OF_DAYS = 90;          //Максимальное кол-во дней жизни (симуляции).

}
