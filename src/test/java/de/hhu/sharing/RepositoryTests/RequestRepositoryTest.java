package de.hhu.sharing.RepositoryTests;


import de.hhu.sharing.data.RequestRepository;
import de.hhu.sharing.data.UserRepository;
import de.hhu.sharing.model.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RequestRepositoryTest {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RequestRepository reqRepo;

    public ArrayList<Request> createRequests(){

        ArrayList<Request> requests= new ArrayList<>();
        LocalDate startdate = LocalDate.of(2000,1,1);
        LocalDate enddate=  LocalDate.of(2000,2,2);
        LocalDate date =  LocalDate.of(1996,2,12);
        Period period = new Period(startdate, enddate);
        
        Address address = new Address("unistrase","duesseldorf", 40233);
        User user1 = new User("user1","password","role","lastname","forname", "email", date, address);
        User user2 = new User("user2","password","role","lastname","forname", "email", date, address);
        userRepo.save(user1);
        userRepo.save(user2);
        User requester1 = userRepo.findByUsername("user1").get();
        User requester2 = userRepo.findByUsername("user2").get();
        Request request1 = new Request(period,requester1 );
        Request request2 = new Request(period ,requester2 );
        Request request3 = new Request(period ,requester1 );

        requests.add(request1);
        requests.add(request2);
        requests.add(request3);

        return requests;
    }

    @Test
    public void testFindAll(){
        ArrayList<Request> requests = createRequests();
        reqRepo.save(requests.get(0));
        reqRepo.save(requests.get(1));

        Assertions.assertThat(reqRepo.findAll().size()).isEqualTo(2);
    }
    @Test
    public void testFindAllByRequester() {

        ArrayList<Request> requests = createRequests();
        reqRepo.save(requests.get(0));
        reqRepo.save(requests.get(1));
        reqRepo.save(requests.get(2));
        User user = requests.get(0).getRequester();
        Assertions.assertThat(reqRepo.findAllByRequester(user).size()).isEqualTo(2);
    }
}