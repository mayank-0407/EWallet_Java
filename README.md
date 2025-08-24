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
   docker-compose up
   ```
