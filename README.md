# iThink Logistics Automation Framework

> A Selenium WebDriver automation framework built using Java, TestNG, Maven, and the Page Object Model (POM) for automating logistics order workflows.

## Project Overview

The iThink Logistics Automation Framework is a Selenium WebDriver-based test automation framework developed using Java, TestNG, Maven, and the Page Object Model (POM). It was designed to automate key business workflows of the Orders module in a logistics management application.

The framework follows a modular and reusable architecture with dedicated components for WebDriver initialization, configuration management, reusable actions, explicit waits, logging, and test data handling.

This project reflects my practical experience in designing and developing an automation framework for a real-world logistics application and demonstrates industry-standard automation practices.

## Technology Stack

| Category | Technologies                 |
|----------|------------------------------|
| Programming Language | Java                         |
| Automation Tool | Selenium WebDriver           |
| Testing Framework | TestNG                       |
| Build Tool | Maven                        |
| Design Pattern | Page Object Model (POM)      |
| Test Data | Microsoft Excel (Apache POI) |
| Logging | Log4j2                       |
| Version Control | Git & GitHub                 |
| IDE | Eclipse IDE / Intellij IDEA  |



## Framework Architecture

The framework follows the **Page Object Model (POM)** design pattern with a modular structure that separates page objects, reusable utilities, test classes, and test data for better maintainability and scalability.

```text
src
├── main
│   ├── java
│   │   └── iThink.Automation
│   │       ├── base
│   │       ├── components
│   │       ├── modules.forwardOrders
│   │       ├── pages
│   │       └── utils
│   │
│   └── resources
│       ├── config.properties
│       └── log4j2.xml
│
└── test
    ├── java
    │   └── iThink.Automation
    │       ├── base
    │       ├── tests
    │       └── utils
    │
    └── resources
        └── testdata
```

The framework separates reusable components, page objects, test classes, configuration, and test data to support maintainable, scalable, and reusable test automation.

## Framework Components

| Component | Responsibility |
|----------|----------------|
| **DriverFactory** | Initializes and manages WebDriver instances for browser execution. |
| **ConfigReader** | Reads configuration values such as application URL and browser settings from the properties file. |
| **WaitUtils** | Provides reusable explicit wait methods for reliable element synchronization. |
| **CommonActions** | Contains reusable UI interaction methods used across multiple page objects. |
| **BasePage** | Provides common functionality shared by all page classes. |
| **BaseTest** | Manages test setup, WebDriver initialization, and test teardown. |
| **Page Objects** | Encapsulate web elements and business actions following the Page Object Model (POM). |
| **ExcelUtils** | Reads test data from Excel files to support data-driven testing. |
| **DataTable Component** | Provides reusable methods for interacting with dynamic web tables. |

## Features

- Selenium WebDriver automation using Java
- Page Object Model (POM) architecture
- Centralized WebDriver management
- Externalized configuration using `config.properties`
- Reusable utility classes for common actions and explicit waits
- Data-driven testing using Excel (Apache POI)
- Dynamic web table handling
- Logging using Log4j2
- Test execution using TestNG
- Maven-based project management
- Modular and reusable framework design

## Prerequisites

Before executing the project, ensure the following software is installed:

- Java JDK 17+
- Apache Maven
- Git
- Eclipse IDE / IntelliJ IDEA
- Google Chrome (managed automatically using WebDriverManager)

## How to Execute

1. Clone the repository.
2. Open the project in Eclipse or IntelliJ IDEA.
3. Update the configuration values in `config.properties` if required.
4. Allow Maven to download all project dependencies.
5. Execute the required TestNG suite (`testng.xml`) or individual test classes.
6. View execution logs in the console and TestNG HTML reports after execution.


## Author

**Bhavesh Deshmukh**

Software Test Engineer

ISTQB® Certified Tester – Foundation Level (CTFL)

This project was developed to demonstrate a modular Selenium WebDriver automation framework using Java, TestNG, Maven, and the Page Object Model (POM), based on practical experience in logistics domain automation.
