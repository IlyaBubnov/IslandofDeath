package services;

import java.util.concurrent.ThreadLocalRandom;

//Класс создан для получения случайного числа типа int.
public class Randomizer {

    //Метод getRandomIntValue генерирует случайное число типа int в диапазоне от 0 до максимально заданного (maxValue).
    public static int getRandomIntValue (int maxValue) {
        return ThreadLocalRandom.current().nextInt(0, maxValue);
    }
}
