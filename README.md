# Orders Processor (Concurrency)

This Java application demonstrates a multi-threaded approach to process orders concurrently. It consists of two main classes: `Orders` and `OrdersProcessor`, designed to efficiently read and process orders from files using multiple threads.

## Table of Contents
- [Overview](#overview)
- [Classes](#classes)
- [Usage](#usage)
- [File Structure](#file-structure)
- [Concurrency](#concurrency)
- [Results](#results)
- [How to Run](#how-to-run)
- [License](#license)

## Overview

The application is designed to read order details from input files, process them concurrently using multiple threads, and generate summarized results. The key classes include:

- `Orders`: Represents an individual thread responsible for reading and processing orders for a specific client.
- `OrdersProcessor`: Manages the overall order processing, including handling concurrency, summarizing results, and generating output.

## Classes

### `Orders`

- `Orders(int number, TreeMap<String, Integer> orders, String base, TreeMap<String, Double> items)`: Constructor for the `Orders` class.
- `run()`: The main execution method for the thread, reads orders from files and updates the shared results.
- `readFromFile()`: Reads order details from the input file.
- `createString()`: Creates a formatted string with order details.

### `OrdersProcessor`

- `main(String[] args)`: Entry point for the application.
- `processOrdersWithThreads(int numOrders, String orderBaseFileName, String resultsFileName)`: Processes orders using multiple threads.
- `processOrdersWithSingleThread(int numOrders, String orderBaseFileName, String resultsFileName)`: Processes orders using a single thread.
- `generateResults(String resultsFileName, TreeMap<String, Item> itemsData)`: Generates summary results.

## Usage

1. Input data is provided in the form of item details and order files.
2. The application processes orders concurrently using multiple threads.
3. Results are generated, including detailed order information and a summary of all orders.

## File Structure

- `processor/`: Package containing the `Orders` and `OrdersProcessor` classes.
- `tests/`: Package with testing support classes.
- `itemsData.txt`: File containing details of available items.
- `results.txt`: Output file containing the summary results.

## Concurrency

Concurrency is achieved by using multiple instances of the `Orders` class, each representing a thread processing orders for a specific client. Synchronization is used to ensure proper updating of shared data structures.

## Results

The application generates detailed order information and a summary of all orders. The results are stored in the `results.txt` file.

## How to Run

1. Compile the Java files: `javac processor/*.java tests/*.java`
2. Run the application: `java processor.OrdersProcessor`

## License

This project is licensed under the [MIT License](LICENSE).
