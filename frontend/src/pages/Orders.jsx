import React, { useState, useEffect } from 'react';
import { userOrderApi } from '../api/axios';
import { setAuthToken } from '../api/axios';

function Orders() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('token');
        setAuthToken(token);

        userOrderApi.get('/api/orders')
            .then(res => {
                setOrders(res.data);
                setLoading(false);
            })
            .catch(() => {
                setError('Failed to load orders');
                setLoading(false);
            });
    }, []);

    if (loading) return <p style={{ padding: '2rem' }}>Loading orders...</p>;
    if (error) return <p style={{ padding: '2rem', color: 'red' }}>{error}</p>;

    return (
        <div style={{ padding: '2rem' }}>
            <h2>My Orders</h2>
            {orders.length === 0 ? (
                <p>No orders yet.</p>
            ) : (
                orders.map(order => (
                    <div key={order.id} style={{ border: '1px solid #ddd', padding: '1rem', borderRadius: '8px', marginBottom: '1rem' }}>
                        <p><strong>Order #{order.id}</strong> — {order.status}</p>
                        <p style={{ color: '#666', fontSize: '0.9rem' }}>{new Date(order.createdAt).toLocaleDateString()}</p>
                        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '0.5rem' }}>
                            <thead>
                            <tr style={{ background: '#f5f5f5' }}>
                                <th style={{ padding: '0.5rem', textAlign: 'left' }}>Product</th>
                                <th style={{ padding: '0.5rem', textAlign: 'right' }}>Price</th>
                                <th style={{ padding: '0.5rem', textAlign: 'right' }}>Qty</th>
                                <th style={{ padding: '0.5rem', textAlign: 'right' }}>Total</th>
                            </tr>
                            </thead>
                            <tbody>
                            {order.items.map(item => (
                                <tr key={item.productId}>
                                    <td style={{ padding: '0.5rem' }}>{item.title}</td>
                                    <td style={{ padding: '0.5rem', textAlign: 'right' }}>${item.price}</td>
                                    <td style={{ padding: '0.5rem', textAlign: 'right' }}>{item.quantity}</td>
                                    <td style={{ padding: '0.5rem', textAlign: 'right' }}>${(item.price * item.quantity).toFixed(2)}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                ))
            )}
        </div>
    );
}

export default Orders;