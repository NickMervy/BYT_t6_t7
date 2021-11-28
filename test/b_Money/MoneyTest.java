package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(1000, (int)EUR10.getAmount());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SEK0.getCurrency());
	}

	@Test
	public void testToString() {
		assertEquals("100.0 SEK", SEK100.toString());
		assertEquals("0.0 SEK", SEK0.toString());
		assertEquals("10.0 EUR", EUR10.toString());
	}

	@Test
	public void testGlobalValue() {
		assertEquals(1500, (int)SEK100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		assertTrue(EUR20.equals(SEK200));
		assertTrue(EUR20.equals(EUR20));
		assertTrue(SEK100.equals(EUR10));
		assertFalse(EUR20.equals(SEK100));
	}

	@Test
	public void testAdd() {
		// positive overflow 
		assertThrows(ArithmeticException.class, () -> new Money(Integer.MAX_VALUE, EUR).add(SEK100));
		// negative overflow 
		assertThrows(ArithmeticException.class, () -> new Money(Integer.MIN_VALUE, EUR).add(SEKn100));
		//assert no change adding 0
		assertTrue(SEK100.equals(SEK100.add(EUR0)));
		assertTrue(SEK100.equals(SEK100.add(SEK0)));
		//assert equality
		assertFalse(SEK100.equals(SEK100.add(EUR10)));
		assertTrue(SEK200.equals(SEK100.add(EUR10)));
		assertTrue(SEK200.equals(SEK0.add(SEK100).add(EUR10)));
		assertTrue(SEK0.equals(SEKn100.add(SEK100)));
		//assert resultant amount
		assertEquals(SEK200.getAmount(), SEK100.add(EUR10).getAmount());
		// assert resultant currencty
		assertEquals(SEK200.getCurrency(), SEK100.add(EUR10).getCurrency());
	}

	@Test
	public void testSub() {
		// positive overflow 
		assertThrows(ArithmeticException.class, () -> new Money(Integer.MAX_VALUE, EUR).sub(SEKn100));
		// negative overflow 
		assertThrows(ArithmeticException.class, () -> new Money(Integer.MIN_VALUE, EUR).sub(SEK100));
		//assert subtractions of negative value
		assertEquals(SEK0.toString(), SEK200.sub(EUR10).sub(SEK100).toString());
		//assert multiple subtractions
		assertEquals(SEK0.toString(), SEK200.sub(EUR10).sub(SEK100).toString());
		//assert resultant amount
		assertEquals(EUR0.getAmount(), EUR10.sub(SEK100).getAmount());
		// assert resultant currencty
		assertEquals(SEK0.getCurrency(), SEK100.sub(SEK100).getCurrency());
	}

	@Test
	public void testIsZero() {
		assertTrue(EUR0.isZero());
		assertTrue(SEK0.isZero());
		assertFalse(SEK200.isZero());
	}

	@Test
	public void testNegate() {
		assertEquals(SEKn100.getAmount(), SEK100.negate().getAmount());
		assertEquals(SEKn100.getCurrency(), SEK100.negate().getCurrency());
		assertEquals(SEKn100.toString(), SEK100.negate().toString());
	}

	@Test
	public void testCompareTo() {
		// assert same object
		assertTrue(SEK0.compareTo(SEK0) == 0);

		// assert equality
		assertTrue(SEK0.compareTo(EUR0) == 0);
		assertTrue(SEK100.compareTo(EUR10) == 0);
		assertFalse(SEK200.compareTo(EUR10) == 0);

		// assert > cases
		assertTrue(EUR20.compareTo(SEK100) > 0);
		assertTrue(SEK0.compareTo(SEKn100) > 0);
		assertFalse(SEK100.compareTo(SEK200) > 0);		
		
		// assert < cases
		assertTrue(SEK100.compareTo(EUR20) < 0);
		assertTrue(EUR0.compareTo(SEK100) < 0);
		assertFalse(SEK100.compareTo(SEKn100) < 0);
	}
}
