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
    ProPayService proService;

    @Autowired
    private TransactionRepository transactions;

    public List<Transaction> getAllFromSender(User user){
        return transactions.findAllBySender(user);
    }

    public List<Transaction> getAllFromReceiver(User user){
        return transactions.findAllByReceiver(user);
    }

    public boolean checkFinances(User requester, Item item, LocalDate startdate, LocalDate enddate){
        long days = DAYS.between(startdate, enddate);
        int rent = item.getRental()*(int) days;
        proService.createAccount(requester);
        int amount = proService.getAccount(requester).getAmount();
        return amount >= (rent+item.getDeposit());
    }
    public int createTransaction(Long requestId, Long itemId) {
        Item item = itemRepo.findById(itemId).get();
        Request request = reqRepo.findById(requestId).get();
        User lender = item.getLender();
        User borrower = request.getRequester();

        long days = DAYS.between(request.getPeriod().getStartdate(),request.getPeriod().getEnddate());
        int rent = item.getRental()*(int) days;

        proService.createAccount(lender);

        Transaction trans = new Transaction(rent, item.getDeposit(), item, lender, borrower);
        
        if(checkFinances(request.getRequester(), item, request.getPeriod().getStartdate(), request.getPeriod().getStartdate())){
            int responseTransfer = proService.transferMoney(borrower, lender, rent);
            int responseDeposit = proService.createDeposit(borrower, lender, trans);
            //Transaction will be saved in proService due to reasons...

            if(responseDeposit!=200)
                return responseDeposit;
            else if(responseTransfer!=200)
                return responseTransfer;
        } else {
            return -1;
        }
        return 200;
    }
}
