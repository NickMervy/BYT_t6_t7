package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException {
		// bug#1 found
		assertThrows(AccountExistsException.class, () -> SweBank.openAccount("Ulrika"));
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {

		SweBank.deposit("Bob", new Money(1000, SEK));

		assertEquals(1000, (int)SweBank.getBalance("Bob"));

		// bug#2 found
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.deposit("asdasd", new Money(1000, SEK)));
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		
		SweBank.withdraw("Bob", new Money(100, SEK));

		// bug#3 found
		assertEquals(-100, (int)SweBank.getBalance("Bob"));
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.deposit("asdasd", new Money(1000, SEK)));
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.getBalance("adssad"));
		assertEquals(0, (int)SweBank.getBalance("Bob"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		// First account does not exist
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.transfer("asdasd", SweBank, "Bob", new Money(1000, SEK)));
		// Second account does not exist
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.transfer("Bob", SweBank, "asdasd", new Money(1000, SEK)));
		// Both accounts does not exist
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.transfer("asdasd", SweBank, "asdasd", new Money(1000, SEK)));

		// transfer to the same account
		SweBank.deposit("Bob", new Money(10000, SEK));
		SweBank.transfer("Bob", SweBank, "Bob", new Money(10000, SEK));
		assertEquals(10000, (int)SweBank.getBalance("Bob"));

		// transfer to the same bank diff acc
		SweBank.transfer("Bob", "Ulrika", new Money(1000, SEK));
		// bug#4 found
		assertEquals(9000, (int)SweBank.getBalance("Bob"));
		assertEquals(1000, (int)SweBank.getBalance("Ulrika"));

		// transfer to another bank account
		SweBank.transfer("Bob", Nordea, "Bob", new Money(1000, SEK));
		assertEquals(8000, (int)SweBank.getBalance("Bob"));
		assertEquals(1000, (int)Nordea.getBalance("Bob"));

		// transfer to another bank account (diff currency type)
		SweBank.transfer("Bob", DanskeBank, "Gertrud", new Money(1000, SEK));
		assertEquals(7000, (int)SweBank.getBalance("Bob"));
		assertEquals(750, (int)DanskeBank.getBalance("Gertrud"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// bug#5 found
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.addTimedPayment("asdasd", "scholarship", 10, 50, new Money(1000, SEK), SweBank, "Ulrika"));
		// Second account does not exist
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.addTimedPayment("Bob", "scholarship", 10, 50, new Money(1000, SEK), SweBank, "asdasd"));
		// Both accounts does not exist
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.addTimedPayment("asdasd", "scholarship", 10, 50, new Money(1000, SEK), SweBank, "asdasd"));


		// bug#6 found
		assertThrows(AccountDoesNotExistException.class, () -> SweBank.removeTimedPayment("asdasd", "scholarship"));
	}
}
