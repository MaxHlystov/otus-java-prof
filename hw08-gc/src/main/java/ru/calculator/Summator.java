package ru.calculator;

import java.util.ArrayList;
import java.util.List;

public class Summator {
    private int valuesCount = 0;
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        valuesCount++;
        if (valuesCount == 6_600_000) {
            valuesCount = 0;
        }

        final int currentValue = data.getValue();
        sum += currentValue;

        sumLastThreeValues = currentValue + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = currentValue;

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (currentValue + 1) - sum);
            someValue = Math.abs(someValue) + valuesCount;
        }
    }

    public Integer getSum() {
        return sum;
    }

    public Integer getPrevValue() {
        return prevValue;
    }

    public Integer getPrevPrevValue() {
        return prevPrevValue;
    }

    public Integer getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public Integer getSomeValue() {
        return someValue;
    }
}
