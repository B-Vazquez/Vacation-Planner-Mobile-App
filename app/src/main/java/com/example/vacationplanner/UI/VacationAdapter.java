package com.example.vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplanner.R;
import com.example.vacationplanner.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Vacation current = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetailsActivity.class);
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("start", current.getStart_date());
                    intent.putExtra("end", current.getEnd_date());
                    intent.putExtra("transport", current.getTransport());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (mVacations != null){
            Vacation current = mVacations.get(position);
            String name = current.getTitle();
            holder.vacationItemView.setTextSize(18);
            holder.vacationItemView.setText(name);
        }
        else{
            holder.vacationItemView.setText("No vacation name");
        }
    }

    @Override
    public int getItemCount() {
        if(mVacations != null) {
            return mVacations.size();
        }
        else return 0;
    }

    public void setVacations(List<Vacation> vacations){
        mVacations=vacations;
        notifyDataSetChanged();
    }
}
