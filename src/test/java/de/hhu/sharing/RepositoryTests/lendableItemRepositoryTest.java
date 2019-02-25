
package de.hhu.sharing.RepositoryTests;

import de.hhu.sharing.data.ItemRepository;
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
public class lendableItemRepositoryTest {

    @Autowired
    ItemRepository itemRepo;

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


    public ArrayList<lendableItem> createItems(){
        User user = createUser("testman");
        lendableItem lendableItem1 = new lendableItem("apfel", "lecker",1,1 ,user );
        lendableItem lendableItem2 = new lendableItem("granatapfel", "grosse Kerne",2,2 ,user );
        lendableItem lendableItem3 = new lendableItem("banane", "kleine Kerne",3,3 ,user );
        ArrayList<lendableItem> lendableItems = new ArrayList<>();
        lendableItems.add(lendableItem1);
        lendableItems.add(lendableItem2);
        lendableItems.add(lendableItem3);
        return lendableItems;
    }


    @Test
    public void testFindAll() {
        ArrayList<lendableItem> lendableItems = createItems();
        itemRepo.save(lendableItems.get(0));
        itemRepo.save(lendableItems.get(1));
        Assertions.assertThat(itemRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindById(){
        ArrayList<lendableItem> lendableItems = createItems();
        itemRepo.save(lendableItems.get(0));
        Optional<lendableItem> optionalItem = itemRepo.findById(lendableItems.get(0).getId());

        Assert.assertTrue(optionalItem.isPresent());
        Assertions.assertThat(optionalItem.get().getId()).isEqualTo(lendableItems.get(0).getId());
    }

    @Test
    public void testFindAllByLender(){
        ArrayList<lendableItem> lendableItems = createItems();
        itemRepo.save(lendableItems.get(0));
        itemRepo.save(lendableItems.get(1));

        List<lendableItem> lendableItemList = itemRepo.findAllByLender(lendableItems.get(0).getOwner());
        Assertions.assertThat(lendableItemList.size()).isEqualTo(2);
        Assertions.assertThat(lendableItemList.get(0).getOwner().getUsername()).isEqualTo("testman");

    }

    @Test
    public void  testFindAllByNameContainingOrDescriptionContaining(){
        ArrayList<lendableItem> lendableItems = createItems();
        itemRepo.save(lendableItems.get(0));
        itemRepo.save(lendableItems.get(1));
        itemRepo.save(lendableItems.get(2));

        List<lendableItem> lendableItemList1 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("apf", "egal");
        List<lendableItem> lendableItemList2 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("banane", "Kern");

        Assertions.assertThat(lendableItemList1.size()).isEqualTo(2);
        Assertions.assertThat(lendableItemList2.size()).isEqualTo(2);

    }

    @Test
    public void testFindFirst2ByLenderNot(){
        ArrayList<lendableItem> lendableItems = createItems();
        itemRepo.save(lendableItems.get(0));
        itemRepo.save(lendableItems.get(1));
        itemRepo.save(lendableItems.get(2));

        User user = createUser("superTestMan");
        List<lendableItem> lendableItemList = itemRepo.findFirst2ByLenderNot(user);
        Assertions.assertThat(lendableItemList.size()).isEqualTo(2);
        Assertions.assertThat(lendableItemList.get(0)).isEqualTo(lendableItems.get(0));
        Assertions.assertThat(lendableItemList.get(1)).isEqualTo(lendableItems.get(1));
    }

    @Test
    public void tesFindByRequests_id(){
        ArrayList<lendableItem> lendableItems = createItems();
        lendableItem lendableItem = lendableItems.get(0);
        User user = createUser("superTestMan");
        Request request = createRequest(user);
        lendableItem.addToRequests(request);
        itemRepo.save(lendableItem);

        Optional<lendableItem> optionalItem = itemRepo.findByRequests_id(request.getId());
        Assertions.assertThat(optionalItem.get()).isEqualTo(lendableItems.get(0));
    }

    @Test
    public void testFindFirstByLender(){
        ArrayList<lendableItem> lendableItems = createItems();
        itemRepo.save(lendableItems.get(0));
        itemRepo.save(lendableItems.get(1));

        Optional <lendableItem> optionalItem = itemRepo.findFirstByLender(lendableItems.get(0).getOwner());
        Assertions.assertThat(optionalItem.get()).isEqualTo(lendableItems.get(0));
    }

    @Test
    public void TestFindAllByRequests_requester(){
        ArrayList<lendableItem> lendableItems = createItems();
        lendableItem lendableItem = lendableItems.get(0);
        itemRepo.save(lendableItem);
        User user = createUser("superTestMan");
        Request request = createRequest(user);
        lendableItem.addToRequests(request);
        itemRepo.save(lendableItem);

        List <lendableItem> lendableItemList = itemRepo.findAllByRequests_requester(user);
        Assertions.assertThat(lendableItemList.size()).isEqualTo(1);
        Assertions.assertThat(lendableItemList.get(0)).isEqualTo(lendableItem);
    }
}