# Malabe Spares Depot

A JavaFX desktop application for managing inventory and sales at a vehicle spare parts business. Built for the CM1601 Programming Fundamentals coursework (IIT / RGU).

## Features

- **Dirty data parsing**: imports legacy inventory and dealer records from inconsistently delimited text files, normalising mixed delimiters and validating each record before it's used. Invalid records are logged, not discarded.
- **Inventory management**: add, update, delete, and sort parts by category; low-stock detection against a per-part threshold.
- **Multi-criteria search**: filter inventory by keyword, category, and price range simultaneously.
- **Cart and checkout**: add parts to a cart against live stock levels, with a 5% bulk discount (3+ units of an item) and a 10% synergy discount (cart contains both Engine and Electrical category parts), stacked in that order.
- **Dealer directory**: view all dealers, or pick four at random sorted by location.
- **Audit log**: every stock-changing action (add/update/delete/checkout) is recorded with a timestamp and viewable in-app.
- **Part images**: inventory table thumbnails and an image preview when adding/editing a part, with a placeholder fallback for parts with no photo.

## Tech stack

- Java 21
- JavaFX 26 (FXML + CSS)
- Maven
- JUnit 5 (Jupiter)

## Project structure

```
src/main/java/
  models/   Plain data classes: Part, Dealer, CartItem, AuditLogEntry
  utils/    Business logic and persistence: parsers, managers, loggers
  gui/      JavaFX controllers, one per tab
  main/     Application entry point

src/test/java/utils/   JUnit test suite (53 tests)

data/
  *_legacy.txt          Raw, dirty source data
  inventoryData.txt      Cleaned, parsed inventory data
  dealerData.txt         Cleaned, parsed dealer data
  invalidRecords.txt     Rejected records with reasons, for debugging
  audit_log.txt          Stock-change history
  images/                Part photos + no_image.png placeholder
```

## Running the application

```bash
./mvnw.cmd javafx:run
```

## Running the tests

```bash
./mvnw.cmd test
```

Runs 53 JUnit tests across `PartParserTest`, `FileParserTest`, `InventoryManagerTest`, `CartManagerTest`, `DealerManagerTest`, `DealerParserTest`, and `ThresholdStoreTest`, covering dirty data parsing, mixed delimiters, cart discount rules, stock deduction, low-stock detection, multi-criteria search, sorting, and invalid input handling.
