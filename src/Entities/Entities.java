package Entities;

import Services.ChanceToDie;
import Services.Randomizer;
import Constants.Action;
import Constants.Movement;

import java.util.Objects;

//Абстрактный класс содержит общие поля (хар-ки) и методы (поведение) для всех животных.
public abstract class Entities {

    private String type;                //Символ существа.
    private double unitWeight;          //Вес существа (кг).
    private double unitHealth;          //Здоровье существа (фактическое) (кг).
    private double perfectUnitHealth;   //100% здоровье существа (50% от его массы) (кг).
    private double maxKGFoodToEat;      //Макс. кг пищи для полного насыщения (кг).
    private int maxUnitSpeed;           //Макс. скорость существа (макс кол-во клеток за ход).
    private int maxUnitsPerOneCell;     //Макс.кол-во существ на одной клетке (в ед.).

    //Конструктор с общими для всех параметрами.
    public Entities (String type, double unitWeight, double unitHealth, double perfectUnitHealth,
                    double maxKGFoodToEat, int maxUnitSpeed, int maxUnitsPerOneCell) {
                    this.type = type;
                    this.unitWeight = unitWeight;
                    this.unitHealth = unitHealth;
                    this.perfectUnitHealth = perfectUnitHealth;
                    this.maxKGFoodToEat = maxKGFoodToEat;
                    this.maxUnitSpeed = maxUnitSpeed;
                    this.maxUnitsPerOneCell = maxUnitsPerOneCell;
    }

    //Геттеры и сеттеры для всех полей класса (кроме сеттера для символа и 100% здоровья существа).
    public String getType() {
        return type;
    }
    public double getUnitWeight() {
        return unitWeight;
    }
    public double getUnitHealth() {
        return unitHealth;
    }
    public double getPerfectUnitHealth() {
        return perfectUnitHealth;
    }
    public double getMaxKGFoodToEat() {
        return maxKGFoodToEat;
    }
    public int getMaxUnitSpeed() {
        return maxUnitSpeed;
    }
    public int getMaxUnitsPerOneCell() {
        return maxUnitsPerOneCell;
    }
    public void setUnitWeight(double unitWeight) {
        this.unitWeight = unitWeight;
    }
    public void setUnitHealth(double unitHealth) {
        this.unitHealth = unitHealth;
    }
    public void setMaxKGFoodToEat(double maxKGFoodToEat) {
        this.maxKGFoodToEat = maxKGFoodToEat;
    }
    public void setMaxUnitSpeed(int maxUnitSpeed) {
        this.maxUnitSpeed = maxUnitSpeed;
    }
    public void setMaxUnitsPerOneCell(int maxUnitsPerOneCell) {
        this.maxUnitsPerOneCell = maxUnitsPerOneCell;
    }

