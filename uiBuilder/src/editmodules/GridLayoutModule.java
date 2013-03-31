package editmodules;

import uibuilder.EditmodeFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import data.ObjectValues;
import data.SampleAdapter;
import de.ur.rk.uibuilder.R;

public class GridLayoutModule extends Module
{
	private LinearLayout box;
	private View requesting;
	
	LinearLayout 
			layoutTypeOne,
			layoutTypeTwo,
			layoutTypeThree,
			layoutTypeFour;
	
	private SampleAdapter samples;
	
	public GridLayoutModule(EditmodeFragment context)
	{
		super(context);
		samples = new SampleAdapter(super.context);
	}

	@Override
	public void getValues()
	{
		// TODO Auto-generated method stub
		adaptToContext();
	}

	@Override
	protected void setupUi()
	{
		// TODO Auto-generated method stub
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_choose_grid_config, null);
		
		layoutTypeOne = (LinearLayout) box.findViewById(R.id.editmode_grid_included_layout_1);
		layoutTypeTwo = (LinearLayout) box.findViewById(R.id.editmode_grid_included_layout_2);
		layoutTypeThree = (LinearLayout) box.findViewById(R.id.editmode_grid_included_layout_3);
		layoutTypeFour = (LinearLayout) box.findViewById(R.id.editmode_grid_included_layout_4);
	}

	/**
	 * 
	 */
	@Override
	protected void setListeners()
	{
		GridLayoutModuleListener gridLayoutListener = new GridLayoutModuleListener();
		box.setOnClickListener(new ExpansionListener(box));

		layoutTypeOne.setOnClickListener(gridLayoutListener);
		layoutTypeTwo.setOnClickListener(gridLayoutListener);
		layoutTypeThree.setOnClickListener(gridLayoutListener);
		layoutTypeFour.setOnClickListener(gridLayoutListener);
	}

	@Override
	public LinearLayout getInstance(View inProgress)
	{
		requesting = inProgress;
		adaptToContext();
		
		return box;
	}

	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub

	}
	
	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class GridLayoutModuleListener implements OnClickListener
	{
	

		@Override
		public void onClick(View gridLayout)
		{
			Bundle objectBundle = (Bundle) requesting.getTag();
			int id = gridLayout.getId();

			switch (id)
			{
			case R.id.editmode_grid_included_layout_1:
			case R.id.editmode_grid_included_layout_2:
			case R.id.editmode_grid_included_layout_3:
			case R.id.editmode_grid_included_layout_4:
				samples.setSampleLayout(requesting, id);
//				objectBundle.putInt(ObjectValues.EXAMPLE_LAYOUT, id);
				break;

			default:
				break;
			}
		}
	}
}
