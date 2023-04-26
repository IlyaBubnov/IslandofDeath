package location;

import entities.Entities;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//Класс "клетка" является одной из локаций острова. В каждой локации что-то происходит (размножение, поедание).
//Между клетками (по локациям) животные перемещаются с заданной скоростью (maxUnitSpeed) в строго определенных
//направлениях (класс Movement в константах).
public class Cell   {
    private final int y;  //Координаты точки (вертикаль). Ну или "ряд".
    private final int x;  //Координаты точки (горизонталь). Ну или "столбец".
    private final List <Entities> entitiesList;    //Список юнитов, содержащихся в конкретной локации.

    //Конструктор, принимающий координаты, сохраняет в поля класса список юнитов, а также координаты ячейки (локации).
    public Cell (int y, int x) {
        this.y = y;
        this.x = x;
        this.entitiesList = new CopyOnWriteArrayList<>();
    }

    //Геттеры к полям класса.
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public List <Entities> getEntitiesList() {
        return this.entitiesList;
    }

    //Метод addEntity добавляет юниты в локацию (в список entitiesList по одному юниту).
    public void addEntity (Entities entity) {
        this.entitiesList.add(entity);
    }

    //Метод removeEntity удаляет юнит из локации.
    public void removeEntity (Entities entity) {
        this.entitiesList.remove(entity);
    }

}
