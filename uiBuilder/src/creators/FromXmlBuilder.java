package creators;

import helpers.ImageTools;
import helpers.Log;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

public class FromXmlBuilder
{
	private LayoutInflater inflater;
	private Context context;
	
	public FromXmlBuilder(Context context)
	{
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	private void insertImageToView(Bundle properties, Bundle databaseBundle, View xmlView)
	{
		
		
		String imgSrc = databaseBundle.getString(ObjectValues.IMG_SRC);
		int icnSrc = databaseBundle.getInt(ObjectValues.ICN_SRC);
		
		if(icnSrc == 0)
		{
			ImageTools.setPic(xmlView, imgSrc);
			properties.putString(ObjectValues.IMG_SRC, imgSrc);
		}
		else
		{
			((ImageView)xmlView).setImageResource(icnSrc);
			
			properties.putInt(ObjectValues.ICN_SRC, icnSrc);
		}
		
	}

	

	protected View buildGrid()
	{
		RelativeLayout xmlGridContainer = createContainer();
		
		GridView xmlGrid = (GridView) inflater.inflate(R.layout.item_gridview_layout, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		
		xmlGrid.setLayoutParams(params);
		xmlGridContainer.addView(xmlGrid);
		
		return xmlGridContainer;
	}
	
	protected View buildTimePicker()
	{
		RelativeLayout xmlTimePickerContainer = createContainer();

		TimePicker xmlTimePicker = (TimePicker) inflater.inflate(R.layout.item_timepicker_layout, null);
		xmlTimePicker.setIs24HourView(true);
		xmlTimePicker.setEnabled(false);

		RelativeLayout.LayoutParams pickerparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pickerparams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);

		xmlTimePicker.setLayoutParams(pickerparams);
		xmlTimePickerContainer.addView(xmlTimePicker);

		return xmlTimePickerContainer;
	}

	protected View buildSeekBar()
	{
		RelativeLayout xmlSeekBarContainer = createContainer();

		SeekBar xmlSeekBar = (SeekBar) inflater.inflate(R.layout.item_seekbar_layout, null);
		xmlSeekBar.setEnabled(false);
		xmlSeekBar.setActivated(true);

		RelativeLayout.LayoutParams seekBarParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		seekBarParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		xmlSeekBar.setLayoutParams(seekBarParams);
		
		xmlSeekBarContainer.addView(xmlSeekBar);

		return xmlSeekBarContainer;
	}

	protected View buildRatingBar()
	{
		RelativeLayout xmlRatingBarContainer = createContainer();

		RatingBar xmlRatingBar = (RatingBar) inflater.inflate(R.layout.item_ratingbar_layout, null);
		xmlRatingBar.setEnabled(true);
		xmlRatingBar.setActivated(true);
		
		RelativeLayout.LayoutParams ratingBarParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ratingBarParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		xmlRatingBar.setLayoutParams(ratingBarParams);

		xmlRatingBarContainer.addView(xmlRatingBar);
		
		RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		xmlRatingBarContainer.setLayoutParams(containerParams);
		
		
		return xmlRatingBarContainer;
	}
	
	protected View buildListView()
	{
		RelativeLayout container = createContainer();
		
		ListView xmlList = (ListView) inflater.inflate(R.layout.item_istview_layout, null);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		
		xmlList.setLayoutParams(params);
		container.addView(xmlList);
		
		RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		container.setLayoutParams(containerParams);
		
		return container;
	}

	protected View buildNumberPicker()
	{
		RelativeLayout xmlPickerLayout = createContainer();

		NumberPicker xmlPicker = (NumberPicker) inflater.inflate(R.layout.item_numberpicker_layout, null);

		xmlPicker.setEnabled(false);
		xmlPicker.setMaxValue(5);
		xmlPicker.setMinValue(1);
		xmlPicker.setWrapSelectorWheel(false);
		xmlPicker.setValue(3);

		RelativeLayout.LayoutParams pickerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		pickerParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		
		xmlPicker.setLayoutParams(pickerParams);
		xmlPickerLayout.addView(xmlPicker);

		return xmlPickerLayout;
	}

	protected LinearLayout buildRelativeContainer()
	{
		LinearLayout relativeLayout = new LinearLayout(context);
		relativeLayout.setBackgroundResource(R.drawable.object_background_default);
		relativeLayout.setOrientation(LinearLayout.HORIZONTAL);

		relativeLayout.setOnDragListener(new OnDragListener()
		{

			@Override
			public boolean onDrag(View v, DragEvent event)
			{
				switch (event.getAction())
				{
				case DragEvent.ACTION_DRAG_STARTED:
					Bundle tagBundle = (Bundle) v.getTag();

					int id = tagBundle.getInt(ObjectValues.TYPE);
					/*
					if (id == R.id.element_container)
					{
						return false;
					}
					return true;
*/
				case DragEvent.ACTION_DRAG_ENTERED:
					
					return true;
				case DragEvent.ACTION_DRAG_EXITED:

					return true;

				case DragEvent.ACTION_DROP:
					// Dropped, reassign View to ViewGroup
					View view = (View) event.getLocalState();
				      ViewGroup owner = (ViewGroup) view.getParent();
				      owner.removeView(view);
				      LinearLayout container = (LinearLayout) v;
				      container.addView(view);
					
					
					view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					container.addView(view);

					return true;
					
				case DragEvent.ACTION_DRAG_LOCATION:
					return true;
				case DragEvent.ACTION_DRAG_ENDED:

				default:
					break;
				}
				return true;
			}
		});
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);

		relativeLayout.setLayoutParams(params);
		return relativeLayout;

	}

