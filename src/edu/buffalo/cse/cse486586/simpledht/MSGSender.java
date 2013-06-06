package edu.buffalo.cse.cse486586.simpledht;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class MSGSender {
	private Messageformat MSG;
	public MSGSender(Messageformat m)
	{
		MSG=m;
	}
	public void messagesender()
	{
		new Client().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, MSG);
	}
	private class Client extends AsyncTask<Messageformat,Void,Void>
	{

		@Override
		protected Void doInBackground(Messageformat... arg0) {
/*			Integer port=0;
			if(portStr.equals("5554"))
				port=11112;
			else if(portStr.equals("5556"))
				port=11108;
*/			Socket outgoing_connection;
			try {
				outgoing_connection=new Socket("10.0.2.2",arg0[0].Port);
				ObjectOutputStream out_buffer=new ObjectOutputStream(outgoing_connection.getOutputStream());
				out_buffer.writeObject(arg0[0]);
			//	out_buffer.flush();
				outgoing_connection.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}


	}
}
