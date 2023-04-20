package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Sheep extends Entities {

    public Sheep() {
        super(TypesOfEntities.SHEEP.getEntitySymbol(),
        70, 35, 35, 15, 3, 140);
    }

    @Override
    public Entities reproduce () {
        return new Sheep();
    }

}
