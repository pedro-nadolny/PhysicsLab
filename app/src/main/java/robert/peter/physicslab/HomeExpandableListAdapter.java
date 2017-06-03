package robert.peter.physicslab;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class HomeExpandableListAdapter implements ExpandableListAdapter {

    private Context _context;
    private List<String> header; // header titles
    private HashMap<String, List<String>> child;

    private final int groupLayout = R.layout.home_list_group;
    private final int childLayout = R.layout.home_list_item;

    public HomeExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this._context = context;
        this.header = listDataHeader;
        this.child = listChildData;
    }

    @Override
    public int getGroupCount() {
        // Get header size
        return this.header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // return children count
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // Get header position
        return this.header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // This will return the child
        return this.child.get(this.header.get(groupPosition)).get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(groupLayout, parent, false);
        }

        final String headerTitle = (String) getGroup(groupPosition);
        TextView header_text = (TextView) convertView.findViewById(R.id.header);
        header_text.setText(headerTitle);

        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.indicator);

        if (isExpanded) {
            header_text.setTextColor(ContextCompat.getColor(_context, R.color.colorPrimaryDark));
            header_text.setTypeface(null, Typeface.BOLD);
            groupIndicator.setImageResource(R.drawable.arrow_down);

        } else {
            header_text.setTextColor(ContextCompat.getColor(_context, android.R.color.black));
            header_text.setTypeface(null, Typeface.NORMAL);
            groupIndicator.setImageResource(R.drawable.arrow_right);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(childLayout, parent, false);
        }

        final String childText = (String) getChild(groupPosition, childPosition);
        TextView child_text = (TextView) convertView.findViewById(R.id.child);
        child_text.setText(childText);

        return convertView;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() { return false; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {}

    @Override
    public void onGroupExpanded(int groupPosition) {}
}