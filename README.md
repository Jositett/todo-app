

# Java Desktop Todo Application

A modern Java desktop todo application built with Java Swing, following MVC architecture, featuring task management with authentication, dark/light theme toggle, CSV data persistence, and import/export functionality.

---

## 📌 Features

- **User Authentication**
    - Login with username/password
    - Secure password storage (hashing)
    - Session management

- **Task Management**
    - Create, edit, delete tasks
    - Task status (complete/incomplete)
    - Priority levels (Low, Medium, High, Urgent)
    - Due date tracking
    - Filtering by status/priority
    - Sorting by due date or priority

- **UI & Themes**
    - Modern UI design using FlatLaf
    - Toggle between **Light** and **Dark** themes
    - Table-based task list with status indicators

- **Data Management**
    - Tasks and user data stored in CSV files
    - Auto-save functionality
    - Import/export tasks with field selection

- **Security & Validation**
    - Input sanitization
    - Password hashing with salt
    - File I/O exception handling

- **Testing**
    - Unit tests for core functionality
    - Test coverage for model and data layer

---

## 🧩 Requirements

- Java 8 or higher
- FlatLaf UI Library (for modern theme support)
- JUnit 4+ (for unit tests)
- CSV files for local data storage

---

## 📦 Installation

### Manual Build (without IDE)

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/todo-app.git
   cd todo-app
   ```

2. Compile:
   ```bash
   javac -d out src/**/*.java
   ```

3. Run:
   ```bash
   java -cp out;lib/flatlaf-3.6.jar Main
   ```

> Replace `;` with `:` for Unix-based systems.

---

## 🚀 Usage

- **Login Screen**: Enter username and password
- **Register**: Create a new user account
- **Tasks**:
    - Add/Edit/Delete tasks
    - Filter and sort tasks
    - Export or import tasks
- **Theme Toggle**: Switch between Light/Dark mode
- **Logout**: Secure logout with session cleanup

---

## 🔧 Future Improvements

### ✅ Already Implemented
- Authentication system
- CSV-based data persistence
- Task filtering/sorting
- Modern UI with theme toggle
- Unit tests

### 🧩 Optional Improvements (Planned)

| Category | Enhancement | Description |
|---------|-------------|-------------|
| **Usability** | Theme Persistence | Save last used theme (light/dark) between sessions |
| **UI** | Custom Theme Colors | Allow user customization of theme colors (e.g., accent color) |
| **Security** | Stronger Password Hashing | Use PBKDF2 or bcrypt for secure password storage |
| **Data** | SQLite Support | Optional migration from CSV to SQLite for scalability |
| **Import/Export** | Excel Export | Add export to `.xlsx` format |
| **UI** | Task Card View | Optional toggle between table and card-based UI |
| **Features** | Notifications | Add due date reminders (JavaFX or AWT TrayIcon) |
| **Data** | Backup/Restore | Add automatic backups of CSV files |
| **Development** | GitHub Actions CI/CD | Automate build, test, and release |
| **Testing** | Integration Tests | Add UI-level testing using TestFX or similar frameworks |
| **Performance** | Lazy Loading | Load large task lists in chunks |
| **Security** | File Locking | Prevent concurrent file access issues |

---

## 🧱 Project Structure

```
todo-app/
├── src/
│   ├── controller/       # MVC controllers
│   ├── model/            # Core models and business logic
│   ├── view/             # UI components (Swing)
│   ├── utils/            # File handling, utilities
│   ├── tests/            # Unit tests
│   └── Main.java         # Entry point
├── lib/                  # Dependencies
│   ├── flatlaf-3.6.jar   # FlatLaf UI library
│   └── junit-4.13.1.jar  # Testing framework
├── data/                 # Generated data files
│   ├── users.csv         # User data
│   └── tasks.csv         # Task data
├── README.md             # Project documentation
└── LICENSE               # License file (MIT)
```

---

## 🛠️ GitHub Project Recommendations

### ✅ Required
- [ ] GitHub Actions CI/CD for automated build/test
- [ ] GitHub Issues + Labels for task tracking
- [ ] Dependabot for dependency updates
- [ ] README.md with clear instructions
- [ ] LICENSE file
- [ ] CONTRIBUTING.md guide
- [ ] CODEOWNERS for maintainer roles
- [ ] GitHub Pages for documentation (optional)

### 🛠️ Tools to Add
- **CI/CD**: Automate build/test with GitHub Actions on PR
- **Code Coverage**: Use `JaCoCo` or similar
- **Dependency Updates**: Use Dependabot
- **Documentation**: GitHub Pages or Markdown-based
- **Issue Labels**: `bug`, `enhancement`, `documentation`, etc.

---

## 📜 License (MIT)

> **Recommended for Open Source Java Projects**

```
MIT License

Copyright (c) 2025 [Your Name or Organization]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

**To apply the license:**
1. Add the full MIT license text to a file named `LICENSE`
2. Update copyright line with your name/organization
3. Include license notice in `README.md`

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repo
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m "Add new feature"`)
4. Push the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

See - [Contributing](CONTRIBUTING.md) for more details.

---

## 📞 Contact

- **Author**: [Your Name]
- **GitHub**: [github.com/your-username](https://github.com/your-username)
- **Email**: your.email@example.com

---