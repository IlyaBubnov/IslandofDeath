package Services;

import Constants.Action;
import Constants.IslandSettings;
import Constants.Movement;
import Entities.Entities;
import Entities.Plants;
import Location.Cell;
import Location.Island;
import Location.Statistics;
import Start.Start;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

//Класс IslandRules является основным классом, описывающим модель взаимодействия юнитов.
//Используя многопоточность, в классе реализуем симуляцию "жизненного цикла" острова. Многопоточность
//помогает оптимизировать работу, при этом обеспечивая корректное и безопасное выполнение операций.
public class IslandRules {

//ПОЛЯ КЛАССА
    private final Island island;            //Объект класса Island (собственно сам остров).
    private final Statistics statistics;    //Объект класса Statistics (собственно необходимая статистика).

    //Чтобы не создавать новый поток под каждую новую задачу, потоки хранятся в неком “хранилище”, и когда поступает
    //новая задача, берется уже существующий поток, а не создается новый. Поэтому для создания пула потоков использую
    //фреймворк Executor, а именно его интерфейс ExecutorService, т.к. он содержит методы для управления завершением
    //работы, а также методы, которые могут возвращать какой-то результат.
    private final ExecutorService runActionInLocation;

    //Пер-ая DAY будет хранить количество дней, которое прошло с начала симуляции. DAY увеличивается на 1 день каждый
    //раз, как начинается новый день. Использую класс AtomicInteger для безопасного использования счетчика дней при
    //многопоточности, т.к., если использовать обычный инкремент (++), можно словить баг.
    public static final AtomicInteger DAY = new AtomicInteger(0); //объявил константой

//КОНСТРУКТОР класса для инициализации нового объекта типа IslandRules. В качестве пар-ра принимает объект типа Island
//(наш остров). Конструктор также создает новый объект типа Statistics, который будем использовать для отслеживания
//статистики на острове. Также создаю новый пул потоков (executorService), который будет использоваться для запуска
//асинхронных задач на острове.
    public IslandRules (Island island) {
        this.island = island;
        this.statistics = new Statistics(island);
        this.runActionInLocation = Executors.newWorkStealingPool();
    }

    //ГЕТТЕРЫ к полям класса
    public Island getIsland() {
        return island;
    }
    public Statistics getStatistics() {
        return statistics;
    }

    //Переопределенные методы equals и hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IslandRules that)) return false;
        return island.equals(that.island);
    }

    @Override
    public int hashCode() {
        return Objects.hash(island);
    }

//***** ПРАВИЛА УДАЛЕНИЯ ЮНИТОВ ИЗ ЛОКАЦИИ *****

    //Метод удаляет юнитов из локации, в случае превышения максимально допустимого кол-ва
    //одного вида в локации (пер-ая maxUnitsPerOneCell).
    private void deleteEntityIfMaxCountIncreased (Entities entity, Cell cell) {
        //Забираю все юниты, содержащиеся в конкретной локации в новый список типа ArrayList.
        List <Entities> entitiesFromCell = cell.getEntitiesList();
        //Используя stream и рефлексию, в потоке оставляем юниты того же типа, что и переданный методу
        //объект entity (т.е. себе подобного). Сох-яю отфильтрованный поток в новый список.
        List <Entities> sameEntities = entitiesFromCell
                .stream()
                .filter(sameEntity -> Objects.equals(sameEntity.getClass().getSimpleName(), entity.getClass().getSimpleName()))
                .toList();
        //Пока кол-во юнитов одного типа превышает максимально допустимое - удаляем по одному из локации
        while (sameEntities.size() >= entity.getMaxUnitsPerOneCell()) {
           cell.removeEntity(entity);
        }
    }

