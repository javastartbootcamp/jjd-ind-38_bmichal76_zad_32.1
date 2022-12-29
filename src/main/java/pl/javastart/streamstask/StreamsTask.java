package pl.javastart.streamstask;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class StreamsTask {

    public static void main(String[] args) {
        StreamsTask streamsTask = new StreamsTask();
        streamsTask.run();
    }

    private void run() {
        List<User> users = new ArrayList<>();

        users.add(new User(1L, "Alicja", 20));
        users.add(new User(2L, "Dominik", 15));
        users.add(new User(3L, "Patrycja", 25));
        users.add(new User(4L, "Marcin", 30));
        users.add(new User(5L, "Tomek", 18));
        users.add(new User(6L, "Damian", 26));

        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1L, "Buty", new BigDecimal("149.99"), ExpenseType.WEAR));
        expenses.add(new Expense(1L, "Sałatka", new BigDecimal("14.99"), ExpenseType.FOOD));
        expenses.add(new Expense(2L, "Bluza", new BigDecimal("100"), ExpenseType.WEAR));
        expenses.add(new Expense(2L, "Skarpetki", new BigDecimal("39"), ExpenseType.WEAR));
        expenses.add(new Expense(2L, "Pizza", new BigDecimal("25"), ExpenseType.FOOD));

        Collection<User> women = findWomen(users);
        Double averageMenAge = averageMenAge(users);
        Map<Long, List<Expense>> expensesByUserId = groupExpensesByUserId(users, expenses);
        Map<User, List<Expense>> expensesByUser = groupExpensesByUser(users, expenses);
    }

    // metoda powinna zwracać listę kobiet (sprawdzając, czy imię kończy się na "a")
    Collection<User> findWomen(Collection<User> users) {
        validateUserCollection(users);
        return users.stream()
                .filter(this::isWomen)
                .collect(Collectors.toList());
    }

    // metoda powinna zwracać średni wiek mężczyzn (sprawdzając, czy imię nie kończy się na "a")
    Double averageMenAge(Collection<User> users) {
        validateUserCollection(users);
        return users.stream()
                .filter(this::isMen)
                .map(user -> (double) user.getAge())
                .collect(Collectors.averagingDouble(value -> value));
    }

    // metoda powinna zwracać wydatki zgrupowane po ID użytkownika
    Map<Long, List<Expense>> groupExpensesByUserId(Collection<User> users, List<Expense> expenses) {
        validateCollections(users, expenses);
        Map<Long, List<Expense>> exp = getGroupedExpenses(expenses);
        return users.stream()
                .collect(Collectors.toMap(User::getId, user -> getExpensesForUser(user, exp)));
    }

    // metoda powinna zwracać wydatki zgrupowane po użytkowniku
    // podobne do poprzedniego, ale trochę trudniejsze
    Map<User, List<Expense>> groupExpensesByUser(Collection<User> users, List<Expense> expenses) {
        validateCollections(users, expenses);
        Map<Long, List<Expense>> exp = getGroupedExpenses(expenses);
        return users.stream()
                .collect(Collectors.toMap(user -> user, user -> getExpensesForUser(user, exp)));
    }

    private boolean isWomen(User user) {
        if (user != null) {
            String name = user.getName();
            return name.endsWith("a");
        }
        return false;
    }

    private boolean isMen(User user) {
        return !isWomen(user);
    }

    private void validateUserCollection(Collection<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("Niezainicjalizowana kolekcja dla użytkowników");
        }
    }

    private void validateCollections(Collection<User> users, List<Expense> expenses) {
        if (users == null || expenses == null) {
            throw new IllegalArgumentException("Niezainicjalizowane kolekcje dla grupowania");
        }
    }

    private Map<Long, List<Expense>> getGroupedExpenses(List<Expense> expense) {
        return expense.stream()
                .collect(Collectors.groupingBy(Expense::getUserId));
    }

    private List<Expense> getExpensesForUser(User user, Map<Long, List<Expense>> exp) {
        Long id = user.getId();
        List<Expense> list = exp.get(id);
        return Objects.requireNonNullElseGet(list, ArrayList::new);
    }

}
