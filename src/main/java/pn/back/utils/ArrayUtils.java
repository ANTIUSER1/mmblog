/**
 * механизм преобразования массива sql в массив  javaи
 */
package pn.back.utils;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArrayUtils {
    public static String[] convertArray(Array tags) throws SQLException {
        ResultSet rs = tags.getResultSet();
        while (rs.next()) {
            try {
                return (String[]) rs.getArray(2).getArray();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return null;
    }
}
