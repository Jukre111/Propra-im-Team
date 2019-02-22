package de.hhu.sharing.RepositoryTests;

import de.hhu.sharing.data.ItemRepository;
import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RequestRepository requestRepo;

    public Request createRequest(User user){

        LocalDate startdate = LocalDate.of(2010,1,1);
        LocalDate enddate = LocalDate.of(2010,2,2);
        Period period = new Period(startdate,enddate);

        Request request = new Request(period,user);
        requestRepo.save(request);
        Request requestEntity = requestRepo.findById(request.getId()).get();

        return requestEntity;
    }

    public User createUser(String username){
        LocalDate birthdate = LocalDate.of(2000,1,1);
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user = new User(username,"password", "role", "lastnmae", "forname", "email",birthdate,address);
        userRepo.save(user);
        User userEntity = userRepo.findByUsername(username).get();
        return userEntity;
    }


    public ArrayList<Item> createItems(){
        User user = createUser("testman");
        Item item1 = new Item("apfel", "lecker",1,1 ,user );
        Item item2 = new Item("granatapfel", "grosse Kerne",2,2 ,user );
        Item item3 = new Item("banane", "kleine Kerne",3,3 ,user );
        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        return items;
    }


    @Test
    public void testFindAll() {
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        itemRepo.save(items.get(1));
        Assertions.assertThat(itemRepo.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindById(){
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        Optional<Item> optionalItem = itemRepo.findById(items.get(0).getId());

        Assert.assertTrue(optionalItem.isPresent());
        if(optionalItem.isPresent()){
            Assertions.assertThat(optionalItem.get().getId()).isEqualTo(items.get(0).getId());
        }
    }

    @Test
    public void testFindAllByLender(){
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        itemRepo.save(items.get(1));

        List<Item> itemList = itemRepo.findAllByLender(items.get(0).getLender());
        Assertions.assertThat(itemList.size()).isEqualTo(2);

    }

    @Test
    public void  testFindAllByNameContainingOrDescriptionContaining(){
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        itemRepo.save(items.get(1));
        itemRepo.save(items.get(2));

        List<Item> itemList1 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("apf", "egal");
        List<Item> itemList2 = itemRepo.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("banane", "Kern");
        
        Assertions.assertThat(itemList1.size()).isEqualTo(2);
        Assertions.assertThat(itemList2.size()).isEqualTo(2);

    }

    @Test
    public void testFindFirst2ByLenderNot(){
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        itemRepo.save(items.get(1));
        itemRepo.save(items.get(2));

        User user = createUser("superTestMan");
        List<Item> itemList = itemRepo.findFirst2ByLenderNot(user);
        Assertions.assertThat(itemList.size()).isEqualTo(2);
    }

    @Test
    public void tesFindByRequests_id(){
        ArrayList<Item> items = createItems();
        Item item = items.get(0);

        User user = createUser("superTestMan");

        Request request = createRequest(user);
        item.addToRequests(request);

        itemRepo.save(item);

        Optional<Item> optionalItem = itemRepo.findByRequests_id(items.get(0).getRequests().get(0).getId());
        Assertions.assertThat(optionalItem.get()).isEqualTo(items.get(0));
    }

    @Test
    public void testFindFirstByLender(){
        ArrayList<Item> items = createItems();
        itemRepo.save(items.get(0));
        itemRepo.save(items.get(1));

        Optional <Item> optionalItem = itemRepo.findFirstByLender(items.get(0).getLender());
        Assertions.assertThat(optionalItem.get()).isEqualTo(items.get(0));
    }

    @Test
    public void TestFindAllByRequests_requester(){
        ArrayList<Item> items = createItems();
        Item item = items.get(0);
        itemRepo.save(item);

        User user = createUser("superTestMan");

       Request request = createRequest(user);

        item.addToRequests(request);

        itemRepo.save(item);

        List <Item> itemList = itemRepo.findAllByRequests_requester(user);
        Assertions.assertThat(itemList.size()).isEqualTo(1);
    }
}
