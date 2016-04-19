package com.example.adrien.lolhelper.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrien.lolhelper.R;

import java.util.List;

/**
 * Created by Adrien CESARO on 15/03/2016.
 */
public class PchampionExpendableList extends BaseExpandableListAdapter{
    private final String TAG = "PchampionExpendableList";
    private Context context;
    private List<PChampHeader> list;
    private LayoutInflater inflater;

    public PchampionExpendableList(Context context, List<PChampHeader> list){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i(TAG, context.getPackageName());
    }


    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return list.get(groupPosition).Child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.profilechamp_explistview, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.champ = (ImageView) convertView.findViewById(R.id.pc_chmp);
            viewHolder.champtxt = (TextView) convertView.findViewById(R.id.pc_champtxt);
            viewHolder.kda = (TextView) convertView.findViewById(R.id.pc_kda);
            viewHolder.win= (TextView) convertView.findViewById(R.id.pc_win);
        }

        PChampHeader pChampHeader = (PChampHeader) getGroup(groupPosition);

        if(pChampHeader.pcIcon!=null) {
            viewHolder.champ.setImageBitmap(pChampHeader.pcIcon);
        }else{
            viewHolder.champ.setImageResource(R.drawable.blank);
        }
        viewHolder.kda.setText(""+pChampHeader.pcKda);
        viewHolder.win.setText(""+pChampHeader.pcwin);
        viewHolder.champtxt.setText(""+pChampHeader.idChamp);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = inflater.inflate(R.layout.profilechamp_child, parent, false);
            }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.Child = (TextView) convertView.findViewById(R.id.pc_child);
        }

        PChampHeader pChampHeader = (PChampHeader) getGroup(groupPosition);

        viewHolder.Child.setText(pChampHeader.Child.toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public class ViewHolder{
        ImageView champ;
        TextView kda;
        TextView win;
        TextView champtxt;
        TextView Child;
    }

}

