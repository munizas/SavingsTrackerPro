package savingstrackerpro.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import savingstrackerpro.SavingsTrackerPro;
import savingstrackerpro.models.MonthlyData;
import savingstrackerpro.util.GsonUtil;

/**
 *
 * @author Agustin Muniz
 */
public class MainController implements Initializable {
    
    @FXML
    private VBox vBox;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem importExpenseMenuItem;
    @FXML
    private MenuItem importMonthlyDataMenuItem;
    @FXML
    private MenuItem saveDataMenuItem;
    @FXML
    private MenuItem expenseMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private ToolBar toolBar;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Button dateRunButton;
    @FXML
    private Label avgLabel;
    @FXML
    private SplitPane splitPane;
    @FXML
    private TableView<MonthlyData> tableView;
    @FXML
    private TableColumn<MonthlyData, String> monthCol;
    @FXML
    private TableColumn<MonthlyData, String> yearCol;
    @FXML
    private TableColumn<MonthlyData, String> incomeCol;
    @FXML
    private TableColumn<MonthlyData, String> savingsCol;
    @FXML
    private TableColumn<MonthlyData, String> dispIncomeCol;
    @FXML
    private TableColumn<MonthlyData, String> expensesCol;
    @FXML
    private TableColumn<MonthlyData, MonthlyData> actionCol;
    @FXML
    private ToolBar statusBar;
    @FXML
    private Label statusLabel;
    
    @FXML
    private LineChart<String, Number> lineChart;
    
    // backing list for table and chart
    private ObservableList<MonthlyData> monthlyDataList = FXCollections.observableArrayList();
    
    @FXML
    private SplitPane expenseEditSplitPane;
    @FXML
    private ExpensePaneController expenseEditSplitPaneController;
    
    @FXML
    private SplitPane monthEditSplitPane;
    @FXML
    private MonthPaneController monthEditSplitPaneController;
    
    FadeTransition fadeOut = new FadeTransition(Duration.seconds(10));
    
    @FXML
    private void handleImportExpenseMenuItem(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Expense Data File");
        File selectedFile = fileChooser.showOpenDialog(SavingsTrackerPro.getStage());
        if (selectedFile != null) {
            SavingsTrackerPro.setExpenseMap(GsonUtil.generateExpenseMap(selectedFile));
            monthEditSplitPaneController.refreshExpenseComboBox();
            showStatusLabel("Expenses Imported");
        }
    }
    
    @FXML
    private void handleImportMonthlyDataMenuItem(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Monthly Data File");
        File selectedFile = fileChooser.showOpenDialog(SavingsTrackerPro.getStage());
        if (selectedFile != null) {
            SavingsTrackerPro.setMonthlyDataMap(GsonUtil.generateMonthlyMap(selectedFile));
            monthlyDataList.clear();
            showStatusLabel("Monthly Data Imported");
        }
    }
    
    private void showStatusLabel(String statusString) {
        statusLabel.setText(statusString);
        statusLabel.setVisible(true);
        fadeOut.play();
    }
    
    @FXML
    private void handleSaveDataMenuItem(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Save Directory");
        File selectedDirectory = directoryChooser.showDialog(SavingsTrackerPro.getStage());
        if (GsonUtil.saveDataToDisk(selectedDirectory, SavingsTrackerPro.getExpenseMap(), SavingsTrackerPro.getMonthlyDataMap()))
            showStatusLabel("Session saved to disk");
    }
    
    @FXML
    private void handleExitMenutItem(ActionEvent event) {
        SavingsTrackerPro.getStage().close();
    }
    
    @FXML
    private void handleDateRunButton(ActionEvent event) {
        refreshMonthlyDataList();
        setAvgText();
    }
    
    private void setAvgText() {
        double avgSavings = monthlyDataList.stream().mapToDouble(MonthlyData::getSavings).sum();
        avgSavings /= monthlyDataList.size();
        avgLabel.setText("Avg. Savings: $" + (double) Math.round(avgSavings*100)/100);
    }
    
    private void refreshMonthlyDataList() {
        monthlyDataList.clear();        
        LocalDate ld = fromDatePicker.getValue();
        Period period = fromDatePicker.getValue().until(toDatePicker.getValue());
        int i = 0;
        do {
            String key = ld.getMonthValue() + "-" + ld.getYear();
            MonthlyData md = SavingsTrackerPro.getMonthlyDataMap().get(key);
            if (md == null)
                md = new MonthlyData(ld.getYear(), ld.getMonthValue());
            monthlyDataList.add(md);
            ld = ld.plusMonths(1);
            i++;
        } while (i < period.toTotalMonths()+1);
    }
    
