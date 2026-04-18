package Common.Model.Enums;

import java.io.Serializable;

public enum OrganizationType implements Serializable {
    TRUST,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;

    public static String[] getTypes() {
        OrganizationType[] values = OrganizationType.values();
        String[] types = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            types[i] = values[i].name();
        }
        return types;
    }
}