	protected RelativeLayout createContainer()
	{
		RelativeLayout xmlPickerLayout = new RelativeLayout(context)
		{

			@Override
			public boolean onInterceptTouchEvent(MotionEvent ev)
			{
				Log.d("CONTAINER", "INTERCEPTING MOTION EVENT");
				return true;
			}

		};
		xmlPickerLayout.setBackgroundResource(R.drawable.object_background_default);
		xmlPickerLayout.setClickable(true);
		xmlPickerLayout.setFocusable(true);
		xmlPickerLayout.setFocusableInTouchMode(true);
		xmlPickerLayout.setEnabled(true);
		xmlPickerLayout.setMotionEventSplittingEnabled(false);
		xmlPickerLayout.setFilterTouchesWhenObscured(false);
		return xmlPickerLayout;
	}

	protected View buildSearchView()
	{
		RelativeLayout xmlSearchViewContainer = createContainer();

		SearchView xmlSearchView = (SearchView) inflater.inflate(R.layout.item_searchview_layout, null);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		xmlSearchView.setLayoutParams(params);

		//xmlSearchView.setEnabled(false);
		xmlSearchViewContainer.addView(xmlSearchView);

		return xmlSearchViewContainer;
	}

	protected View buildCheckBox() //
	{
		LinearLayout xmlCheckBox = (LinearLayout) inflater.inflate(R.layout.item_checkbox_layout, null);

		return xmlCheckBox;
	}

	protected View buildSwitch()
	{
		Switch xmlSwitch = (Switch) inflater.inflate(R.layout.item_switch_layout, null);
		xmlSwitch.setBackgroundResource(R.drawable.object_background_default);

		return xmlSwitch;
	}

	protected View buildRadioButtons()
	{

		RadioButton xmlRadioButton = (RadioButton) inflater.inflate(R.layout.item_radiobutton_layout, null);
		xmlRadioButton.setBackgroundResource(R.drawable.object_background_default);

		return xmlRadioButton;
	}

	/**
	 * Generate new TextView from xml resource
	 * 
	 * @return new TextView
	 */
	protected TextView buildTextview()
	{
		TextView xmlTextView = (TextView) inflater.inflate(R.layout.item_textview_layout, null);

		return xmlTextView;
	}

	/**
	 * Generate new ImageView from xml resource
	 * 
	 * @return the newly generated ImageView
	 */
	protected ImageView buildImageView()
	{
		ImageView xmlImageView = (ImageView) inflater.inflate(R.layout.item_imageview_layout, null);
		return xmlImageView;
	}

	/**
	 * Generate new EditText from xml resource
	 * 
	 * @return new Edittext
	 */
	protected EditText buildEditText()
	{
		EditText xmlEditText = (EditText) inflater.inflate(R.layout.item_edittext_layout, null);

		return xmlEditText;
	}

	/**
	 * Generate new Button from xml resource
	 * 
	 * @return new Button
	 */
	protected Button buildButton()
	{
		Button xmlButton = (Button) inflater.inflate(R.layout.item_button_layout, null);
		
		return xmlButton;
	}
}
