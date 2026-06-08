import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { setAuthToken } from '../api/axios';

function Navbar() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const handleLogout = () => {
        localStorage.removeItem('token');
        setAuthToken(null);
        navigate('/login');
    };

    return (
        <nav style={{ padding: '1rem', background: '#333', color: 'white', display: 'flex', gap: '1rem', alignItems: 'center' }}>
            <span style={{ fontWeight: 'bold', marginRight: 'auto' }}>SimpleStore</span>
            {token ? (
                <>
                    <Link to="/products" style={{ color: 'white' }}>Products</Link>
                    <Link to="/orders" style={{ color: 'white' }}>Orders</Link>
                    <button onClick={handleLogout} style={{ cursor: 'pointer' }}>Logout</button>
                </>
            ) : (
                <>
                    <Link to="/login" style={{ color: 'white' }}>Login</Link>
                    <Link to="/register" style={{ color: 'white' }}>Register</Link>
                </>
            )}
        </nav>
    );
}

export default Navbar;