# SachBookAPI

## Overview
SachBookAPI is a RESTful API for a book management application built using Spring Boot. It provides endpoints for user authentication, book management, order processing, cart management, and more.

## Features
- **Authentication**: Register, login, logout, forgot password, and profile management.
- **Book Management**: Search, sort, and filter books, view new and discounted books.
- **Order Management**: Create, view, and cancel orders.
- **Cart Management**: Add, update, and remove items from the cart, calculate total.
- **Review System**: Create, view, update, and delete book reviews.
- **Category Management**: View all book categories.
- **Admin Features**: Manage books, orders, users, and categories.

## Technologies Used
- Java 17
- Spring Boot 3.4.4
- Spring Security
- Spring Data JPA
- MySQL
- JWT for authentication
- Lombok
- Maven

## Getting Started
1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd SachBookAPI
   ```

2. **Configure the database**:
   - Ensure MySQL is running on port 3306.
   - Create a database named `bookdb`.
   - Update the database credentials in `src/main/resources/application.properties` if necessary.

3. **Build the project**:
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Access the API**:
   The API will be available at `http://localhost:8080`.

## API Endpoints
- **Authentication**:
  - `POST /api/auth/register`: Register a new user.
  - `POST /api/auth/login`: Login.
  - `POST /api/auth/logout`: Logout.
  - `POST /api/auth/forgot-password`: Request password reset.
  - `GET /api/auth/profile`: Get user profile.
  - `PUT /api/auth/profile`: Update user profile.
  - `PUT /api/auth/change-password`: Change password.

- **Books**:
  - `GET /api/books/new`: Get new books.
  - `GET /api/books/discounted`: Get discounted books.
  - `GET /api/books/search`: Search books.
  - `GET /api/books/sorted`: Get sorted books.
  - `GET /api/books/{id}`: Get book by ID.

- **Orders**:
  - `POST /api/orders`: Create an order.
  - `GET /api/orders`: Get user orders.
  - `GET /api/orders/{orderId}`: Get order by ID.
  - `PUT /api/orders/cancel/{orderId}`: Cancel an order.

- **Cart**:
  - `GET /api/cart`: Get user cart.
  - `POST /api/cart/add`: Add item to cart.
  - `PUT /api/cart/item/{cartItemId}`: Update cart item.
  - `DELETE /api/cart/item/{cartItemId}`: Remove item from cart.
  - `GET /api/cart/total`: Get cart total.

- **Reviews**:
  - `POST /api/reviews/user/{userId}/book/{bookId}`: Create a review.
  - `GET /api/reviews/book/{bookId}`: Get reviews by book ID.
  - `PUT /api/reviews/{reviewId}`: Update a review.
  - `DELETE /api/reviews/{reviewId}`: Delete a review.

- **Categories**:
  - `GET /api/categories`: Get all categories.

- **Admin**:
  - `POST /api/admin/books`: Create a book.
  - `PUT /api/admin/books/{id}`: Update a book.
  - `DELETE /api/admin/books/{id}`: Delete a book.
  - `PUT /api/admin/orders/{orderId}/status`: Update order status.
  - `PUT /api/admin/users/{id}`: Update user.
  - `DELETE /api/admin/users/{id}`: Delete user.
  - `POST /api/admin/categories`: Create a category.
  - `PUT /api/admin/categories/{id}`: Update a category.
  - `DELETE /api/admin/categories/{id}`: Delete a category.

## Testing
Run the tests using:
```bash
./mvnw test
```

## License
This project is licensed under the MIT License.
