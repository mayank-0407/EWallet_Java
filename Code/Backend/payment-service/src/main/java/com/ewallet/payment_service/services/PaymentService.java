package com.ewallet.payment_service.services;

import com.ewallet.payment_service.dto.*;
import com.ewallet.payment_service.enums.TransactionStatus;
import com.ewallet.payment_service.exceptions.*;
import com.ewallet.payment_service.feignconfig.FeignDBService;
import com.ewallet.payment_service.models.TransactionModel;
import com.ewallet.payment_service.models.UserModel;
import com.ewallet.payment_service.models.WalletModel;
import com.ewallet.payment_service.producer.RefundProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentService {


    @Autowired
    private FeignDBService dbService;

    private final RefundProducer refundProducer;

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(RefundProducer refundProducer) {
        this.refundProducer = refundProducer;
    }

    public TransactionModel changePaymentStatusToProcessing(Long id){
        try{
            TransactionStatusDTO idDetails = new TransactionStatusDTO();
            idDetails.setTransactionId(id);
            idDetails.setStatus(TransactionStatus.PROCESSING);

            TransactionModel updatedStatus = dbService.updateTransactionStatus(idDetails).getBody();
            logger.info("Payment Status Changed to Processing for Id : {}",id);
            return updatedStatus;
        } catch (Exception e) {
            logger.error("Error Encountered while changing the status to Processing");
            throw new TransactionProcessingException("Failed to change payment status to PROCESSING for transaction ID: " + id);
        }

    }

    public Boolean processTransaction(TransactionModel transactionDetails){
        try{
            WalletInitDTO tempWalletId = new WalletInitDTO();
            UserModel tempUserModelTransfer = new UserModel();
            tempUserModelTransfer.setWalletId(transactionDetails.getSenderWalletId());

            UserModel senderUserDetails = dbService.getUserByWalletId(tempUserModelTransfer).getBody();

            if (senderUserDetails == null) {
                throw new WalletNotFoundException("Sender user not found for walletId: " + transactionDetails.getSenderWalletId());
            }
            tempWalletId.setUserId(senderUserDetails.getId().toString());

            WalletModel senderWalletDetails = dbService.getWalletDetails(tempWalletId).getBody();
//            --------------------------------------------------------------------------------------------- sender Wallet Id and Wallet
            tempUserModelTransfer.setWalletId(transactionDetails.getReceiverWalletId());

            UserModel receiverUserDetails = dbService.getUserByWalletId(tempUserModelTransfer).getBody();

            if (receiverUserDetails == null) {
                throw new WalletNotFoundException("Receiver user not found for walletId: " + transactionDetails.getReceiverWalletId());
            }
            tempWalletId.setUserId(receiverUserDetails.getId().toString());

            WalletModel receiverWalletDetails = dbService.getWalletDetails(tempWalletId).getBody();

            if(senderWalletDetails == null || receiverWalletDetails == null){
//                TransactionStatusDTO updateStatusToComplete = new TransactionStatusDTO(transactionDetails.getId(), TransactionStatus.FAILED);
//                dbService.changeTransactionStatusToComplete(updateStatusToComplete);
                logger.error("Unable to Find the Sender/Receiver Wallet details");
                throw new WalletNotFoundException("Unable to find sender or receiver wallet details");
            }

            if(Objects.equals(senderWalletDetails.getId(), receiverWalletDetails.getId())){
                logger.error("Self Transfer is Not allowed");
                throw new SelfTransferException("Self Transfer is Not allowed");
            }

            if(senderWalletDetails.getBalance()>=transactionDetails.getAmount()  && transactionDetails.getAmount()<=senderWalletDetails.getLimit()) {

                try {
                    senderWalletDetails.setBalance(senderWalletDetails.getBalance() - transactionDetails.getAmount());

                    WalletModel updatedSenderWalletFromDB = dbService.updateBalance(senderWalletDetails).getBody();
                }catch(Exception e){
                    logger.error("Unable to update The Balance {}",senderWalletDetails);
                    throw new TransactionProcessingException("Failed to update sender wallet balance");
                }

                try{
                    receiverWalletDetails.setBalance(receiverWalletDetails.getBalance() + transactionDetails.getAmount());

                    WalletModel updatedReceiverWalletFromDB = dbService.updateBalance(receiverWalletDetails).getBody();
                } catch (Exception e) {
                    // process the refund
                    RefundTransactionDTO refundTransaction = new RefundTransactionDTO(senderWalletDetails, transactionDetails.getId(), transactionDetails.getAmount());
                    try {
                        refundProducer.initiateRefund(refundTransaction);
                    } catch (Exception refundEx) {
                        logger.error("Failed to process refund", refundEx);
                        throw new RefundProcessingException("Failed to process refund after receiver wallet update failure");
                    }
                    logger.error("Encountered Some Error so Processing the Refund");
                    throw new TransactionProcessingException("Receiver wallet update failed, refund processed");
                }

                // Payment Completed
                TransactionStatusDTO updateStatusToComplete = new TransactionStatusDTO(transactionDetails.getId(), TransactionStatus.COMPLETED);
                dbService.changeTransactionStatusToComplete(updateStatusToComplete);
                logger.info("Payment Completed!!");
                return true;
            }else{
                //payment Failed
                transactionDetails.setRemarks("InSufficient Balance!");
                dbService.saveTransaction(transactionDetails);

                TransactionStatusDTO updateStatusToFailed = new TransactionStatusDTO(transactionDetails.getId(), TransactionStatus.INSUFFICIENT_BALANCE);
                dbService.changeTransactionStatusToComplete(updateStatusToFailed);

                logger.error("Payment Failed due to insufficient balance");
                throw new InsufficientBalanceException("Insufficient balance for wallet ID: " + senderWalletDetails.getId());
            }
        }  catch (InsufficientBalanceException | RefundProcessingException ex) {
            logger.error("Transaction failed due to : {}", ex.getMessage());
            return false;
        } catch (WalletNotFoundException | TransactionProcessingException ex) {
            logger.error("Transaction failed due to with null: {}", ex.getMessage());
            return null;
        }catch (Exception e) {
            logger.error("Unexpected error encountered: {}", e.getMessage(), e);
            return null;
        }
    }
    public TransactionModel changePaymentStatusToFailed(Long id){
        try{
            TransactionStatusDTO idDetails = new TransactionStatusDTO();
            idDetails.setTransactionId(id);
            idDetails.setStatus(TransactionStatus.FAILED);

            TransactionModel updatedStatus = dbService.updateTransactionStatus(idDetails).getBody();
            logger.info("Status Changed to Failed!");
            return updatedStatus;
        } catch (Exception e) {
            logger.error("Unable to Change Status to Failed {}",e.getMessage());
            throw new TransactionProcessingException("Failed to change payment status to FAILED for transaction ID: " + id);
        }

    }

}
