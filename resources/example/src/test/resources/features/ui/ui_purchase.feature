# language: es
@ui
Característica: Iniciar una compra simple desde la interfaz
  Como comprador
  Quiero entrar a la página principal de compra, seleccionar un evento disponible y registrar mi correo
  Para iniciar la compra de un ticket

  Escenario: Iniciar una compra seleccionando un evento disponible
    Dado que existe al menos un evento futuro con tickets disponibles
    Y que ingreso a la página principal de compra
    Cuando selecciono el evento disponible creado para la prueba
    Y escribo el correo "comprador-bdd@example.com"
    Y hago clic en el botón comprar
    Entonces debería ver el formulario de pago
