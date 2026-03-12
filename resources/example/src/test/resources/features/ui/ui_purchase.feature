# language: es
@ui
Característica: Compra de tickets para eventos
  Como comprador
  Quiero seleccionar un evento disponible y completar mi registro
  Para adquirir un ticket para el evento

  Escenario: Inicio de proceso de compra exitoso
    Dado que existe un evento disponible para la venta
    Y que me encuentro en el portal de compra
    Cuando selecciono el evento para comprar
    Y proporciono mi correo electrónico "comprador-bdd@example.com"
    Y confirmo la intención de compra
    Entonces el sistema debe permitirme proceder al pago
