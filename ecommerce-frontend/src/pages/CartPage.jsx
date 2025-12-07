import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";

const CartPage = () => {
  const { userId: pathUserId } = useParams();
  const { accessToken, userId } = useAuth();
  const [cart, setCart] = useState(null);

  const loadCart = async () => {
    try {
      const res = await api.get(`/cart-service/cart/${userId}`, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      setCart(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadCart();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const updateQty = async (productId, quantity) => {
    try {
      await api.patch(
        `/cart-service/cart/${userId}/items/${productId}`,
        null,
        {
          params: { quantity },
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );
      loadCart();
    } catch (err) {
      console.error(err);
    }
  };

  const removeItem = async (productId) => {
    try {
      await api.delete(`/cart-service/cart/${userId}/items/${productId}`, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      loadCart();
    } catch (err) {
      console.error(err);
    }
  };

  const placeOrder = async () => {
    try {
      for (const item of cart.items) {
        await api.post(
          "/order-service/orders/create",
          {
            userId,
            productId: item.productId,
            quantity: item.quantity,
            paymentMode: "COD",
          },
          { headers: { Authorization: `Bearer ${accessToken}` } }
        );
      }
      await api.delete(`/cart-service/cart/${userId}`, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      await loadCart();
      alert("Order(s) placed!");
    } catch (err) {
      console.error(err);
      alert("Could not place order");
    }
  };

  if (!cart) return <div className="page">Loading cart...</div>;

  if (!cart.items || cart.items.length === 0)
    return (
      <div className="page">
        <h2>Your cart is empty</h2>
      </div>
    );

  return (
    <div className="page">
      <h2>Your Cart</h2>
      <table className="table">
        <thead>
          <tr>
            <th>Product</th>
            <th>Type</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Total</th>
            <th />
          </tr>
        </thead>
        <tbody>
          {cart.items.map((item) => (
            <tr key={item.productId}>
              <td>{item.productName}</td>
              <td>{item.productType}</td>
              <td>₹{item.price}</td>
              <td>
                <input
                  type="number"
                  min={0}
                  value={item.quantity}
                  onChange={(e) =>
                    updateQty(item.productId, Number(e.target.value))
                  }
                />
              </td>
              <td>₹{item.lineTotal}</td>
              <td>
                <button onClick={() => removeItem(item.productId)}>
                  Remove
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="cart-summary">
        <p>Total quantity: {cart.totalQuantity}</p>
        <p>Total amount: ₹{cart.totalAmount}</p>
        <button onClick={placeOrder}>Place Order</button>
      </div>
    </div>
  );
};

export default CartPage;
