package processor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.TreeMap;

public class Orders extends Thread {
    private int number;
    private TreeMap<String, Integer> orders;
    private String base;
    private TreeMap<String, Double> items;

    public Orders(int number, TreeMap<String, Integer> orders, String base, TreeMap<String, Double> items) {
        this.number = number;
        this.orders = orders;
        this.base = base;
        this.items = items;
    }

    private TreeMap<String, Integer> readfromfile() throws IOException {
        this.orders = new TreeMap<String, Integer>();
        FileReader f = new FileReader(base + number + ".txt");
        BufferedReader br = new BufferedReader(f);
        String contentLine = br.readLine();
        int client = Integer.parseInt(contentLine.split(":")[1].trim());
        System.out.println("Reading order for client with id: " + client);
        contentLine = br.readLine();
        while (contentLine != null) {
            String key = contentLine.split(" ")[0];
            if (orders.containsKey(key)) {
                orders.put(key, orders.get(key) + 1);
            } else {
                orders.put(key, 1);
            }
            contentLine = br.readLine();
        }
        br.close();
        return orders;
    }

    private String createString() {
        String ans = "";
        ans += "----- Order details for client with Id: " + (1000 + number) + " -----";
        ans += "\n";
        double cost = 0.0;
        for (String s : orders.keySet()) {
            ans += "Item's name: " + s + ", Cost per item: " + NumberFormat.getCurrencyInstance().format(items.get(s))
                    + ", Quantity: " + orders.get(s) + ", Cost: "
                    + NumberFormat.getCurrencyInstance().format((double) (items.get(s) * orders.get(s)));
            cost += (double) (items.get(s) * orders.get(s));
            ans += "\n";
        }
        ans += "Order Total: " + NumberFormat.getCurrencyInstance().format(cost);
        ans += "\n";

        return ans;
    }

    public void run() {
        try {
            this.orders = this.readfromfile();
            synchronized (OrdersProcessor.output) {
                OrdersProcessor.summing.put(this.number, orders);
                OrdersProcessor.output.put(this.number, this.createString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
