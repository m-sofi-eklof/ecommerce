import React, { useState, useEffect } from 'react';
import { productApi, userOrderApi } from '../api/axios';
import { setAuthToken } from '../api/axios';

function Products() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [orderSuccess, setOrderSuccess] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('token');
        setAuthToken(token);

        productApi.get('/api/products')
            .then(res => {
                setProducts(res.data);
                setLoading(false);
            })
            .catch(() => {
                setError('Failed to load products');
                setLoading(false);
            });
    }, []);

    const handleOrder = async (productId) => {
        try {
            await userOrderApi.post('/api/orders', {
                items: [{ productId, quantity: 1 }]
            });
            setOrderSuccess(`Order placed successfully!`);
            setTimeout(() => setOrderSuccess(''), 3000);
        } catch (err) {
            setError('Failed to place order');
            setTimeout(() => setError(''), 3000);
        }
    };

    if (loading) return <p style={{ padding: '2rem' }}>Loading products...</p>;
    if (error) return <p style={{ padding: '2rem', color: 'red' }}>{error}</p>;

    return (
        <div style={{ padding: '2rem' }}>
            <h2>Products</h2>
            {orderSuccess && <p style={{ color: 'green', marginBottom: '1rem' }}>{orderSuccess}</p>}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '1rem' }}>
                {products.map(product => (
                    <div key={product.id} style={{ border: '1px solid #ddd', padding: '1rem', borderRadius: '8px' }}>
                        <img src={product.image} alt={product.title} style={{ width: '100%', height: '200px', objectFit: 'contain' }} />
                        <h4 style={{ margin: '0.5rem 0' }}>{product.title}</h4>
                        <p style={{ color: '#666', fontSize: '0.9rem' }}>{product.category}</p>
                        <p style={{ fontWeight: 'bold' }}>${product.price}</p>
                        <button
                            onClick={() => handleOrder(product.id)}
                            style={{ width: '100%', padding: '0.5rem', marginTop: '0.5rem', cursor: 'pointer', background: '#333', color: 'white', border: 'none', borderRadius: '4px' }}
                        >
                            Order
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Products;