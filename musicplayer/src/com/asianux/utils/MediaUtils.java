package com.asianux.utils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.asianux.musicplayer.R;

import android.R.bool;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

public class MediaUtils {

	private static final Uri albumArtUri = Uri
			.parse("content://media/external/audio/albumart");

	public MediaUtils() {
		super();
	}

	public static List<Mp3Info> getMp3Info(Context context) {

		List<Mp3Info> list = new ArrayList<Mp3Info>();

		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();

			Mp3Info mp3Info = new Mp3Info();

			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));

			String title = cursor.getString((cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE)));

			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));

			String album = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM));

			long albumId = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));

			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE));

			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));

			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

			if (isMusic != 0) {
				mp3Info.setId(id);
				mp3Info.setTitle(title);
				mp3Info.setArtist(artist);
				mp3Info.setAlbum(album);
				mp3Info.setAlbumId(albumId);
				mp3Info.setDuration(duration);
				mp3Info.setSize(size);
				mp3Info.setUrl(url);
				list.add(mp3Info);
			}
		}

		return list;

	}

	public static String formatTime(long time) {
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}

	public static Bitmap getMusicAlbum(Context context, long song_id,
			long album_id, Boolean allowdefalut) {

		System.out.println("--------------------------------------\n");

		if (album_id < 0) {
			if (song_id < 0) {
				Bitmap bm = getArtworkFromFile(context, song_id, -1);
				if (bm != null) {
					return bm;
				}
			}

			if (allowdefalut) {
				return getDefaultArtwork(context);
			}
			return null;
		}

		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(albumArtUri, album_id);
		if (uri != null) {
			InputStream in = null;
			try {
				in = res.openInputStream(uri);
				BitmapFactory.Options options = new BitmapFactory.Options();
				// 先制定原始大小
				options.inSampleSize = 1;
				// 只进行大小判断
				options.inJustDecodeBounds = true;
				// 调用此方法得到options得到图片的大小
				BitmapFactory.decodeStream(in, null, options);

				options.inSampleSize = computeSampleSize(options, 600);

				// 我们得到了缩放比例，现在开始正式读入Bitmap数据
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				in = res.openInputStream(uri);
				return BitmapFactory.decodeStream(in, null, options);
			} catch (FileNotFoundException e) {
				Bitmap bm = getArtworkFromFile(context, song_id, album_id);
				if (bm != null) {
					if (bm.getConfig() == null) {
						bm = bm.copy(Bitmap.Config.RGB_565, false);
						if (bm == null && allowdefalut) {
							return getDefaultArtwork(context);
						}
					}
				} else if (allowdefalut) {
					bm = getDefaultArtwork(context);
				}
				return bm;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private static int computeSampleSize(Options options, int target) {

		// TODO Auto-generated method stub

		int w = options.outWidth;
		int h = options.outHeight;
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0) {
			return 1;
		}
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target) {
				candidate -= 1;
			}
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target) {
				candidate -= 1;
			}
		}
		return candidate;
	}

	private static Bitmap getDefaultArtwork(Context context) {
		// TODO Auto-generated method stub

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;

		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(R.drawable.defaultalbum), null, opts);
	}

	private static Bitmap getArtworkFromFile(Context context, long songid,
			long albumid) {
		// TODO Auto-generated method stub

		Bitmap bm = null;
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			FileDescriptor fd = null;
			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();
				}
			} else {
				Uri uri = ContentUris.withAppendedId(albumArtUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					fd = pfd.getFileDescriptor();
				}
			}
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			options.inSampleSize = 100;
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bm;
	}

}