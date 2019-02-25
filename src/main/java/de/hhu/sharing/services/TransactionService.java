package de.hhu.sharing.services;

import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.propay.Transaction;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactions;

    @Autowired
    private ProPayService proPayService;

    public Transaction getFromProcessId(Long processId){
        return transactions.findByProcessId(processId)
                .orElseThrow(
                    () -> new RuntimeException("Item not found!"));
    }

    public List<Transaction> getAllFromSender(User user){
        return transactions.findAllBySender(user);
    }

    public List<Transaction> getAllFromReceiver(User user){
        return transactions.findAllByReceiver(user);
    }

    public void createTransaction(BorrowingProcess process, User borrower, User lender){
        Item item = process.getItem();
        int days = (int) DAYS.between(process.getPeriod().getStartdate(),process.getPeriod().getEnddate()) + 1;
        int rent = item.getRental() * days;
        Transaction transaction = new Transaction(rent, item.getDeposit(),process.getId(), item, borrower, lender);
        proPayService.initiateTransaction(transaction);
        transactions.save(transaction);
    }

    public void setDepositRevoked (Transaction trans, String status){
        trans.setDepositRevoked(status);
        transactions.save(trans);
    }
}
