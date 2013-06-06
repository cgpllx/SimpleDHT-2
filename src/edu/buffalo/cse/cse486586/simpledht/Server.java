package edu.buffalo.cse.cse486586.simpledht;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


import android.content.Context;
import android.os.AsyncTask;

public class Server {
	int server_port=10000;
	Context context;
	public Server(Context c)
	{
		context=c;
	}
	public void StartServer()
	{
		ServerSocket server_socket;
		try {
			server_socket = new ServerSocket(server_port);
			new ServerThread().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, server_socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private class ServerThread extends AsyncTask<ServerSocket,Void,Void>{

		@Override
		protected Void doInBackground(ServerSocket... arg0) {
			Socket incoming_connection;
			Messageformat RcvdMSG=new Messageformat();
			try {
				while(true)
				{

					incoming_connection=arg0[0].accept();
					ObjectInputStream in_buffer=new ObjectInputStream(incoming_connection.getInputStream());
					RcvdMSG=(Messageformat)in_buffer.readObject();
					MSGHandler mh=new MSGHandler(RcvdMSG,context);
					Messageformat result=new Messageformat();
					result=mh.HandleMsg();
					if(result.Key!=null)
					{
						ObjectOutputStream out_buffer=new ObjectOutputStream(incoming_connection.getOutputStream());
						out_buffer.writeObject(result);
					}
				}
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}
