package Location;

import Services.EntityCreation;
import Services.Randomizer;
import Constants.TypesOfEntities;
import Entities.Entities;

//Класс Island состоит из локаций - клеток (Cell).
public class Island {

    //Параметры острова.
    private final int length;       //Длина ВЫСОТА острова (в клетках).
    private final int width;        //Ширина острова (в клетках).
    private final Cell [][] cells;  //Двумерный массив, представляющий остров.

    //Создаем конструктор, который, зная два пар-ра длину и ширину, создает остров - двумерный массив,
    //состоящий из локаций-клеток.
    public Island(int length, int width) {
        this.length = length;
        this.width = width;
        this.cells = new Cell[length][width];
    }

    //Геттеры для всех полей класса
    public int getLength() {
        return length;
    }
    public int getWidth() {
        return width;
    }
    public Cell[][] getCells() {
        return cells;
    }

    //Метод constructingAnIsland(), используя заданные длину и ширину острова, создает необходимое (длина*ширину)
    //коло-во локаций-ячеек. Например, задали длину в 3 ячейки,а ширину - в 5, соответственно метод создаст
    //(инициализирует) двумерный массив cells на 15 локаций ([3][5]).
    public void constructingAnIsland() {
        for (int i = 0; i < this.getLength(); i++) {    //проходим по высоте (строкам)
            for (int j = 0; j < this.getWidth(); j++) { //проходим по ширине (столбам - кол-во ячеек в строке)
                cells [i][j] = new Cell(i, j);          //в ячейке ([0][0] например) создаем локацию класса Cell
            }
        }
    }

    //Метод fillCells заполняет каждую локацию (ячейку двумерного массива) объектами.
    //Используя циклы "for-i" и while, проходим по всем ячейкам массива cells и заполняем их произвольными объектами
    //до тех пор, пока не достигнуто максимальное количество объектов в каждой ячейке (int random).
    //Т.о. в каждую ячейку-локацию острова помещаем свой список entitiesList класса Cell, который и будет содержать
    //произвольное кол-во юнитов, тем самым в каждой локации мы будем иметь разное кол-во юнитов.
    public void fillCells (int maxUnitsInOneCell) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                //entityCount - пер-я счетчик для цикла while
                int entityCount = 0;
                //в пер-ю random сох-ем случайное число в диапазоне от 0 до maxUnitsInOneCell
                int random = Randomizer.getRandomIntValue(maxUnitsInOneCell);
                //используя метод addEntity класса Cell, заполняем entitiesList произвольными юнитами, что
                //достигается за счет метода getRandomEntity()
                while (entityCount <= random) { //
                    cells [i][j].addEntity(getRandomEntity());
                    entityCount++;
                }
            }
        }
    }

    //Метод getRandomEntity создает случайный юнит, используя класс-фабрику EntityCreation и класс Randomizer.
    private Entities getRandomEntity() {
        //В пер-ю listOfEntities сохраняем массив-список юнитов из констант TypesOfEntities.
        TypesOfEntities[] listOfEntities = TypesOfEntities.values();
        //В пер-ю entityType сохраняем значение (юнит) из случайной ячейки только-что созданного массива юнитов,
        //используя Randomizer (в [] получаем случайное число от 0 до размера списка listOfEntities).
        TypesOfEntities entityType = listOfEntities [Randomizer.getRandomIntValue(listOfEntities.length)];
        //Используя фабрику по созданию юнитов возвращаем случайно полученный юнит.
        return EntityCreation.createEntity(entityType);
    }
}
