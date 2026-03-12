# language: es
@api
Característica: Aceptación de solicitudes de reserva para eventos
  Como comprador
  Quiero enviar una solicitud de reserva para un evento de mi interés
  Para que el sistema la procese

  Escenario: Aceptación de una solicitud de reserva para un evento con disponibilidad
    Dado que existe un evento con tickets disponibles
    Cuando solicito la reserva de un ticket para dicho evento
    Entonces la solicitud de reserva debe ser aceptada
    Y el sistema debe confirmar la recepción de la solicitud
