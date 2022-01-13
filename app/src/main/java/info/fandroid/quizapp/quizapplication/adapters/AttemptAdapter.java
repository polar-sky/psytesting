package info.fandroid.quizapp.quizapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import info.fandroid.quizapp.quizapplication.R;
import info.fandroid.quizapp.quizapplication.json.Results;

public class AttemptAdapter extends ArrayAdapter<Results> {

    private LayoutInflater inflater;
    private int layout;
    private List<Results> resultList;
    private Context mContext;

    public AttemptAdapter(Context context, int tvResourceId, List<Results> results) {
        super(context, tvResourceId, results);
        this.resultList = results;
        this.layout = tvResourceId;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }
    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvListAttempts.setText(resultList.get(position).getData_attempt() +
                " (" + resultList.get(position).getAType() + ")");
        return convertView;
    }

    public Results getItem(int position) {
        return resultList.get(position);
    }

    private class ViewHolder {
        final TextView tvListAttempts;
        ViewHolder(View view){
            tvListAttempts = view.findViewById(R.id.tvListAttempts);
        }
    }
}
