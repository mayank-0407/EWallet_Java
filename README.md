# 💳 E-Wallet Application In Java

## 📖 Overview

**EWallet_Java** is an extensible **microservices-based e-wallet application** built in **Java (Spring Boot)**.  

It provides a scalable and modular digital wallet system with the following features:

- **Service Discovery** via **Eureka**  
- **API Gateway** with **Spring Cloud Gateway**  
- **Asynchronous processing** using **RabbitMQ**  
- **Database support** (PostgreSQL or other DBs)  
- **Modular microservices** (Auth, Wallet, Transaction, Payment, Refund, etc.)  

### 🔑 Core Functionalities
- Secure **user authentication** with role-based access  
- Wallet **balance management**  
- **Transactions** between users  
- Support for **future integrations** (refunds, merchants, etc.)  
- Fault-tolerant & scalable **microservices architecture**  

---

## 🐳 Local Setup with Docker

Follow these steps to set up the project locally using **Docker**.

### ✅ Prerequisites
- [Docker](https://docs.docker.com/get-docker/) (ensure daemon is running)  
- [Docker Compose](https://docs.docker.com/compose/)  

### 🚀 Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/mayank-0407/EWallet_Java.git
   cd EWallet_Java
   ```
2. **Run Docker Compose**
   ```bash
   docker-compose up --build
   ```
   This will:

   - Build images for all microservices
   - Start containers for Eureka, API Gateway, RabbitMQ, PostgreSQL, and frontend (if included)
   - Create a dedicated Docker network (ewallet-net)
  
3. **Access the components**

   - Eureka Dashboard → http://localhost:8761
   - API Gateway → http://localhost:8080
   - Frontend UI → http://localhost:5173
   - Microservices (if ports are exposed):
        - Auth → http://localhost:8090
        - Wallet → http://localhost:8092
        - Transaction → http://localhost:8094
        - Payment → http://localhost:8096
        - Refund → http://localhost:8097
   - RabbitMQ Management → http://localhost:15672
  
4. **Stop and clean up**
   ```bash
   docker-compose down
   ```

## 🤝 Contributing

### We welcome contributions from the community!

🔧 **Steps to Contribute**
   - Fork the repository
   - Create a feature branch (git checkout -b feature/my-awesome-feature)
   - Make your changes (code, docs, or bug fixes)
   - Test locally via Docker Compose
   - Commit with a descriptive message (git commit -m "feat: add email verification in auth-service")
   - Push to your fork (git push origin feature/my-awesome-feature)
   - Open a Pull Request (PR)
   - Clearly describe your changes
   - Link any related issues
   - Ensure CI/Docker builds pass

## 📂 Project Structure
   ```bash
   EWallet_Java/
   ├── Backend/
   │   ├── apigateway/
   │   ├── authservice/
   │   ├── walletservice/
   │   ├── transactionservice/
   │   ├── payment-service/
   │   ├── refund-service/
   │   └── server-registry-eureka/
   ├── Frontend/   (if applicable)
   ├── docker-compose.yml
   └── README.md
   ```

### Each microservice includes:
   - src/ (Java source code)
   - Dockerfile (multi-stage Maven build recommended)
   - application.yml (Eureka + service config)
