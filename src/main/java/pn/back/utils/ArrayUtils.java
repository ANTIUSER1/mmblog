package pn.back.utils;

import java.sql.Array;
import java.sql.SQLException;

public class ArrayUtils {
    public static String[] convertArray(Array tags) throws SQLException {
//
//        try {
//            String[] data = (String[]) tags.getArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return new String[]{tags.getArray().toString()};
    }
}
