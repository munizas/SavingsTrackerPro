package savingstrackerpro;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import savingstrackerpro.models.Expense;
import savingstrackerpro.models.MonthlyData;

/**
 * SavingsTrackerPro 1.0
 * 
 * JavaFX application that tracks a user's savings from month to month.
 * As of this writing three models are used in this application: Expense, MonthlyExpense, MonthlyData
 * 
 * Data is loaded and stored as two JSON files, one for Expense objects and one for MonthlyData objects.
 * 
 * 
 * @author Agustin Muniz
 */
public class SavingsTrackerPro extends Application {
    
    private static Stage stage;
    
    private static Map<String, Expense> expenseDataMap = new HashMap<>();
    private static Map<String, MonthlyData> monthlyDataMap = new HashMap<>();
    
    private static Image editMonthImage;
    private static Image reloadImage;
    private static Image addImage;
    private static Image deleteImage;
    private static Image saveImage;
    private static Image exitImage;
    private static Image walletImage;
    private static Image infoImage;
    private static Image loadImage;
    private static Image successImage;
    
    public static Map<String, Expense> getExpenseMap() {
        return expenseDataMap;
    }
    
    public static void setExpenseMap(Map<String, Expense> map) {
        expenseDataMap = map;
    }
    
    public static Map<String, MonthlyData> getMonthlyDataMap() {
        return monthlyDataMap;
    }
    
    public static void setMonthlyDataMap(Map<String, MonthlyData> map) {
        monthlyDataMap = map;
    }

    public SavingsTrackerPro() {
        editMonthImage = new Image(getClass().getResourceAsStream("/resources/edit-icon-24.png"));
        reloadImage = new Image(getClass().getResourceAsStream("/resources/button-round-reload-icon-small.png"));
        addImage = new Image(getClass().getResourceAsStream("/resources/button-round-plus-icon-24.png"));
        deleteImage = new Image(getClass().getResourceAsStream("/resources/button-round-cancel-icon-24.png"));
        saveImage = new Image(getClass().getResourceAsStream("/resources/floppy-save-icon-24.png"));
        exitImage = new Image(getClass().getResourceAsStream("/resources/button-round-stop-icon-24.png"));
        walletImage = new Image(getClass().getResourceAsStream("/resources/money-wallet-icon-24.png"));
        infoImage = new Image(getClass().getResourceAsStream("/resources/button-bubble-info-icon-24.png"));
        loadImage = new Image(getClass().getResourceAsStream("/resources/load-download-icon-24.png"));
        successImage = new Image(getClass().getResourceAsStream("/resources/button-round-tick-ok-icon-24.png"));
    }
    
    public static Image getEditMonthImage() {
        return editMonthImage;
    }
    
    public static Image getReloadImage() {
        return reloadImage;
    }
    
    public static Image getAddImage() {
        return addImage;
    }
    
    public static Image getDeleteImage() {
        return deleteImage;
    }
    
    public static Image getSaveImage() {
        return saveImage;
    }
    
    public static Image getExitImage() {
        return exitImage;
    }
    
    public static Image getWalletImage() {
        return walletImage;
    }
    
    public static Image getInfoImage() {
        return infoImage;
    }
    
    public static Image getLoadImage() {
        return loadImage;
    }
    
    public static Image getSuccessImage() {
        return successImage;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        SavingsTrackerPro.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/savingstrackerpro/controllers/FXMLMain.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public static Stage getStage() {
        return stage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
