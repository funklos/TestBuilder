package helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import de.ur.rk.uibuilder.R;

/**
 * taken from @autor android developers photobyintent example, class extracted from activity
 * and modified to include gallery intent to save images and mail intent to share via email
 * @author funklos
 *
 */
public class ImageTools
{
	private Context c;
	
	private Uri path;
	private int screenId;
	private String photoPath;
	private static final String JPEG_FILE_PREFIX = "UI_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private AlbumDirFactory storageFactory = null;
	
	public static final int CAMERA = 1;
	public static final int GALLERY = 2;
	public static final int CROP = 3; //NOT IN USE BECAUSE THIS FUNCTION IS DEVICE SPECIFIC
	public static final int SHARE = 4;

	
	public ImageTools(Context c)
	{
		this.c = c;
		storageFactory = new AlbumDirFactory();
	}
	
	/**
	 * @autor android developers photobyintent example MODIFIED by @author funklos
	 * @param which
	 * @return
	 */
	public Intent getIntent(int which)
	{
		switch (which)
		{
		case CAMERA:
			
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

			File f = null;

			try
			{
				f = setUpPhotoFile(ALBUM);
				photoPath = f.getAbsolutePath();
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e)
			{
				e.printStackTrace();
				f = null;
				photoPath = null;
			}
			
			return cameraIntent;
			
		case GALLERY:
			
			Intent galleryIntent = new Intent();
			galleryIntent.setType("image/*");
			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			
			return galleryIntent;
			
		case SHARE:
			
			Intent mailIntent = new Intent(Intent.ACTION_SEND);
			mailIntent.putExtra(Intent.EXTRA_SUBJECT, c.getString(R.string.intent_share_extra_subject));
			mailIntent.putExtra(Intent.EXTRA_TEXT, c.getString(R.string.intent_share_extra_text));
			mailIntent.setType("image/png");
			
			return mailIntent;
		default:
			break;
		}
		return null;
		
		

	}
	/**
	 * @autor android developers photobyintent example MODIFIED by @author funklos
	 * @param destination
	 */
	public void handleBigCameraPhoto(View destination)
	{

		if (photoPath != null)
		{
			setPic(destination);
			galleryAddPic();
			photoPath = null;
		}

	}

	/**
	 * @autor android developers photobyintent example MODIFIED by @author funklos
	 * @param destination
	 * @param data
	 */
	public void handleGalleryImport(View destination, Intent data)
	{
		path = data.getData();
		photoPath = getPath(path);
		
		if (photoPath != null)
		{
			setPic(destination);
		}
	}
	/**
	 * @author funklos
	 * @param root
	 * @param cres
	 */
	private Uri tempUri;
	public Uri requestBitmap(View root, ContentResolver cres, boolean insert, boolean intern, int screenId)
	{
		RelativeLayout content = (RelativeLayout) root.findViewById(R.id.design_area);

		Bitmap image = Bitmap.createBitmap(content.getWidth(), content.getHeight(), Bitmap.Config.ARGB_8888);
		content.draw(new Canvas(image));
		
		File f;
		String photoUri = null;
		
		if (intern == false)
		{
			
			try
			{
				f = setUpPhotoFile(ALBUM);
				photoPath = f.getAbsolutePath();
				
				FileOutputStream out = new FileOutputStream(f);
	
				image.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				
				//HUNDLING
				photoUri = MediaStore.Images.Media.insertImage(
				         cres, photoPath, null, null);
				
				
			} catch (IOException e)
			{
				e.printStackTrace();
				f = null;
				photoPath = null;
			}
		}
		else
		{	
			
			try
			{
				this.screenId = screenId;
				
				f = setUpPhotoFile(PREVIEW);
				
				FileOutputStream fos = new FileOutputStream(f);
				fos = new FileOutputStream(f);
				photoPath = f.getAbsolutePath();

				image.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				
				
				Uri screenUri = Uri.parse(photoPath);
				photoPath = null;
				
				return screenUri;
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		if (insert)
			galleryAddPic();
		
		tempUri = Uri.parse(photoPath);
		photoPath = null;
		return Uri.parse(photoUri);
	}

	/**
	 * @autor android developers photobyintent example MODIFIED by @author funklos
	 * @param v the target to receive the picture
	 */
	private void setPic(View v)
	{
		setPic(v, photoPath);
		/* Add the imagepath to the tagBundle */
		
		Bundle tagBundle = (Bundle) v.getTag();
		tagBundle.putString(ObjectValues.IMG_SRC, photoPath);
		tagBundle.putInt(ObjectValues.ICN_SRC, 0);
	}

	public static void setPic(View v, String path)
	{
		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		
		if (path != null)
		{
			int targetW = ((ImageView) v).getWidth();
			int targetH = ((ImageView) v).getHeight();
	
			/* Get the size of the image */
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;
	
			/* Figure out which way needs to be reduced less */
			int scaleFactor = 1;
			if ((targetW > 0) || (targetH > 0))
			{
				scaleFactor = Math.min(photoW / targetW, photoH / targetH);
			}
	
			/* Set bitmap options to scale the image decode target */
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;
	
			/* Decode the JPEG file into a Bitmap */
			Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
			
			
	
			/* Associate the Bitmap to the ImageView */
			((ImageView) v).setImageBitmap(bitmap);
		}
	}

	/**
	 * Approach from stackoverflow.com, but slightly modified to use the
	 * contentResolver instead of the deprecated managedQuery.
	 * 
	 * @author Zelimir from "stackoverflow" src
	 *         http://stackoverflow.com/questions
	 *         /4859011/not-able-to-pick-photo-from-gallery?rq=1
	 * @param uri
	 * @return
	 */

	private String getPath(Uri uri)
	{
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = c.getApplicationContext().getContentResolver().query(uri, projection, null, null, null);

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}
	
	/**
	 * @autor android developers photobyintent example
	 */
	private void galleryAddPic()
	{
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(photoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		c.getApplicationContext().sendBroadcast(mediaScanIntent);
	}

	/**
	 * @autor android developers photobyintent example
	 */
	private String getAlbumName()
	{
		return c.getString(R.string.album_name);
	}
	
	private String getPreviewFolder()
	{
		return "previews";
	}

	/**
	 * @autor android developers photobyintent example
	 */
	private static final int ALBUM = 0x00, PREVIEW = 0x01;
	
	
	private File getAlbumDir(int which)
	{
		File storageDir = null;
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			switch (which)
			{
			case ALBUM:
				storageDir = storageFactory.getAlbumStorageDir(getAlbumName());
				break;

			case PREVIEW:
				storageDir = storageFactory.getPreviewStorageDir(getPreviewFolder());
			}
			

			if (storageDir != null)
			{
				if (!storageDir.mkdirs())
				{
					if (!storageDir.exists())
					{
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else
		{
			Log.v(c.getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}
	
	/**
	 * 
	 * @author funklos modified to handle multiple paths
	 * @autor android developers photobyintent example
	 */
	private File setUpPhotoFile(int which) throws IOException
	{

		File f = createImageFile(which);
		photoPath = f.getAbsolutePath();

		return f;
	}

	/**
	 * @author funklos modified to handle multiple paths
	 * @autor android developers photobyintent example
	 */
	private File createImageFile(int which) throws IOException
	{
		String imageFileName = "";
		
		switch (which)
		{
		case ALBUM:
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
			break;

		case PREVIEW:
			imageFileName = JPEG_FILE_PREFIX + screenId;

		}
		File albumF = getAlbumDir(which);
		File f = new File(albumF, imageFileName + JPEG_FILE_SUFFIX);

		return f;
	}


}
