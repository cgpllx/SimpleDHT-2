package edu.buffalo.cse.cse486586.simpledht;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OnGDumpClickListener implements OnClickListener{
	private final TextView mTextView;
	private final ContentResolver mContentResolver;
	private final Uri mUri;
	private static final String KEY_FIELD = "key";
	private static final String VALUE_FIELD = "value";
	public OnGDumpClickListener(TextView _tv, ContentResolver _cr) {
		mTextView = _tv;
		mContentResolver = _cr;
		mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
		//		mContentValues = initTestValues();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private Uri buildUri(String scheme, String authority) {
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.authority(authority);
		uriBuilder.scheme(scheme);
		return uriBuilder.build();
	}
	private class Task extends AsyncTask<Void, String, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Cursor resultCursor = mContentResolver.query(mUri, null,null, null, null);
			int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
			int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
			Messageformat gdump=new Messageformat();
			Messageformat result=new Messageformat();
			if (!(keyIndex == -1 || valueIndex == -1) && (resultCursor!=null) && resultCursor.moveToFirst()) 
			{
				String returnKey = resultCursor.getString(keyIndex);
				String returnValue = resultCursor.getString(valueIndex);
				publishProgress("<"+returnKey+","+returnValue+">\n");
				while(!resultCursor.isLast())
				{
					resultCursor.moveToNext();
					returnKey = resultCursor.getString(keyIndex);
					returnValue = resultCursor.getString(valueIndex);
					publishProgress("<"+returnKey+","+returnValue+">\n");
				}
				resultCursor.close();			
			}
			gdump.MSGType="gdump";
			try{
				if(!GlobalData.Predecessor.equals(GlobalData.MyID))
				{
					Socket outgoing1=new Socket("10.0.2.2",GlobalData.send_port(GlobalData.Predecessor));
					ObjectOutputStream out_buffer1=new ObjectOutputStream(outgoing1.getOutputStream());
					out_buffer1.writeObject(gdump);
					ObjectInputStream in_buffer=new ObjectInputStream(outgoing1.getInputStream());
					result=(Messageformat)in_buffer.readObject();
					for(int i=0;i<result.gdump.length;i++)
						publishProgress("<"+result.gdump[i][0]+","+result.gdump[i][1]+">\n");
					outgoing1.close();
				}
				if(!GlobalData.Predecessor.equals(GlobalData.Successor))
				{
					Socket outgoing2=new Socket("10.0.2.2",GlobalData.send_port(GlobalData.Successor));
					ObjectOutputStream out_buffer2=new ObjectOutputStream(outgoing2.getOutputStream());
					out_buffer2.writeObject(gdump);
					ObjectInputStream in_buffer2=new ObjectInputStream(outgoing2.getInputStream());
					result=(Messageformat)in_buffer2.readObject();
					for(int i=0;i<result.gdump.length;i++)
						publishProgress("<"+result.gdump[i][0]+","+result.gdump[i][1]+">\n");
					outgoing2.close();
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		protected void onProgressUpdate(String...strings) {
			mTextView.append(strings[0]);

			return;
		}
	}
}
