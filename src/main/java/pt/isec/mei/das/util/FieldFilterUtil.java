package pt.isec.mei.das.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldFilterUtil {

    public static Map<String, Object> filterFields(Object object, List<String> fields) {
        Map<String, Object> filteredData = new HashMap<>();
        Class<?> clazz = object.getClass();
        for (String field : fields) {
            try {
                Field declaredField = clazz.getDeclaredField(field);
                declaredField.setAccessible(true);
                Object value = declaredField.get(object);
                filteredData.put(field, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Requested fields list is incorrect");
            }
        }
        return filteredData;
    }
}
