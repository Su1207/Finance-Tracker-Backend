# Personal Finance Manager - Backend

A simple REST API built with Spring Boot and PostgreSQL for managing personal finances. This backend handles user authentication, transactions, categories and provides data for the React frontend.

## 🛠️ Technologies Used

- **Java 17** - Programming language
- **Spring Boot** - Java framework for building APIs
- **PostgreSQL** - Database
- **Maven** - Build tool
- **Docker** - For deployment
- **Render.com** - Cloud hosting

## ✨ Features

- User registration and login (session-based)
- Add, edit, delete transactions
- Create custom categories
- Monthly and yearly analytics
- Secure API endpoints

## 📋 What You Need

- Java 17 or higher
- PostgreSQL database
- Any code editor (IntelliJ IDEA, VS Code, etc.)

## 🚀 Quick Start

### 1. Clone the Project
```bash
git clone https://github.com/yourusername/finance-tracker-backend.git
cd finance-tracker-backend
```

### 2. Setup Database

**Option 1: Local PostgreSQL**
- Install PostgreSQL on your computer
- Create a database named `finance_manager`

**Option 2: Use Docker (easier)**
```bash
docker run --name finance-db -e POSTGRES_DB=finance_manager -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres
```

### 3. Configure Application

Edit `src/main/resources/application.yml`:

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/finance_manager
    username: admin
    password: password
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### 4. Run the Application

```bash
# Using Maven
./mvnw spring-boot:run

# Or if you have Maven installed
mvn spring-boot:run
```

Your API will be running at: `http://localhost:8081`

## 📁 Project Structure

```
src/main/java/
├── controller/     # API endpoints
├── model/         # Database models (User, Transaction, Category)
├── repository/     # Database operations
├── service/        # Business logic
├── dto/          # Model structure
├── exception/    # Exception handled
└── config/         # Configuration files
```

## 🔌 API Endpoints

### Authentication
- `POST /auth/register` - Create new user
- `POST /auth/login` - User login
- `POST /auth/logout` - User logout
- `GET /auth/me` - Fetch user

### Transactions
- `GET /transactions` - Get all user transactions
- `POST /transactions` - Create new transaction
- `PUT /transactions/{id}` - Update transaction
- `DELETE /transactions/{id}` - Delete transaction

### Categories
- `GET /categories` - Get user categories
- `POST /categories` - Create new category
- `PUT /categories/{id}` - Update category
- `DELETE /categories/{id}` - Delete category

### Analytics
- `GET /analytics/monthly` - Monthly spending data
- `GET /analytics/yearly` - Yearly spending data

## 🐳 Running with Docker

### Build Docker Image
```bash
docker build -t finance-backend .
```

### Run with Docker Compose
```bash
docker-compose up
```

This will start both the database and backend automatically.

## 🌐 Deploy to Render

### 1. Push to GitHub
```bash
git add .
git commit -m "Initial commit"
git push origin main
```

### 2. Create Database on Render
1. Go to Render.com
2. Create a new PostgreSQL database
3. Note the connection details

### 3. Deploy Backend
1. Create a new Web Service on Render
2. Connect your GitHub repository
3. Add environment variables:
   ```
   SPRING_DATASOURCE_URL=your_render_database_url
   SPRING_DATASOURCE_USERNAME=your_db_username
   SPRING_DATASOURCE_PASSWORD=your_db_password
   ```

### 4. Build Settings
- Build Command: `mvn clean package -DskipTests`
- Start Command: `java -jar target/finance-manager-backend-1.0.0.jar`

## ⚙️ Environment Variables

For production, set these variables:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/your-db-name
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
```

## 🔧 Common Commands

```bash
# Run the application
mvn spring-boot:run

# Build the project
mvn clean package

# Run tests
mvn test

# Check if everything compiles
mvn compile
```

## 📊 Database Tables

The application creates these tables automatically:

- **users** - Stores user information
- **transactions** - Stores income and expense records
- **categories** - Stores transaction categories

## 🆘 Troubleshooting

**Database connection failed?**
- Make sure PostgreSQL is running
- Check your database credentials in `application.yml`

**Port 8081 already in use?**
- Change the port in `application.yml` to `8080` or any other available port

**Build failed?**
- Make sure you have Java 17 installed
- Run `java -version` to check

## 🔗 Frontend Connection

The React frontend connects to this backend at:
- Development: `http://localhost:8081/`
- Production: `https://finance-tracker-backend-avyb.onrender.com`
Make sure to update the frontend's API URL accordingly.

---

**Ready to go! 🚀**
