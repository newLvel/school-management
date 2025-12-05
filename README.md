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

- Java JDK 17 or higher
- Apache Maven

### Running the Application

1.  **Clone the repository:**

    ```bash
    git clone <repository-url>
    cd SchoolManagement
    ```

2.  **Run with Maven:**
    The project is configured to run easily with the JavaFX Maven plugin. Execute the following command in the project root:

    ```powershell
    # For Windows (Powershell)
    $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"; .\mvnw.cmd clean javafx:run

    # For macOS/Linux
    export JAVA_HOME="/path/to/your/jdk-17"
    ./mvnw clean javafx:run
    ```

3.  **Login:**
    The application will start, and a database file `school_management.db` will be created with mock data.
    - **Username**: `admin`
    - **Password**: `admin`

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
