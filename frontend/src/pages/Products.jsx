import React, { useState, useEffect } from 'react';
import { productApi } from '../api/axios';
import { setAuthToken } from '../api/axios';

function Products() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

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

    if (loading) return <p style={{ padding: '2rem' }}>Loading products...</p>;
    if (error) return <p style={{ padding: '2rem', color: 'red' }}>{error}</p>;

    return (
        <div style={{ padding: '2rem' }}>
            <h2>Products</h2>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '1rem' }}>
                {products.map(product => (
                    <div key={product.id} style={{ border: '1px solid #ddd', padding: '1rem', borderRadius: '8px' }}>
                        <img src={product.image} alt={product.title} style={{ width: '100%', height: '200px', objectFit: 'contain' }} />
                        <h4 style={{ margin: '0.5rem 0' }}>{product.title}</h4>
                        <p style={{ color: '#666', fontSize: '0.9rem' }}>{product.category}</p>
                        <p style={{ fontWeight: 'bold' }}>${product.price}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Products;