package com.ewallet.database_service.repository;


import com.ewallet.database_service.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionModel,Long> {
    Optional<TransactionModel> findById(Long id);
    TransactionModel findTopByReceiverWalletIdOrderByIdDesc(String receiverWalletId);
    TransactionModel findTopBySenderWalletIdOrderByIdDesc(String senderWalletId);

    @Query(value = """
    SELECT * FROM Transaction_Model t1
    WHERE t1.sender_wallet_id = :senderWalletId
      AND t1.transaction_init_time = (
        SELECT MAX(t2.transaction_init_time)
        FROM Transaction_Model t2
        WHERE t2.receiver_wallet_id = t1.receiver_wallet_id
          AND t2.sender_wallet_id = :senderWalletId
    )
    ORDER BY t1.transaction_init_time DESC
    LIMIT 5
    """, nativeQuery = true)
    List<TransactionModel> findTop5UniqueTransactionsByReceiverWalletId(@Param("senderWalletId") String senderWalletId); // fetch recent transactions for send Money
    List<TransactionModel> findBySenderWalletId(String senderWalletId);
    List<TransactionModel> findByReceiverWalletId(String receiverWalletId);

    @Query(value = "SELECT * FROM transaction_model " +
            "WHERE sender_wallet_id = :walletId " +
            "OR (receiver_wallet_id = :walletId AND status NOT IN ('REFUNDED', 'INSUFFICIENT_BALANCE')) " +
            "ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<TransactionModel> findTop5TransactionsBySenderOrReceiverWalletId(String walletId);// fetch 5 dashboard Transactions

    @Query(value = "SELECT * FROM transaction_model " +
            "WHERE sender_wallet_id = :walletId " +
            "OR (receiver_wallet_id = :walletId AND status NOT IN ('REFUNDED', 'INSUFFICIENT_BALANCE'))" +
            "ORDER BY id DESC", nativeQuery = true)
    List<TransactionModel> findAllTransactionsWithoutRefund(String walletId);// fetch all Transactions for all Transactions
}