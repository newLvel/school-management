# School Management System - Development Plan

## 1. Screen Analysis & Functional Requirements

Based on the design mockups in `ui_design/`, the application will consist of the following main sections:

### A. Dashboard (`dashboard.webp`)
- **Purpose:** Central hub for system overview.
- **Key Features:**
  - Statistics cards (Total Students, Total Teachers, Total Classes).
  - Quick access navigation sidebar.
  - Recent activity/Calendar (inferred).

### B. Student Management (`student.jpg`)
- **Purpose:** Manage student records.
- **Key Features:**
  - Data Grid/Table view of all students.
  - Search and filter functionality.
  - CRUD Actions: Add, Edit, Delete, View Profile.
  - **Data Fields:** Name, Roll No, Class, Parent Contact, Address, DOB.

### C. Teacher Management (`teacher.webp`)
- **Purpose:** Manage teacher staff records.
- **Key Features:**
  - List of teachers.
  - Subject/Class assignment.
  - Contact details and qualifications.
  - CRUD operations.

### D. Parent Management (`parents.jpg`, `parent_form.jpg`)
- **Purpose:** Manage parent/guardian information.
- **Key Features:**
  - List view of parents.
  - Detailed entry form.
  - Linkage to Students (Parent-Student relationship).

### E. Class Management (`class.jpg`, `create_new_class_view...`)
- **Purpose:** Organize academic classes and sections.
- **Key Features:**
  - List of active classes.
  - **Create New Class:** Form to define class name, section, and assigned teacher.
  - **Detailed View:** View students and subjects assigned to a specific class.

### F. Exams & Grades (`exam.jpg`)
- **Purpose:** Manage examination schedules and results.
- **Key Features:**
  - Exam scheduling.
  - Grade entry/recording.

### G. Transport (`transport.jpg`)
- **Purpose:** Manage school transport logistics.
- **Key Features:**
  - Route management.
  - Bus/Vehicle assignment.

### H. Settings (`settings.jpg`)
- **Purpose:** System configuration.
- **Key Features:**
  - School profile setup.
  - User management.

---

## 2. Database Schema (SQLite)

### Core Tables
1.  **`users`**: `id`, `username`, `password_hash`, `role` (admin, teacher)
2.  **`students`**: `id`, `first_name`, `last_name`, `dob`, `gender`, `address`, `parent_id`, `class_id`, `transport_id`
3.  **`teachers`**: `id`, `first_name`, `last_name`, `email`, `phone`, `specialization`, `hire_date`
4.  **`parents`**: `id`, `father_name`, `mother_name`, `email`, `phone`, `address`
5.  **`classes`**: `id`, `class_name`, `section`, `teacher_id`
6.  **`subjects`**: `id`, `subject_name`, `code`
7.  **`class_subjects`**: `class_id`, `subject_id`, `teacher_id`
8.  **`exams`**: `id`, `exam_name`, `date`, `class_id`
9.  **`grades`**: `id`, `exam_id`, `student_id`, `subject_id`, `score`, `remarks`
10. **`transport_routes`**: `id`, `route_name`, `vehicle_number`, `driver_name`, `cost`

---

## 3. Technology Stack

-   **Language:** Java (JDK 17)
-   **UI Framework:** JavaFX 17 (Modular)
-   **Build Tool:** Maven
-   **Database:** SQLite (Requires adding `sqlite-jdbc` dependency)
-   **Existing Dependencies:**
    -   `javafx-controls`, `javafx-fxml`
    -   `bootstrapfx-core` (for styling)
    -   `validatorfx` (for form validation)
    -   `ikonli-javafx` (for icons)
    -   *Note: Will likely remove `fxgl` if not needed for game elements.*

---

## 4. Project Structure

```
src/main/java/com/example/schoolmanagement/
├── Main.java (Renamed from HelloApplication)
├── controllers/          # FXML Controllers
│   ├── MainLayoutController.java
│   ├── DashboardController.java
│   ├── StudentController.java
│   └── ...
├── models/               # Data Models
│   ├── Student.java
│   ├── Teacher.java
│   └── ...
├── dao/                  # Data Access Objects
│   ├── DBConnection.java
│   ├── StudentDAO.java
│   └── ...
└── utils/
    ├── SceneManager.java
    └── AlertHelper.java

src/main/resources/com/example/schoolmanagement/
├── fxml/                 # Views
├── css/                  # Styles
└── images/               # Assets
```

---

## 5. Implementation Roadmap (Incremental)

**Phase 1: Setup (Current)**
- [x] Analyze requirements.
- [x] Create `plan.md`.
- [ ] Add SQLite dependency to `pom.xml`.

**Phase 2: Development (Module by Module)**

1.  **Database & Base Architecture**
    -   Setup SQLite connection.
    -   Create `DBConnection` class.
    -   Create Table Initialization method.
    -   Create `MainLayout` (Sidebar + Content).

2.  **Dashboard Screen**
    -   Implement `dashboard.fxml`.
    -   Connect to DB to show real counts (even if 0 initially).

3.  **Teachers Screen**
    - [x] Teacher List & Add/Edit Form.
    - [x] Teacher CRUD.

4.  **Classes Screen**
    - [x] Class List & Creation Form.
    - [x] Assign Teachers to Classes.

5.  **Parents Screen**
    - [x] Parent Management.

6.  **Students Screen**
    - [x] Student List.
    - [x] Registration Form (Needs Class, Parent, & Transport data).

7.  **Exams & Grades**
    - [x] Exam Scheduling.
    - [x] Subject Management.
    - [x] Grade Entry (Select Exam -> Select Subject -> Enter Scores).

8.  **Transport & Settings**
    - [x] Route management.
    - [x] Admin settings (User Management).

**Phase 3: Polish & Packaging**
-   Final testing.
-   UI Styling adjustments (BootstrapFX).
