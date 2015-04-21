//*********************************************************
//*	Zach Morgan
//*	CSCI 361
//*	4/27/13
//*	Mini-Project
//*	10354361
//*********************************************************

import java.net.*;
import java.io.*;
import java.util.*;

public class FTPServer {
	public static void main(String[] args) throws Exception {
		// Get command line argument.
		if (args.length != 1) {System.out.println("Required arguments: port");
			return;
		}
		int port = Integer.parseInt(args[0]);
		ServerSocket soc = new ServerSocket(port);
		System.out.println("FTP Server Started on Port Number " + port);
		while (true) {
			System.out.println("Waiting for Connection ...");
			FTPServerThread thread = new FTPServerThread(soc.accept());

		}
	}
}

class FTPServerThread extends Thread {
	Socket ClientSoc;

	DataInputStream is;
	DataOutputStream os;

	FTPServerThread(Socket soc) {

		try {
			ClientSoc = soc;
			is = new DataInputStream(ClientSoc.getInputStream());
			os = new DataOutputStream(ClientSoc.getOutputStream());
			System.out.println("FTP Client Connected ...");
			start();

		} catch (Exception ex) {
		}
	}

	Boolean LogOn(String username, String password) throws Exception {
		if (username.compareTo("a") == 0) {
			if (password.compareTo("1") == 0) {
				os.writeUTF("  ~Authentication successful~");
				return true;
			} else {
				os.writeUTF("Password incorrect");
				return false;
			}

		} else if (username.compareTo("b") == 0) {
			if (password.compareTo("2") == 0) {
				os.writeUTF("  ~Authentication successful~");
				return true;
			} else {
				os.writeUTF("Password incorrect");
				return false;
			}
		}
		os.writeUTF("Username not in system");
		return false;
	}

	void SendFile() throws Exception {
		String filename = is.readUTF();
		File f = new File(System.getProperty("user.dir") + "\\" + filename);
		if (!f.exists()) {
			os.writeUTF("File Not Found");
			return;
		} else {
			os.writeUTF("READY");
			FileInputStream fin = new FileInputStream(f);
			int ch;
			do {
				ch = fin.read();
				os.writeUTF(String.valueOf(ch));
			} while (ch != -1);
			fin.close();
			os.writeUTF("File Receive Successful");
		}
	}

	void ReceiveFile() throws Exception {
		String filename = is.readUTF();
		if (filename.compareTo("File not found") == 0) {
			return;
		}
		File f = new File(System.getProperty("user.dir") + "\\" + filename);
		String option;

		if (f.exists()) {
			os.writeUTF("File already exists");
			option = is.readUTF();
		} else {
			os.writeUTF("SendFile");
			option = "Y";
		}

		if (option.compareTo("Y") == 0) {
			FileOutputStream fout = new FileOutputStream(f);
			int ch;
			String temp;
			do {
				temp = is.readUTF();
				ch = Integer.parseInt(temp);
				if (ch != -1) {
					fout.write(ch);
				}
			} while (ch != -1);
			fout.close();
			os.writeUTF("File Send Successful");
		} else {
			return;
		}

	}

	public void run() {
		String homeDirectory = System.getProperty("user.dir");
		boolean active = true;
		boolean logon = false;
		while (active) {
			try {
				System.out.println("Waiting for Command ...");
				String Command = is.readUTF();
				if (Command.compareTo("LOGON") == 0) {
					System.out.println("\tLOGON Command Received ...");
					String username = is.readUTF();
					String password = is.readUTF();
					logon = LogOn(username, password);

				} else if ((Command.compareTo("GET") == 0) && logon) {
					System.out.println("\tGET Command Received ...");
					SendFile();
					continue;
				} else if ((Command.compareTo("SEND") == 0) && logon) {
					System.out.println("\tSEND Command Received ...");
					ReceiveFile();
					continue;
				} else if ((Command.compareTo("DIR") == 0) && logon) {
					System.out.println("\tDIR Command Received ...");
					os.writeUTF("\nCurrent directory:\n\t"
							+ System.getProperty("user.dir"));
					String files = "";
					File folder = new File(System.getProperty("user.dir"));
					File[] listOfFiles = folder.listFiles();

					for (int i = 0; i < listOfFiles.length; i++) {

						if (listOfFiles[i].isFile()) {
							files = files + "\n\t\t" + listOfFiles[i].getName();
						}
					}
					os.writeUTF(files);
					continue;
				} else if ((Command.compareTo("CD") == 0) && logon) {
					System.out.println("\tCD Command Received ...");
					System.setProperty("user.dir", is.readUTF());
					os.writeUTF("Directory change Successful");
					continue;
				} else if ((Command.compareTo("DISCONNECT") == 0) && logon) {
					System.out.println("\tDisconnect Command Received ...");
					System.setProperty("user.dir", homeDirectory);
					active = false;
				}
			} catch (Exception ex) {
			}
		}
	}
}