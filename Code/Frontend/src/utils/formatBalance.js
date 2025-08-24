import React from 'react';

function formatBalance(balance) {
  // Convert balance to a number if it's a string
  const numBalance = typeof balance === 'string' ? parseFloat(balance) : balance;

  // Use toLocaleString for automatic comma separation based on US English locale
  return numBalance.toLocaleString('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2, // Ensure two decimal places are always shown
    maximumFractionDigits: 2,
  });
}
export default formatBalance;