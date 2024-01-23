```java
package processor;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int clientId;
    private List<Item> items;

    public Order(int clientId) {
        this.clientId = clientId;
        this.items = new ArrayList<>();
    }

    public int getClientId() {
        return clientId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client ID: ").append(clientId).append("\n");
        for (Item item : items) {
            sb.append(item).append("\n");
        }
        return sb.toString();
    }

    public int getNumItems(Item item) {
        int count = 0;
        for (Item orderItem : items) {
            if (orderItem.equals(item)) {
                count++;
            }
        }
        return count;
    }
}
