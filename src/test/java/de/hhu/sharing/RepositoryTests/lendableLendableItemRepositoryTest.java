
package de.hhu.sharing.RepositoryTests;

import de.hhu.sharing.data.LendableItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import de.hhu.sharing.storage.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class lendableLendableItemRepositoryTest {

    @Autowired
    LendableItemRepository itemRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RequestRepository requestRepo;

    @MockBean
    StorageService storageService;

    public Request createRequest(User user){

        LocalDate startdate = LocalDate.of(2010,1,1);
        LocalDate enddate = LocalDate.of(2010,2,2);
        Period period = new Period(startdate,enddate);

        Request request = new Request(period,user);
        requestRepo.save(request);
        return requestRepo.findById(request.getId()).get();
    }

    public User createUser(String username){
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastnmae", "forname", "email",birthdate,address);
        userRepo.save(user);
        return userRepo.findByUsername(username).get();
    }


    public ArrayList<LendableItem> createItems(){
        User user = createUser("testman");
        LendableItem lendableItem1 = new LendableItem("apfel", "lecker",1,1 ,user );
        LendableItem lendableItem2 = new LendableItem("granatapfel", "grosse Kerne",2,2 ,user );
        LendableItem lendableItem3 = new LendableItem("banane", "kleine Kerne",3,3 ,user );
        ArrayList<LendableItem> LendableItems = new ArrayList<>();
        LendableItems.add(lendableItem1);
        LendableItems.add(lendableItem2);
        LendableItems.add(lendableItem3);
        return LendableItems;
    }


    @Test
    public void testFindAll() {
        ArrayList<LendableItem> LendableItems = createItems();
        itemRepo.save(LendableItems.get(0));
        itemRepo.save(LendableItems.get(1));
        Assertions.assertThat(itemRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindById(){
        ArrayList<LendableItem> LendableItems = createItems();
        itemRepo.save(LendableItems.get(0));
        Optional<LendableItem> optionalItem = itemRepo.findById(LendableItems.get(0).getId());

        Assert.assertTrue(optionalItem.isPresent());
        Assertions.assertThat(optionalItem.get().getId()).isEqualTo(LendableItems.get(0).getId());
    }

    @Test
    public void testFindAllByLender(){
        ArrayList<LendableItem> LendableItems = createItems();
        itemRepo.save(LendableItems.get(0));
        itemRepo.save(LendableItems.get(1));

        List<LendableItem> lendableItemList = itemRepo.findAllByOwner(LendableItems.get(0).getOwner());
        Assertions.assertThat(lendableItemList.size()).isEqualTo(2);
        Assertions.assertThat(lendableItemList.get(0).getOwner().getUsername()).isEqualTo("testman");

    }

    @Test
    public void  testFindAllByNameContainingOrDescriptionContaining(){
        ArrayList<LendableItem> LendableItems = createItems();
        itemRepo.save(LendableItems.get(0));
        itemRepo.save(LendableItems.get(1));
        itemRepo.save(LendableItems.get(2));

        List<LendableItem> lendableItemList1 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("apf", "egal");
        List<LendableItem> lendableItemList2 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("banane", "Kern");

        Assertions.assertThat(lendableItemList1.size()).isEqualTo(2);
        Assertions.assertThat(lendableItemList2.size()).isEqualTo(2);

    }

    @Test
    public void testFindFirst2ByLenderNot(){
        ArrayList<LendableItem> LendableItems = createItems();
        itemRepo.save(LendableItems.get(0));
        itemRepo.save(LendableItems.get(1));
        itemRepo.save(LendableItems.get(2));

        User user = createUser("superTestMan");
        List<LendableItem> lendableItemList = itemRepo.findFirst2ByOwnerNot(user);
        Assertions.assertThat(lendableItemList.size()).isEqualTo(2);
        Assertions.assertThat(lendableItemList.get(0)).isEqualTo(LendableItems.get(0));
        Assertions.assertThat(lendableItemList.get(1)).isEqualTo(LendableItems.get(1));
    }

    @Test
    public void tesFindByRequests_id(){
        ArrayList<LendableItem> LendableItems = createItems();
        LendableItem lendableItem = LendableItems.get(0);
        User user = createUser("superTestMan");
        Request request = createRequest(user);
        lendableItem.addToRequests(request);
        itemRepo.save(lendableItem);

        Optional<LendableItem> optionalItem = itemRepo.findByRequests_id(request.getId());
        Assertions.assertThat(optionalItem.get()).isEqualTo(LendableItems.get(0));
    }

    @Test
    public void testFindFirstByLender(){
        ArrayList<LendableItem> LendableItems = createItems();
        itemRepo.save(LendableItems.get(0));
        itemRepo.save(LendableItems.get(1));

        Optional <LendableItem> optionalItem = itemRepo.findFirstByOwner(LendableItems.get(0).getOwner());
        Assertions.assertThat(optionalItem.get()).isEqualTo(LendableItems.get(0));
    }

    @Test
    public void TestFindAllByRequests_requester(){
        ArrayList<LendableItem> LendableItems = createItems();
        LendableItem lendableItem = LendableItems.get(0);
        itemRepo.save(lendableItem);
        User user = createUser("superTestMan");
        Request request = createRequest(user);
        lendableItem.addToRequests(request);
        itemRepo.save(lendableItem);

        List <LendableItem> lendableItemList = itemRepo.findAllByRequests_requester(user);
        Assertions.assertThat(lendableItemList.size()).isEqualTo(1);
        Assertions.assertThat(lendableItemList.get(0)).isEqualTo(lendableItem);
    }
}