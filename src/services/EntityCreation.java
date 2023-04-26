package services;

import constants.TypesOfEntities;
import entities.Entities;
import entities.Plants;
import entities.herbivores.*;
import entities.predators.*;

//Класс-фабрика по созданию сущностей (животных и растений).
public class EntityCreation {

    //Метод createEntity создает и возвращает объект нужного типа. Принимает в качестве параметра тип животного/растения
    //(enum из TypesOfEntities), а возвращает любой юнит, унаследованный от Entities. Т.о., мы можем
    //"конструировать" любое животное/растение и возвращать его. Используем switch для наглядности и удобства.
    public static Entities createEntity (TypesOfEntities type) {
        return switch (type) {
            case WOLF -> new Wolf();
            case SNAKE -> new Snake();
            case FOX -> new Fox();
            case BEAR -> new Bear();
            case EAGLE -> new Eagle();
            case HORSE -> new Horse();
            case DEER -> new Deer();
            case RABBIT -> new Rabbit();
            case MOUSE -> new Mouse();
            case GOAT -> new Goat();
            case SHEEP -> new Sheep();
            case BOAR -> new Boar();
            case BUFFALO -> new Buffalo();
            case DUCK -> new Duck();
            case CATERPILLAR -> new Caterpillar();
            case PLANTS -> new Plants();
        };
    }
}
