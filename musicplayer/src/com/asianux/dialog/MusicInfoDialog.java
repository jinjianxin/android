package com.asianux.dialog;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.asianux.musicplayer.R;
import com.asianux.utils.Mp3Info;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.SlidingDrawer;
import android.widget.TextView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月24日 下午9:50:44
 * 类说明
 */

public class MusicInfoDialog extends Dialog {

	private String path = null;
	
	private TextView fileName = null;
	private TextView singer = null;
	private TextView album = null;
	private TextView style = null;
	private TextView length = null;
	private TextView size = null;
	private TextView format = null;
	private TextView year = null;
	private TextView channels = null;
	private TextView bit = null;
	private TextView sampleRate = null;
	private TextView pathText = null;
	
	public MusicInfoDialog(Context context,String path) {
		super(context);
		this.path = path;
		
		System.out.println(path);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_info_dialog);
		
		setTitle(R.string.music_info_title);
		
		fileName = (TextView) findViewById(R.id.music_info_file);
		singer = (TextView) findViewById(R.id.music_info_singer);
		album = (TextView) findViewById(R.id.music_info_album);
		length = (TextView) findViewById(R.id.music_length);
		size = (TextView) findViewById(R.id.music_size);
		format = (TextView) findViewById(R.id.music_format);
		year = (TextView) findViewById(R.id.music_year);
		bit = (TextView) findViewById(R.id.music_bit);
		channels = (TextView) findViewById(R.id.music_channels);
		sampleRate  =(TextView) findViewById(R.id.music_sample_rate);
		pathText = (TextView) findViewById(R.id.music_path);
		
		//style = (TextView) findViewById(R.id.music_info_style);
		
		 File file = new File(path);
		 try {
			MP3File mp3File = (MP3File) AudioFileIO.read(file);
			Tag tag = mp3File.getTag();
			
			String str;
			
			str = tag.getFirst(FieldKey.TITLE);
			
			if(str.isEmpty())
			{
				fileName.setText(R.string.noName);
			}
			else
			{
				fileName.setText(tag.getFirst(FieldKey.TITLE));
			}
			
			str = null;
			
			str = tag.getFirst(FieldKey.ARTIST);
					
			if(str.isEmpty())
			{
				singer.setText(R.string.noName);
			}
			else
			{
				singer.setText(tag.getFirst(FieldKey.ARTIST));
			}
			
			str = null;
			
			str = tag.getFirst(FieldKey.ALBUM);
			
			if(str.isEmpty())
			{
				album.setText(R.string.noName);
			}
			else
			{
				album.setText(tag.getFirst(FieldKey.ALBUM));
			}
			
			MP3AudioHeader header = mp3File.getMP3AudioHeader();
			
			Resources resource = getContext().getResources();
			
			str = null;
			
			str = resource.getString(R.string.length);
			str = String.format(str, header.getTrackLengthAsString());
			length.setText(str);
			
			str = null;
			
			str = resource.getString(R.string.size);
			str = String.format(str, file.length());
			size.setText(str);
			
			str = null;
			
			str=resource.getString(R.string.format);
			str = String.format(str, header.getFormat());
			format.setText(str);
	
			str = null;

			str = tag.getFirst(FieldKey.YEAR);
			if (str.isEmpty()) {
				str = resource.getString(R.string.year);
				str = String.format(str,resource.getString(R.string.noName));
				year.setText(str);
			} else {

				str = resource.getString(R.string.year);
				str = String.format(str, tag.getFirst(FieldKey.YEAR));
				year.setText(str);
			}
			
			str = null;
			
			str=resource.getString(R.string.bit);
			str = String.format(str, header.getBitRate());
			bit.setText(str);
			
			str = null;
			
			str=resource.getString(R.string.channels);
			str = String.format(str, header.getChannels());
			channels.setText(str);
			
			str = null;
			
			str=resource.getString(R.string.sample_rate);
			str = String.format(str, header.getSampleRate());
			sampleRate.setText(str);
			
			str = null;
			
			str=resource.getString(R.string.path);
			str = String.format(str,path);
			pathText.setText(str);
				
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
             
		
	}

}
