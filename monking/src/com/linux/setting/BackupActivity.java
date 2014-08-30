package com.linux.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.linux.dialog.BackupDialog;
import com.linux.monking.R;
import com.linux.utils.FileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class BackupActivity extends Activity implements OnClickListener {
	
	private TextView backupText = null;
	private TextView recoverText = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.backup_layout);
		
		backupText = (TextView) findViewById(R.id.backup_text_view);
		recoverText = (TextView) findViewById(R.id.recover_text_view);
		
		backupText.setOnClickListener(this);
		recoverText.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v == backupText)
		{
			AlertDialog.Builder builder = new Builder(this);
			 builder.setMessage("此操作将会覆盖旧数据");

			 builder.setTitle("警告");
	
			 builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
									
					BackupTask backupTask = new BackupTask(BackupActivity.this);
					backupTask.execute("backup");
					
				}
			});
			 
			 builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});

			builder.create().show();
			
		}else if(v == recoverText)
		{
			 AlertDialog.Builder builder = new Builder(this);
			 builder.setMessage("此操作将会覆盖旧数据");

			 builder.setTitle("警告");
	
			 builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
									
					BackupTask backupTask = new BackupTask(BackupActivity.this);
					backupTask.execute("recover");
					
				}
			});
			 
			 builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});

			builder.create().show();
			
		}
	}
	
	
	public class BackupTask extends AsyncTask<String, Void, Integer> {
	    private static final String COMMAND_BACKUP = "backupDatabase";
	    public static final String COMMAND_RESTORE = "restroeDatabase";
	    private Context mContext;

	    public BackupTask(Context context) {
	        this.mContext = context;
	    }
	    
	    @Override
	    protected Integer doInBackground(String... params) {
	        // TODO Auto-generated method stub
	    	
	    	File dbFile = BackupActivity.this.getDatabasePath("monking.db");
	    	FileUtils fileUtils = FileUtils.getInstance();
	    	String backUp = fileUtils.getBackupPath();
	    	
	    	if(backUp !=null)
	    	{
	            File exportDir = new File(backUp);
		        if (!exportDir.exists()) {
		            exportDir.mkdirs();
		        }
		        
		        File backup = new File(exportDir, dbFile.getName());
		        if(backup.exists())
		        {
		        	backup.delete();
		        }
		        String command = params[0];

				try {
					
					if(command.equals("backup"))
					{
						fileCopy(dbFile, backup);	
					}
					else
					{
						fileCopy(backup, dbFile);
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
			return null;	
	    }

	    private void fileCopy(File dbFile, File backup) throws IOException {
	        // TODO Auto-generated method stub
	        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
	        FileChannel outChannel = new FileOutputStream(backup).getChannel();
	        try {
	            inChannel.transferTo(0, inChannel.size(), outChannel);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } finally {
	            if (inChannel != null) {
	                inChannel.close();
	            }
	            if (outChannel != null) {
	                outChannel.close();
	            }
	        }
	    }
	}
	

}
