package src.Client.Model.Enums;

import java.io.Serializable;

public enum UnitOfMeasure implements Serializable {
    KILOGRAMS,
    METERS,
    CENTIMETERS,
    SQUARE_METERS,
    MILLILITERS;

    public static String[] getUnits() {
        UnitOfMeasure[] values = UnitOfMeasure.values();
        int len = values.length;
        String[] types = new String[len];
        for (int i = 0; i < len; i++) {
            types[i] = values[i].name();
        }
        return types;
    }
}