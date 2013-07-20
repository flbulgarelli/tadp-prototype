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
			operador3 , operador4->  operador4 + operador3 + delegate.operador1 + delegate.operador2
		}
		
		assert a.suma(3, 0) == 8


		def b = new Object()
		b.prototype = a
		
		assert b.operador1 == 1
		assert b.suma(3, 2) == 10
		
		b.operador2 = 10
		assert a.operador2 == 4

		assert b.suma(3, 4) == 18
		
		b.otraSuma = {operador -> delegate.operador1 + operador}
		
		assert b.otraSuma(5) == 6
		
		b.sumaFinal = {operador1 + operador2}
		
		assert b.sumaFinal() == 11

		//Objeto callable
		def aCallable = new Object()
		aCallable.estadoPropio = 20
		
		aCallable.callOn = { callable, anObject, args ->
			anObject.suma(*args) + anObject.otraSuma(args[0]) + estadoPropio
		}
		
		b.cosaRara = aCallable
		
		assert b.cosaRara(3,4) == 42
		
	}
}