//***** ПРАВИЛА ПЕРЕМЕЩЕНИЯ ЮНИТОВ МЕЖДУ ЛОКАЦИЯМИ ОСТРОВА *****

    //Метод move определяет, в каких направлениях и с какой скоростью из локации (cell) будет двигаться юнит (entities)
    private void move (Entities entities, Cell cell) {
        //Используя рандомайзер, получим случайное кол-во очков (шагов) скорости от 0 до maxUnitSpeed. Но рандомайзер
        //использует диапазон от 0 (вкл) до maxValue (не вкл), поэтому, чтобы учесть макс кол-во очков перемещения,
        //увеличим maxValue на 1.
        int randomSpeed = Randomizer.getRandomIntValue(entities.getMaxUnitSpeed() + 1);
        //В цикле while выбираем направление движения на основе значения перечисления Movement, которое
        //определяем, используя метод chooseDirection(). В зависимости от направления, используя оператор switch,
        //вызываем соответствующий метод moveUp(), moveDown(), moveRight(), moveLeft().
        while (randomSpeed > 0) {
            Movement movement = entities.choiceOfDirection();
                switch (movement) {
                    case UP -> this.moveUp(entities, cell);
                    case DOWN -> this.moveDown(entities, cell);
                    case RIGHT -> this.moveRight(entities, cell);
                    case LEFT -> this.moveLeft(entities, cell);
                }
            randomSpeed--;
        }
    }

    //Методы moveUp(), moveDown(), moveRight(), moveLeft() отвечают за перемещение юнита на острове на одну
    //клетку-локацию в соответствующем направлении. На вход методов подаю два аргумента: юнит (который должен быть
    //перемещен) и текущую локацию (которую животное занимает в данный момент).
    private void moveUp (Entities entity, Cell cell) {
        //Определяю координаты текущей локации
        int horizontal = cell.getX();
        int vertical = cell.getY();
        //Затем проверяю, находится ли юнит на верхней границе острова - если нет, то создаю ссылку
        //на новую ячейку (следующую сверху) и добавляю в нее этот юнит, а из текущей ячейки юнит удаляется.
        //Если юнит находится на верхней границе острова, то метод не выполняет никаких действий. Аналогичный
        //подход для методов moveDown(), moveRight(), moveLeft().
            if (vertical < island.getLength() - 1) {
                Cell newCell = island.getCells()[vertical + 1][horizontal];
                newCell.addEntity(entity);
                cell.removeEntity(entity);
        }
    }

    private void moveDown (Entities entity, Cell cell) {

        int horizontal = cell.getX();
        int vertical = cell.getY();

        if (vertical > 0) {
            Cell newCell = island.getCells()[vertical - 1][horizontal];
            newCell.addEntity(entity);
            cell.removeEntity(entity);
        }
    }

    private void moveRight (Entities entity, Cell cell) {

        int horizontal = cell.getX();
        int vertical = cell.getY();

        if (horizontal < island.getWidth() - 1) {
            Cell newCell = island.getCells()[vertical][horizontal + 1];
            newCell.addEntity(entity);
            cell.removeEntity(entity);
        }
    }

    private void moveLeft (Entities entity, Cell cell) {

        int horizontal = cell.getX();
        int vertical = cell.getY();

        if (horizontal > 0) {
            Cell newCell = island.getCells()[vertical][horizontal - 1];
            newCell.addEntity(entity);
            cell.removeEntity(entity);
        }
    }

//***** ПРАВИЛА ПИТАНИЯ ЮНИТОВ *****

    //Метод foodFeatures реализует алгоритм "приема пищи". 2 пар-ра на вход: юнит, который будет есть и локация,
    //где он будет "охотиться".
    private void foodFeatures (Entities hunter, Cell cell) {
        //Забираю все юниты, содержащиеся в конкретной локации в новый список типа ArrayList.
        ArrayList <Entities> entitiesFromCell = cell.getEntitiesList();
        //Используя stream и рефлексию, из потока выбираю только те юниты, который не являются тем же типом, что и
        //переданный методу foodFeatures объект entity (т.е. нельзя съесть себе подобного). Сох-яю отфильтрованный поток
        //в новый список.
        List <Entities> entitiesForFood = entitiesFromCell
                .stream()
                .filter(food -> !Objects.equals(food.getClass().getSimpleName(), hunter.getClass().getSimpleName()))
                .toList();
        //Если в entitiesForFood есть какие-либо юниты, то рандомно выбирается случайная жертва и вызывается метод
        //eat() класса Entities для переданного юнита. Если юнит съедает жертву, то она удаляется из локации.
        if (!entitiesForFood.isEmpty()) {
            Entities food = entitiesForFood.get(Randomizer.getRandomIntValue(entitiesForFood.size()));
            if (hunter.eat(food)) {
                cell.removeEntity(food);
            }
        }
    }

