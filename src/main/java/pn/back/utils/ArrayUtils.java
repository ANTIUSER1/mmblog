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


    public static String convertFromArray(String[] tags) {
        StringBuffer result = new StringBuffer();
        for (int k = 0; k < tags.length - 1; k++) {
            result.append(" ").append(tags[k]).append(" , ");
        }
        result.append(" ").append(tags[tags.length - 1]).append("   ");
        return result.toString();
    }
}
