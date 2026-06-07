import axios from 'axios';

console.log('User Order URL:', process.env.REACT_APP_USER_ORDER_URL);
console.log('Product URL:', process.env.REACT_APP_PRODUCT_URL);

const userOrderApi = axios.create({
    baseURL: process.env.REACT_APP_USER_ORDER_URL,
});

const productApi = axios.create({
    baseURL: process.env.REACT_APP_PRODUCT_URL,
});

const setAuthToken = (token) => {
    if (token) {
        userOrderApi.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        productApi.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
        delete userOrderApi.defaults.headers.common['Authorization'];
        delete productApi.defaults.headers.common['Authorization'];
    }
};

export { userOrderApi, productApi, setAuthToken };