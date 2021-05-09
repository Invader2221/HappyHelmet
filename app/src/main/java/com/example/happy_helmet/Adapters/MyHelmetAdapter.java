package com.example.happy_helmet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy_helmet.GetterSetters.HelmetDetails;
import com.example.happy_helmet.R;
import com.example.happy_helmet.Recycler.MyHelmetActivity;
import com.example.happy_helmet.ShareBarcode;

import java.util.List;

public class MyHelmetAdapter extends RecyclerView.Adapter<HelmetFinder> {


    Context mContext;
    private List<HelmetDetails> myHelmetDetails;

    public MyHelmetAdapter(Context mContext, List<HelmetDetails> myHelmetDetails) {
        this.mContext = mContext;
        this.myHelmetDetails = myHelmetDetails;
    }



    @Override
    public HelmetFinder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.helmet_row_view, viewGroup, false);

        return new HelmetFinder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull final HelmetFinder helmetFinder, final int i) {
        helmetFinder.helmetId.setText(myHelmetDetails.get(i).getHid());
        helmetFinder.helmetName.setText(myHelmetDetails.get(i).getName());


        helmetFinder.hCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShareBarcode.class);
                intent.putExtra("DECRYPTDATA", myHelmetDetails.get(i).getHid());
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myHelmetDetails.size();
    }
}


class HelmetFinder extends RecyclerView.ViewHolder{

    TextView helmetId,helmetName;
    CardView hCardView;

    public HelmetFinder(View itemView) {
        super(itemView);

        helmetId = itemView.findViewById(R.id.helmet_id);
        helmetName = itemView.findViewById(R.id.helmet_name);
        hCardView= itemView.findViewById(R.id.helmetDetailsCard);

    }


}
