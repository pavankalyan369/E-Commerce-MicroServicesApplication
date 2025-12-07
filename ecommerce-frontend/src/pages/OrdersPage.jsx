import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";

const OrdersPage = () => {
  const { userId: pathUserId } = useParams();
  const { accessToken, userId } = useAuth();
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await api.get(`/order-service/orders/user/${userId}`, {
          headers: { Authorization: `Bearer ${accessToken}` },
        });
        setOrders(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    load();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <div className="page">
      <h2>My Orders</h2>
      {orders.length === 0 ? (
        <p>No orders yet.</p>
      ) : (
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Product</th>
              <th>Qty</th>
              <th>Total</th>
              <th>Status</th>
              <th>Payment</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((o) => (
              <tr key={o.id}>
                <td>{o.id}</td>
                <td>{o.productName}</td>
                <td>{o.quantity}</td>
                <td>₹{o.totalAmount}</td>
                <td>{o.status}</td>
                <td>{o.paymentStatus}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default OrdersPage;
