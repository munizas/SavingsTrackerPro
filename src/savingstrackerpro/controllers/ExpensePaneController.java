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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import savingstrackerpro.SavingsTrackerPro;
import savingstrackerpro.models.Expense;

/**
 * FXML Controller class
 *
 * @author Agustin Muniz
 */
public class ExpensePaneController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField defaultAmountField;
    @FXML
    private Button addButton;
    @FXML
    private TableColumn<Expense, String> nameCol;
    @FXML
    private TableColumn<Expense, Number> defaultAmountCol;
    @FXML
    private TableColumn<Expense, Expense> actionCol;
    @FXML
    private TableView<Expense> tableView;
    @FXML
    private Button closeButton;
    
    // backing list for table
    private ObservableList<Expense> expenseDataList = FXCollections.observableArrayList();
    
    public Button getCloseButton() {
        return closeButton;
    }
    
    @FXML
    private void handleAddButton(ActionEvent e) {
        Expense ex = new Expense(nameField.getText(), Double.parseDouble(defaultAmountField.getText()));
        SavingsTrackerPro.getExpenseMap().put(ex.getName(), ex);
        refresh();
    }
    
    public void refresh() {
        clearTextFields();
        setTableData();
    }
    
    private void clearTextFields() {
        nameField.setText("");
        defaultAmountField.setText("");
    }
    
    private void setTableData() {
        expenseDataList.clear();
        expenseDataList.addAll(SavingsTrackerPro.getExpenseMap().values());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        defaultAmountCol.setCellValueFactory(new PropertyValueFactory("defaultAmount"));
        defaultAmountCol.setCellFactory(TextFieldTableCell.<Expense, Number>forTableColumn(new NumberStringConverter()));
        defaultAmountCol.setOnEditCommit((TableColumn.CellEditEvent<Expense, Number> t) -> {
            ((Expense) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
            ).setDefaultAmount(t.getNewValue().doubleValue());
        });
        
        actionCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));
        actionCol.setCellFactory(new Callback<TableColumn<Expense, Expense>, TableCell<Expense, Expense>>() {
            @Override
            public TableCell<Expense, Expense> call(TableColumn<Expense, Expense> btnCol) {
                return new TableCell<Expense, Expense>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setTooltip(new Tooltip("Delete expense"));
                    }

                    @Override
                    public void updateItem(final Expense ex, boolean empty) {
                        if (ex != null) {
                            super.updateItem(ex, empty);
                            buttonGraphic.setImage(SavingsTrackerPro.getDeleteImage());
                            setGraphic(button);
                            button.setOnAction(e -> handleDeleteButton(ex));
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        
        tableView.setItems(expenseDataList);
        
        addButton.setGraphic(new ImageView(SavingsTrackerPro.getAddImage()));
        addButton.setTooltip(new Tooltip("Add expense"));
    }
    
    private void handleDeleteButton(Expense ex) {
        ex.delete();
        setTableData();
    }
    
}
