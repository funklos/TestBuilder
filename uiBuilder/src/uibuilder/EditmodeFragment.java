package uibuilder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import de.ur.rk.uibuilder.R;

public class EditmodeFragment extends Fragment implements OnClickListener
{
	private View layoutView;
	private LinearLayout layout;
	private LayoutInflater inflater;
	


	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.inflater = inflater;

		layout = (LinearLayout) getActivity().findViewById(R.id.fragment_sidebar);
		adaptLayoutToContext();

		layoutView = inflater.inflate(R.layout.layout_editmode_fragment, container, false);
		layoutView.setOnClickListener(this);

		return layoutView;
	}

	private void adaptLayoutToContext()
	{

		layout.addView(inflater.inflate(R.layout.editmode_entry_enter_text, null));
		layout.addView(inflater.inflate(R.layout.editmode_entry_align_content, null));
		layout.addView(inflater.inflate(R.layout.editmode_entry_item_count, null));
	}


	

	@Override
	public void onClick(View v)
	{
		
			switch (v.getId())
			{
			
			}

		
		
	}
}