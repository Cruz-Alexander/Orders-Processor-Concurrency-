```java
//package processor;
//
//import java.io.*;
//import java.text.NumberFormat;
//import java.util.*;
//
//public class OrdersProcessor {
//      private Map<Integer, Order> ordersMap;
//
//      public OrdersProcessor() {
//              ordersMap = new HashMap<>();
//      }
//
//      private Map<String, Item> readItemsData(String itemsDataFileName) {
//              Map<String, Item> itemsData = new HashMap<>();
//              try (Scanner scanner = new Scanner(new File(itemsDataFileName))) {
//                      while (scanner.hasNextLine()) {
//                              String line = scanner.nextLine();
//                              String[] parts = line.split(" "); // Split based on spaces
//                              String itemName = parts[0];
//                              double itemPrice = Double.parseDouble(parts[1]);
//                              Item item = new Item(itemName, itemPrice);
//                              itemsData.put(itemName, item);
//                      }
//              } catch (FileNotFoundException e) {
//                      e.printStackTrace();
//              }
//              return itemsData;
//      }
//
//      private void processOrder(String orderFileName, Map<String, Item> itemsData) {
//          try (Scanner scanner = new Scanner(new File(orderFileName))) {
//              int clientId = Integer.parseInt(scanner.nextLine().split(":")[1].trim());
//              Order order = new Order(clientId);
//
//              while (scanner.hasNextLine()) {
//                  String line = scanner.nextLine();
//                  if (line.isEmpty()) continue;
//
//                  StringTokenizer tokenizer = new StringTokenizer(line, " /");
//                  String itemName = tokenizer.nextToken().trim();
//                  int quantity = Integer.parseInt(tokenizer.nextToken().trim());
//
//                  Item item = itemsData.get(itemName);
//                  if (item != null) {
//                      for (int i = 0; i < quantity; i++) {
//                          order.addItem(item);
//                      }
//                  }
//              }
//
//              synchronized (ordersMap) {
//                  ordersMap.put(clientId, order);
//              }
//          } catch (FileNotFoundException e) {
//              e.printStackTrace();
//          }
//      }
//
//      private void processOrdersWithThreads(int numOrders, String orderBaseFileName, String resultsFileName) {
//              long startTime = System.currentTimeMillis();
//              List<Thread> threads = new ArrayList<>();
//              Map<String, Item> itemsData = readItemsData("itemsData.txt");
//
//              for (int i = 1; i <= numOrders; i++) {
//                      String orderFileName = orderBaseFileName + i + ".txt";
//                      Thread thread = new Thread(() -> processOrder(orderFileName, itemsData));
//                      thread.start();
//                      threads.add(thread);
//              }
//
//              for (Thread thread : threads) {
//                      try {
//                              thread.join();
//                      } catch (InterruptedException e) {
//                              e.printStackTrace();
//                      }
//              }
//
//              generateResults(resultsFileName, itemsData);
//              long endTime = System.currentTimeMillis();
//              System.out.println("Processing time (msec): " + (endTime - startTime));
//      }
//
//      private void processOrdersWithSingleThread(int numOrders, String orderBaseFileName, String resultsFileName) {
//              long startTime = System.currentTimeMillis();
//              Map<String, Item> itemsData = readItemsData("itemsData.txt");
//
//              for (int i = 1; i <= numOrders; i++) {
//                      String orderFileName = orderBaseFileName + i + ".txt";
//                      processOrder(orderFileName, itemsData);
//              }
//
//              generateResults(resultsFileName, itemsData);
//              long endTime = System.currentTimeMillis();
//              System.out.println("Processing time (msec): " + (endTime - startTime));
//      }
//
//      private void generateResults(String resultsFileName) {
//          List<Order> orders = new ArrayList<>();
//          synchronized (ordersMap) {
//              orders.addAll(ordersMap.values());
//          }
//
//          Collections.sort(orders, Comparator.comparingInt(Order::getClientId));
//          try (PrintWriter writer = new PrintWriter(new File(resultsFileName))) {
//              for (Order order : orders) {
//                  writer.println("----- Order details for client with Id: " + order.getClientId() + " -----");
//                  for (Item item : order.getItems()) {
//                      writer.println("Item's name: " + item.getName() + ", Cost per item: $" + String.format("%.2f", item.getPrice()) + 
//                              ", Quantity: " + order.getNumItems(item) + ", Cost: $" + String.format("%.2f", item.getPrice() * order.getNumItems(item)));
//                  }
//                  writer.println("Order Total: $" + String.format("%.2f", getTotalOrderCost(order)));
//                  writer.println();
//              }
//              writer.println("***** Summary of all orders *****");
//              writer.println("--------------------------------------------------");
//              Map<String, Integer> itemQuantityMap = new HashMap<>();
//              double totalRevenue = 0.0;
//
//              for (Order order : orders) {
//                  for (Item item : order.getItems()) {
//                      String itemName = item.getName();
//                      itemQuantityMap.put(itemName, itemQuantityMap.getOrDefault(itemName, 0) + order.getNumItems(item));
//                      totalRevenue += item.getPrice() * order.getNumItems(item);
//                  }
//              }
//
//              List<String> itemNames = new ArrayList<>(itemQuantityMap.keySet());
//              Collections.sort(itemNames);
//              for (String itemName : itemNames) {
//                  int quantitySold = itemQuantityMap.get(itemName);
//                  writer.println("Summary - Item's name: " + itemName + ", Cost per item: $" + String.format("%.2f", itemsData.get(itemName).getPrice()) + 
//                          ", Number sold: " + quantitySold + ", Item's Total: $" + String.format("%.2f", quantitySold * itemsData.get(itemName).getPrice()));
//              }
//
//              writer.println("--------------------------------------------------");
//              writer.println("Total Number of Items Sold: " + orders.stream().mapToInt(Order::getNumItems).sum());
//              writer.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
//              writer.println("TERPSPUBLICTESTS");
//          } catch (FileNotFoundException e) {
//              e.printStackTrace();
//          }
//      }
//
//      private double getTotalOrderCost(Order order) {
//              double totalCost = 0.0;
//              for (Item item : order.getItems()) {
//                      totalCost += item.getPrice();
//              }
//              return totalCost;
//      }
//
//      public static void main(String[] args) {
//              Scanner scanner = new Scanner(System.in);
//              OrdersProcessor processor = new OrdersProcessor();
//
//              System.out.print("Enter item's data file name: ");
//              String itemsDataFileName = scanner.nextLine();
//
//              System.out.print("Enter 'y' for multiple threads, any other character otherwise: ");
//              boolean useMultipleThreads = scanner.nextLine().equalsIgnoreCase("y");
//
//              System.out.print("Enter number of orders to process: ");
//              int numOrders = Integer.parseInt(scanner.nextLine());
//
//              System.out.print("Enter order's base filename: ");
//              String

 orderBaseFileName = scanner.nextLine();
//
//              System.out.print("Enter result's filename: ");
//              String resultsFileName = scanner.nextLine();
//
//              if (useMultipleThreads) {
//                      processor.processOrdersWithThreads(numOrders, orderBaseFileName, resultsFileName);
//              } else {
//                      processor.processOrdersWithSingleThread(numOrders, orderBaseFileName, resultsFileName);
//              }
//
//              scanner.close();
//      }
//}
//
package processor;

//previous code didn't format correctly

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
