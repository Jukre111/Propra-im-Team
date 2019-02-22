package de.hhu.sharing.services;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.TransactionRepository;
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
    ItemRepository itemRepo;

    @Autowired
    RequestRepository reqRepo;

    @Autowired
    private ProPayService proPayService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private TransactionRepository transactions;

    public List<Transaction> getAllFromSender(User user){
        return transactions.findAllBySender(user);
    }

    public List<Transaction> getAllFromReceiver(User user){
        return transactions.findAllByReceiver(user);
    }

    public int createTransaction(Long requestId){
        Item item = itemService.getFromRequestId(requestId);
        Request request = requestService.get(requestId);
        User borrower = request.getRequester();
        User lender = item.getLender();

        int days = (int) DAYS.between(request.getPeriod().getStartdate(),request.getPeriod().getEnddate()) + 1;
        int rent = item.getRental() * days;

        Transaction transaction = new Transaction(rent, item.getDeposit(), item, borrower, lender);
        if(proPayService.checkFinances(borrower, item, request.getPeriod().getStartdate(), request.getPeriod().getStartdate())){
            proPayService.transferMoney(borrower, lender, rent);
            proPayService.createDeposit(borrower, lender, transaction);
        } else {
            return -42;
        }
        return 200;
    }
}