    private void updateDateRunButton() {
        dateRunButton.setDisable(fromDatePicker.getValue()==null || toDatePicker.getValue()==null);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final Callback<DatePicker, DateCell> fromDateCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.getDayOfMonth() != 1 || (toDatePicker.getValue()!=null && item.isAfter(toDatePicker.getValue().plusDays(1)))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }   
                    }
                };
            }
        };
        fromDatePicker.setDayCellFactory(fromDateCellFactory);
        fromDatePicker.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                updateDateRunButton();
            } 
        });
        
        final Callback<DatePicker, DateCell> toDateCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.getDayOfMonth() != 1 || (fromDatePicker.getValue()!=null && item.isBefore(fromDatePicker.getValue()))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }   
                    }
                };
            }
        };
        toDatePicker.setDayCellFactory(toDateCellFactory);
        toDatePicker.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                updateDateRunButton();
            } 
        });
        
        dateRunButton.setDisable(true);
        dateRunButton.setGraphic(new ImageView(SavingsTrackerPro.getReloadImage()));
        dateRunButton.setTooltip(new Tooltip("Retrieve Records"));
        
        yearCol.setCellValueFactory(new PropertyValueFactory("year"));
        monthCol.setCellValueFactory(new PropertyValueFactory("monthName"));
        incomeCol.setCellValueFactory(new PropertyValueFactory("rollingIncome"));
        savingsCol.setCellValueFactory(new PropertyValueFactory("savings"));
        dispIncomeCol.setCellValueFactory(new PropertyValueFactory("disposableIncome"));
        expensesCol.setCellValueFactory(new PropertyValueFactory("expenseTotal"));
        
        actionCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));
        actionCol.setCellFactory(new Callback<TableColumn<MonthlyData, MonthlyData>, TableCell<MonthlyData, MonthlyData>>() {
            @Override
            public TableCell<MonthlyData, MonthlyData> call(TableColumn<MonthlyData, MonthlyData> btnCol) {
                return new TableCell<MonthlyData, MonthlyData>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setTooltip(new Tooltip("Edit"));
                    }

                    @Override
                    public void updateItem(final MonthlyData md, boolean empty) {
                        if (md != null) {
                            super.updateItem(md, empty);
                            buttonGraphic.setImage(SavingsTrackerPro.getEditMonthImage());
                            setGraphic(button);
                            button.setOnAction(e -> handleEditButton(md));
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        
        tableView.setItems(monthlyDataList);
        
        monthlyDataList.addListener((Observable c) -> refreshChart());
        
        monthEditSplitPaneController.getCloseButton().setOnAction(e -> returnFromMonthEditPane());
        expenseEditSplitPaneController.getCloseButton().setOnAction(e -> returnFromExpensePane());
        
        saveDataMenuItem.setGraphic(new ImageView(SavingsTrackerPro.getSaveImage()));
        exitMenuItem.setGraphic(new ImageView(SavingsTrackerPro.getExitImage()));
        expenseMenuItem.setGraphic(new ImageView(SavingsTrackerPro.getWalletImage()));
        importExpenseMenuItem.setGraphic(new ImageView(SavingsTrackerPro.getLoadImage()));
        importMonthlyDataMenuItem.setGraphic(new ImageView(SavingsTrackerPro.getLoadImage()));
        aboutMenuItem.setGraphic(new ImageView(SavingsTrackerPro.getInfoImage()));
        
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setNode(statusLabel);
        
        statusLabel.setGraphic(new ImageView(SavingsTrackerPro.getSuccessImage()));
        statusLabel.setVisible(false);
    }
    
    @FXML
    private void handleExpenseMenuItem() {
        vBox.getChildren().removeAll(vBox.getChildren());
        vBox.getChildren().add(menuBar);
        vBox.getChildren().add(expenseEditSplitPane);
        vBox.getChildren().add(statusBar);
        expenseEditSplitPaneController.refresh();
    }
    
    private void returnFromExpensePane() {
        setChildren();
    }
    
    private void handleEditButton(MonthlyData md) {
        vBox.getChildren().removeAll(vBox.getChildren());
        vBox.getChildren().add(menuBar);
        vBox.getChildren().add(monthEditSplitPane);
        vBox.getChildren().add(statusBar);
        monthEditSplitPaneController.populateMonthPane(md);
    }
    
    public void returnFromMonthEditPane() {
        MonthlyData md = monthEditSplitPaneController.getEditingMonthlyData();
        SavingsTrackerPro.getMonthlyDataMap().put(md.getMapKey(), md);
        refreshMonthlyDataList();
        setAvgText();
        setChildren();
    }
    
    private void setChildren() {
        vBox.getChildren().removeAll(vBox.getChildren());
        vBox.getChildren().add(menuBar);
        vBox.getChildren().add(toolBar);
        vBox.getChildren().add(splitPane);
        vBox.getChildren().add(statusBar);
    }
    
    private XYChart.Series series;
    private void refreshChart() {
        lineChart.getData().clear();
        series = new XYChart.Series();
        series.setName("My Savings Profile");
        monthlyDataList.stream().sorted().forEach((md) -> {
            series.getData().add(new XYChart.Data(md.getMonthName()+"-"+md.getYear(), md.getSavings()));
        });
        
        lineChart.getData().add(series);
        
        lineChart.getData().stream().forEach((s) -> {
            s.getData().stream().forEach((d) -> {
                Tooltip.install(d.getNode(), new Tooltip(d.getXValue()+": $"+d.getYValue()));
            });
        });
    }
    
}
