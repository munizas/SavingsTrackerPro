package savingstrackerpro.models;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import savingstrackerpro.SavingsTrackerPro;

/**
 * Represents financial information for a given month.
 * 
 * @author Agustin Muniz
 */
public class MonthlyData implements Comparable{
    
    private int year;
    private int month;
    private double rollingIncome; // user input (monthly income plus surplus from previous month)
    private double savings; // user input
    private double disposableIncome; // calced: rollingIncome - expenseTotal
    private double afterSavings; // calced: disposableIncome - savings
    private double expenseTotal; // calced: summation of MonthlyExpense objects
    private Collection<MonthlyExpense> expenses;
    
    // observable list of expenses to track changes
    transient private ObservableList<MonthlyExpense> observableExpenses;
    
    /**
     * Empty constructor used by Gson
     */
    public MonthlyData() {}
    
    public MonthlyData(int year, int month) {
        this.year = year;
        this.month = month;
        initMonthlyExpenses();
    }
    
    public String getMapKey() {
        return month + "-" + year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public String getMonthName() {
        return Month.of(month).toString();
    }
    
    public int getYear() {
        return year;
    }
    
    public double getRollingIncome() {
        return rollingIncome;
    }
    
    public void setRollingIncome(double rollingIncome) {
        this.rollingIncome = rollingIncome;
        calculateDisposableIncome();
    }
    
    public double getDisposableIncome() {
        return disposableIncome;
    }

    private void calculateDisposableIncome() {
        disposableIncome = getRollingIncome() - getExpenseTotal();
        calculateAmountAfterSavings();
    }
    
    public double getSavings() {
        return savings;
    }
    
    public void setSavings(double savings) {
        this.savings = savings;
        calculateAmountAfterSavings();
    }
    
    public double getAmountAfterSavings() {
        return afterSavings;
    }
    
    private void calculateAmountAfterSavings() {
        afterSavings = getDisposableIncome() - getSavings();
    }
    
    public Collection<MonthlyExpense> getExpenses() {
        checkObservableList();
        return observableExpenses;
    }
    
    public void setExpenses(Collection<Expense> expenses) {
        checkObservableList();
        ArrayList<MonthlyExpense> mes = new ArrayList<>();
        expenses.stream().map((e) -> {
            return createMonthlyExpense(e);
        }).forEach((me) -> {
            mes.add(me);
        });
        observableExpenses.clear();
        observableExpenses.addAll(mes);
    }
    
    public void addExpense(Expense e) {
        checkObservableList();
        observableExpenses.add(createMonthlyExpense(e));
    }
    
    private MonthlyExpense createMonthlyExpense(Expense e) {
        MonthlyExpense me = new MonthlyExpense(getMonth(), getYear(), e.getName());
        me.setActive(true);
        me.setAmount(e.getDefaultAmount());
        return me;
    }
    
    private void initObservableArrayList() {
        if (expenses == null)
            observableExpenses = FXCollections.observableArrayList();
        else {
            observableExpenses = FXCollections.observableArrayList(expenses);
            calcExpenseTotal();
        }
        observableExpenses.addListener((Observable c) -> {
            calcExpenseTotal();
        });
    }
    
    public double getExpenseTotal() {
        if (observableExpenses == null)
            calcExpenseTotal();
        return expenseTotal;
    }
    
    /**
     * Calculates the expense total. Should be called whenever
     * the expenses collection is updated in anyway.
     */
    private void calcExpenseTotal() {
        checkObservableList();
        expenseTotal = 0;
        observableExpenses.stream().filter(ex -> ex.isActive()).forEach(expense -> {
            expenseTotal += expense.getAmount();
        });
        calculateDisposableIncome();
    }
    
    private void checkObservableList() {
        if (observableExpenses == null)
            initObservableArrayList();
    }
    
    /**
     * create the initial list of monthly expense objects from
     * the default expense list.
     */
    private void initMonthlyExpenses() {
        setExpenses(SavingsTrackerPro.getExpenseMap().values());
    }
    
    public void refreshExpenses() {
        calcExpenseTotal();
    }
    
    @Override
    public String toString() {
        return "(" + getMonth() + ", " + getYear() + ", expenses: " + getExpenses() + ")";
    }
    
    public String getLabel() {
        return this.getMonthName() + " - " + getYear();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof MonthlyData) {
            MonthlyData md = (MonthlyData) o;
            if (getYear() < md.getYear()) return -1;
            if (getYear() > md.getYear()) return 1;
            if (getMonth() < md.getMonth()) return -1;
            if (getMonth() > md.getMonth()) return 1;
            return 0;
        }
        throw new IllegalArgumentException("Non MonthlyData object compared.");
    }
    
    public void remove(MonthlyExpense me) {
        checkObservableList();
        observableExpenses.remove(me);
    }
    
}