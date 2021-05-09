package com.example.happy_helmet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy_helmet.R;
import com.example.happy_helmet.GetterSetters.UserHistoryDetails;

import java.util.List;

public class UserHistoryAdapter extends RecyclerView.Adapter<HistoryFinder> {


    Context userContext;
    private List<UserHistoryDetails> userHistoryDetails;

    public UserHistoryAdapter(Context userContext, List<UserHistoryDetails> userHistoryDetails) {
        this.userContext = userContext;
        this.userHistoryDetails = userHistoryDetails;
    }


    @Override
    public HistoryFinder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_history_row, viewGroup, false);

        return new HistoryFinder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryFinder historyFinder, final int i) {
        historyFinder.helmetId.setText(userHistoryDetails.get(i).getHelmetIdSmall());
        historyFinder.ChechIn.setText(userHistoryDetails.get(i).getHelmetCheckIn());
        historyFinder.CheckOut.setText(userHistoryDetails.get(i).getHelmetCkeckOut());


        historyFinder.hCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userHistoryDetails.size();
    }
}


class HistoryFinder extends RecyclerView.ViewHolder {

    TextView helmetId, ChechIn, CheckOut;
    CardView hCardView;

    public HistoryFinder(View itemView) {
        super(itemView);

        helmetId = itemView.findViewById(R.id.helmet_id);
        ChechIn = itemView.findViewById(R.id.checkIn);
        CheckOut = itemView.findViewById(R.id.checkOut);
        hCardView = itemView.findViewById(R.id.helmetHistoryCard);

    }
}
