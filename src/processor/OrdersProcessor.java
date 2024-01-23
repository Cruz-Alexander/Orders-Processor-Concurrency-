```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class OrdersProcessor {

    private static TreeMap<String, Double> shop;
    public static TreeMap<Integer, String> output;
    public static TreeMap<Integer, TreeMap<String, Integer>> summing;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String itemfile, results, data, base;
        boolean multi;
        int ordernumber;
        summing = new TreeMap<Integer, TreeMap<String, Integer>>();
        output = new TreeMap<Integer, String>();
        shop = new TreeMap<String, Double>();

        System.out.println("Enter item's data file name:");
        itemfile = scanner.nextLine();
        try {
            data = Files.readString(Paths.get(itemfile));
            String[] items = data.split("\n");
            for (String item : items) {
                String[] line = item.split(" ");
                shop.put(line[0], Double.parseDouble(line[1]));
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }

        System.out.println("Enter 'y' for multiple threads, any other character otherwise: ");
        if (scanner.nextLine().equals("y")) {
            multi = true;
        } else {
            multi = false;
        }
        System.out.println("Enter number of orders to process: ");
        ordernumber = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter order's base filename: ");
        base = scanner.nextLine();
        System.out.println("Enter result's filename: ");
        results = scanner.nextLine();
        scanner.close();

        long startTime = System.currentTimeMillis();

        if (multi) {
            ArrayList<Orders> thread = new ArrayList<Orders>();
            for (int i = 1; i <= ordernumber; i++) {
                thread.add(new Orders(i, new TreeMap<String, Integer>(), base, shop));
            }
            for (Orders o : thread) {
                o.start();
            }
            for (Orders o : thread) {
                o.join();
            }

        } else {
            ArrayList<Orders> thread = new ArrayList<Orders>();
            for (int i = 1; i <= ordernumber; i++) {
                thread.add(new Orders(i, new TreeMap<String, Integer>(), base, shop));
            }
            for (Orders o : thread) {
                o.start();
                o.join();
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(results));
        for (Integer i : output.keySet()) {
            writer.append(output.get(i));
        }

        TreeMap<String, Integer> sumtable = new TreeMap<String, Integer>();
        String summary = "***** Summary of all orders *****" + "\n";
        double sumtotal = 0.0;
        for (int i : summing.keySet()) {
            for (String s : summing.get(i).keySet()) {
                sumtable.put(s, 0);
            }
        }
        for (int i : summing.keySet()) {
            for (String s : summing.get(i).keySet()) {
                sumtable.put(s, sumtable.get(s) + summing.get(i).get(s));
            }
        }

        for (String s : sumtable.keySet()) {
            summary += "Summary - Item's name: " + s + ", Cost per item: "
                    + NumberFormat.getCurrencyInstance().format(shop.get(s)) + ", Number sold: " + sumtable.get(s)
                    + ", Item's Total: "
                    + NumberFormat.getCurrencyInstance().format((double) (shop.get(s) * sumtable.get(s)));
            sumtotal += (double) (shop.get(s) * sumtable.get(s));
            summary += "\n";

        }

        summary += "Summary Grand Total: " + NumberFormat.getCurrencyInstance().format(sumtotal);
        summary += "\n";

        writer.append(summary);
        writer.close();
        long endTime = System.currentTimeMillis();
        System.out.println("Processing time (msec): " + (endTime - startTime));
        System.out.println("Results can be found in the file: " + results);

    }

}