//***** ПРАВИЛА РАЗМНОЖЕНИЯ ЮНИТОВ *****

    //Метод reproduceFeatures реализует алгоритм воспроизводства юнитов (как животных, так и растений).
    //Будем считать, что:
        //- растение порождает "само-себя" (по воздуху: пыльца или т.п.).
        //- если животное встречает себе подобного, то считаем что это "самец и самка".
        //- все животные могут иметь не более "одного детеныша за раз".
    private void reproduceFeatures (Entities entity, Cell cell) {
        //Забираю все юниты, содержащиеся в конкретной локации в новый список типа ArrayList.
        ArrayList <Entities> entitiesFromCell = cell.getEntitiesList();
        //Определяю "комфортные условия" для размножения. Сохраняю их в 2 булевые пер-ые.
        //Если наш юнит на вход метода (entity)- это растение, то оно само порождает себе подобное в этой локации.
        boolean isItPlant = entity.getClass().getSimpleName().equalsIgnoreCase(Plants.class.getSimpleName());
        //Если кол-во юнитов в локации меньше максимально возможного - плодитесь.
        boolean thereIsAPlace = entitiesFromCell.size() < IslandSettings.MAX_UNITS_IN_ONE_CELL;

        if (isItPlant && thereIsAPlace) {
            cell.addEntity(entity.reproduce());
        }
        //Используя stream и рефлексию, в потоке оставляем юниты того же типа, что и переданный методу
        //reproduceFeatures объект entity (т.е. себе подобного). Сох-яю отфильтрованный поток в новый список.
        if (!isItPlant && thereIsAPlace) {
            List <Entities> sameEntities = entitiesFromCell
                    .stream()
                    .filter(sameEntity -> Objects.equals(sameEntity.getClass().getSimpleName(), entity.getClass().getSimpleName()))
                    .toList();
            //Если нашелся хотя бы один "сородич" - воспроизводим.
            if (sameEntities.size() > 0) {
                cell.addEntity(entity.reproduce());
            }
        }
    }

//***** ПРАВИЛА ВЫБОРА ДЕЙСТВИЯ ЮНИТОВ *****

    //Метод action определяет поведение юнита (есть, ходить, размножаться, ничего не делать). Использую switch.
    private void action (Action action, Entities entity, Cell cell) {
        switch (action) {
            case EAT -> foodFeatures (entity, cell);
            case MOVE -> move (entity, cell);
            case REPRODUCE -> reproduceFeatures (entity, cell);
            case DO_NOTHING -> restoreHealth (entity);
        }
    //При этом, вне зависимости от выбора действия, все юниты в конце каждого дня (такта симуляции) теряют очки
    //здоровья (-10% от 100% здоровья (perfectUnitHealth)).
    //Для этого, после того, как юнит что-то сделал, используем метод loosingHealth.
        loosingHealth(entity);
    }

    //Метод снижает здоровье юнита на 10% (-10% от 100% здоровья (perfectUnitHealth)) в конце каждого дня.
    private void loosingHealth (Entities entity) {
        double healthAtTheEndOfTheDay =
                entity.getUnitHealth() - (entity.getPerfectUnitHealth()/IslandSettings.LOOSE_HEALTH_EVERY_DAY);
        entity.setUnitHealth(healthAtTheEndOfTheDay);
    }

    //Метод увеличивает здоровье юнита на 5% (+5% от 100% здоровья (perfectUnitHealth)), если юнит ничего не делает.
    private void restoreHealth (Entities entity) {
        //Если здоровье юнита неидеально (не 100%), то увеличиваем его на 5%, но не превышаем 100% здоровье.
        boolean healthIsNotPerfect = entity.getUnitHealth() < entity.getPerfectUnitHealth();
        if (healthIsNotPerfect) {
            double healthIfDoNothing =
                    entity.getUnitHealth() + (entity.getPerfectUnitHealth()/IslandSettings.RESTORE_HEALTH_IF_DO_NOTHING);
            entity.setUnitHealth(healthIfDoNothing);
            entity.setUnitHealth(Math.min(entity.getUnitHealth(), entity.getPerfectUnitHealth()));
        }
    }

