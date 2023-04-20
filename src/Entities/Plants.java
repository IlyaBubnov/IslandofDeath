package Entities;

import Constants.TypesOfEntities;

//Класс описывает растения (пусть будет 1 небольшой куст) на карте.
public class Plants extends Entities {

    public Plants() {
        super(TypesOfEntities.PLANTS.getEntitySymbol(),
        1, 0.5, 0.5, 0, 0, 200);
    }

    @Override
    public Entities reproduce() {
        return new Plants();
    }
}
