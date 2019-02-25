package de.hhu.sharing.services;

import de.hhu.sharing.data.ConflictRepository;
import de.hhu.sharing.model.*;
import groovy.transform.AutoImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConflictService {

    @Autowired
    private ConflictRepository conflicts;

    @Autowired
    private BorrowingProcessService borrowingProcessService;

    public Conflict get(Long id){
        Conflict conflict = this.conflicts.findById(id)
                .orElseThrow(
                        ()-> new RuntimeException("Conflict not found"));
        return conflict;
    }

    public Conflict getFromBorrowindProcess(BorrowingProcess process) {
        return this.conflicts.findByProcess(process);
    }

    public List<Conflict> getAll() {
        return conflicts.findAll();
    }

    public void create(User lender, User borrower, BorrowingProcess process, User prosecuter, String problem) {
        Conflict conflict = new Conflict(lender, borrower, process, new Message(prosecuter.getUsername(), problem));
        conflicts.save(conflict);
    }

    public boolean noConflictWith(Item item) {
        return conflicts.findAllByItem(item).isEmpty();
    }

    public void removeConflict(Conflict conflict){
        conflicts.delete(conflict);
    }
}
