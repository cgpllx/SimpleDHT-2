package edu.buffalo.cse.cse486586.simpledht;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.content.Context;
import android.database.Cursor;

public class MSGHandler {
	private Messageformat MSG;
	Context context;
	private static final String KEY_FIELD = "key";
	private static final String VALUE_FIELD = "value";
	private  Uri URI;
	ContentResolver cr;
	public MSGHandler(Messageformat m,Context c)
	{
		MSG=m;
		context=c;
		cr=context.getContentResolver();
	}
	@SuppressWarnings("null")
	public Messageformat HandleMsg()
	{
		Messageformat result=new Messageformat();
		result.Key=null;
		if(GlobalData.MyID.equals("AVD0"))
		{
			if(MSG.MSGType.equals("JoinReq"))
			{
				if(GlobalData.Predecessor.equals(GlobalData.MyID)&& GlobalData.Predecessor.equals(GlobalData.Successor))
				{
					GlobalData.Predecessor=(GlobalData.Successor=MSG.from);
					Messageformat Reply=new Messageformat();
					Reply.from="AVD0";
					Reply.MSGType="JoinRes";
					Reply.Port=GlobalData.send_port(MSG.from);
					Reply.Predecessor="AVD0";
					Reply.Successor="AVD0";
					MSGSender send=new MSGSender(Reply);
					send.messagesender();
				}
				else if(GlobalData.Predecessor.equals(GlobalData.Successor))
				{
					Messageformat Reply1=new Messageformat();
					Messageformat Reply2=new Messageformat();
					if(GlobalData.Hash_AVD1.compareTo(GlobalData.Hash_AVD2)>0)
					{
						if(GlobalData.Hash_AVD0.compareTo(GlobalData.Hash_AVD1)>0)
						{
							GlobalData.Predecessor="AVD1";
							GlobalData.Successor="AVD2";
							Reply1.from="AVD0";
							Reply1.MSGType="JoinRes";
							Reply1.Port=GlobalData.send_port("AVD1");
							Reply1.Predecessor="AVD2";
							Reply1.Successor="AVD0";
							MSGSender send1=new MSGSender(Reply1);
							send1.messagesender();
							Reply2.from="AVD0";
							Reply2.MSGType="JoinRes";
							Reply2.Port=GlobalData.send_port("AVD2");
							Reply2.Predecessor="AVD0";
							Reply2.Successor="AVD1";
							MSGSender send2=new MSGSender(Reply2);
							send2.messagesender();
						}
						else if(GlobalData.Hash_AVD0.compareTo(GlobalData.Hash_AVD2)>0)
						{
							GlobalData.Predecessor="AVD2";
							GlobalData.Successor="AVD1";
							Reply1.from="AVD0";
							Reply1.MSGType="JoinRes";
							Reply1.Port=GlobalData.send_port("AVD1");
							Reply1.Predecessor="AVD0";
							Reply1.Successor="AVD2";
							MSGSender send1=new MSGSender(Reply1);
							send1.messagesender();
							Reply2.from="AVD0";
							Reply2.MSGType="JoinRes";
							Reply2.Port=GlobalData.send_port("AVD2");
							Reply2.Predecessor="AVD1";
							Reply2.Successor="AVD0";
							MSGSender send2=new MSGSender(Reply2);
							send2.messagesender();							
						}
						else
						{
							GlobalData.Predecessor="AVD1";
							GlobalData.Successor="AVD2";
							Reply1.from="AVD0";
							Reply1.MSGType="JoinRes";
							Reply1.Port=GlobalData.send_port("AVD1");
							Reply1.Predecessor="AVD2";
							Reply1.Successor="AVD0";
							MSGSender send1=new MSGSender(Reply1);
							send1.messagesender();
							Reply2.from="AVD0";
							Reply2.MSGType="JoinRes";
							Reply2.Port=GlobalData.send_port("AVD2");
							Reply2.Predecessor="AVD0";
							Reply2.Successor="AVD1";
							MSGSender send2=new MSGSender(Reply2);
							send2.messagesender();	
						}
					}
					else
					{
						if(GlobalData.Hash_AVD0.compareTo(GlobalData.Hash_AVD2)>0)
						{
							GlobalData.Predecessor="AVD2";
							GlobalData.Successor="AVD1";
							Reply1.from="AVD0";
							Reply1.MSGType="JoinRes";
							Reply1.Port=GlobalData.send_port("AVD1");
							Reply1.Predecessor="AVD0";
							Reply1.Successor="AVD2";
							MSGSender send1=new MSGSender(Reply1);
							send1.messagesender();
							Reply2.from="AVD0";
							Reply2.MSGType="JoinRes";
							Reply2.Port=GlobalData.send_port("AVD2");
							Reply2.Predecessor="AVD1";
							Reply2.Successor="AVD0";
							MSGSender send2=new MSGSender(Reply2);
							send2.messagesender();	
						}
						else if(GlobalData.Hash_AVD0.compareTo(GlobalData.Hash_AVD1)>0)
						{
							GlobalData.Predecessor="AVD1";
							GlobalData.Successor="AVD2";
							Reply1.from="AVD0";
							Reply1.MSGType="JoinRes";
							Reply1.Port=GlobalData.send_port("AVD1");
							Reply1.Predecessor="AVD2";
							Reply1.Successor="AVD0";
							MSGSender send1=new MSGSender(Reply1);
							send1.messagesender();
							Reply2.from="AVD0";
							Reply2.MSGType="JoinRes";
							Reply2.Port=GlobalData.send_port("AVD2");
							Reply2.Predecessor="AVD0";
							Reply2.Successor="AVD1";
							MSGSender send2=new MSGSender(Reply2);
							send2.messagesender();	
						}
						else
						{
							GlobalData.Predecessor="AVD2";
							GlobalData.Successor="AVD1";
							Reply1.from="AVD0";
							Reply1.MSGType="JoinRes";
							Reply1.Port=GlobalData.send_port("AVD1");
							Reply1.Predecessor="AVD0";
							Reply1.Successor="AVD2";
							MSGSender send1=new MSGSender(Reply1);
							send1.messagesender();
							Reply2.from="AVD0";
							Reply2.MSGType="JoinRes";
							Reply2.Port=GlobalData.send_port("AVD2");
							Reply2.Predecessor="AVD1";
							Reply2.Successor="AVD0";
							MSGSender send2=new MSGSender(Reply2);
							send2.messagesender();		

						}
					}
				}
			}
			else if(MSG.MSGType.equals("insert"))
			{

				URI = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
				ContentValues cv=new ContentValues();
				cv.put(KEY_FIELD,MSG.Key);
				cv.put(VALUE_FIELD, MSG.Value);
				cr.insert(URI, cv);		
			}
			else if(MSG.MSGType.equals("query"))
			{
				URI = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
				Cursor resultCursor = cr.query(URI, null,MSG.Key, null, null);
				if (resultCursor != null)
				{
					int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
					int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
					resultCursor.moveToFirst();
					result.Key=resultCursor.getString(keyIndex);
					result.Value = resultCursor.getString(valueIndex);
					result.from=GlobalData.MyID;
					resultCursor.close();
				}
			}
			else if(MSG.MSGType.equals("gdump"))
			{
				String[][] resultset=null;
				URI = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
				Cursor resultCursor = cr.query(URI, null,null, null, null);
				int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
				int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
				if (!(keyIndex == -1 || valueIndex == -1) && (resultCursor!=null) && resultCursor.moveToFirst()) 
				{
					resultset=new String[resultCursor.getCount()][2];
					String returnKey = resultCursor.getString(keyIndex);
					String returnValue = resultCursor.getString(valueIndex);
					resultset[0][0]=returnKey;
					resultset[0][1]=returnValue;
					int i=0;
					while(!resultCursor.isLast())
					{
						i++;
						resultCursor.moveToNext();
						returnKey = resultCursor.getString(keyIndex);
						returnValue = resultCursor.getString(valueIndex);
						resultset[i][0]=returnKey;
						resultset[i][1]=returnValue;	
					}
					resultCursor.close();
					result.Key="key0";
					result.gdump=resultset;
				}
			}
		}
		else
		{
			if(MSG.MSGType.equals("JoinRes"))
			{
				GlobalData.Predecessor=MSG.Predecessor;
				GlobalData.Successor=MSG.Successor;

			}
			else if(MSG.MSGType.equals("insert"))
			{
				URI = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
				ContentValues cv=new ContentValues();
				cv.put(KEY_FIELD,MSG.Key);
				cv.put(VALUE_FIELD, MSG.Value);
				cr.insert(URI, cv);	

			}
			else if(MSG.MSGType.equals("query"))
			{
				URI = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
				Cursor resultCursor = cr.query(URI, null,MSG.Key, null, null);
				if (resultCursor != null)
				{
					int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
					int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
					resultCursor.moveToFirst();
					result.Key=resultCursor.getString(keyIndex);
					result.Value = resultCursor.getString(valueIndex);
					result.from=GlobalData.MyID;
				}
				resultCursor.close();
			}
			else if(MSG.MSGType.equals("gdump"))
			{
				String[][] resultset=null;
				URI = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
				Cursor resultCursor = cr.query(URI, null,null, null, null);
				int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
				int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
				if (!(keyIndex == -1 || valueIndex == -1) && (resultCursor!=null) && resultCursor.moveToFirst()) 
				{
					resultset=new String[resultCursor.getCount()][2];
					String returnKey = resultCursor.getString(keyIndex);
					String returnValue = resultCursor.getString(valueIndex);
					resultset[0][0]=returnKey;
					resultset[0][1]=returnValue;
					int i=0;
					while(!resultCursor.isLast())
					{
						i++;
						resultCursor.moveToNext();
						returnKey = resultCursor.getString(keyIndex);
						returnValue = resultCursor.getString(valueIndex);
						resultset[i][0]=returnKey;
						resultset[i][1]=returnValue;	
					}
					resultCursor.close();
					result.Key="key0";
					result.gdump=resultset;
				}
			}

		}
		return result;

	}
	private Uri buildUri(String scheme, String authority) {
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.authority(authority);
		uriBuilder.scheme(scheme);
		return uriBuilder.build();
	}

}
