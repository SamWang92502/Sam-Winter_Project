// src/api/account.js

import axiosInstance from '../axiosConfig';

export const createAccount = async (accountData) => {
  try {
    const response = await axiosInstance.post('/create-account', accountData);
    return response.data;
  } catch (error) {
    console.error('Error creating account:', error);
    throw error;
  }
};
