package com.example.vacationplanner.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplanner.R;
import com.example.vacationplanner.entities.Vacation;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Vacation> mReportVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public ReportAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

     public class ReportViewHolder extends RecyclerView.ViewHolder{
        private final TextView reportTitle;
        private final TextView reportHotel;
        private final TextView reportStart;
        private final TextView reportEnd;

        public ReportViewHolder(@NonNull View reportView){
            super(reportView);
            reportTitle = reportView.findViewById(R.id.reportTitle);
            reportHotel = reportView.findViewById(R.id.reportHotel);
            reportStart = reportView.findViewById(R.id.reportStart);
            reportEnd = reportView.findViewById(R.id.reportEnd);
        }
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reportView = mInflater.inflate(R.layout.report_item, parent,false);
        return new ReportViewHolder(reportView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        if(mReportVacations != null){
            Vacation current = mReportVacations.get(position);
            holder.reportTitle.setText(current.getTitle());
            holder.reportHotel.setText(current.getHotel());
            holder.reportStart.setText(current.getStart_date());
            holder.reportEnd.setText(current.getEnd_date());
        }
        else{
            holder.reportTitle.setText("Null");
            holder.reportHotel.setText("Null");
            holder.reportStart.setText("Null");
            holder.reportEnd.setText("Null");
        }
    }

    @Override
    public int getItemCount() {
        if(mReportVacations != null){
            return mReportVacations.size();
        }
        else{
            return 0;
        }
    }

    public void setReportVacations(List<Vacation> reportVacations){
        mReportVacations = reportVacations;
        notifyDataSetChanged();
    }
}
