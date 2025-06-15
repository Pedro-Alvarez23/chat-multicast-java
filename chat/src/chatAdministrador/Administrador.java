package chatAdministrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Administrador {

	private MulticastSocket socket;
	private InetAddress grupo;
	private int puerto = 4446;
	private JFrame frame;
	private JTextArea textArea;
	private JTextField textField;

	public Administrador() {
		initialize(); // Inicializa la GUI
		try {
			socket = new MulticastSocket(puerto); // Crea un nuevo socket multicast
			grupo = InetAddress.getByName("230.0.0.0"); // Obtiene la dirección IP del grupo multicast
			socket.joinGroup(grupo); // El socket se une al grupo multicast
			recibirMensajes(); // Inicia un hilo para recibir mensajes
			System.out.println("Servidor Multicast iniciado...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initialize() {
		frame = new JFrame("Servidor Multicast");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarMensaje();
			}
		});
		panel.add(btnEnviar, BorderLayout.EAST);

		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JButton btnSalir = new JButton("Salir");
		scrollPane.setRowHeaderView(btnSalir);

		textField = new JTextField();
		scrollPane.setColumnHeaderView(textField);
		textField.setColumns(10);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		frame.setVisible(true);
	}

	private void recibirMensajes() {
		Thread thread = new Thread(new Runnable() {
			String mensaje;
			public void run() {//creamos un hilo para recivir el multicast
				try {
					while (true) {
						byte[] buffer = new byte[1000];
						DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);//recivimos los datos
						socket.receive(paquete);
						mensaje = new String(paquete.getData(), 0, paquete.getLength());
						textArea.append( mensaje + "\n");//mostramos el texto recivido
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}



	private void enviarMensaje() {
		try {
			String mensaje = textField.getText(); // Obtiene el mensaje del campo de texto
			byte[] buffer = mensaje.getBytes();
			DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, grupo, puerto);
			socket.send(paquete); // Envía el mensaje al grupo multicast
			textField.setText(""); // Limpia el campo de texto después de enviar el mensaje
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void salir() {
		try {
			socket.leaveGroup(grupo); // El socket abandona el grupo multicast
			socket.close(); // Se cierra el socket
			System.exit(0); // Se termina la aplicación
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Administrador();// Creamos una instancia del servidor
	}
}