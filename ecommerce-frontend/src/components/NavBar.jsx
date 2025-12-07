import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const NavBar = () => {
  const { accessToken, logout, userId, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/" className="logo">
          MyShop
        </Link>
        <Link to="/products">Products</Link>
        {accessToken && (
          <>
            <Link to={`/cart/${userId}`}>Cart</Link>
            <Link to={`/orders/user/${userId}`}>My Orders</Link>
          </>
        )}
        {isAdmin && (
          <>
            <Link to="/admin/products">Admin Products</Link>
            <Link to="/admin/register">Register Admin</Link>
          </>
        )}
      </div>
      <div className="navbar-right">
        {!accessToken ? (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Sign Up</Link>
          </>
        ) : (
          <button onClick={handleLogout} className="btn-logout">
            Logout
          </button>
        )}
      </div>
    </nav>
  );
};

export default NavBar;
