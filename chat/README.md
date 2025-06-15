# Chat Multicast en Java

Este proyecto implementa una aplicación de chat en Java utilizando sockets UDP Multicast, con una interfaz gráfica construida con Swing. Permite la comunicación en tiempo real entre múltiples clientes y un administrador que también puede enviar mensajes.

## Características

- Comunicación en red usando Multicast con `DatagramSocket`
- Interfaz gráfica de usuario para cliente y administrador
- Envío y recepción de mensajes en tiempo real
- Diseño basado en hilos para mantener la interfaz fluida

## Estructura del proyecto

src/
├── chatCliente/
│ └── Cliente.java
├── chatAdministrador/
│ └── Administrador.java


## Tecnologías utilizadas

- Java 8+
- Swing
- Sockets UDP Multicast (`MulticastSocket`)
- Programación concurrente (hilos)

## Cómo ejecutar

1. Compilar:

```bash
javac src/chatCliente/Cliente.java
javac src/chatAdministrador/Administrador.java
Ejecutar el administrador:

bash
Copiar
Editar
java -cp src chatAdministrador.Administrador
Ejecutar uno o varios clientes:

bash
Copiar
Editar
java -cp src chatCliente.Cliente