import React, { useState } from "react";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";

const AdminRegisterPage = () => {
  const { accessToken } = useAuth();
  const [form, setForm] = useState({ username: "", password: "" });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");
    try {
      await api.post("/auth/admin/register", form, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      setMessage("Admin registered successfully");
    } catch (err) {
      setError(err.response?.data?.error || "Failed to register admin");
    }
  };

  return (
    <div className="page">
      <h2>Register Admin</h2>
      <form className="form" onSubmit={handleSubmit}>
        <input
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
        />
        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
        />
        {message && <div className="success">{message}</div>}
        {error && <div className="error">{error}</div>}
        <button type="submit">Create Admin</button>
      </form>
    </div>
  );
};

export default AdminRegisterPage;
