# language: es
@api
Característica: Reservar un ticket para un evento
  Como comprador
  Quiero realizar una reserva para un evento de mi interés
  Para asegurar mi asistencia

  Escenario: Realizar una reserva exitosa para un evento con disponibilidad
    Dado que existe un evento con tickets disponibles
    Cuando solicito la reserva de un ticket para dicho evento
    Entonces la reserva debe ser aceptada exitosamente
    Y el sistema debe confirmar el identificador del ticket reservado
