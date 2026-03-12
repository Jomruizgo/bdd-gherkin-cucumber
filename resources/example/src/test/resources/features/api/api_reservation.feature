# language: es
@api
Característica: Aceptación de una reserva válida por la API
  Como consumidor del backend
  Quiero enviar una solicitud válida de reserva
  Para que el sistema la acepte de forma asíncrona

  Escenario: Enviar una reserva válida al producer
    Dado que existe un evento con un ticket disponible
    Cuando envío una solicitud válida de reserva al producer
    Entonces la respuesta debería ser 202 Accepted
    Y el cuerpo debería incluir el ticketId reservado
