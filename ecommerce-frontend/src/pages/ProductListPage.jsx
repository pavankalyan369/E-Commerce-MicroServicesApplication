import React, { useEffect, useState } from "react";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const ProductListPage = () => {
  const [products, setProducts] = useState([]);
  const { accessToken, userId } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const load = async () => {
      try {
        const res = await api.get("/product-service/products/all");
        setProducts(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    load();
  }, []);

  const handleAddToCart = async (productId) => {
    if (!accessToken || !userId) {
      navigate("/login");
      return;
    }
    try {
      await api.post(
        `/cart-service/cart/${userId}/items`,
        { productId, quantity: 1 },
        { headers: { Authorization: `Bearer ${accessToken}` } }
      );
      alert("Added to cart");
    } catch (err) {
      console.error(err);
      alert("Could not add to cart");
    }
  };

  return (
    <div className="page">
      <h2>Products</h2>
      <div className="grid">
        {products.map((p) => (
          <div key={p.id} className="card">
            <h3>{p.name}</h3>
            <p>Type: {p.type}</p>
            <p>Price: ₹{p.price}</p>
            <p>Stock: {p.stock}</p>
            <button onClick={() => handleAddToCart(p.id)}>Add to cart</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductListPage;
