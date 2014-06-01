package savingstrackerpro.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import savingstrackerpro.models.Expense;
import savingstrackerpro.models.MonthlyData;

/**
 *
 * @author Agustin Muniz
 */public class GsonUtil {
    
    private static final Gson gson = new Gson();
    
    public static Map<String, Expense> generateExpenseMap(File file) {
        String json = getFileContent(file);
        Type cType = new TypeToken<Map<String, Expense>>(){}.getType();
        return gson.fromJson(json, cType);
    }
    
    public static Map<String, MonthlyData> generateMonthlyMap(File file) {
        String json = getFileContent(file);
        Type cType = new TypeToken<Map<String, MonthlyData>>(){}.getType();
        return gson.fromJson(json, cType);
    }
    
    private static String getFileContent(File file) {
        String fileContent = "";
        if (file != null) {
            try {
                fileContent = new String(Files.readAllBytes(Paths.get(file.getPath())));
            } catch (IOException ex) {
                Logger.getLogger(GsonUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fileContent;
    }
    
    public static boolean saveDataToDisk(File selectedDirectory, Map<String, Expense> expenseMap, Map<String, MonthlyData> monthlyMap) {
        if (selectedDirectory != null) {
            try {
                String json = gson.toJson(expenseMap);
                Files.write(Paths.get(selectedDirectory.getPath()+"/expensedata.json"), json.getBytes());
                json = gson.toJson(monthlyMap);
                Files.write(Paths.get(selectedDirectory.getPath()+"/monthlydata.json"), json.getBytes());
                return true;
            } catch (IOException ex) {
                Logger.getLogger(GsonUtil.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
    
}
