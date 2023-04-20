package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Boar extends Entities {

    public Boar() {
        super(TypesOfEntities.BOAR.getEntitySymbol(),
        400, 200, 200,50, 2, 50);
    }

    @Override
    public Entities reproduce() {
        return new Boar();
    }

}
