package ar.edu.tadp.prototype

class Prototype {

	static getArgs(args){
		if(args.size() == 1) {
			return args[0]
		} else {
			return args
		}
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
				&& dynamicProperties[name] instanceof Closure
				&& dynamicProperties[name].getMaximumNumberOfParameters() == args.size()) {
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
