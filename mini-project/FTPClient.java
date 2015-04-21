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

class FTPClient {
	public static void main(String... args) throws Exception 
	{
		if (args.length != 2) {
	         System.out.println("Required arguments: host port");
	         return;
	    }
		final String host = args[0]; 
		final int port = Integer.parseInt(args[1]); 
		Socket soc = new Socket(host, port);
		FTPClientThread thread = new FTPClientThread(soc);
		thread.Menu();

	}
}

class FTPClientThread {
	Socket ClientSoc;

	DataInputStream is;
	DataOutputStream os;
	BufferedReader br;

	FTPClientThread(Socket soc) {
		try {
			ClientSoc = soc;
			is = new DataInputStream(ClientSoc.getInputStream());
			os = new DataOutputStream(ClientSoc.getOutputStream());
			br = new BufferedReader(new InputStreamReader(System.in));
		} catch (Exception ex) {
		}
	}

	Boolean LogOn() throws Exception {

		os.writeUTF("LOGON");

		System.out.print("Enter username: ");
		String userName = br.readLine();
		os.writeUTF(userName);

		System.out.print("Enter password: ");
		String password = br.readLine();
		System.out.print("\n\n");
		os.writeUTF(password);

		String msgFromServer = is.readUTF();

		if (msgFromServer.compareTo("  ~Authentication successful~") == 0) {
			System.out.println(msgFromServer + "\n\n");
			return true;
		}
		System.out.println(msgFromServer + "\n\n");
		return false;
	}

	void SendFile() throws Exception {

		String filename;
		System.out.print("Enter file name: ");
		filename = br.readLine();

		File f = new File(filename);
		if (!f.exists()) {
			System.out.println("File does not exist.");
			os.writeUTF("File not found");
			return;
		}

		os.writeUTF(filename);

		String msgFromServer = is.readUTF();
		if (msgFromServer.compareTo("File already exists") == 0) {
			String Option;
			System.out.println("File already exists. Want to OverWrite (Y/N) ?");
			Option = br.readLine();
			if (Option == "Y") {
				os.writeUTF("Y");
			} else {
				os.writeUTF("N");
				return;
			}
		}

		System.out.println("Sending File ...");
		FileInputStream fin = new FileInputStream(f);
		int ch;
		do {
			ch = fin.read();
			os.writeUTF(String.valueOf(ch));
		} while (ch != -1);
		fin.close();
		System.out.println(is.readUTF());

	}

	void ReceiveFile() throws Exception {
		String fileName;
		System.out.print("Enter File Name :");
		fileName = br.readLine();
		os.writeUTF(fileName);
		String msgFromServer = is.readUTF();

		if (msgFromServer.compareTo("File Not Found") == 0) {
			System.out.println("File not found on Server ...");
			return;
		} else if (msgFromServer.compareTo("READY") == 0) {
			System.out.println("Receiving File ...");
			File f = new File(fileName);
			if (f.exists()) {
				String Option;
				System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
				Option = br.readLine();
				if (Option == "N") {
					os.flush();
					return;
				}
			}
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
			System.out.println(is.readUTF());

		}

	}

	public void Menu() throws Exception {
		boolean logon = false;
		while (!logon) {
			logon = LogOn();
		}

		while (true) {
			System.out.println("#################################################");
			System.out.println("#\t\t  ~OPTIONS~\t\t\t#");
			System.out.println("#\t\t\t\t\t\t#");
			System.out.println("#\t1. Send file from client directory\t#");
			System.out.println("#\t2. Receive file from server directory\t#");
			System.out.println("#\t3. View server directory and files\t#");
			System.out.println("#\t4. Change server directory\t\t#");
			System.out.println("#\t5. Log Out\t\t\t\t#");
			System.out.println("#################################################");
			System.out.print("\nEnter a number for an action: ");
			int choice;
			choice = Integer.parseInt(br.readLine());
			System.out.print("\n\n");
			if (choice == 1) {
				os.writeUTF("SEND");
				SendFile();
				System.out.print("\n\n");
			} else if (choice == 2) {
				os.writeUTF("GET");
				ReceiveFile();
				System.out.print("\n\n");
			} else if (choice == 3) {
				os.writeUTF("DIR");
				System.out.println(is.readUTF());
				System.out.println(is.readUTF());
				System.out.print("\n\n");

			} else if (choice == 4) {
				os.writeUTF("CD");
				System.out.println("Enter new directory: ");
				os.writeUTF(br.readLine());
				System.out.println(is.readUTF());
				System.out.print("\n\n");

			} else {
				os.writeUTF("DISCONNECT");
				System.out.print("\n\n");
				System.exit(1);
			}
		}
	}
}