/**
 * механизм преобразования массива sql в массив  javaи
 */
package pn.back.utils;

import java.sql.Array;
import java.sql.ResultSet;

public class ArrayUtils {
    public static String[] convertArray(Array tags) {
        if (tags != null) {
            System.out.println("  \n");
            try {
                ResultSet rs = tags.getResultSet();
                while (rs.next()) {
                    Array data = rs.getArray(2);
                    String[] res = (String[]) data.getArray();
                    return res;
                }
            } catch (Exception e) {
                e.getMessage();

            }
        }
        return null;
    }


}
