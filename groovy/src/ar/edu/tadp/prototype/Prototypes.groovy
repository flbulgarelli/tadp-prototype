package ar.edu.tadp.prototype

class Prototypes {

	static getArgs(args){
		return args as List
	}

	static init() {
		Object.metaClass {
			prototypes = null

			dynamicProperties = null

			propertyMissing = { String name, value ->
				dynamicProperties[name] = value
			}

			propertyMissing = { String name ->
				if(dynamicProperties.containsKey(name)){
					return dynamicProperties[name]
				} else {
					for (prototype in prototypes) {
						try {
							return prototype."$name"
						} catch (MissingPropertyException e) {
						}
					}
				}

				throw new MissingPropertyException(name, delegate.getClass())
			}

			addPrototype = { aPrototype ->
				prototypes.add(aPrototype)
			}

			methodMissing = { String name, args ->
				try {
					def method = delegate."$name"
					return method.callOn(method, delegate, args)
				} catch(MissingPropertyException e){
					throw new MissingMethodException(name, delegate.getClass(), args)
				}
			}

			def oldConstructor = Object.metaClass.retrieveConstructor()

			constructor = {
				def newInstance = oldConstructor.newInstance()
				newInstance.dynamicProperties = [:]
				newInstance.prototypes = []
				newInstance
			}
		}

		/*
		 * ¿Por qué esto y no el withParams? Esto permite mandar un objeto que implemente el mensaje callOn, y funciona :)
		 */
		Closure.metaClass {
			//El closure viene por parámetro porque hay un lío con el delegate... no sé.
			callOn = {closure, anObject, args ->
				def clonedClosure = closure.clone()
				clonedClosure.setResolveStrategy(Closure.DELEGATE_FIRST)
				clonedClosure.setDelegate(anObject)
				clonedClosure.call(*args)
			}
		}
	}
}
