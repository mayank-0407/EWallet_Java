import { useState } from "react";
function filterTransactions(){
    const [filteredTransactions, setFilteredTransactions] =
    useState(transactions);

  const handleFilter = (filters) => {
    let filteredData = [...transactions];

    if (filters.date) {
      filteredData = filteredData.filter((transaction) =>
        transaction.date.startsWith(filters.date)
      );
    }

    if (filters.status && filters.status !== "All") {
      filteredData = filteredData.filter(
        (transaction) => transaction.status === filters.status
      );
    }

    if (filters.amount) {
      filteredData = filteredData.filter((transaction) =>
        String(transaction.amount).includes(filters.amount)
      );
    }

    if (filters.type && filters.type !== "All") {
      filteredData = filteredData.filter(
        (transaction) => transaction.type === filters.type
      );
    }

    setFilteredTransactions(filteredData);
  };
  return(
    
  )
}
export default filterTransactions;