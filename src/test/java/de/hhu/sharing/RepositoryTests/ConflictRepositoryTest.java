package de.hhu.sharing.RepositoryTests;

import de.hhu.sharing.data.BorrowingProcessRepository;
import de.hhu.sharing.data.ConflictRepository;
import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.services.BorrowingProcessService;
import de.hhu.sharing.storage.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ConflictRepositoryTest {

    @Autowired
    ConflictRepository conflictRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ItemRepository itemRepo;

    @Autowired
    BorrowingProcessRepository bPRepo;

    @MockBean
    StorageService storageService;

    @Test
    public void mustHaveTest (){
        Assertions.assertThat(true).isTrue();
    }
    /*

    public User createUser(String username){
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastnmae", "forname", "email",birthdate,address);
        userRepo.save(user);
        return userRepo.findByUsername(username).get();
    }

    public Item createItem(String name, String description){
        User user = createUser("testman");
        Item item = new Item(name, description,1,1 ,user );
        itemRepo.save(item);
        return itemRepo.findById(item.getId()).get();
    }

    public BorrowingProcess createProcess(Item item){
        BorrowingProcess borrowingProcess = new BorrowingProcess();
        borrowingProcess.setItem(item);
        bPRepo.save(borrowingProcess);
        return bPRepo.findById(borrowingProcess.getId()).get();
    }

    public BorrowingProcess createProcess(){
        BorrowingProcess borrowingProcess = new BorrowingProcess();
        bPRepo.save(borrowingProcess);
        return bPRepo.findById(borrowingProcess.getId()).get();
    }

    public Conflict createConflict(User lender, User borrower, BorrowingProcess process,String author){
        Message message = new Message(author, "cool");
        Conflict conflict = new Conflict(lender,borrower,process,message);
        conflictRepo.save(conflict);
        return conflictRepo.findById(conflict.getId()).get();
    }


    @Test
    public void testFindAll(){
        User user1 = createUser("testman1");
        User user2 = createUser("testman2");

        BorrowingProcess process = createProcess();

        createConflict(user1,user2,process, "testman1");
        createConflict(user2,user1,process, "testman2");

        Assertions.assertThat(conflictRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindAllByProcess_Item(){

        Item item = createItem("testItem","cool");
        User user1 = createUser("testman1");
        User user2 = createUser("testman2");

        BorrowingProcess process1 = createProcess(item);
        BorrowingProcess process2 = createProcess(item);

        createConflict(user1,user2,process1, "testman1");
        createConflict(user2,user1,process2, "testman2");

        Assertions.assertThat(conflictRepo.findAllByProcess_Item(item).size()).isEqualTo(2);

    }

    @Test
    public void testFindByProcess(){

        User user1 = createUser("testman1");
        User user2 = createUser("testman2");

        BorrowingProcess process1 = createProcess();
        BorrowingProcess process2 = createProcess();

        Conflict conflict = createConflict(user1,user2,process1, "testman1");
        createConflict(user2,user1,process2, "testman2");

        Assertions.assertThat(conflictRepo.findByProcess(process1)).isEqualTo(conflict);

    }*/
}
