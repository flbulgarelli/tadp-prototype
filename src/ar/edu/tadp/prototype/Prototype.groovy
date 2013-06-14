package ar.edu.tadp.prototype

class Prototype {

	static getArgs(args){
		return args as List
	}

	static init() {
		Object.metaClass {
			prototype = null

			dynamicProperties = [:]

			propertyMissing = { String name, value ->
				dynamicProperties[name] = value
			}

			propertyMissing = { String name ->
				if(dynamicProperties.containsKey(name)){
					return dynamicProperties[name]
				} else if(prototype != null) {
					return prototype."$name"
				}

				throw new MissingPropertyException(name, delegate.getClass())
			}

			callMethod = {name, args, target ->
				if(dynamicProperties.containsKey(name)
				&& dynamicProperties[name] instanceof Closure) {
					return dynamicProperties[name].rehydrate(target, target, target).call(getArgs(args))
				} else if(prototype!=null) {
					return prototype.callMethod(name, args, target)
				}
				throw new MissingMethodException(name, delegate.getClass(), args)
			}

			methodMissing = { String name, args ->
				callMethod(name, args, delegate)
			}
		}
	}
}
