package model;

import Util.Constants;

/**
 * Created by VerenaSchlott on 05/06/18.
 */

public enum PasswordStrength {
    SIMPLE,
    MIDDLE,
    STRONG;

    @Override
    public String toString() {
        switch(this) {
            case SIMPLE:
                return Constants.SIMPLE;
            case MIDDLE:
                return Constants.MIDDLE;
            case STRONG:
                return Constants.STRONG;
            default:
                return "";
        }
    }
}
