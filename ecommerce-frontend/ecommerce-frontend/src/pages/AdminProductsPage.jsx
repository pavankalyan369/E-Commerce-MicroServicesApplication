import React, { useEffect, useState } from "react";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";

const AdminProductsPage = () => {
  const { accessToken } = useAuth();
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState({
    id: "",
    name: "",
    price: "",
    type: "",
    weight: "",
    stock: "",
  });

  const load = async () => {
    try {
      const res = await api.get("/product-service/products/all", {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      setProducts(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    load();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await api.post(
        "/product-service/products/create",
        {
          ...form,
          id: Number(form.id),
          price: Number(form.price),
          weight: Number(form.weight),
          stock: Number(form.stock),
        },
        { headers: { Authorization: `Bearer ${accessToken}` } }
      );
      setForm({
        id: "",
        name: "",
        price: "",
        type: "",
        weight: "",
        stock: "",
      });
      load();
    } catch (err) {
      console.error(err);
      alert("Failed to create product");
    }
  };

  return (
    <div className="page">
      <h2>Admin - Products</h2>

      <h3>Create Product</h3>
      <form className="form" onSubmit={handleCreate}>
        <input
          name="id"
          placeholder="ID"
          value={form.id}
          onChange={handleChange}
        />
        <input
          name="name"
          placeholder="Name"
          value={form.name}
          onChange={handleChange}
        />
        <input
          name="type"
          placeholder="Type"
          value={form.type}
          onChange={handleChange}
        />
        <input
          name="price"
          placeholder="Price"
          value={form.price}
          onChange={handleChange}
        />
        <input
          name="weight"
          placeholder="Weight"
          value={form.weight}
          onChange={handleChange}
        />
        <input
          name="stock"
          placeholder="Stock"
          value={form.stock}
          onChange={handleChange}
        />
        <button type="submit">Create</button>
      </form>

      <h3>Existing Products</h3>
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Price</th>
            <th>Stock</th>
          </tr>
        </thead>
        <tbody>
          {products.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.name}</td>
              <td>{p.type}</td>
              <td>₹{p.price}</td>
              <td>{p.stock}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminProductsPage;
