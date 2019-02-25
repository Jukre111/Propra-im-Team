package de.hhu.sharing.ServiceTests;

class TransactionRentalServiceTest {
    /*@Mock
    ItemRepository itemRepo;

    @Mock
    RequestRepository reqRepo;

    @Mock
    ProPayService proService;

    @InjectMocks
    TransactionRentalService transSevice;

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckFinancesTrue() {
        User borrower = new User();
        Request request = new Request();
        lendableItem lendableItem = new lendableItem();

        borrower.setUsername("User");
        lendableItem.setRental(10);
        lendableItem.setDeposit(100);

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(1, lendableItem.getDeposit()));

        request.setPeriod(new Period(LocalDate.now(),LocalDate.now().plusDays(5)));
        request.setRequester(borrower);
        Mockito.when(proService.getAccount(borrower.getUsername())).thenReturn(new Account("User", 200, reservations));

        Assertions.assertThat(transSevice.checkFinances(request.getRequester(), lendableItem, request.getPeriod().getStartdate(), request.getPeriod().getEnddate())).isTrue();
    }

    @Test
    public void testCheckFinances() {
        User borrower = new User();
        Request request = new Request();
        lendableItem lendableItem = new lendableItem();

        borrower.setUsername("User");
        lendableItem.setRental(10);
        lendableItem.setDeposit(100);

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(1, lendableItem.getDeposit()));

        request.setPeriod(new Period(LocalDate.now(),LocalDate.now().plusDays(5)));
        request.setRequester(borrower);
        Mockito.when(proService.getAccount(borrower.getUsername())).thenReturn(new Account("User", 100, reservations));

        Assertions.assertThat(transSevice.checkFinances(request.getRequester(), lendableItem, request.getPeriod().getStartdate(), request.getPeriod().getEnddate())).isFalse();
    }*/

}
