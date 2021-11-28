package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		String id = "Rent";

		// assert if empty
		assertFalse(testAccount.timedPaymentExists(id));
		// assert after adding
		testAccount.addTimedPayment(id, 30, 10, new Money(100, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists(id));
		// assert after removing
		testAccount.removeTimedPayment(id);
		assertFalse(testAccount.timedPaymentExists(id));
	}
	
	@Test
	public void testTimedPayment()  {
		testAccount.addTimedPayment("Rent", 10, 0, new Money(2000000, SEK), SweBank, "Alice");
		for (int i = 0; i < 50; i++) {
			testAccount.tick();
		}

		// bug#7 found
		assertEquals(0, (int)testAccount.getBalance().getAmount());
	}

	@Test
	public void testAddWithdraw() {
		testAccount.deposit(new Money(1000, SEK));
		assertEquals(10001000, (int)testAccount.getBalance().getAmount());
		testAccount.withdraw(new Money(1000, SEK));
		assertEquals(10000000, (int)testAccount.getBalance().getAmount());

		//withdraw different everything
		testAccount.withdraw(new Money(10000000, SEK));
		assertEquals(0, (int)testAccount.getBalance().getAmount());
		//deposit with different currency
		testAccount.deposit(new Money(10000000, new Currency("EUR", 0.20)));
		assertEquals(13333333, (int)testAccount.getBalance().getAmount());
	}
	
	@Test
	public void testGetBalance() {
		assertTrue(new Money(10000000, SEK).equals(testAccount.getBalance()));
	}
}