//***** ПРАВИЛО СМЕРТИ ЮНИТОВ *****
    //Булевый метод служит "маркером смерти" юнита в случае, если его здоровье <= 0.
    private boolean areYouDead (Entities entity) {
        return entity.getUnitHealth() <= 0;
    }

//***** УСЛОВИЯ ОСТАНВКИ СИМУЛЯЦИИ *****
    //Метод isThisTheEnd, основываясь на текущей дате (принимает ее в качестве параметра), проверяет, наступил ли конец
    //симуляции (если да - true, иначе - false).
    private boolean isThisTheEnd (int day) {
        return day >= IslandSettings.MAX_NUMBER_OF_DAYS;
    }

    //Метод stopSimulation закрывает пул потоков ses, запущенный в классе Start и останавливает симуляцию.
    private void stopSimulation () {
        Start.ses.shutdown();
    }

//***** СИМУЛЯЦИЯ *****
    //Метод createActionInLocation используется для выполнения задачи в отдельном потоке класса ExecutorService
    //runActionInLocation, который будем вызывать в методе createSimulation.
    private Runnable createActionInLocation (Cell cell) {
        //Задача метода createActionInLocation заключается в проходе по списку юнитов в отдельно взятой локации (cell)
        //и выборе действия для каждого из них. В результате работы метода получаю новый список юнитов с учетом
        //всевозможных взаимодействий и свойств (размножение, еда, перемещение, потеря/рост здоровья и т.п.)
        //Т.к. в дальнейшем мне нужно передать работу этого метода в поток, то буду возвращать объект типа Runnable.
        //Использую список типа CopyOnWriteArrayList для потокобезопасности. Это значит, что только один поток может
        //работать с экземпляром списка за раз, тогда как другие потоки должны ждать, пока эта блокировка не будет
        //освобождена. С одним экземпляром ArrayList могут работать несколько потоков одновременно, хоть он и быстрее.
        return () -> {//Каким - то образом подсмотрел как вернуть поток через лямбду. Так приходилось кастить и вылетал эксепшн...
            List<Entities> entities = new CopyOnWriteArrayList<>(cell.getEntitiesList());
                for (Entities entity : entities) {
                    if (areYouDead(entity)) {
                        cell.removeEntity(entity);
                    } else {
                        action(entity.choiceOfAction(), entity, cell);
                    }
                    deleteEntityIfMaxCountIncreased (entity, cell);
                }
        };
    }

    //Метод createSimulation. Т.к. в дальнейшем мне нужно передать работу этого метода в поток, то буду возвращать
    //объект типа Runnable. Также будем возвращать поток (объект Runnable), используя лямбда - выражение.
    //В целом, метод createSimulation, отвечает за создание заданий для каждой локации острова и запуска их в отдельных
    //потоках. Каждый поток отвечает за обработку отдельной локации, а пул потоков runActionInLocation управляет их
    //выполнением. Это позволяет оптимизировать процесс обработки каждой локации и ускорить выполнение всей программы.
    public Runnable createSimulation () {
        return () -> {
            //Задаю переменную - счетчик, которую в дальнейшем буду увеличивать на 1 с помощью атомарного счетчика
            //(пер-ая DAY, которая объявлена и инициализирована в поле класса).
            int counter;
            //В циклах fori прохожусь по всем локациям острова
            for (int i = 0; i < island.getLength(); i++) {
                for (int j = 0; j < island.getWidth(); j++) {
                    //Копирую каждую локацию, создавая ячейку класса Cell, и передаю уже ее в пул потоков
                    //runActionInLocation.
                    Cell cell = island.getCells()[i][j];
                    runActionInLocation.submit(createActionInLocation(cell));
                }
            }
            //Как только количество дней симуляции достигает максимально заданного - останавливаем.
            counter = DAY.getAndIncrement();
            if (isThisTheEnd(counter)) {
               stopSimulation();
            }
        };
    }
}
