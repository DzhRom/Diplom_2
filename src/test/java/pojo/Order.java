package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Order {
    private boolean success;
    private List<Object> data;
    private OrderGetJSON orderGetJSON;

    public Order(boolean success, List<Object> data, OrderGetJSON orderGetJSON) {
        this.success = success;
        this.data = data;
        this.orderGetJSON = orderGetJSON;
    }

    public Order()  {}

    @Override
    public String toString() {
        return "success=" + success + ", data=" + data + ", orderGetJSON=" + orderGetJSON;
    }
}
