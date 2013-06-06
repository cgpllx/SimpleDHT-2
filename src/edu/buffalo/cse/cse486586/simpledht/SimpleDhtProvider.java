package edu.buffalo.cse.cse486586.simpledht;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class SimpleDhtProvider extends ContentProvider {
	private Database Msgdb;
	static final int message_ID = 2;
	static final String[] ColNames={"key","value"};
	static final String PROVIDER_NAME ="edu.buffalo.cse.cse486586.simpledht.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://"+ PROVIDER_NAME);
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}
	private static final UriMatcher matchURI;
	static{
		matchURI = new UriMatcher(UriMatcher.NO_MATCH);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Messageformat InsertMsg=new Messageformat();
		String key=(String)values.get("key");
		String value=(String)values.get("value");
		SQLiteDatabase DB = Msgdb.getWritableDatabase();
		long id=0;
		try{
			if(GlobalData.MyID.equals(GlobalData.Predecessor) && GlobalData.MyID.equals(GlobalData.Successor))
			{
				//				id=DB.insertWithOnConflict(Database.TABLE_MESSAGEDB, "", values, SQLiteDatabase.CONFLICT_REPLACE);
				id=DB.insert(Database.TABLE_MESSAGEDB, "", values);
			}
			else if(GlobalData.Predecessor.equals(GlobalData.Successor))
			{
				if(genHash(GlobalData.get_port(GlobalData.Predecessor).toString()).compareTo(genHash(GlobalData.get_port(GlobalData.MyID).toString()))>0)
				{
					if(genHash(key).compareTo(genHash(GlobalData.get_port(GlobalData.Predecessor).toString()))>0)
					{
						id=DB.insertWithOnConflict(Database.TABLE_MESSAGEDB, "", values, SQLiteDatabase.CONFLICT_REPLACE);
					}
					else
					{
						InsertMsg.Key=key;
						InsertMsg.Value=value;
						InsertMsg.MSGType="insert";
						InsertMsg.from=GlobalData.MyID;
						InsertMsg.Port=GlobalData.send_port(GlobalData.Successor);
						MSGSender send=new MSGSender(InsertMsg);
						send.messagesender();
					}
				}
				else
				{
					if(genHash(key).compareTo(genHash(GlobalData.get_port(GlobalData.MyID).toString()))>0)
					{
						InsertMsg.Key=key;
						InsertMsg.Value=value;
						InsertMsg.MSGType="insert";
						InsertMsg.from=GlobalData.MyID;
						InsertMsg.Port=GlobalData.send_port(GlobalData.Successor);
						MSGSender send=new MSGSender(InsertMsg);
						send.messagesender();
					}
					else
					{
						id=DB.insertWithOnConflict(Database.TABLE_MESSAGEDB, "", values, SQLiteDatabase.CONFLICT_REPLACE);
					}
				}
			}
			else
			{
				if(genHash(GlobalData.get_port(GlobalData.Predecessor).toString()).compareTo(genHash(GlobalData.get_port(GlobalData.MyID).toString()))>0)
				{

					if((genHash(key).compareTo(genHash(GlobalData.get_port(GlobalData.Predecessor).toString()))>0)||(genHash(GlobalData.get_port(GlobalData.MyID).toString()).compareTo(genHash(key))>0))
					{
						id=DB.insertWithOnConflict(Database.TABLE_MESSAGEDB, "", values, SQLiteDatabase.CONFLICT_REPLACE);	
					}
					else
					{
						InsertMsg.Key=key;
						InsertMsg.Value=value;
						InsertMsg.MSGType="insert";
						InsertMsg.from=GlobalData.MyID;
						InsertMsg.Port=GlobalData.send_port(GlobalData.Successor);
						MSGSender send=new MSGSender(InsertMsg);
						send.messagesender();
					}

				}
				else if((genHash(GlobalData.get_port(GlobalData.MyID).toString()).compareTo(genHash(GlobalData.get_port(GlobalData.Predecessor).toString()))>0)&& (genHash(GlobalData.get_port(GlobalData.Successor).toString()).compareTo(genHash(GlobalData.get_port(GlobalData.MyID).toString()))>0))
				{
					if((genHash(GlobalData.get_port(GlobalData.MyID).toString()).compareTo(genHash(key))>0) && (genHash(key).compareTo(genHash(GlobalData.get_port(GlobalData.Predecessor).toString()))>0))
					{
						id=DB.insertWithOnConflict(Database.TABLE_MESSAGEDB, "", values, SQLiteDatabase.CONFLICT_REPLACE);
					}
					else
					{
						InsertMsg.Key=key;
						InsertMsg.Value=value;
						InsertMsg.MSGType="insert";
						InsertMsg.from=GlobalData.MyID;
						InsertMsg.Port=GlobalData.send_port(GlobalData.Successor);
						MSGSender send=new MSGSender(InsertMsg);
						send.messagesender();
					}
				}
				else
				{
					if((genHash(key).compareTo(genHash(GlobalData.get_port(GlobalData.Predecessor).toString()))>0)&&(genHash(GlobalData.get_port(GlobalData.MyID).toString()).compareTo(genHash(key))>0))
					{
						id=DB.insertWithOnConflict(Database.TABLE_MESSAGEDB, "", values, SQLiteDatabase.CONFLICT_REPLACE);
					}
					else
					{
						InsertMsg.Key=key;
						InsertMsg.Value=value;
						InsertMsg.MSGType="insert";
						InsertMsg.from=GlobalData.MyID;
						InsertMsg.Port=GlobalData.send_port(GlobalData.Successor);
						MSGSender send=new MSGSender(InsertMsg);
						send.messagesender();
					}
				}
			}
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();

		}
		return null;
	}

	@Override
	public boolean onCreate() {
		Msgdb=new Database(getContext());
		TelephonyManager tel =(TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
		String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
		if(portStr.equals("5554"))
		{
			GlobalData.MyID="AVD0";
		}
		else if (portStr.equals("5556"))
			GlobalData.MyID="AVD1";
		else if( portStr.equals("5558"))
			GlobalData.MyID="AVD2";
		try
		{
			GlobalData.Hash_AVD0=genHash("5554");
			GlobalData.Hash_AVD1=genHash("5556");
			GlobalData.Hash_AVD2=genHash("5558");
		}
		catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Server serv1=new Server(getContext());
		serv1.StartServer();
		if(!GlobalData.MyID.equals("AVD0"))
		{
			Messageformat Joinreq=new Messageformat();
			Joinreq.from=GlobalData.MyID;
			Joinreq.MSGType="JoinReq";
			Joinreq.Port=GlobalData.send_port("AVD0");
			MSGSender send=new MSGSender(Joinreq);
			send.messagesender();
		}
		else
		{
			GlobalData.Successor=(GlobalData.Predecessor="AVD0");
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionargs,String sortorder) 
	{
		// TODO Auto-generated method stub
		if(selection!=null)
		{
			String Hash_MyID=null;
			String Hash_Pred=null;
			String Hash_Succ=null;
			String Hash_Key=null;
			try
			{
				Hash_MyID=genHash(GlobalData.get_port(GlobalData.MyID).toString());
				Hash_Pred=genHash(GlobalData.get_port(GlobalData.Predecessor).toString());
				Hash_Succ=genHash(GlobalData.get_port(GlobalData.Successor).toString());
				Hash_Key=genHash(selection);
			}
			catch(NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}

			if(GlobalData.MyID.equals(GlobalData.Predecessor) && GlobalData.MyID.equals(GlobalData.Successor))
			{
				return qfunction(uri, selection);
			}
			else if(GlobalData.Predecessor.equals(GlobalData.Successor))
			{
				if(Hash_Pred.compareTo(Hash_MyID)>0)
				{
					if(Hash_Key.compareTo(Hash_Pred)>0)
					{
						return qfunction(uri, selection);
					}
					else
					{
						return qforward(uri, selection);
					}
				}
				else
				{
					if(Hash_Key.compareTo(Hash_MyID)>0)
					{
						return qforward(uri, selection);
					}
					else
					{
						return qfunction(uri, selection);
					}
				}
			}
			else
			{
				if(Hash_Pred.compareTo(Hash_MyID)>0)
				{

					if((Hash_Key.compareTo(Hash_Pred)>0)||(Hash_MyID.compareTo(Hash_Key)>0))
					{
						return qfunction(uri, selection);
					}
					else
					{
						return qforward(uri, selection);
					}

				}
				else if((Hash_MyID.compareTo(Hash_Pred)>0)&& (Hash_Succ.compareTo(Hash_MyID)>0))
				{
					if((Hash_MyID.compareTo(Hash_Key)>0) && (Hash_Key.compareTo(Hash_Pred)>0))
					{
						return qfunction(uri, selection);
					}
					else
					{
						return qforward(uri, selection);
					}
				}
				else
				{
					if((Hash_Key.compareTo(Hash_Pred)>0)&&(Hash_MyID.compareTo(Hash_Key)>0))
					{
						return qfunction(uri, selection);
					}
					else
					{
						return qforward(uri, selection);
					}
				}
			}
		}
		else
		{
			return qfunction(uri,null);
		}
	}
	public Cursor qfunction(Uri uri,String selection)
	{
		SQLiteDatabase DB = Msgdb.getWritableDatabase();
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(Database.TABLE_MESSAGEDB);
		if (matchURI.match(uri) == message_ID)
			sqlBuilder.appendWhere(Database.key + " = " + uri.getPathSegments().get(1));
		if(selection!=null)
		{
			selection = "KEY = '"+selection+"'";
			Cursor c = sqlBuilder.query(
					DB,
					null,
					selection,
					null,
					null,
					null,
					null);
			return c;
		}
		else
		{
			Cursor c = sqlBuilder.query(
					DB,
					null,
					null,
					null,
					null,
					null,
					null);
			return c;
		}
	}
	private Cursor qforward(Uri uri,String selection)
	{
		Messageformat QueryMSG=new Messageformat();
		Messageformat QueryRes=new Messageformat();
		QueryMSG.Key=selection;
		QueryMSG.Value=null;
		QueryMSG.MSGType="query";
		QueryMSG.from=GlobalData.MyID;
		QueryMSG.Port=GlobalData.send_port(GlobalData.Successor);
		try{
			Socket outgoing=new Socket("10.0.2.2",GlobalData.send_port(GlobalData.Successor));
			ObjectOutputStream out_buffer=new ObjectOutputStream(outgoing.getOutputStream());
			out_buffer.writeObject(QueryMSG);
			ObjectInputStream in_buffer=new ObjectInputStream(outgoing.getInputStream());
			QueryRes=(Messageformat)in_buffer.readObject();
			String[] response={QueryRes.Key,QueryRes.Value};
			MatrixCursor mc=new MatrixCursor(ColNames);
			mc.addRow(response);
			outgoing.close();
			return mc;	
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
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private String genHash(String input) throws NoSuchAlgorithmException {
		MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		byte[] sha1Hash = sha1.digest(input.getBytes());
		Formatter formatter = new Formatter();
		for (byte b : sha1Hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}
}
