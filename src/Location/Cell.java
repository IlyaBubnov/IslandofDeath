package Location;

import Entities.Entities;
import java.util.ArrayList;

//Класс "клетка" является одной из локаций острова. В каждой локации что-то происходит (размножение, поедание).
//Между клетками (по локациям) животные перемещаются с заданной скоростью (maxUnitSpeed) в строго определенных
//направлениях (класс Movement в константах).
public class Cell   {
    private final int y;  //Координаты точки (вертикаль). Ну или "ряд".
    private final int x;  //Координаты точки (горизонталь). Ну или "столбец".
    private final ArrayList <Entities> entitiesList;    //Список юнитов, содержащихся в конкретной локации.

    //Конструктор, принимающий координаты, сохраняет в поля класса список юнитов, а также координаты ячейки (локации).
    public Cell (int y, int x) {
        this.y = y;
        this.x = x;
        this.entitiesList = new ArrayList <Entities> ();
    }

    //Геттеры к полям класса.
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public ArrayList <Entities> getEntitiesList() {
        return entitiesList;
    }

    //Метод addEntity добавляет животных в локацию (в список entitiesList по одному юниту).
    public void addEntity (Entities entity) {
        entitiesList.add(entity);
    }

    //Метод removeEntity удаляет юнит из локации
    public void removeEntity (Entities entity) {
        entitiesList.remove(entity);
    }

}
