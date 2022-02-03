package homework;


import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.comparingLong;


public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private TreeMap<Customer, String> map = new TreeMap<>(comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return copyEntry(this.map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyEntry(this.map.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        this.map.put(customer, data);
    }


    @Override
    public String toString() {
        return "CustomerService {" + this.map.toString() + "}";
    }

    private static Customer copyCustomer(Customer old) {
        return new Customer(old.getId(), old.getName(), old.getScores());
    }

    private Map.Entry<Customer, String> copyEntry(Map.Entry<Customer, String> entry) {
        if (entry != null) {
            Customer customer = entry.getKey();
            return new SimpleImmutableEntry(
                    copyCustomer(customer),
                    entry.getValue()
                );
        }
        return null;
    }
}
