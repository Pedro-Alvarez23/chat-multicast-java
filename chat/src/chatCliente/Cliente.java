package chatCliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    private JFrame frame;
    private MulticastSocket socket;
    private InetAddress grupo;
    private int puerto = 4446;
    private String nombreUsuario;
    private JTextField textField;
    private JTextArea textArea;
    static String nombre;

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        nombre = entrada.next(); // Lee el nombre de usuario desde la entrada est�ndar
        // Permite que la interfaz gr�fica se ejecute en un subproceso distinto al hilo principal
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Cliente window = new Cliente(); // Crea una instancia de la clase
                    window.frame.setVisible(true); // Hace visible la ventana del chat
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Cliente() {
        initialize(); // Inicializa la interfaz gr�fica del cliente
        recibirMensajes(); // Comienza a recibir mensajes en un hilo separado
    }

    private void initialize() {
        try {
            grupo = InetAddress.getByName("230.0.0.0"); // Direcci�n IP del grupo multicast
            socket = new MulticastSocket(puerto); // Crea un socket multicast en el puerto especificado
            socket.joinGroup(grupo); // El socket se une al grupo multicast
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame(); 
        frame.setBounds(100, 100, 450, 300); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        // Bot�n para enviar mensajes
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarMensaje(); 
            }
        });
        frame.getContentPane().add(btnEnviar, BorderLayout.EAST); 

        // Bot�n para salir de la aplicaci�n
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salir(); // Llama al m�todo salir() cuando se presiona el bot�n
            }
        });
        frame.getContentPane().add(btnSalir, BorderLayout.WEST); 

        textField = new JTextField();
        frame.getContentPane().add(textField, BorderLayout.SOUTH); 
        textField.setColumns(10); 

        textArea = new JTextArea(); 
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER); 
    }

    // M�todo para enviar mensajes al grupo multicast
    private void enviarMensaje() {
        try {
            // Construye el mensaje con el nombre de usuario y el texto ingresado en el campo de texto
            String mensaje = "[" + nombre + "]:" + textField.getText();
            byte[] buffer = mensaje.getBytes(); // Convierte el mensaje a un array de bytes
            // Crea un paquete de datos que contiene el mensaje y lo env�a al grupo multicast en el puerto especificado
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, grupo, puerto);
            socket.send(paquete); // Env�a el paquete de datos
            textField.setText(""); // Limpia el campo de texto despu�s de enviar el mensaje
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // M�todo para recibir mensajes en un hilo separado
    private void recibirMensajes() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) { // Bucle infinito para recibir mensajes continuamente
                        byte[] buffer = new byte[1000]; // Crea un buffer para almacenar los datos recibidos
                        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length); // Crea un paquete para almacenar los datos recibidos
                        socket.receive(paquete); // Recibe el paquete de datos
                        // Convierte los datos recibidos en una cadena y los agrega al �rea de texto para mostrarlos
                        String mensaje = new String(paquete.getData(), 0, paquete.getLength());
                        textArea.append(mensaje + "\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start(); // Inicia el hilo para recibir mensajes
    }

    // M�todo para salir de la aplicaci�n
    private void salir() {
        try {
            socket.leaveGroup(grupo); // El socket abandona el grupo multicast
            socket.close(); // Cierra el socket
            System.exit(0); // Termina la aplicaci�n
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}