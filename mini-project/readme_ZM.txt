*********************************************************
*	Zach Morgan
*	10354361
*	CSCI 361
*	4/27/13
*	Mini-Project
*	Files in Package:
*	 FTPServer.java, FTPServer.class, FTPServerThread
*	 FTPClient.java, FTPClient.class, FTPClientThread
*********************************************************

*FTPServer.java, FTPServer.class, FTPServerThread
	-This file performs as an FTP Server: sends and receives files in conjunction
	with an FTP Client.

	-To run FTPServer type "java FTPServer (port)" in the command line after 
	navigating to the folder in which the .class file is located.

	-FTP Clients are now able to interact with the FTP Server.

	-Once a connection is made and a command is sent, the server is able to send files,
	receive files, change directories, or view the current directory and its files.

*FTPClient.java, FTPClient.class, FTPClientThread
	-This file performs as an FTP Client: sends and receives files in conjunction
	with an FTP Server.

	-To run FTPClient type "java FTPClient (host) (port)" in the command line after 
	navigating to the folder in which the .class file is located. This 
	should be executed with a FTP Server also running.

	-Once a connection is made, the client sends a command based on user input. Then it
	is able to send files, receive files, change directories on the server, or view the 
	current directory and its files.