package com.asianux.adapter;

import java.util.ArrayList;
import java.util.List;

import com.asianux.musicplayer.ListActivity;
import com.asianux.musicplayer.R;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	View[] itemView;
	Context context;

	public ListAdapter(Context context, List<Mp3Info> list) {
		// TODO Auto-generated constructor stub

		this.context = context;

		itemView = new View[list.size()];
		int i = 0;
		for (Mp3Info mp3Info : list) {

			itemView[i] = MakeItemView(mp3Info);
			i++;

		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemView.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null)
			return itemView[position];

		return convertView;
	}

	/**
	 * @param mp3Info
	 * @return
	 */
	public View MakeItemView(Mp3Info mp3Info) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.listitemview, null);

		ImageView imageView = (ImageView) itemView.findViewById(R.id.albumImage);
		imageView.setImageResource(R.drawable.defaultalbum);
		
/*		Bitmap map = MediaUtils.getMusicAlbum(context, mp3Info.getId(), mp3Info.getAlbumId(), true);
		imageView.setImageBitmap(map);*/

		TextView fileName = (TextView) itemView.findViewById(R.id.fileName);
		fileName.setText(mp3Info.getTitle());

		TextView signerName = (TextView) itemView.findViewById(R.id.signerName);
		signerName.setText(mp3Info.getArtist());

		TextView duringTime = (TextView) itemView.findViewById(R.id.duringTime);
		duringTime.setText(MediaUtils.formatTime(mp3Info.getDuration()));

		ImageButton collectButton = (ImageButton) itemView
				.findViewById(R.id.collectButton);

		collectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		return itemView;

	}

}
