import React from "react";
import { Routes, Route } from "react-router-dom";
import NavBar from "./components/NavBar";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import AdminRegisterPage from "./pages/AdminRegisterPage";
import ProductListPage from "./pages/ProductListPage";
import CartPage from "./pages/CartPage";
import OrdersPage from "./pages/OrdersPage";
import AdminProductsPage from "./pages/AdminProductsPage";
import ProtectedRoute from "./components/ProtectedRoute";

const App = () => {
  return (
    <div className="app">
      <NavBar />
      <main className="main">
        <Routes>
          <Route path="/" element={<ProductListPage />} />
          <Route path="/products" element={<ProductListPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          <Route element={<ProtectedRoute requireAdmin={true} />}>
            <Route path="/admin/register" element={<AdminRegisterPage />} />
            <Route path="/admin/products" element={<AdminProductsPage />} />
          </Route>

          <Route element={<ProtectedRoute />}>
            <Route path="/cart/:userId" element={<CartPage />} />
            <Route path="/orders/user/:userId" element={<OrdersPage />} />
          </Route>
        </Routes>
      </main>
    </div>
  );
};

export default App;
