package editmodules;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.ur.rk.uibuilder.R;

public class ModuleZOrder extends Module
{
	private View box;
	
	private View requesting;
	private Button toFront;
	private Button toBack;
	
	public ModuleZOrder(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getValues()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupUi()
	{
		// TODO Auto-generated method stub
		box = super.inflater.inflate(R.layout.editmode_entry_z_order, null);
		
		toFront = (Button) box.findViewById(R.id.editmode_z_order_front);;
		toBack = (Button) box.findViewById(R.id.editmode_z_order_back);
		
		setListeners();
	}

	private void setListeners()
	{
		box.setOnClickListener(new ExpansionListener(box));
		
		toFront.setOnClickListener(new ZorderModuleListener());
		toBack.setOnClickListener(new ZorderModuleListener());	
	}

	@Override
	public View getInstance(View inProgress)
	{
		requesting = inProgress;
		
		return box;
	}

	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub

	}
	
	private class ZorderModuleListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			ViewGroup parent = (ViewGroup) requesting.getParent();

			switch (v.getId())
			{
			case R.id.editmode_z_order_back:
				
				pushToBack(parent);
				break;

			case R.id.editmode_z_order_front:
				
				bringToFront();
				break;

			default:
				break;
			}
			
			parent.invalidate();
		}

		/**
		 * @param parent
		 */
		private void pushToBack(ViewGroup parent)
		{
			parent.removeView(requesting);
			ArrayList<View> allItems = new ArrayList<View>();

			allItems.add(requesting);

			int number = parent.getChildCount();
			for (int i = 0; i < number; i++)
			{
				allItems.add(parent.getChildAt(i));
			}
			parent.removeAllViews();

			for (View child : allItems)
			{
				parent.addView(child);
			}
		}

		/**
		 * 
		 */
		private void bringToFront()
		{
			requesting.bringToFront();
		}
	}

}
