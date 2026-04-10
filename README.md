# ⚽ Football Manager 26

A desktop football management simulation game built with **Java 21** and **JavaFX 21**, where you can manage clubs, players, tactics, and more — all from a rich graphical interface.

---

## 🖥️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI Framework | JavaFX 21 (FXML, Controls, Media, Web, Swing) |
| Build Tool | Maven |
| UI Libraries | ControlsFX, BootstrapFX, FormsFX, TilesFX, Ikonli |
| Game Engine | FXGL 17.3 |
| Validation | ValidatorFX |
| Testing | JUnit Jupiter 5.12.1 |

---

## ✨ Features

- 🏟️ Manage football clubs and squads
- 👤 View and edit player profiles and stats
- 📋 Set formations and tactical strategies
- 📅 Simulate match fixtures and results
- 📊 Track league standings and team performance
- 🎨 Rich JavaFX-powered graphical interface

---

## 📋 Prerequisites

Make sure you have the following installed before running the project:

- **Java 21** or higher → [Download](https://adoptium.net/)
- **Maven 3.8+** → [Download](https://maven.apache.org/download.cgi)

You can verify your versions with:

```bash
java -version
mvn -version
```

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/AlWasee111/Football-Manager-26.git
cd Football-Manager-26
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn clean javafx:run
```

---

## 📁 Project Structure

```
Football-Manager-26/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/footballmanager/footballmanager/
│       │       └── MainApplication.java   # App entry point
│       └── resources/                     # FXML layouts, CSS, images
├── pom.xml                                # Maven build config
├── mvnw / mvnw.cmd                        # Maven wrapper scripts
└── README.md
```

---

## 🧪 Running Tests

```bash
mvn test
```

Tests are written using **JUnit Jupiter 5** and can be found in `src/test/java/`.

---

## 🤝 Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push to your branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 📄 License

This project is open source. Feel free to use, modify, and distribute it.

---

## 👤 Author

**AlWasee111** — [GitHub Profile](https://github.com/AlWasee111)