    //Для "наглядного" вывода инфы в консоль переопределен toString().
    @Override
    public String toString() {
        return  type + " - " + Entities.this.getClass() + "\n" +
                "(unitWeight = " + unitWeight +
                ", unitHealth = " + unitHealth +
                ", perfectUnitHealth = " + perfectUnitHealth +
                ", maxKGFoodToEat = " + maxKGFoodToEat +
                ", maxUnitSpeed = " + maxUnitSpeed +
                ", maxUnitsPerOneCell = " + maxUnitsPerOneCell + ")";
    }
    //!!!Переопределил equals и hashcode для дальнейшей возможности использования объектов класса Entities в HashMap
    //для сбора статистики. Дабы не получать одинаковых ключей (ключ стринг - символ животного). В мапе не м.б.
    //одинаковых ключей, а у меня в одной локации однозначно м.б. несколько юнитов одного типа (волки к примеру)!!!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entities entities)) return false;
        return Double.compare(entities.unitWeight, unitWeight) == 0 && Double.compare(entities.unitHealth, unitHealth)
                == 0 && Double.compare(entities.perfectUnitHealth, perfectUnitHealth)
                == 0 && Double.compare(entities.maxKGFoodToEat, maxKGFoodToEat)
                == 0 && maxUnitSpeed == entities.maxUnitSpeed && maxUnitsPerOneCell
                == entities.maxUnitsPerOneCell && type.equals(entities.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, unitWeight, unitHealth, perfectUnitHealth, maxKGFoodToEat,
                maxUnitSpeed, maxUnitsPerOneCell);
    }

    //Метод choiceOfDirection отвечает за выбор направления движения (задано константами в классе Movement) юнитов
    //между локациями.
    public Movement choiceOfDirection () {
        //Определим размер (кол-во) допустимых перемещений.
        int movementLength = Movement.values().length;
        //Вернем произвольное направление, используя рандомайзер.
        return Movement.values()[Randomizer.getRandomIntValue(movementLength)];
    }

    //Метод choiceOfAction отвечает за выбор действия (задано константами в классе Action) юнитов.
    public Action choiceOfAction () {
        //Учитывая, что у каждого возможного действия есть своя доля вероятности (так интереснее), то, в отличие
        //от метода choiceOfDirection, сначала рандомно выбираем действие, а потом, опять же рандомно, определяем
        //шанс на это действие. Если шанс "не выпал", то выбираем ничего не делать (DO_NOTHING).
        Action action = Action.values()[Randomizer.getRandomIntValue(Action.values().length)];
            if (Randomizer.getRandomIntValue(100) < action.getActionChanceInPercent()) {
                return action;
            } else {
                return Action.DO_NOTHING;
            }
    }

    //Метод reproduce отвечает за размножение существ (животных и растений). Метод размножения сделан абстрактным и
    //переопределяется для каждого существа, создавая тем самым новый объект класса-наследника. Считаем, что
    //если в одной локации оказались 2 юнита одного типа, то они, если не голодны, спариваются. Растение за
    //один цикл (день) симуляции, если оказалось в локации и не было съедено, по средствам анемохории (распростра-е
    // семян по воздуху) "удваивается" в этой клетке. Пусть будет так.
    public abstract Entities reproduce ();
    //!!!Учесть, что юнитов одного вида в одной локации не м.б. больше maxUnitsPerOneCell!!!

    public boolean eat (Entities victim) {

        //Если жертва (victim) умирает (см. метод chanceToDie)
        boolean victimDead = ChanceToDie.chanceToDie((Entities) this, victim);
        //Если здоровье охотника неидеально (не 100%)
        boolean healthIsNotPerfect = ((Entities) this).getUnitHealth() < ((Entities) this).getPerfectUnitHealth();

        //Если жертва умирает и здоровье охотника не 100%
        if (victimDead && healthIsNotPerfect) {
            //Если здоровье жертвы больше макс.кг пищи для полного насыщения охотника, то здоровье охотника увеличиваем
            //на макс.кг пищи для полного насыщения охотника. Если здоровье охотника стало больше 100%, то снижаем
            //его до 100%. Иначе устанавливаем здоровье охотника, увеличенное на макс.кг пищи для полного
            //насыщения охотника. Для сокращения кода используем метод min класса Math.
            if (victim.getUnitHealth() >= ((Entities) this).getMaxKGFoodToEat()) {
                ((Entities) this).setUnitHealth(((Entities) this).getUnitHealth() + ((Entities) this).getMaxKGFoodToEat());
                ((Entities) this).setUnitHealth(Math.min(((Entities) this).getUnitHealth(), ((Entities) this).getPerfectUnitHealth()));
                //Иначе, если здоровье жертвы меньше макс.кг пищи для полного насыщения охотника, здоровье охотника
                //увеличиваем на здоровье жертвы. Если здоровье охотника стало больше 100%, то снижаем его до 100%.
                //Иначе устанавливаем здоровье охотника, увеличенное на здоровье жертвы.
            } else {
                ((Entities) this).setUnitHealth(((Entities) this).getUnitHealth() + victim.getUnitHealth());
                ((Entities) this).setUnitHealth(Math.min(((Entities) this).getUnitHealth(), ((Entities) this).getPerfectUnitHealth()));
            }
            return true;
        } else {
            return false;
        }
    }
}
