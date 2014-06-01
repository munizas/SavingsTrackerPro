package savingstrackerpro.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import savingstrackerpro.SavingsTrackerPro;
import savingstrackerpro.components.MonthlyExpenseActiveCheckBoxCell;
import savingstrackerpro.models.Expense;
import savingstrackerpro.models.MonthlyData;
import savingstrackerpro.models.MonthlyExpense;

/**
 * FXML Controller class
 *
 * @author Agustin Muniz
 */
public class MonthPaneController implements Initializable {
    
    @FXML
    private TitledPane tPane;
    @FXML
    private SplitPane monthEditSplitPane;
    @FXML
    private TextField incomeTextField;
    @FXML
    private TextField expenseTotalTextField;
    @FXML
    private TextField disposableIncomeTextField;
    @FXML
    private TextField savingsTextField;
    @FXML
    private ComboBox expensesComboBox;
    @FXML
    private Button addExpenseButton;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<MonthlyExpense, String> nameCol;
    @FXML
    private TableColumn<MonthlyExpense, Number> amountCol;
    @FXML
    private TableColumn<MonthlyExpense, Boolean> activeCol;
    @FXML
    private TableColumn<MonthlyExpense, String> noteCol;
    @FXML
    private TableColumn<MonthlyExpense, MonthlyExpense> actionCol;
    @FXML
    private Button closeButton;
    
    private MonthlyData md;
    
    // backing list for table and chart
    private ObservableList<MonthlyExpense> monthlyExpenseDataList = FXCollections.observableArrayList();
    
    public Button getCloseButton() {
        return closeButton;
    }
    
    public void populateMonthPane(MonthlyData md) {
        this.md = md;
        tPane.setText(md.getLabel());
        setTextFields();
        setTableData();
        refreshExpenseComboBox();
    }
    
    public void refreshExpenseComboBox() {
        expensesComboBox.setItems(FXCollections.observableArrayList(SavingsTrackerPro.getExpenseMap().values()));
    }
    
    private void setTextFields() {
        incomeTextField.setText(md.getRollingIncome()+"");
        expenseTotalTextField.setText(md.getExpenseTotal()+"");
        disposableIncomeTextField.setText(md.getDisposableIncome()+"");
        savingsTextField.setText(md.getSavings()+"");
    }
    
    private void setTableData() {
        monthlyExpenseDataList.clear();
        md.getExpenses().stream().forEach(e -> {
            monthlyExpenseDataList.add(e);
        });
    }
    
    public void refreshPane() {
        md.refreshExpenses();
        setTextFields();
    }
    
    public MonthlyData getEditingMonthlyData() {
        return md;
    }
    
    public void handleAddExpenseButton(ActionEvent event) {
        Expense e = (Expense) expensesComboBox.getSelectionModel().getSelectedItem();
        md.addExpense(e);
        refreshPane();
        setTableData();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        
        amountCol.setCellValueFactory(new PropertyValueFactory("amount"));
        amountCol.setCellFactory(TextFieldTableCell.<MonthlyExpense, Number>forTableColumn(new NumberStringConverter()));
        amountCol.setOnEditCommit((CellEditEvent<MonthlyExpense, Number> t) -> {
            ((MonthlyExpense) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
            ).setAmount(t.getNewValue().doubleValue());
            refreshPane();
        });
        
        Callback<TableColumn<MonthlyExpense, Boolean>, TableCell<MonthlyExpense, Boolean>> booleanCellFactory = (TableColumn<MonthlyExpense, Boolean> p) -> new MonthlyExpenseActiveCheckBoxCell(this);
        
        activeCol.setCellValueFactory(new PropertyValueFactory("active"));
        activeCol.setCellFactory(booleanCellFactory);
        
        noteCol.setCellValueFactory(new PropertyValueFactory("note"));
        noteCol.setCellFactory(TextFieldTableCell.<MonthlyExpense>forTableColumn());
        noteCol.setOnEditCommit((CellEditEvent<MonthlyExpense, String> t) -> {
            ((MonthlyExpense) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
            ).setNote(t.getNewValue());
        });
        
        actionCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));
        actionCol.setCellFactory(new Callback<TableColumn<MonthlyExpense, MonthlyExpense>, TableCell<MonthlyExpense, MonthlyExpense>>() {
            @Override
            public TableCell<MonthlyExpense, MonthlyExpense> call(TableColumn<MonthlyExpense, MonthlyExpense> btnCol) {
                return new TableCell<MonthlyExpense, MonthlyExpense>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setTooltip(new Tooltip("Delete expense"));
                    }

                    @Override
                    public void updateItem(final MonthlyExpense me, boolean empty) {
                        if (me != null) {
                            super.updateItem(me, empty);
                            buttonGraphic.setImage(SavingsTrackerPro.getDeleteImage());
                            setGraphic(button);
                            button.setOnAction(e -> handleDeleteButton(me));
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        
        tableView.setItems(monthlyExpenseDataList);
        
        incomeTextField.setOnAction(e -> {
            md.setRollingIncome(Double.parseDouble(incomeTextField.getText()));
            refreshPane();
        });
        
        savingsTextField.setOnAction(e -> {
            md.setSavings(Double.parseDouble(savingsTextField.getText()));
            refreshPane();
        });
        
        incomeTextField.focusedProperty().addListener(c -> {
            md.setRollingIncome(Double.parseDouble(incomeTextField.getText()));
            refreshPane();
        });
        
        savingsTextField.focusedProperty().addListener(c -> {
            md.setSavings(Double.parseDouble(savingsTextField.getText()));
            refreshPane();
        });
        
        addExpenseButton.setGraphic(new ImageView(SavingsTrackerPro.getAddImage()));
        addExpenseButton.setTooltip(new Tooltip("Add expense"));
    }
    
    private void handleDeleteButton(MonthlyExpense me) {
        md.remove(me);
        refreshPane();
        setTableData();
    }

}
