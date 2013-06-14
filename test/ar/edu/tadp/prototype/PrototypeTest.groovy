package ar.edu.tadp.prototype

import org.junit.BeforeClass;
import org.junit.Test

class PrototypeTest {

	static {
		Prototype.init()
	}

	@Test
	void testSuma(){
		def a = new Object()

		a.operador1 = 1
		a.operador2 = 4

		a.suma = {
			operador3 -> operador3 + delegate.operador1 + delegate.operador2
		}
		assert a.suma(3) == 8


		def b = new Object()
		b.prototype = a
		
		assert b.operador1 == 1
		assert b.suma(3) == 8
		
		b.operador2 = 10
		
		assert b.suma(3) == 14
	}
}
