/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import savingstrackerpro.models.Expense;
import savingstrackerpro.models.MonthlyData;
import savingstrackerpro.models.MonthlyExpense;

/**
 *
 * @author Agustin Muniz
 */
public class MonthlyDataUnitTest {
    private static Map<String, MonthlyData> monthlyData;
    
    MonthlyData md;
    Expense ex1, ex2;
    
    @Before
    public void setUp() {
        monthlyData = new HashMap<>();
        
        md = new MonthlyData(2015, 1);
        
        ex1 = new Expense("food", 50.34);
        ex2 = new Expense("car", 468.34);
    }
    
    @After
    public void tearDown() {
        md = null;
    }
    
    //@Test
    public void montlyDataObject() {
        md.addExpense(ex1);
        System.out.println("expense total = " + md.getExpenseTotal());
        
        md.addExpense(ex2);
        System.out.println("expense total = " + md.getExpenseTotal());
    }
    
    //@Test
    public void setExpensesTest() {
        ArrayList<Expense> exs = new ArrayList<>();
        exs.add(ex1);
        exs.add(ex2);
        
        System.out.println("expense total = " + md.getExpenseTotal());
        md.setExpenses(exs);
        System.out.println("expense total = " + md.getExpenseTotal());
    }
    
    //@Test
    public void testDisposableIncome() {
        System.out.println("md disposable income: " + md.getDisposableIncome());
        md.addExpense(ex1);
        System.out.println("md disposable income: " + md.getDisposableIncome());
        md.setRollingIncome(200);
        System.out.println("md disposable income: " + md.getDisposableIncome());
    }
    
    @Test
    public void testAmountAfterSavings() {
        System.out.println("md after savings: " + md.getAmountAfterSavings());
        md.addExpense(ex1);
        System.out.println("md after savings: " + md.getAmountAfterSavings());
        md.setRollingIncome(200);
        System.out.println("md after savings: " + md.getAmountAfterSavings());
        md.setSavings(120);
        System.out.println("md after savings: " + md.getAmountAfterSavings());
    }

}
