package homework;


import java.util.LinkedList;

public class CustomerReverseOrder {
    private LinkedList<Customer> collection = new LinkedList<>();

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    public void add(Customer customer) {
        this.collection.add(customer);
    }

    public Customer take() {
        return this.collection.pollLast(); // это "заглушка, чтобы скомилировать"
    }
}
