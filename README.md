# School Management System (EduManager)

This is a comprehensive school management system built with JavaFX and SQLite.

## Features

- **Dashboard**: At-a-glance overview of key school metrics.
- **Student Management**: Manage student profiles, including bio, attendance, and grades.
- **Teacher & Parent Management**: Keep records of staff and parents.
- **Class Management**: Organize classes and assign teachers.
- **Exams & Grading**: Schedule exams and record student grades.
- **Transport**: Manage school bus routes.
- **User Management**: Control access with a simple user system.

## How to Run



### Prerequisites



-   Java JDK 17 or higher



### Running the Application



The project includes the Maven Wrapper, which means you **do not** need to have Maven installed on your system. The wrapper script will automatically download the correct version for you.



1.  **Clone the repository:**

    ```bash

    git clone <repository-url>

    cd SchoolManagement

    ```



2.  **Run with the Maven Wrapper:**

    Open a terminal in the project root and execute the appropriate command for your operating system:



    **For Windows (Command Prompt or PowerShell):**

    ```powershell

    # The script will download Maven if you don't have it.

    .\mvnw.cmd clean javafx:run

    ```



    **For macOS/Linux:**

    ```bash

    # The script will download Maven if you don't have it.

    ./mvnw clean javafx:run

    ```



3.  **Login:**

    The application will start, and a database file `school_management.db` will be created with mock data.

    -   **Username**: `admin`

    -   **Password**: `admin`



## Standalone Application

A standalone runnable JAR can be built using the Maven Assembly plugin.

1.  **Build the JAR:**
    ```bash
    ./mvnw clean package
    ```
2.  **Run the JAR:**
    The executable JAR will be located in the `target` directory (e.g., `SchoolManagement-1.0-SNAPSHOT-jar-with-dependencies.jar`).
    ```bash
    java -jar target/SchoolManagement-1.0-SNAPSHOT-jar-with-dependencies.jar
    ```

---
