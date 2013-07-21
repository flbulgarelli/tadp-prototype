class Object
  attr_accessor :prototype

  def method_missing(name, *args)
    asignacion = name.to_s.scan /(.+)=/
    if !asignacion.empty?
      proto_asignar_slot(asignacion[0][0], args[0])
    elsif !prototype.nil?
      prototype.send(name, *args)
    else
      super
    end
  end

  def proto_asignar_slot(slot, valor)
    if (valor.instance_of?(Proc))
      define_singleton_method(slot, &valor)
    else
      define_singleton_method(slot) do
        valor
      end
    end
  end
end