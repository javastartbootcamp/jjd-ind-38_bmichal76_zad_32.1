package pl.javastart.streamstask;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExampleTest {
    private List<User> users;
    private List<Expense> expenses;
    private StreamsTask streamsTask;

    @Before
    public void ints() {
        users = new ArrayList<>();
        users.add(new User(1L, "Alicja", 20));
        users.add(new User(2L, "Dominik", 15));
        users.add(new User(3L, "Patrycja", 25));
        users.add(new User(4L, "Marcin", 30));
        users.add(new User(5L, "Tomek", 18));
        users.add(new User(6L, "Damian", 26));

        expenses = new ArrayList<>();
        expenses.add(new Expense(1L, "Buty", new BigDecimal("149.99"), ExpenseType.WEAR));
        expenses.add(new Expense(1L, "Sałatka", new BigDecimal("14.99"), ExpenseType.FOOD));
        expenses.add(new Expense(2L, "Bluza", new BigDecimal("100"), ExpenseType.WEAR));
        expenses.add(new Expense(2L, "Skarpetki", new BigDecimal("39"), ExpenseType.WEAR));
        expenses.add(new Expense(2L, "Pizza", new BigDecimal("25"), ExpenseType.FOOD));

        streamsTask = new StreamsTask();
    }

    @Test
    public void getListForTwoWoman() {
        //when
        List<User> women = streamsTask.findWomen(users).stream().toList();

        //then
        Assert.assertEquals(2, women.size());
        List<User> tab = List.of(new User(1L, "Alicja", 20), new User(3L, "Patrycja", 25));
        Assert.assertEquals(tab, women);
    }

    @Test
    public void getListForTreeWoman() {
        //when
        List<User> newUsers = new ArrayList<>(users);
        newUsers.add(new User(7L, "Dominika", 27));
        List<User> women = streamsTask.findWomen(newUsers).stream().toList();

        //then
        Assert.assertEquals(3, women.size());
        List<User> tab = List.of(new User(1L, "Alicja", 20), new User(3L, "Patrycja", 25)
                , new User(7L, "Dominika", 27));
        Assert.assertEquals(tab, women);
    }

    @Test
    public void getWomanListIfUserListIsNull() {
        //when
        String msg = "";
        try {
            streamsTask.findWomen(null);
        } catch (IllegalArgumentException e) {
            msg = e.getMessage();
        }

        //then
        Assert.assertEquals("Niezainicjalizowana kolekcja dla użytkowników", msg);
        Assert.assertThrows(IllegalArgumentException.class, () -> streamsTask.findWomen(null));
    }

    @Test
    public void getWomanListForEmptyUserList() {
        //when
        List<User> newUsers = new ArrayList<>();
        List<User> women = streamsTask.findWomen(newUsers).stream().toList();

        //then
        Assert.assertEquals(0, women.size());
    }

    @Test
    public void averageMenAgeForInitsList() {
        //when
        Double averageMenAge = streamsTask.averageMenAge(users);

        //then
        Double expected = 22.25;
        Assert.assertEquals(expected, averageMenAge);
    }

    @Test
    public void averageMenAgeForExtendedList() {
        //when

        List<User> newUsers = new ArrayList<>(users);
        newUsers.add(new User(7L, "Zenek", 28));
        Double averageMenAge = streamsTask.averageMenAge(newUsers);

        //then
        Double expected = 23.4;
        Assert.assertEquals(expected, averageMenAge);
    }

    @Test
    public void averageMenAgeForEmptyList() {
        //when
        List<User> newUsers = new ArrayList<>();
        Double averageMenAge = streamsTask.averageMenAge(newUsers);

        //then
        Double expected = 0.0;
        Assert.assertEquals(expected, averageMenAge);
    }

    @Test
    public void averageMenAgeIfUserListIsNull() {
        //when
        String msg = "";
        try {
            streamsTask.averageMenAge(null);
        } catch (IllegalArgumentException e) {
            msg = e.getMessage();
        }
        //then
        Assert.assertEquals("Niezainicjalizowana kolekcja dla użytkowników", msg);
        Assert.assertThrows(IllegalArgumentException.class, () -> streamsTask.averageMenAge(null));
    }

    @Test
    public void expensesByUserIdForInitsList() {
        //when
        Map<Long, List<Expense>> expensesByUserId = streamsTask.groupExpensesByUserId(users, expenses);

        //then
        List<Expense> expenseUser1 = expensesByUserId.get(1L);
        List<Expense> expenseUser2 = expensesByUserId.get(2L);
        List<Expense> expenseUser3 = expensesByUserId.get(3L);
        List<Expense> expenseUser4 = expensesByUserId.get(4L);
        List<Expense> expenseUser5 = expensesByUserId.get(5L);
        List<Expense> expenseUser6 = expensesByUserId.get(6L);

        List<Expense> expenses1 = List.of(
                new Expense(1L, "Buty", new BigDecimal("149.99"), ExpenseType.WEAR),
                new Expense(1L, "Sałatka", new BigDecimal("14.99"), ExpenseType.FOOD));

        List<Expense> expenses2 = List.of(
                new Expense(2L, "Bluza", new BigDecimal("100"), ExpenseType.WEAR),
                new Expense(2L, "Skarpetki", new BigDecimal("39"), ExpenseType.WEAR),
                new Expense(2L, "Pizza", new BigDecimal("25"), ExpenseType.FOOD));

        List<Expense> empty = new ArrayList<>();

        Assert.assertEquals(expenses1, expenseUser1);
        Assert.assertEquals(expenses2, expenseUser2);
        Assert.assertEquals(empty, expenseUser3);
        Assert.assertEquals(empty, expenseUser4);
        Assert.assertEquals(empty, expenseUser5);
        Assert.assertEquals(empty, expenseUser6);
    }

    @Test
    public void expensesByUserIdForNullLists() {
        Assert.assertThrows(IllegalArgumentException.class, () -> streamsTask.groupExpensesByUserId(null, null));
    }

    @Test
    public void expensesByUserForInitsList() {
        //when
        Map<User, List<Expense>> expensesByUser = streamsTask.groupExpensesByUser(users, expenses);

        //then
        List<Expense> expenseUser1 = expensesByUser.get(users.get(0));
        List<Expense> expenseUser2 = expensesByUser.get(users.get(1));
        List<Expense> expenseUser3 = expensesByUser.get(users.get(2));
        List<Expense> expenseUser4 = expensesByUser.get(users.get(3));
        List<Expense> expenseUser5 = expensesByUser.get(users.get(4));
        List<Expense> expenseUser6 = expensesByUser.get(users.get(5));

        List<Expense> expenses1 = List.of(
                new Expense(1L, "Buty", new BigDecimal("149.99"), ExpenseType.WEAR),
                new Expense(1L, "Sałatka", new BigDecimal("14.99"), ExpenseType.FOOD));

        List<Expense> expenses2 = List.of(
                new Expense(2L, "Bluza", new BigDecimal("100"), ExpenseType.WEAR),
                new Expense(2L, "Skarpetki", new BigDecimal("39"), ExpenseType.WEAR),
                new Expense(2L, "Pizza", new BigDecimal("25"), ExpenseType.FOOD));

        List<Expense> empty = new ArrayList<>();

        Assert.assertEquals(expenses1, expenseUser1);
        Assert.assertEquals(expenses2, expenseUser2);
        Assert.assertEquals(empty, expenseUser3);
        Assert.assertEquals(empty, expenseUser4);
        Assert.assertEquals(empty, expenseUser5);
        Assert.assertEquals(empty, expenseUser6);
    }

    @Test
    public void expensesByUserForNullLists() {
        Assert.assertThrows(IllegalArgumentException.class, () -> streamsTask.groupExpensesByUser(null, null));
    }

}
