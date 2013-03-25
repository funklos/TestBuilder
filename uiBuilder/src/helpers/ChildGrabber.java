package helpers;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChildGrabber
{
	static ArrayList<View> childrenList = new ArrayList<View>();
	
	public static ArrayList<View> getChildren(View layout)
	{
		recursiveWalkThrough(layout);
		return childrenList;
	}
	
	private static void recursiveWalkThrough(View layout)
	{

		if (layout instanceof ViewGroup && layout.getTag() == null)
		{
			int count = ((ViewGroup) layout).getChildCount();

			for (int i = 0; i < count; i++)
			{
				recursiveWalkThrough(((ViewGroup) layout).getChildAt(i));

			}
		} else if (layout instanceof TextView || layout instanceof LinearLayout
				|| layout instanceof RelativeLayout
				|| layout instanceof ImageView)
		{

			childrenList.add(layout);
			
			

		}
		
	}

}
