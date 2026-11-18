# Online Shopping System — VS Code Ready

A console-based Java project using JDBC and MySQL. The project uses Maven so VS Code can download the MySQL JDBC driver automatically.

## Requirements
- JDK 17 or newer
- Visual Studio Code
- Extension: **Extension Pack for Java** (VS Code may recommend it)
- MySQL Server and MySQL Workbench (or MySQL command line)

## 1. Create the database
Start MySQL, open `sql/schema.sql` in MySQL Workbench, and execute the whole script. It creates the `online_shopping` database, tables, and sample products.

## 2. Set your MySQL password
The default username is `root` and the default password is blank.
If your MySQL password is not blank, set the environment variable before launching VS Code from that terminal:

**Windows PowerShell**
```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="your_mysql_password"
```

**macOS/Linux**
```bash
export DB_USER=root
export DB_PASSWORD='your_mysql_password'
```

You can also edit the defaults in `src/main/java/DBConnection.java`. Do not commit real passwords to GitHub.

## 3. Open and run in VS Code
1. Extract this ZIP file.
2. In VS Code choose **File → Open Folder** and select the extracted `OnlineShoppingSystem_VSCode` folder (the folder containing `pom.xml`).
3. Install **Extension Pack for Java** if prompted.
4. Wait for Maven dependencies to finish loading.
5. Open `src/main/java/Main.java` and click **Run** above `main`, or open the VS Code terminal and run:
```bash
mvn clean compile exec:java
```

## Troubleshooting
- `mvn is not recognized`: install Apache Maven and add it to PATH, or use the Maven support provided by VS Code.
- `Communications link failure`: make sure the MySQL server is running.
- `Access denied for user`: set `DB_USER` and `DB_PASSWORD` correctly.
- `Unknown database`: run `sql/schema.sql` first.

## Features
- Display products and stock
- Add items to a cart
- View cart total
- Checkout and save orders
- Reduce stock within a database transaction

This is a learning project, not a production payment system.
