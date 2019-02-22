package de.hhu.sharing.services;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.TransactionRepository;
import de.hhu.sharing.model.BorrowingProcess;
import de.hhu.sharing.model.Item;
import de.hhu.sharing.model.Request;
import de.hhu.sharing.propay.Transaction;
import de.hhu.sharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactions;

    @Autowired
    private ProPayService proPayService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RequestService requestService;

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
        proPayService.transferMoney(borrower, lender, rent);
        proPayService.createDeposit(borrower, lender, transaction);
        transactions.save(transaction);
    }
}
