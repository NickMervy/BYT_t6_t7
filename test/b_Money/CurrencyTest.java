package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("DKK", DKK.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals(0.20, DKK.getRate(),  0.00001);
	}
	
	@Test
	public void testSetRate() {
		SEK.setRate(0.1);
		assertEquals(0.1, SEK.getRate(), 0.00001);
	}
	
	/**
	 * UniversalValue test
	 * The integer values are respresented according to the convention defined as "The last two digits denote two decimals".
	 * Testing the following cases (using SEK/EUR currency):
	 * - input that causes Integer overflow => thorws ArithmeticException;
	 * - inout 0 => 0 expected;
	 * - input 300 (by conv. 30000) => 300 * 0.15 = 45.00 (4500 by convention);
	 * - input 10 (by conv. 1000)=> 10 * 0.15 = 1.5 (150 by convention);
	 * - input 1 (by conv 100) => 1 * 0.15 = 0.15 (15 by convention);
	 */
	@Test
	public void testUniversalValue() {
		assertThrows(ArithmeticException.class, () -> EUR.universalValue(Integer.MAX_VALUE - 100));
		assertEquals(0, (int)SEK.universalValue(000));
		assertEquals(2, (int)SEK.universalValue(10));
		assertEquals(4500, (int)SEK.universalValue(30000));
		assertEquals(150, (int)SEK.universalValue(1000));
		assertEquals(15, (int)SEK.universalValue(100));
	}
	

	/**
	 * UniversalValue test
	 * The integer values are respresented according to the convention defined as "The last two digits denote two decimals".
	 * Testing the following cases (using SEK/EUR currency):
	 * - input that causes Integer overflow => thorws ArithmeticException;
	 * - inout 0 => 0 expected;
	 * - input 10 (by conv. 1000)=> 10 * 0.15 = 1.5 (150 by convention);
	 * - input 1 (by conv 100) => 1 * 0.15 = 0.15 (15 by convention);
	 */
	@Test
	public void testValueInThisCurrency() {
		assertThrows(ArithmeticException.class, () -> EUR.universalValue(Integer.MAX_VALUE - 100));
		assertEquals(0, (int)SEK.valueInThisCurrency(000, EUR));
		assertEquals(133, (int)SEK.valueInThisCurrency(100, DKK));
		assertEquals(1333, (int)SEK.valueInThisCurrency(1000, DKK));
	}
}
